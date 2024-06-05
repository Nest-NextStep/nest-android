package com.bangkit.nest.ui.main.catalog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nest.R
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.response.MajorItem
import com.bangkit.nest.databinding.FragmentCatalogBinding
import com.bangkit.nest.utils.ViewModelFactory

class CatalogFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<CatalogViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearch()
        getAllMajor()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSearch() {
        binding?.root?.setOnTouchListener { _, _ ->
            hideKeyboard()
            binding?.searchEditText?.clearFocus()
            false
        }

        binding?.searchEditText?.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val query = binding?.searchEditText?.text
                if (!query.isNullOrEmpty()) {
                    findMajor(query.toString())
                } else {
                    getAllMajor()
                }

                hideKeyboard()
                binding?.searchEditText?.clearFocus()
                true
            } else {
                false
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = requireActivity().currentFocus
        if (currentFocusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    private fun findMajor(query: String) {
        viewModel.findMajor(query.toString()).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        setupLoading()
                    }
                    is Result.Success -> {
                        setupSuccess(result.data.majorRecommended, result.data.majorsAll)
                    }
                    is Result.Error -> {
                        setupError(result.error)
                    }
                }
            }
        }
    }
    private fun getAllMajor() {
        viewModel.getAllMajor().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        setupLoading()
                    }
                    is Result.Success -> {
                        setupSuccess(result.data.majorRecommended, result.data.majorsAll)
                    }
                    is Result.Error -> {
                        setupError(result.error)
                    }
                }
            }
        }
    }

    private fun setupLoading() {
        binding?.apply {
            progressBar.isVisible = true

            errorTextView.isVisible = false
            noRecommendedMajorTextView.isVisible = false
            recommendedMajorTextView.isVisible = false
            recommendedMajorRecyclerView.isVisible = false
            noAnotherMajorTextView.isVisible = false
            allMajorTextView.isVisible = false
            allMajorRecyclerView.isVisible = false
        }

    }

    private fun setupSuccess(recommendedMajors: List<MajorItem>, anotherMajors: List<MajorItem>) {
        binding?.apply {
            progressBar.isVisible = false

            recommendedMajorTextView.isVisible = true
            recommendedMajorRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            if (recommendedMajors.isNotEmpty()) {
                recommendedMajorRecyclerView.isVisible = true
                setListMajorData(recommendedMajorRecyclerView, recommendedMajors, "recommended")
            } else {
                recommendedMajorRecyclerView.isVisible = false
                noRecommendedMajorTextView.isVisible = true
                // Adjust constraints for allMajorTextView
                val layoutParams = allMajorTextView.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.topToBottom = R.id.noRecommendedMajorTextView
                allMajorTextView.layoutParams = layoutParams
            }

            allMajorTextView.isVisible = true
            allMajorRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            if (anotherMajors.isNotEmpty()) {
                allMajorRecyclerView.isVisible = true
                setListMajorData(allMajorRecyclerView, anotherMajors, "another")
            } else {
                allMajorRecyclerView.isVisible = false
                noAnotherMajorTextView.isVisible = true
            }

        }
    }

    private fun setupError(errorMessage: String) {
        binding?.apply {
            progressBar.isVisible = false
            noRecommendedMajorTextView.isVisible = false
            recommendedMajorTextView.isVisible = false
            recommendedMajorRecyclerView.isVisible = false
            noAnotherMajorTextView.isVisible = false
            allMajorTextView.isVisible = false
            allMajorRecyclerView.isVisible = false

            errorTextView.isVisible = true
            errorTextView.text = errorMessage
        }
    }

    private fun setListMajorData(recyclerView: RecyclerView, majors: List<MajorItem>, viewType: String) {
        val adapter = ListMajorAdapter(viewType)
        adapter.submitList(majors)
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}