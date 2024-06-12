package com.bangkit.nest.ui.auth.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import androidx.navigation.fragment.findNavController
import com.bangkit.nest.R
import com.bangkit.nest.databinding.FragmentLoginBinding
import com.bangkit.nest.utils.ViewModelFactory
import com.bangkit.nest.data.Result
import com.bangkit.nest.ui.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fieldValidation(binding?.emailEditTextLayout, binding?.emailEditText)
        fieldValidation(binding?.passwordEditTextLayout, binding?.passwordEditText)

        binding?.registerNowTextView?.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_login_to_navigation_register)
        }

        binding?.root?.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
                binding?.emailEditText?.clearFocus()
                binding?.passwordEditText?.clearFocus()
            }
            false
        }

        binding?.loginButton?.setOnClickListener {
            hideKeyboard()

            val usernameEmail = binding?.emailEditText?.text.toString()
            val password = binding?.passwordEditText?.text.toString()
            var isValidInput = true

            // Field validation
            if (usernameEmail.isEmpty()) {
                binding?.emailEditTextLayout?.error = getString(R.string.empty_field)
                isValidInput = false
            }
            if (password.isEmpty()) {
                binding?.passwordEditTextLayout?.error = getString(R.string.empty_field)
                isValidInput = false
            }

            // Login
            if (isValidInput) {
                viewModel.login(usernameEmail, password).observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding?.progressBar?.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding?.progressBar?.visibility = View.GONE

                                // move to MainActivity
                                val intent = Intent(requireContext(), MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                activity?.finish()
                            }
                            is Result.Error -> {
                                binding?.progressBar?.visibility = View.GONE
                                showError(result.error)
                            }
                        }
                    }
                }
            }
        }

        binding?.passwordEditText?.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                binding?.loginButton?.performClick()
                true
            } else {
                false
            }
        }
    }

    private fun showError(errorMessage: String) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.failed))
            .setMessage(errorMessage)
            .setPositiveButton(getString(R.string.ok), null)
            .show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
    }

    private fun fieldValidation(editTextLayout: TextInputLayout?, editText: TextInputEditText?) {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    editTextLayout?.error = null
                } else {
                    editTextLayout?.error = getString(R.string.empty_field)
                }
            }
        }
        editText?.addTextChangedListener(textWatcher)
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