package com.bangkit.nest.ui.main.assess.question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.nest.R
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.response.OptionDataItem
import com.bangkit.nest.data.remote.response.QuestionsDataItem
import com.bangkit.nest.databinding.FragmentQuestionBinding
import com.bangkit.nest.ui.main.MainActivity
import com.bangkit.nest.utils.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson

class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuestionViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val categories = listOf("R", "I", "A", "S", "E", "C", "TIPI", "VCL")
    private var sections: MutableList<List<Pair<QuestionsDataItem, List<OptionDataItem>>>> = mutableListOf()
    private var currentSectionIndex = 0

    private val selectedAnswers = mutableMapOf<String, Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchQuestions()

        binding.previousButton.setOnClickListener {
            if (currentSectionIndex > 0) {
                currentSectionIndex--
                updateUI()
            }
        }
        binding.nextButton.setOnClickListener {
            if (areAllQuestionsAnswered()) {
                if (currentSectionIndex < sections.size - 1) {
                    currentSectionIndex++
                    updateUI()
                    binding.nestedScrollView.scrollTo(0, 0)
                } else {
                    submitResultsAndNavigate()
                }
            } else {
                Toast.makeText(context, "Please answer all questions before proceeding.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.toolbar.findViewById<View>(R.id.closeButton).setOnClickListener {
            showEndTestDialog()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showEndTestDialog()
            }
        })
    }

    private fun fetchQuestions() {
        categories.forEach { category ->
            viewModel.getQuestions(category).observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is Result.Loading -> {
                        setupLoading()
                    }
                    is Result.Success -> {
                        binding.errorTextView.isVisible = false
                        val questionsData = result.data.questionsData?.filterNotNull() ?: emptyList()
                        val optionData = result.data.optionData?.filterNotNull() ?: emptyList()
                        val questionsWithOptions = questionsData.map { question ->
                            question to optionData
                        }
                        sections.add(questionsWithOptions)
                        updateUI()
                    }
                    is Result.Error -> {
                        binding.errorTextView.text = result.error
                        binding.errorTextView.isVisible = true
                        binding.progressBar.isVisible = false
                    }
                }
            })
        }
    }

    private fun updateUI() {
        val section = sections[currentSectionIndex]

        binding.sectionTitleTextView.text = "Section ${currentSectionIndex + 1}"
        binding.questionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.questionsRecyclerView.adapter = QuestionAdapter(section, selectedAnswers) { questionId, optionCode ->
            selectedAnswers[questionId] = optionCode
        }

        binding.previousButton.visibility = if (currentSectionIndex == 0) View.INVISIBLE else View.VISIBLE
        binding.nextButton.text = if (currentSectionIndex == sections.size - 1) "Submit" else "Next"

        binding.progressBar.isVisible = false
        binding.errorTextView.isVisible = false
        binding.sectionTitleTextView.isVisible = true
        binding.nestedScrollView.isVisible = true
        binding.buttonContainer.isVisible = true
    }

    private fun areAllQuestionsAnswered(): Boolean {
        val currentSectionQuestions = sections[currentSectionIndex]
        return currentSectionQuestions.all { (question, _) -> selectedAnswers.containsKey(question.questionId) }
    }

    private fun showEndTestDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.end_assessment_test))
            .setMessage(getString(R.string.end_test_message))
            .setPositiveButton(getString(R.string.end_test)) { _, _ ->
                findNavController().navigate(R.id.action_questionFragment_to_navigation_assess,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.navigation_assess, true).build())
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
    }


    private fun setupLoading() {
        binding.apply {
            progressBar.isVisible = true

            sectionTitleTextView.isVisible = false
            nestedScrollView.isVisible = false
            buttonContainer.isVisible = false
            errorTextView.isVisible = false
        }
    }

    private fun submitResultsAndNavigate() {
        viewModel.submitResults(selectedAnswers).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Result.Success -> {
                    val gson = Gson()
                    val resultJson = gson.toJson(result.data)
                    val bundle = Bundle().apply {
                        putString("result", resultJson)
                    }
                    findNavController().navigate(R.id.action_questionFragment_to_resultFragment, bundle)
                }
                is Result.Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        })
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
