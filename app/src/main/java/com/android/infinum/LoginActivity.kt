package com.android.infinum

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.infinum.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.isClickable = false

        if (binding.emailInput.editText?.text.toString().isNotEmpty() &&
            binding.passwordInput.editText?.text.toString().isNotEmpty())
            binding.loginButton.isClickable = true

        binding.loginButton.setOnClickListener {

            val mail = checkMail(binding.emailInput.editText?.text.toString())
            val password = checkPassword(binding.passwordInput.editText?.text.toString())

            if (mail && password) {
                val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.apply {
                    putString(getString(R.string.username), binding.emailInput.editText?.text.toString().substringBefore("@"))
                }.apply()

                val intent = Intent(this, ShowsActivity::class.java)
                startActivity(intent)
            }else Toast.makeText(this, "Wrong email or password format!", Toast.LENGTH_LONG).show()

        }

    }

    private fun checkMail(email: String): Boolean {
        val regex = """^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}${'$'}""".toRegex()
        return regex.containsMatchIn(email)
    }

    private fun checkPassword(password: String): Boolean {
        val regex = """^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{6,}${'$'}""".toRegex()
        return regex.containsMatchIn(password)
    }


}