package com.android.infinum

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.infinum.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.loginButton.isEnabled=checkPassword(binding.passwordInput.editText?.text.toString()) &&
                    checkMail(binding.emailInput.editText?.text.toString())
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.passwordInputEditor.addTextChangedListener(textWatcher)
        binding.emailInputEditor.addTextChangedListener(textWatcher)


        binding.loginButton.setOnClickListener {

            val intent =
                WelcomeActivity.buildIntent(
                    this,
                    binding.emailInput.editText?.text.toString()
                )
            startActivity(intent)

        }
    }

    private fun checkMail(email: String) : Boolean {

        if (!email.contains("@")|| !Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()) {
            Toast.makeText(this, "email", Toast.LENGTH_SHORT).show()
            binding.emailInput.error = "Please enter valid e-mail."
            return false
        }else
            binding.emailInput.error= null
        return true
    }

    private fun checkPassword(password : String) : Boolean {

        if (!password.length?.let { len -> len >= 6 }) {
            println(password)

            binding.passwordInput.error = "Password should be at least 6 characters long."
            return false
        }else
            binding.passwordInput.error= null
        return true
    }

}