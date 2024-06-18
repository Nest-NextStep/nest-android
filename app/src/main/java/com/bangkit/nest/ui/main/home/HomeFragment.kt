package com.bangkit.nest.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.nest.R
import com.bangkit.nest.data.Result
import com.bangkit.nest.databinding.FragmentHomeBinding
import com.bangkit.nest.databinding.FragmentProfileBinding
import com.bangkit.nest.ui.main.catalog.ListMajorAdapter
import com.bangkit.nest.ui.main.profile.ProfileViewModel
import com.bangkit.nest.utils.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.randomMajorRecyclerView?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding?.testCard?.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_beginFragment)
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

        viewModel.stateProfile.observe(viewLifecycleOwner) {result ->
            when (result) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    val fullName = viewModel.profileData.value?.userFullname
                    if (fullName != null) {
                        binding?.usernameTextView?.text = fullName
                    } else {
                        binding?.usernameTextView?.text = viewModel.profileData.value?.username
                    }
                    val gender = viewModel.profileData.value?.userGender
                    if (gender != null) {
                        if (gender.equals("laki-laki", ignoreCase = true)) {
                            binding?.genderImageView?.setImageResource(R.drawable.ic_male_profile)
                        } else if (gender.equals("perempuan", ignoreCase = true)) {
                            binding?.genderImageView?.setImageResource(R.drawable.ic_female_profile)
                        } else {
                            binding?.genderImageView?.setImageResource(R.drawable.ic_profile)
                        }
                    } else {
                        binding?.genderImageView?.setImageResource(R.drawable.ic_profile)
                    }
                }
                is Result.Error -> {
                    binding?.genderImageView?.setImageResource(R.drawable.ic_profile)
                }
            }
        }

        viewModel.stateMajor.observe(viewLifecycleOwner) {result ->
            when (result) {
                is Result.Loading -> {
                    binding?.progressBar?.isVisible = true
                    binding?.randomMajorRecyclerView?.isVisible = false
                }
                is Result.Success -> {
                    binding?.progressBar?.isVisible = false
                    val adapter = ListMajorAdapter("another")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.getProfileData()
    }

}