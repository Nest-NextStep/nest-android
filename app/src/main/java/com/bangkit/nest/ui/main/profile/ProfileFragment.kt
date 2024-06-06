package com.bangkit.nest.ui.main.profile

//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.activity.viewModels
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.ViewModelProvider
//import com.bangkit.nest.databinding.FragmentProfileBinding
//import com.bangkit.nest.ui.main.MainViewModel
//import com.bangkit.nest.utils.ViewModelFactory
//
//class ProfileFragment : Fragment() {
//
//    private var _binding: FragmentProfileBinding? = null
//    private val binding get() = _binding!!
//
//    private val viewModel by viewModels<MainViewModel> {
//        ViewModelFactory.getInstance(requireContext())
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val profileViewModel =
//            ViewModelProvider(this).get(ProfileViewModel::class.java)
//
//        _binding = FragmentProfileBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.textProfile
//        profileViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//        return root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.logoutButton.setOnClickListener {
//            viewModel.logout()
//        }
//
//    }
//
//        override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}