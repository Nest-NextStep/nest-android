package com.bangkit.nest.ui.main.catalog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
        binding?.scrollView?.post {
            binding?.scrollView?.scrollTo(0, 0)
        }
        setupSearch()
        setupAdapter()
        observeViewModel()
        viewModel.getAllMajor()
    }

    private fun setupAdapter() {
        binding?.apply {
            allMajorRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            recommendedMajorRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    setupLoading()
                }
                is Result.Success -> {
                    setupSuccess(viewModel.majorRecommended.value ?: emptyList(), viewModel.majorsAll.value ?: emptyList())
                }
                is Result.Error -> {
                    setupError(result.error)
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSearch() {
        binding?.root?.setOnTouchListener { _, _ ->
            hideKeyboard()
            val query = binding?.searchEditText?.text
            if (query.isNullOrEmpty()) {
                binding?.searchEditText?.clearFocus()
            }
            false
        }

        binding?.searchEditTextLayout?.setEndIconOnClickListener {
            viewModel.getAllMajor()
            hideKeyboard()
            binding?.searchEditText?.clearFocus()
            binding?.searchEditText?.text = null
        }

        binding?.searchEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    findMajor(query)
                } else {
                    viewModel.getAllMajor()
                }
            }
        })


        binding?.searchEditText?.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard()
                val query = binding?.searchEditText?.text
                if (query.isNullOrEmpty()) {
                    binding?.searchEditText?.clearFocus()
                }
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
            recommendedMajorRecyclerView.isVisible = true
            setListMajorData(recommendedMajorRecyclerView, recommendedMajors, "recommended")

            if (recommendedMajors.isNotEmpty()) {
                noRecommendedMajorTextView.isVisible = false
            } else {
                noRecommendedMajorTextView.isVisible = true
            }

            allMajorTextView.isVisible = true
            if (anotherMajors.isNotEmpty()) {
                allMajorRecyclerView.isVisible = true
                noAnotherMajorTextView.isVisible = false
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
        val adapter = ListMajorAdapter(viewType, this)
        adapter.submitList(majors)
        recyclerView.adapter = adapter
    }

    fun saveMajorId(id: Int) {
        viewModel.saveMajorId(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}