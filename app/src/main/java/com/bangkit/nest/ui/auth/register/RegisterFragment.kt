package com.bangkit.nest.ui.auth.register

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bangkit.nest.R
import com.bangkit.nest.data.Result
import com.bangkit.nest.databinding.FragmentRegisterBinding
import com.bangkit.nest.utils.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fieldValidation(binding?.usernameEditTextLayout, binding?.usernameEditText)
        fieldValidation(binding?.emailEditTextLayout, binding?.emailEditText)
        fieldValidation(binding?.passwordEditTextLayout, binding?.passwordEditText)
        fieldValidation(binding?.confirmPasswordEditTextLayout, binding?.confirmPasswordEditText)

        binding?.loginNowTextView?.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_register_to_navigation_login)
        }

        binding?.root?.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                clearInput()
            }
            false
        }

        binding?.registerButton?.setOnClickListener {
            clearInput()

            val username = binding?.usernameEditText?.text.toString()
            val email = binding?.emailEditText?.text.toString()
            val password = binding?.passwordEditText?.text.toString()
            val confirmPassword = binding?.confirmPasswordEditText?.text.toString()
            var isValidInput = true

            // Field validation
            if (username.isEmpty()) {
                binding?.usernameEditTextLayout?.error = getString(R.string.empty_field)
                isValidInput = false
            }
            if (password.isEmpty()) {
                binding?.passwordEditTextLayout?.error = getString(R.string.empty_field)
                isValidInput = false
            } else if (binding?.passwordEditTextLayout?.error != null) {
                isValidInput = false
            }
            if (email.isEmpty()) {
                binding?.emailEditTextLayout?.error = getString(R.string.empty_field)
                isValidInput = false
            } else if (binding?.emailEditTextLayout?.error != null) {
                isValidInput = false
            }
            if (confirmPassword.isEmpty()) {
                binding?.confirmPasswordEditTextLayout?.error = getString(R.string.empty_field)
                isValidInput = false
            } else if (binding?.confirmPasswordEditTextLayout?.error != null) {
                isValidInput = false
            }

            // Register
            if (isValidInput) {
                viewModel.register(username, email, password).observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding?.progressBar?.isVisible = true
                                binding?.registerButton?.text = ""
                                binding?.registerButton?.isEnabled = false
                            }
                            is Result.Success -> {
                                binding?.progressBar?.isVisible = false
                                binding?.registerButton?.text = getString(R.string.register)
                                binding?.registerButton?.isEnabled = true
                                showAlertDialog(isError = false)
                            }
                            is Result.Error -> {
                                binding?.progressBar?.isVisible = false
                                binding?.registerButton?.text = getString(R.string.register)
                                binding?.registerButton?.isEnabled = true
                                showAlertDialog(isError = true, result.error)
                            }
                        }
                    }
                }
            }
        }

        binding?.confirmPasswordEditText?.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                binding?.registerButton?.performClick()
                true
            } else {
                false
            }
        }
    }

    private fun clearInput() {
        hideKeyboard()
        binding?.usernameEditText?.clearFocus()
        binding?.emailEditText?.clearFocus()
        binding?.passwordEditText?.clearFocus()
        binding?.confirmPasswordEditText?.clearFocus()
    }

    private fun fieldValidation(editTextLayout: TextInputLayout?, editText: TextInputEditText?) {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (editText == binding?.emailEditText) {
                    validateEmail()
                } else if (editText == binding?.passwordEditText) {
                    validatePasswordLength()
                } else if (editText == binding?.confirmPasswordEditText) {
                    validatePasswordConfirmation()
                } else {
                    if (s.isNotEmpty()) {
                        editTextLayout?.error = null
                    } else {
                        editTextLayout?.error = getString(R.string.empty_field)
                    }
                }
            }
        }
        editText?.addTextChangedListener(textWatcher)
    }

    private fun validateEmail(): Boolean {
        val email = binding?.emailEditText?.text.toString()
        val emailEditTextLayout = binding?.emailEditTextLayout

        // Validate email address
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditTextLayout?.error = getString(R.string.invalid_email)
            return false
        } else if (email.isEmpty()) {
            emailEditTextLayout?.error = getString(R.string.empty_field)
            return true
        } else {
            emailEditTextLayout?.error = null
            return true
        }
    }

    private fun validatePasswordConfirmation(): Boolean {
        val password = binding?.passwordEditText?.text.toString()
        val confirmPassword = binding?.confirmPasswordEditText?.text.toString()
        val confirmPasswordEditTextLayout = binding?.confirmPasswordEditTextLayout

        // Validate password match
        if (password != confirmPassword) {
            confirmPasswordEditTextLayout?.error = getString(R.string.password_mismatch)
            return false
        } else if (confirmPassword.isEmpty()) {
            confirmPasswordEditTextLayout?.error = getString(R.string.empty_field)
            return false
        } else {
            confirmPasswordEditTextLayout?.error = null
            return true
        }
    }

    private fun validatePasswordLength() : Boolean {
        val password = binding?.passwordEditText?.text.toString()
        val passwordEditTextLayout = binding?.passwordEditTextLayout

        // Validate password length
        if (password.length < 6) {
            passwordEditTextLayout?.error = getString(R.string.short_password)
            return false
        } else {
            passwordEditTextLayout?.error = null
            return true
        }
    }

    private fun showAlertDialog(isError: Boolean, errorMessage: String? = null) {
        if (!isError) {
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.success))
                .setMessage(getString(R.string.register_success))
                .setPositiveButton(getString(R.string.continue_)) { _, _ ->
                    binding?.loginNowTextView?.performClick()
                }
                .show()
        } else {
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.failed))
                .setMessage(errorMessage)
                .setPositiveButton(getString(R.string.ok), null)
                .show()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = requireActivity().currentFocus
        if (currentFocusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}