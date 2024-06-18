package com.bangkit.nest.ui.main.profile.edit

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bangkit.nest.R
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.request.ChangePasswordRequest
import com.bangkit.nest.data.remote.request.ProfileRequest
import com.bangkit.nest.databinding.DialogChangePasswordBinding
import com.bangkit.nest.databinding.FragmentEditProfileBinding
import com.bangkit.nest.ui.main.MainActivity
import com.bangkit.nest.utils.ViewModelFactory
import com.bangkit.nest.utils.convertDate
import com.bangkit.nest.utils.dpToPixels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<EditProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDatePicker()
        setupFieldFill()
        setupBackButton()
        setupInput()
        setupDropdownReligion()
        setupSubmitButton()
        setupResetPassword()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupInput() {
        binding?.scrollView?.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }
    }

    private fun setupSubmitButton() {
        binding?.submitButton?.setOnClickListener {
            hideKeyboard()

            val request = getInputValid()
            if (request != null) {
                viewModel.editProfile(request).observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding?.progressBar?.isVisible = true
                                binding?.submitButton?.text = ""
                                binding?.submitButton?.isEnabled = false
                            }
                            is Result.Success -> {
                                binding?.progressBar?.isVisible = false
                                binding?.submitButton?.text = getString(R.string.submit)
                                binding?.submitButton?.isEnabled = true
                                showAlertDialog(isError = false)
                            }
                            is Result.Error -> {
                                binding?.progressBar?.isVisible = false
                                binding?.submitButton?.text = getString(R.string.submit)
                                binding?.submitButton?.isEnabled = true
                                showAlertDialog(isError = true, getString(R.string.error_occurred))
                            }
                        }
                    }
                }
            } else {
                showAlertDialog(true, getString(R.string.field_required))
            }
        }
    }

    private fun getInputValid(): ProfileRequest? {
        val request = ProfileRequest()
        val fullName = binding?.fullNameEditText?.text.toString()
        if (fullName.isEmpty()) {
            return null
        } else {
            request.userFullName = fullName
        }

        val birthDate = binding?.birthDateEditText?.text.toString()
        if (birthDate.isEmpty()) {
            return null
        } else {
            request.userBirthData = convertDate(birthDate, toJson = true)
        }

        val radioGender = binding?.radioGender?.checkedRadioButtonId
        if (radioGender == -1) {
            return null
        } else {
            var gender = getString(R.string.others)
            if (radioGender == R.id.gender_1) {
                gender = getString(R.string.male)
            } else if (radioGender == R.id.gender_2) {
                gender = getString(R.string.female)
            }
            request.userGender = gender
        }

        val radioEducation = binding?.radioEducation?.checkedRadioButtonId
        if (radioEducation == -1) {
            return null
        } else {
            var education = "Sekolah menengah"
            if (radioEducation == R.id.education_1) {
                education = "Kurang dari sekolah menengah"
            } else if (radioEducation == R.id.education_3) {
                education = getString(R.string.label_education_3)
            } else if (radioEducation == R.id.education_4) {
                education = getString(R.string.label_education_4)
            }
            request.userEducation = education
        }

        val school = binding?.schoolEditText?.text.toString()
        if (school.isEmpty()) {
            return null
        } else {
            request.userCurrentSchool = school
        }

        val religion = binding?.menuReligionOption?.text.toString()
        if (religion.isEmpty()) {
            return null
        } else {
            request.userReligion = religion
        }

        val radioEngNat = binding?.radioEngNat?.checkedRadioButtonId
        if (radioEngNat == -1) {
            return null
        } else {
            var engNat = getString(R.string.tidak)
            if (radioEngNat == R.id.engNat_1) {
                engNat = getString(R.string.ya)
            }
            request.userEngNat = engNat
        }

        return request
    }

    private fun showAlertDialog(isError: Boolean, errorMessage: String? = null) {
        if (!isError) {
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.success))
                .setMessage(getString(R.string.update_profile_success))
                .setPositiveButton(getString(R.string.continue_)) { _, _ ->
                    backToProfilePage()
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

    private fun setupBackButton() {
        binding?.backButton?.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.unsaved_changes))
                .setMessage(getString(R.string.unsaved_changes_confirmation))
                .setPositiveButton(getString(R.string.discard)) { _, _ ->
                    backToProfilePage()
                }
                .setNegativeButton(getString(R.string.cancel), null)
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

    private fun setupResetPassword() {
        binding?.resetPasswordButton?.setOnClickListener {
            val dialogBinding = DialogChangePasswordBinding.inflate(layoutInflater)
            val changePasswordView = dialogBinding.root

            val passwordEditText = dialogBinding.newPasswordEditText
            val passwordEditTextLayout = dialogBinding.newPasswordEditTextLayout
            val confirmPasswordEditText = dialogBinding.confirmNewPasswordEditText
            val confirmPasswordEditTextLayout = dialogBinding.confirmNewPasswordEditTextLayout

            passwordEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val password = passwordEditText.text.toString()
                    // Validate password length
                    if (password.length < 6) {
                        passwordEditTextLayout.error = getString(R.string.short_password)
                    } else if (password.isEmpty()) {
                        passwordEditTextLayout.error = getString(R.string.empty_field)
                    } else {
                        passwordEditTextLayout.error = null
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            confirmPasswordEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val password = passwordEditText.text.toString()
                    val confirmPassword = confirmPasswordEditText.text.toString()
                    // Validate password match
                    if (password != confirmPassword) {
                        confirmPasswordEditTextLayout.error = getString(R.string.password_mismatch)
                    } else if (confirmPassword.isEmpty()) {
                        confirmPasswordEditTextLayout.error = getString(R.string.empty_field)
                    } else {
                        confirmPasswordEditTextLayout.error = null
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.reset_password))
                .setView(changePasswordView)
                .setPositiveButton(getString(R.string.reset), null)
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
            dialog.setOnShowListener {
                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
                positiveButton.setOnClickListener {
                    if (passwordEditText.text.toString().isEmpty()) {
                        passwordEditTextLayout.error = getString(R.string.empty_field)
                    } else if (confirmPasswordEditText.text.toString().isEmpty()) {
                        confirmPasswordEditTextLayout.error = getString(R.string.empty_field)
                    } else if (passwordEditTextLayout.error == null && confirmPasswordEditTextLayout.error == null) {
                        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(passwordEditText.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                        positiveButton.isEnabled = false

                        val request = ChangePasswordRequest(confirmPasswordEditText.text.toString())
                        viewModel.changePassword(request).observe(viewLifecycleOwner) { result ->
                            when (result) {
                                is Result.Loading -> {
                                    dialogBinding.progressBar.isVisible = true
                                    dialogBinding.newPasswordEditTextLayout.isVisible = false
                                    dialogBinding.confirmNewPasswordEditTextLayout.isVisible = false
                                    dialogBinding.informationTextView.isVisible = false
                                }
                                is Result.Success -> {
                                    dialogBinding.progressBar.isVisible = false
                                    dialogBinding.newPasswordEditTextLayout.isVisible = false
                                    dialogBinding.confirmNewPasswordEditTextLayout.isVisible = false
                                    dialog.setTitle(getString(R.string.success))
                                    dialogBinding.informationTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_green))
                                    dialogBinding.informationTextView.text = getString(R.string.success_reset_password)
                                    dialogBinding.informationTextView.isVisible = true

                                    // Dismiss the dialog after 3 seconds
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        viewModel.logout()
                                        dialog.dismiss()
                                    }, 3000)
                                }
                                is Result.Error -> {
                                    dialogBinding.progressBar.isVisible = false
                                    dialogBinding.newPasswordEditTextLayout.isVisible = false
                                    dialogBinding.confirmNewPasswordEditTextLayout.isVisible = false
                                    dialog.setTitle(getString(R.string.failed))
                                    dialogBinding.informationTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                                    dialogBinding.informationTextView.text = getString(R.string.error_occurred)
                                    dialogBinding.informationTextView.isVisible = true
                                    // Dismiss the dialog after 4 seconds
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        dialog.dismiss()
                                    }, 4000)
                                }
                            }
                        }
                    }
                }
                // Enable buttons initially
                positiveButton.isEnabled = true
            }
            dialog.show()
        }
    }

    private fun backToProfilePage() {
        findNavController().popBackStack()
    }

    private fun setupFieldFill() {
        val fullName = arguments?.getString("fullName", "") ?: ""
        binding?.fullNameEditText?.setText(fullName)

        val birthDate = arguments?.getString("birthDate", "") ?: ""
        binding?.birthDateEditText?.setText(birthDate)

        val school = arguments?.getString("school", "") ?: ""
        binding?.schoolEditText?.setText(school)
        val education = arguments?.getString("education")
        if (education != null) {
            if ( getString(R.string.label_education_2).contains(education, ignoreCase = true) ) {
                binding?.radioEducation?.check(R.id.education_2)
            } else if ( getString(R.string.label_education_1).contains(education, ignoreCase = true) ) {
                binding?.radioEducation?.check(R.id.education_1)
            } else if ( getString(R.string.label_education_3).equals(education, ignoreCase = true) ) {
                binding?.radioEducation?.check(R.id.education_3)
            } else {
                binding?.radioEducation?.check(R.id.education_4)
            }
        }

        val gender = arguments?.getString("gender")
        if (gender != null) {
            if (gender.equals("laki-laki", ignoreCase = true)) {
                binding?.radioGender?.check(R.id.gender_1)
            } else if (gender.equals("perempuan", ignoreCase = true)) {
                binding?.radioGender?.check(R.id.gender_2)
            } else {
                binding?.radioGender?.check(R.id.gender_3)
            }
        }

        val engNat = arguments?.getString("engNat")
        if (engNat != null) {
            if ( engNat.equals(getString(R.string.ya), ignoreCase = true) ) {
                binding?.radioEngNat?.check(R.id.engNat_1)
            } else {
                binding?.radioEngNat?.check(R.id.engNat_2)
            }
        }

        val items = arrayOf(getString(R.string.label_religion_1), getString(R.string.label_religion_2), getString(R.string.label_religion_3), getString(R.string.label_religion_4), getString(R.string.label_religion_5), getString(R.string.label_religion_6), getString(R.string.label_religion_7), getString(R.string.label_religion_8),
            getString(R.string.label_religion_9), getString(R.string.label_religion_10), getString(R.string.label_religion_11), getString(R.string.label_religion_12))
        val autoCompleteTextView = binding?.menuReligionOption as? MaterialAutoCompleteTextView
        autoCompleteTextView?.setSimpleItems(items)
        autoCompleteTextView?.dropDownHeight = 600
        val backgroundDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dpToPixels(8f, requireContext())
            setColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        autoCompleteTextView?.setDropDownBackgroundDrawable(backgroundDrawable)
        val religion = arguments?.getString("religion")
        if (religion != null) {
            if ( religion.equals(getString(R.string.label_religion_1), ignoreCase = true) ) {
                autoCompleteTextView?.setText(items[0], false)
            } else if ( religion.equals(getString(R.string.label_religion_2), ignoreCase = true) ) {
                autoCompleteTextView?.setText(items[1], false)
            } else if ( religion.equals(getString(R.string.label_religion_3), ignoreCase = true) ) {
                autoCompleteTextView?.setText(items[2], false)
            } else if ( religion.equals(getString(R.string.label_religion_4), ignoreCase = true) ) {
                autoCompleteTextView?.setText(items[3], false)
            } else if ( religion.equals(getString(R.string.label_religion_5), ignoreCase = true) ) {
                autoCompleteTextView?.setText(items[4], false)
            } else if ( religion.equals(getString(R.string.label_religion_6), ignoreCase = true) ) {
                autoCompleteTextView?.setText(items[5], false)
            } else if ( religion.equals(getString(R.string.label_religion_7), ignoreCase = true) ) {
                autoCompleteTextView?.setText(items[6], false)
            } else if ( religion.equals(getString(R.string.label_religion_8), ignoreCase = true) ) {
                autoCompleteTextView?.setText(items[7], false)
            } else if ( religion.equals(getString(R.string.label_religion_9), ignoreCase = true) ) {
                autoCompleteTextView?.setText(items[8], false)
            } else if ( religion.equals(getString(R.string.label_religion_10), ignoreCase = true) ) {
                autoCompleteTextView?.setText(items[9], false)
            } else if ( religion.equals(getString(R.string.label_religion_11), ignoreCase = true) ) {
                autoCompleteTextView?.setText(items[10], false)
            } else if ( religion.equals(getString(R.string.label_religion_12), ignoreCase = true) ) {
                autoCompleteTextView?.setText(items[11], false)
            }
        }
    }

    private fun setupDropdownReligion() {
        binding?.menuReligionOption?.setOnClickListener {
            hideKeyboard()
        }
    }

    private fun setupDatePicker() {
        binding?.birthDateEditTextLayout?.setEndIconOnClickListener {
            // Get the current date in milliseconds
            val today = System.currentTimeMillis()

            // Set up the calendar instance
            val calendar = Calendar.getInstance(TimeZone.getDefault())

            // Set up the start date (January 1, 1950)
            calendar.set(1950, Calendar.JANUARY, 1)
            val startDate = calendar.timeInMillis

            // Retrieve the birthDate argument
            val birthDate = arguments?.getString("birthDate", "") ?: ""
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val openAtDate: Long = if (birthDate.isNotEmpty()) {
                dateFormat.parse(birthDate)?.time ?: today
            } else {
                today
            }

            // Configure constraints to restrict the dates
            val constraints = CalendarConstraints.Builder()
                .setStart(startDate)
                .setEnd(today)
                .setOpenAt(openAtDate)
                .setValidator(DateValidatorPointBackward.now())
                .build()
            // Build the date picker
            val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.select_birth_date))
                .setSelection(openAtDate)
                .setCalendarConstraints(constraints)
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setTextInputFormat(dateFormat)
            val datePicker = datePickerBuilder.build()
            datePicker.show(childFragmentManager, "DATE_PICKER")
            datePicker.addOnPositiveButtonClickListener { selection ->
                val date = dateFormat.format(Date(selection))
                binding?.birthDateEditText?.setText(date)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as MainActivity).setBottomNavigationVisibility(true)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setBottomNavigationVisibility(false)
    }
}