package com.bangkit.nest.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.nest.R
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.local.pref.UserPreference
import com.bangkit.nest.data.local.pref.dataStore
import com.bangkit.nest.data.repository.UserPrefRepository
import com.bangkit.nest.databinding.FragmentHomeBinding
import com.bangkit.nest.ui.main.catalog.ListMajorAdapter
import com.bangkit.nest.utils.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var userPrefRepository: UserPrefRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val userPreference = UserPreference.getInstance(requireContext().dataStore)
        userPrefRepository = UserPrefRepository.getInstance(userPreference)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.randomMajorRecyclerView?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding?.testCard?.setOnClickListener {
            lifecycleScope.launch {
                val isProfileCompleted = userPrefRepository.getIsProfileCompleted()
                if (isProfileCompleted) {
                    findNavController().navigate(R.id.action_home_to_beginFragment)
                } else {
                    showProfileIncompleteDialog()
                }
            }
        }

        binding?.addTaskCard?.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_addTaskFragment)
        }

        binding?.checkCalendarCard?.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_calendarTaskFragment)
        }

        binding?.seeAllButton?.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_CatalogFragment)
        }

        viewModel.stateProfile.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> { /* Handle loading state */ }
                is Result.Success -> {
                    val fullName = viewModel.profileData.value?.userFullname
                    binding?.usernameTextView?.text = fullName ?: viewModel.profileData.value?.username
                    val gender = viewModel.profileData.value?.userGender
                    binding?.genderImageView?.setImageResource(
                        when {
                            gender.equals("laki-laki", ignoreCase = true) -> R.drawable.ic_male_profile
                            gender.equals("perempuan", ignoreCase = true) -> R.drawable.ic_female_profile
                            else -> R.drawable.ic_profile
                        }
                    )
                }
                is Result.Error -> {
                    binding?.genderImageView?.setImageResource(R.drawable.ic_profile)
                }
            }
        }

        viewModel.stateMajor.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding?.progressBar?.isVisible = true
                    binding?.randomMajorRecyclerView?.isVisible = false
                }
                is Result.Success -> {
                    binding?.progressBar?.isVisible = false
                    val adapter = ListMajorAdapter("another", fragmentHome = this)
                    adapter.submitList(viewModel.majorData.value)
                    binding?.randomMajorRecyclerView?.adapter = adapter
                    binding?.randomMajorRecyclerView?.isVisible = true
                }
                is Result.Error -> {
                    binding?.progressBar?.isVisible = false
                    binding?.randomMajorRecyclerView?.isVisible = false
                }
            }
        }
    }

    private fun showProfileIncompleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.incomplete_profile))
            .setMessage(getString(R.string.complete_profile))
            .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
            .create()
            .apply {
                setOnShowListener {
                    getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)?.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.purple)
                    )
                }
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.getProfileData()
    }
}
