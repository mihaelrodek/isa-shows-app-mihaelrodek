package com.android.infinum

import android.app.Activity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.infinum.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    companion object{
        private const val pwRegex = """^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{6,}${'$'}"""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.isClickable = false

        if (binding.emailInput.editText?.text.toString().isNotEmpty() &&
            binding.passwordInput.editText?.text.toString().isNotEmpty())
            binding.loginButton.isClickable = true

        binding.loginButton.setOnClickListener {

            checkMailAndPassword(binding.emailInput.editText?.text.toString(),binding.passwordInput.editText?.text.toString())

        }

    }

    private fun checkMailAndPassword(email: String, password: String) {

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            Toast.makeText(this, "Wrong email. Please try again.", Toast.LENGTH_SHORT).show()
        else if(!pwRegex.toRegex().containsMatchIn(password)){
            Toast.makeText(this, "Wrong password format. Password should contain one capital letter, one lower letter and one number.", Toast.LENGTH_SHORT).show()
        }
        else {
            val intent =
                WelcomeActivity.buildIntent(
                    this,
                    binding.emailInput.editText?.text.toString()
                )
            startActivity(intent)
        }
    }

}