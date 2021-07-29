package com.android.infinum.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.infinum.R
import com.android.infinum.databinding.FragmentLoginBinding
import com.android.infinum.viewmodels.LoginViewModel
import com.android.infinum.viewmodels.RegistrationViewModel


class LoginFragment : Fragment() {

    companion object {
        private const val AT_SEPARATOR = "@"
        private const val PW_LEN = 6
        private const val SHARED_PREFS = "sharedPrefs"
    }

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private var registered = false

    private var rememberMe = false
    private var email = ""

    private lateinit var prefs: SharedPreferences

    private val viewModel: LoginViewModel by viewModels()

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

        prefs = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val edit = prefs.edit()
        edit.apply {
            rememberMe = prefs.getBoolean("showSeen", false)
            registered = prefs.getBoolean("reg", false)
            email = prefs.getString(getString(R.string.username),"username").toString()
        }.apply()

        Toast.makeText(context, email, Toast.LENGTH_SHORT).show()

        if (rememberMe) {
            findNavController().navigate(R.id.action_login_to_shows)
        }

        if (registered) {
            binding.registerButton.isVisible = !registered
            binding.loginText.text = getString(R.string.registere_successful)
        }

        viewModel.getLoginLiveData()
            .observe(this.viewLifecycleOwner) { isLogged ->
                if (isLogged) {
                    Toast.makeText(context, "USPJEŠABN LOGIN", Toast.LENGTH_SHORT).show()

                    val sharedPreferences = this.requireActivity()
                        .getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.apply {
                        putString(
                            getString(R.string.username),
                            binding.emailInput.editText?.text.toString()
                        )
                        putBoolean("RememberMe", binding.rememberMe.isChecked)
                    }.apply()

                    findNavController().navigate(R.id.action_login_to_shows)
                } else {
                    Toast.makeText(context, "NIJE USPJEŠNA REGISTRACIJA", Toast.LENGTH_SHORT).show()
                }
            }

        binding.apply {

            binding.loginButton.setOnClickListener {
                viewModel.login(binding.emailInputEditor.text.toString(),binding.passwordInputEditor.text.toString())
            }
        }
        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }

    override fun onStop() {
        super.onStop()
        with(prefs.edit()) {
            putBoolean("reg", false)
            apply()
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