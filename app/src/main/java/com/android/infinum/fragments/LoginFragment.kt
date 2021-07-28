package com.android.infinum.fragments

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.infinum.R
import com.android.infinum.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    companion object {
        private const val AT_SEPARATOR = "@"
        private const val PW_LEN = 6
        private const val SHARED_PREFS = "sharedPrefs"
    }

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


            val sharedPreferences = this.requireActivity()
                .getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply {
                putString(
                    getString(R.string.username),
                    binding.emailInput.editText?.text.toString().substringBefore(AT_SEPARATOR)
                )
            }.apply()

            findNavController().navigate(R.id.action_first_to_second)
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