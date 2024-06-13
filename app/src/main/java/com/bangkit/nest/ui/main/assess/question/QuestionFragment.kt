package com.bangkit.nest.ui.main.assess.question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.nest.R
import com.bangkit.nest.databinding.FragmentQuestionBinding
import com.bangkit.nest.ui.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

data class Question(
    val questionText: String,
    val options: List<String>
)

class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!

    private val sections = listOf(
        listOf(
            Question("Mau tes aja sih gimana kalau dia pertanyaannya cukup panjang gitu sih udah oke", listOf("Tidak Suka", "Cukup Tidak Suka", "Netral", "Cukup Suka", "Suka")),
            Question("Section 1 - Question 2", listOf("Tidak Suka", "Cukup Tidak Suka", "Netral", "Cukup Suka", "Suka"))
        ),
        listOf(
            Question("Section 2 - Question 1", listOf("Tidak Suka", "Cukup Tidak Suka", "Netral", "Cukup Suka", "Suka")),
            Question("Section 2 - Question 2", listOf("Tidak Suka", "Cukup Tidak Suka", "Netral", "Cukup Suka", "Suka"))
        ),
        listOf(
            Question("Section 3 - Question 1", listOf("Tidak Suka", "Cukup Tidak Suka", "Netral", "Cukup Suka", "Suka")),
            Question("Section 3 - Question 2", listOf("Tidak Suka", "Cukup Tidak Suka", "Netral", "Cukup Suka", "Suka"))
        )
        // Add more sections as needed
    )

    private var currentSectionIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        binding.questionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set up click listeners for navigation buttons
        binding.previousButton.setOnClickListener {
            if (currentSectionIndex > 0) {
                currentSectionIndex--
                updateUI()
            }
        }
        binding.nextButton.setOnClickListener {
            if (currentSectionIndex < sections.size - 1) {
                currentSectionIndex++
                updateUI()
                // Scroll to the top when updating UI
                binding.nestedScrollView.scrollTo(0, 0)
            } else {
                // Handle submit action
                // e.g., navigate to a summary or result fragment, or submit the answers
            }
        }

        // Handle close button click
        binding.toolbar.findViewById<View>(R.id.closeButton).setOnClickListener {
            showEndTestDialog()
        }

        // Add a callback for the back button press
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showEndTestDialog()
            }
        })

        // Initial UI update
        updateUI()
    }

    private fun updateUI() {
        val section = sections[currentSectionIndex]

        binding.sectionTitleTextView.text = "Section ${currentSectionIndex + 1}"
        binding.questionsRecyclerView.adapter = QuestionAdapter(section)

        // Hide the previous button on the first section
        binding.previousButton.visibility = if (currentSectionIndex == 0) View.INVISIBLE else View.VISIBLE

        // Change the next button text to "Submit" on the last section
        binding.nextButton.text = if (currentSectionIndex == sections.size - 1) "Submit" else "Next"
    }

    private fun showEndTestDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.end_assessment_test))
            .setMessage(getString(R.string.end_test_message))
            .setPositiveButton(getString(R.string.end_test)) { _, _ ->
                findNavController().navigate(R.id.action_questionFragment_to_navigation_assess)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
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
