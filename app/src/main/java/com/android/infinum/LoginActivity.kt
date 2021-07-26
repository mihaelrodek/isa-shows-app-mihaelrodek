package com.android.infinum

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.android.infinum.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    companion object {
        private const val AT_SEPARATOR = "@"
        private const val PW_LEN = 6
        private const val SHARED_PREFS = "sharedPrefs"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.passwordInputEditor.doOnTextChanged { text, start, before, count ->
            binding.loginButton.isEnabled =
                checkPassword(binding.passwordInput.editText?.text.toString().trim()) &&
                        checkMail(binding.emailInput.editText?.text.toString().trim())
        }

        binding.emailInputEditor.doOnTextChanged { text, start, before, count ->
            binding.loginButton.isEnabled =
                checkPassword(binding.passwordInput.editText?.text.toString().trim()) &&
                        checkMail(binding.emailInput.editText?.text.toString().trim())
        }


        binding.loginButton.setOnClickListener {

            val sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply {
                putString(
                    getString(R.string.username),
                    binding.emailInput.editText?.text.toString().substringBefore(
                        AT_SEPARATOR
                    )
                )
            }.apply()

            val intent = Intent(this, ShowsActivity::class.java)
            startActivity(intent)
        }
    }


    private fun checkMail(email: String): Boolean {

        val emailCheck = email.trim()

        if (!emailCheck.contains(AT_SEPARATOR) || !Patterns.EMAIL_ADDRESS.matcher(emailCheck)
                .matches() || emailCheck.isEmpty()
        ) {
            binding.emailInput.error = getString(R.string.invalid_mail)
            return false
        } else
            binding.emailInput.error = null
        return true
    }

    private fun checkPassword(password: String): Boolean {

        if (!password.length?.let { len -> len >= PW_LEN }) {
            binding.passwordInput.error = getString(R.string.invalid_password)
            return false
        } else
            binding.passwordInput.error = null
        return true
    }


}