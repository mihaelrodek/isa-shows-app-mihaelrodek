package com.android.infinum

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.infinum.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity(){

    private lateinit var binding: ActivityWelcomeBinding


    companion object {
        private const val EXTRA_EMAIL = "EXTRA_EMAIL"

        fun buildIntent(activity: Activity, email: String) : Intent {
            val intent = Intent(activity, WelcomeActivity::class.java)
            intent.putExtra(EXTRA_EMAIL, email)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.extras?.getString(EXTRA_EMAIL)

        val emailWithouAt = email?.substringBefore("@")

        binding.welcomeDialog.text= "Welcome, ${emailWithouAt}!"

    }


}