package com.android.infinum

import android.app.Activity
import android.content.Context
import android.content.Intent
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

    companion object {
        private const val USERNAME_SEPARATOR = "@"
        private const val PW_LEN = 6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.passwordInputEditor.addTextChangedListener(textWatcher)
        binding.emailInputEditor.addTextChangedListener(textWatcher)


        binding.loginButton.setOnClickListener {

            val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply {
                putString(getString(R.string.username), binding.emailInput.editText?.text.toString().substringBefore("@"))
            }.apply()

            val intent = Intent(this, ShowsActivity::class.java)
            startActivity(intent)
        }

    }


    private fun checkMail(email: String) : Boolean {

        val emailCheck = email.trim()

        if (!emailCheck.contains(USERNAME_SEPARATOR)|| !Patterns.EMAIL_ADDRESS.matcher(emailCheck).matches() || emailCheck.isEmpty()) {
            binding.emailInput.error = getString(R.string.invalid_mail)
            return false
        }else
            binding.emailInput.error= null
        return true
    }

    private fun checkPassword(password : String) : Boolean {

        if (!password.length?.let { len -> len >= PW_LEN }) {
            println(password)

            binding.passwordInput.error = getString(R.string.invalid_password)
            return false
        }else
            binding.passwordInput.error= null
        return true
    }


}