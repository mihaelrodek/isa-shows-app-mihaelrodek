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

        binding.loginButton.isEnabled = false

        if (binding.emailInput.editText?.text.toString().isNotEmpty() &&
            binding.passwordInput.editText?.text.toString().isNotEmpty())
            binding.loginButton.isEnabled = true

        binding.loginButton.setOnClickListener {

            checkMailAndPassword(binding.emailInput.editText?.text.toString(),binding.passwordInput.editText?.text.toString())

        }

    }

    private fun checkMailAndPassword(email: String, password: String) {


        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && pwRegex.toRegex().containsMatchIn(password)) {
            val intent =
                WelcomeActivity.buildIntent(
                    this,
                    binding.emailInput.editText?.text.toString()
                )
            startActivity(intent)
        } else Toast.makeText(this, "Wrong email or password format!", Toast.LENGTH_LONG)
            .show()

    }

}