package com.example.tugaspertemuan2_kalkulator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtInput: TextView = findViewById(R.id.txt_input)
        val txtOutput: TextView = findViewById(R.id.txt_output)
        val buttonClear: Button = findViewById(R.id.button_clear)
        val buttonEqual: Button = findViewById(R.id.button_equal)
        val buttonBackspace: ImageButton = findViewById(R.id.button_backspace)

        val buttons = listOf<Button>(
            findViewById(R.id.button_0),
            findViewById(R.id.button_1),
            findViewById(R.id.button_2),
            findViewById(R.id.button_3),
            findViewById(R.id.button_4),
            findViewById(R.id.button_5),
            findViewById(R.id.button_6),
            findViewById(R.id.button_7),
            findViewById(R.id.button_8),
            findViewById(R.id.button_9),
            findViewById(R.id.button_plus),
            findViewById(R.id.button_minus),
            findViewById(R.id.button_multiply),
            findViewById(R.id.button_divide)
        )

        for (button in buttons) {
            button.setOnClickListener {
                txtInput.text = "${txtInput.text}${button.text}"
            }
        }

        buttonEqual.setOnClickListener {
            val input = txtInput.text.toString()
            try {
                val postfix = infixToPostfix(input)
                val result = evaluatePostfix(postfix)
                txtOutput.text = result.toString()
                Toast.makeText(this,"Hasil = " + result, Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                txtOutput.text = "Error"
                Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
            }
        }

        buttonClear.setOnClickListener {
            txtInput.text = ""
            txtOutput.text = ""
        }

        buttonBackspace.setOnClickListener {
            val currentText = txtInput.text.toString()
            if (currentText.isNotEmpty()) {
                txtInput.text = currentText.dropLast(1)
            }
        }
    }

    // Konversi ekspresi infix ke postfix (RPN)
    private fun infixToPostfix(expression: String): List<String> {
        val result = mutableListOf<String>()
        val stack = Stack<String>()
        val tokens = expression.split("(?<=[-+*/])|(?=[-+*/])".toRegex())

        val precedence = mapOf(
            "+" to 1,
            "-" to 1,
            "*" to 2,
            "/" to 2
        )

        for (token in tokens) {
            if (token.isNumber()) {
                result.add(token)
            } else {
                while (stack.isNotEmpty() && precedence[stack.peek()] ?: 0 >= precedence[token] ?: 0) {
                    result.add(stack.pop())
                }
                stack.push(token)
            }
        }

        while (stack.isNotEmpty()) {
            result.add(stack.pop())
        }

        return result
    }

    // Evaluasi ekspresi postfix
    private fun evaluatePostfix(postfix: List<String>): Double {
        val stack = Stack<Double>()

        for (token in postfix) {
            if (token.isNumber()) {
                stack.push(token.toDouble())
            } else {
                val b = stack.pop()
                val a = stack.pop()
                stack.push(
                    when (token) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> a / b
                        else -> throw IllegalArgumentException("Invalid operator")
                    }
                )
            }
        }

        return stack.pop()
    }

    // Helper extension function to check if a string is a number
    private fun String.isNumber(): Boolean {
        return this.toDoubleOrNull() != null
    }
}

