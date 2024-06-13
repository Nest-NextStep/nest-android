package com.bangkit.nest.ui.main.profile

import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.nest.R
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.response.MajorItem
import com.bangkit.nest.data.remote.response.ProfileData
import com.bangkit.nest.databinding.FragmentProfileBinding
import com.bangkit.nest.ui.main.catalog.detail.ListUniversityAdapter
import com.bangkit.nest.utils.ViewModelFactory
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
            Toast.makeText(requireContext(), "Button clicked", Toast.LENGTH_SHORT).show()
        }
    }
    private fun observeViewModel() {
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
//        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        layoutManager.gravity = Gravity.CENTER_HORIZONTAL
//        binding?.majorsRecyclerView?.layoutManager.layoutManager = layoutManager

//        val layoutManager = FlexboxLayoutManager(context).apply {
//            justifyContent = JustifyContent.CENTER
//            alignItems = AlignItems.CENTER
//            flexDirection = FlexDirection.ROW
//            flexWrap = FlexWrap.WRAP
//        }
//        binding?.majorsRecyclerView?.layoutManager = layoutManager

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

            if (profileData?.userGender != null) {
                if (profileData.userGender.equals("laki-laki", ignoreCase = true)) {
                    profileImage.setImageResource(R.drawable.ic_male_profile)
                } else if (profileData.userGender.equals("perempuan", ignoreCase = true)) {
                    profileImage.setImageResource(R.drawable.ic_female_profile)
                } else {
                        profileImage.setImageResource(R.drawable.ic_profile)
                }
            } else {
                profileImage.setImageResource(R.drawable.ic_profile)
            }
            profileImage.isVisible = true

            majorsTextView.isVisible = true
            majorsRecyclerView.isVisible = true
            val adapter = ListRecommendedMajorAdapter()
            adapter.submitList(majorData)
            majorsRecyclerView.adapter = adapter
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

            errorTextView.isVisible = true
            errorTextView.text = errorMessage
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.reloadProfileDataIfNeeded() // Reload profile data every time the fragment is resumed
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}