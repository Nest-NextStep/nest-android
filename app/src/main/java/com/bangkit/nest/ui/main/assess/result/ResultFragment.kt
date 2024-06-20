package com.bangkit.nest.ui.main.assess.result

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bangkit.nest.R
import com.bangkit.nest.databinding.FragmentResultBinding
import com.bangkit.nest.ui.main.MainActivity
import com.bangkit.nest.data.remote.response.TestResultResponse
import com.google.gson.Gson

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.setBottomNavigationVisibility(false)

        binding.doneButton.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_navigation_assess)
        }

        val resultJson = arguments?.getString("result")
        Log.d("ResultFragment", "Received resultJson: $resultJson")

        val gson = Gson()
        val result: TestResultResponse? = gson.fromJson(resultJson, TestResultResponse::class.java)
        Log.d("ResultFragment", "Parsed result: $result")

        if (result != null) {
            displayResult(result)
        }
    }

    private fun displayResult(result: TestResultResponse) {
        binding.textViewMajorName.text = result.majorName
        binding.textViewMajorDescription.text = result.majorDescription
        binding.recommendedMajorCard.visibility = View.VISIBLE
        binding.recommendedMajorCard.setOnClickListener {
            val bundle = Bundle()
            result.majorId?.let { id -> bundle.putInt("itemId", id) }
            findNavController().navigate(R.id.action_result_to_catalogDetail, bundle)
        }

        binding.errorTextView.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setBottomNavigationVisibility(false)
    }

    override fun onPause() {
        super.onPause()
        (activity as? MainActivity)?.setBottomNavigationVisibility(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
