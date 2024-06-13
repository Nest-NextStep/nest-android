package com.bangkit.nest.ui.main.assess

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.nest.R
import com.bangkit.nest.databinding.FragmentAssessBinding

class AssessFragment : Fragment() {

    private var _binding: FragmentAssessBinding? = null
    private val binding get() = _binding!!
    private lateinit var assessViewModel: AssessViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        assessViewModel = ViewModelProvider(this)[AssessViewModel::class.java]
        _binding = FragmentAssessBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up Toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)

        // Set up RecyclerView
        binding.recyclerViewTestResults.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewTestResults.adapter = TestResultsAdapter(getDummyData())

        // Handle button click
        binding.buttonTakeAssessment.setOnClickListener {
            // Navigate to BeginFragment
            findNavController().navigate(R.id.action_assessFragment_to_beginFragment)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDummyData(): List<TestResult> {
        return listOf(
            TestResult("Assessment 1", "Your recommended Major"),
            TestResult("Assessment 2", "Another recommended Major")
        )
    }
}
