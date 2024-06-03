package com.bangkit.nest.ui.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bangkit.nest.R
import com.bangkit.nest.databinding.FragmentProfileBinding
import com.bangkit.nest.ui.main.MainActivity
import com.bangkit.nest.utils.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
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

        (activity as? MainActivity)?.setBottomNavigationVisibility(false)

        binding?.editProfileButton?.setOnClickListener {
            Toast.makeText(requireContext(), "Button clicked", Toast.LENGTH_SHORT).show()
        }

        binding?.logoutButton?.setOnClickListener {
            handleLogout()
        }

        var name = getString(R.string.hello) + ", Amy!"
        binding?.nameTextView?.text = name

        val imgUrl = "https://i.ibb.co.com/1z68FvJ/woman-2-1.png"
        Glide.with(requireView())
            .load(imgUrl)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(binding?.profileImage!!)
    }

    private fun handleLogout() {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}