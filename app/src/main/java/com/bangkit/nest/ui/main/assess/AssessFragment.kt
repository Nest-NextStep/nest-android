package com.bangkit.nest.ui.main.assess

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.nest.R
import com.bangkit.nest.data.Result
import com.bangkit.nest.databinding.FragmentAssessBinding
import com.bangkit.nest.utils.ViewModelFactory

class AssessFragment : Fragment() {

    private var _binding: FragmentAssessBinding? = null
    private val binding get() = _binding!!
    private lateinit var assessViewModel: AssessViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        assessViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[AssessViewModel::class.java]
        _binding = FragmentAssessBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up Toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)

        // Set up RecyclerView
        binding.recyclerViewTestResults.layoutManager = LinearLayoutManager(context)

        // Fetch and observe data
        assessViewModel.getUserMajorResults().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewTestResults.visibility = View.GONE
                    binding.imgNoResults.visibility = View.GONE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.errorTextView.isVisible = false

                    val data = result.data.resultResponse ?: emptyList()
                    if (data.isEmpty()) {
                        binding.recyclerViewTestResults.visibility = View.GONE
                        binding.imgNoResults.visibility = View.VISIBLE
                    } else {
                        binding.recyclerViewTestResults.visibility = View.VISIBLE
                        binding.imgNoResults.visibility = View.GONE
                        binding.recyclerViewTestResults.adapter = TestResultsAdapter(data)
                    }
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerViewTestResults.visibility = View.GONE
                    binding.imgNoResults.visibility = View.GONE
                    binding.errorTextView.text = result.error
                    binding.errorTextView.visibility = View.VISIBLE
                }
            }
        }

        // Button click navigates to BeginFragment
        binding.buttonTakeAssessment.setOnClickListener {
            findNavController().navigate(R.id.action_assessFragment_to_beginFragment)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
