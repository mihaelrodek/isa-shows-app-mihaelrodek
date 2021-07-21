package com.android.infinum

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.infinum.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.isClickable = false

        if (binding.emailInput.editText?.text.toString().isNotEmpty() &&
            binding.passwordInput.editText?.text.toString().isNotEmpty())
            binding.loginButton.isClickable = true

        binding.loginButton.setOnClickListener {

            val mail = checkMail(binding.emailInput.editText?.text.toString())
            val password = checkPassword(binding.passwordInput.editText?.text.toString())

            if (mail && password) {
                val sharedPreferences = this.requireActivity()
                    .getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.apply {
                    putString(getString(R.string.username), binding.emailInput.editText?.text.toString().substringBefore("@"))
                }.apply()

                findNavController().navigate(R.id.action_first_to_second)
            }else Toast.makeText(context, "Wrong email or password format!", Toast.LENGTH_LONG).show()

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