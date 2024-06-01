package com.bangkit.nest.ui.auth.login

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.nest.databinding.FragmentLoginBinding
import com.bangkit.nest.utils.ViewModelFactory
import com.bangkit.nest.data.Result

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
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("LoginFragment", "Success")

//        val email = ""
//        val password = ""
//
//        viewModel.login(email, password).observe(viewLifecycleOwner) { result ->
//            if (result != null) {
//                when (result) {
//                    is Result.Loading -> {
////                        binding?.progressBar?.visibility = View.VISIBLE
//                    }
//                    is Result.Success -> {
////                        binding?.progressBar?.visibility = View.GONE
//
//                    }
//                    is Result.Error -> {
////                        binding?.progressBar?.visibility = View.GONE
//
//                    }
//                }
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
