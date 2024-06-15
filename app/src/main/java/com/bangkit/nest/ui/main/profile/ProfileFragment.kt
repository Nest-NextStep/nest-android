package com.bangkit.nest.ui.main.profile

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.nest.R
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.response.MajorItem
import com.bangkit.nest.data.remote.response.ProfileData
import com.bangkit.nest.databinding.FragmentProfileBinding
import com.bangkit.nest.ui.main.catalog.detail.ListUniversityAdapter
import com.bangkit.nest.utils.ViewModelFactory
import com.bangkit.nest.utils.convertDate
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        setupLogout()
        setupAdapter()
        setupEditProfile()
    }

    private fun setupEditProfile() {
        binding?.editProfileButton?.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_editProfile, bundle)
        }
    }
    private fun observeViewModel() {
        viewModel.getProfileData()
        viewModel.state.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    setupLoading()
                }
                is Result.Success -> {
                    setupSuccess(viewModel.profileData.value, viewModel.majorData.value ?: emptyList())
                }
                is Result.Error -> {
                    setupError(result.error)
                }
            }
        }
    }

    private fun setupLogout() {
        binding?.logoutButton?.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.logout_confirmation))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.logout()
                }
                .setNegativeButton(getString(R.string.no), null)
                .show()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
        }
    }

    private fun setupAdapter() {
        binding?.majorsRecyclerView?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupLoading() {
        binding?.apply {
            progressBar.isVisible = true

            errorTextView.isVisible = false
            editProfileButton.isVisible = false
            usernameTextView.isVisible = false
            profileImage.isVisible = false
            majorsTextView.isVisible = false
            majorsRecyclerView.isVisible = false
            noMajorsTextView.isVisible = false

            userDataCard.isVisible = false
            fullNameTitleTextView.isVisible = false
            fullNameTextView.isVisible = false
            emailTitleTextView.isVisible = false
            emailTextView.isVisible = false
            birthDateTitleTextView.isVisible = false
            birthDateTextView.isVisible = false
            schoolTitleTextView.isVisible = false
            schoolTextView.isVisible = false
            religionTitleTextView.isVisible = false
            religionTextView.isVisible = false
            engNatTitleTextView.isVisible = false
            engNatTextView.isVisible = false

            noUserDataCard.isVisible = false
            notCompletedTextView.isVisible = false
            completeTextView.isVisible = false
        }
    }

    private fun setupSuccess(profileData: ProfileData? = null, majorData: List<MajorItem>) {
        binding?.apply {
            progressBar.isVisible = false
            errorTextView.isVisible = false

            editProfileButton.isVisible = true

            usernameTextView.isVisible = true
            val usernameText = "Hello, ${profileData?.username}!"
            usernameTextView.text = usernameText

            majorsTextView.isVisible = true
            if (majorData.isNotEmpty()) {
                noMajorsTextView.isVisible = false
                majorsRecyclerView.isVisible = true
                val adapter = ListRecommendedMajorAdapter()
                adapter.submitList(majorData)
                majorsRecyclerView.adapter = adapter
            } else {
                majorsRecyclerView.isVisible = false
                noMajorsTextView.isVisible = true
            }

            if (profileData?.userGender != null) {
                if (profileData.userGender.equals("laki-laki", ignoreCase = true)) {
                    profileImage.setImageResource(R.drawable.ic_male_profile)
                } else if (profileData.userGender.equals("perempuan", ignoreCase = true)) {
                    profileImage.setImageResource(R.drawable.ic_female_profile)
                } else {
                        profileImage.setImageResource(R.drawable.ic_profile)
                }
                bundle.putString("gender", profileData.userGender)
                bundle.putString("education", profileData.userEducation)

                userDataCard.isVisible = true
                fullNameTitleTextView.isVisible = true
                fullNameTextView.isVisible = true
                fullNameTextView.text = profileData.userFullname
                bundle.putString("fullName", profileData.userFullname)

                emailTitleTextView.isVisible = true
                emailTextView.isVisible = true
                emailTextView.text = profileData.userEmail

                birthDateTitleTextView.isVisible = true
                birthDateTextView.isVisible = true
                val birthDate = profileData.userBirthDate?.let { convertDate(it, false) }
                birthDateTextView.text = birthDate
                bundle.putString("birthDate", birthDate)

                schoolTitleTextView.isVisible = true
                schoolTextView.isVisible = true
                schoolTextView.text = profileData.userSchool
                bundle.putString("school", profileData.userSchool)

                religionTitleTextView.isVisible = true
                religionTextView.isVisible = true
                religionTextView.text = profileData.userReligion
                bundle.putString("religion", profileData.userReligion)

                engNatTitleTextView.isVisible = true
                engNatTextView.isVisible = true
                engNatTextView.text = if (profileData.userEngNat.equals(getString(R.string.ya), ignoreCase = true)) {
                    getString(R.string.yes)
                } else {
                    getString(R.string.no)
                }
                bundle.putString("engNat", profileData.userEngNat)

                noUserDataCard.isVisible = false
                notCompletedTextView.isVisible = false
                completeTextView.isVisible = false
            } else {
                profileImage.setImageResource(R.drawable.ic_profile)

                noUserDataCard.isVisible = true
                notCompletedTextView.isVisible = true
                completeTextView.isVisible = true

                userDataCard.isVisible = false
                fullNameTitleTextView.isVisible = false
                fullNameTextView.isVisible = false

                emailTitleTextView.isVisible = false
                emailTextView.isVisible = false

                birthDateTitleTextView.isVisible = false
                birthDateTextView.isVisible = false

                schoolTitleTextView.isVisible = false
                schoolTextView.isVisible = false
            }
            profileImage.isVisible = true
        }
    }

    private fun setupError(errorMessage: String) {
        binding?.apply {
            progressBar.isVisible = false

            editProfileButton.isVisible = false
            usernameTextView.isVisible = false
            profileImage.isVisible = false
            majorsTextView.isVisible = false
            majorsRecyclerView.isVisible = false
            noMajorsTextView.isVisible = false

            userDataCard.isVisible = false
            fullNameTitleTextView.isVisible = false
            fullNameTextView.isVisible = false
            emailTitleTextView.isVisible = false
            emailTextView.isVisible = false
            birthDateTitleTextView.isVisible = false
            birthDateTextView.isVisible = false
            schoolTitleTextView.isVisible = false
            schoolTextView.isVisible = false
            religionTitleTextView.isVisible = false
            religionTextView.isVisible = false
            engNatTitleTextView.isVisible = false
            engNatTextView.isVisible = false

            noUserDataCard.isVisible = false
            notCompletedTextView.isVisible = false
            completeTextView.isVisible = false

            errorTextView.isVisible = true
            errorTextView.text = errorMessage
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.reloadProfileData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}