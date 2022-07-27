package com.android.infinum.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.infinum.R
import com.android.infinum.databinding.FragmentRegisterBinding
import com.android.infinum.viewmodels.RegistrationViewModel

class RegisterFragment : Fragment() {

    companion object {
        private const val AT_SEPARATOR = "@"
        private const val PW_LEN = 6
        private const val SHARED_PREFS = "sharedPrefs"
    }

    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    private val viewModel: RegistrationViewModel by viewModels()

    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.passwordInputEditor.doOnTextChanged { text, start, before, count ->
            binding.register.isEnabled =
                checkPasswords(
                    binding.passwordInput.editText?.text.toString().trim(),
                    binding.passwordInput.editText?.text.toString().trim()
                ) &&
                        checkMail(binding.emailInput.editText?.text.toString().trim())
        }

        binding.emailInputEditor.doOnTextChanged { text, start, before, count ->
            binding.register.isEnabled =
                checkPasswords(
                    binding.passwordInput.editText?.text.toString().trim(),
                    binding.passwordInput.editText?.text.toString().trim()
                ) &&
                        checkMail(binding.emailInput.editText?.text.toString().trim())
        }

        binding.passwordInputRepeatEditor.doOnTextChanged { text, start, before, count ->
            binding.register.isEnabled =
                checkPasswords(
                    binding.passwordInput.editText?.text.toString().trim(),
                    binding.passwordInput.editText?.text.toString().trim()
                ) &&
                        checkMail(binding.emailInput.editText?.text.toString().trim())
        }

        viewModel.getRegistrationResultLiveData()
            .observe(this.viewLifecycleOwner) { isRegisterSuccessful ->
                if (isRegisterSuccessful) {
                    Toast.makeText(context, "USPJEŠNA REGISTRACIJA", Toast.LENGTH_SHORT).show()
                    sharedPref =
                        activity?.getPreferences(Context.MODE_PRIVATE) ?: return@observe
                    with(sharedPref.edit()) {
                        putBoolean("reg", true)
                        apply()
                    }
                    findNavController().navigate(R.id.action_registration_to_login)
                } else {
                    Toast.makeText(context, "NIJE USPJEŠNA REGISTRACIJA", Toast.LENGTH_SHORT).show()
                }
            }
        binding.apply {

            binding.register.setOnClickListener {

                viewModel.register(binding.emailInputEditor.text.toString(), passwordInputEditor.text.toString(), binding.passwordInputRepeatEditor.text.toString())

            }
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

    private fun checkPasswords(password: String, passwordRepeat: String): Boolean {
        val cp = checkPassword(password)
        val cp2 = checkPassword(passwordRepeat)

        if (cp && cp2) {
            if (password.equals(passwordRepeat)) {
                binding.passwordInput.error = null
                binding.passwordInputRepeat.error = null
                return true
            } else {
                binding.passwordInputRepeat.error = "Passwords don't match"
                return false
            }
        } else {
            binding.passwordInput.error = getString(R.string.invalid_password)
            binding.passwordInputRepeat.error = getString(R.string.invalid_password)
        }
        return false
    }
}