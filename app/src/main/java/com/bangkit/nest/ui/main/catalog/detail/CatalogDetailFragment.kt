package com.bangkit.nest.ui.main.catalog.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.response.DetailMajorResponse
import com.bangkit.nest.databinding.FragmentCatalogDetailBinding
import com.bangkit.nest.ui.main.MainActivity
import com.bangkit.nest.utils.ViewModelFactory
import com.bumptech.glide.Glide

class CatalogDetailFragment : Fragment() {

    private var _binding: FragmentCatalogDetailBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<CatalogDetailViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCatalogDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.scrollView?.post {
            binding?.scrollView?.scrollTo(0, 0)
        }
        setUpAdapter()

        // Retrieve arguments
        val itemId = arguments?.getInt("itemId", 0) ?: 0
        getDetailMajor(itemId)
    }

    private fun getDetailMajor(id: Int) {
        viewModel.getDetailMajor(id).observe(viewLifecycleOwner) {result ->
            when (result) {
                is Result.Loading -> {
                    setupLoading()
                }
                is Result.Success -> {
                    setupSuccess(result.data)
                }
                is Result.Error -> {
                    setupError(result.error)
                }
            }
        }
    }

    private fun setUpAdapter() {
        binding?.apply {
            universityRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            jobsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            opinionsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupLoading() {
        binding?.apply {
            progressBar.isVisible = true

            errorTextView.isVisible = false
            majorImageView.isVisible = false
            textViewMajorName.isVisible = false
            textViewMajorDescription.isVisible = false
            universityListTextView.isVisible = false
            universityRecyclerView.isVisible = false
            potentialJobsTextView.isVisible = false
            jobsRecyclerView.isVisible = false
            opinionsTextView.isVisible = false
            opinionsRecyclerView.isVisible = false
        }
    }

    private fun setupError(errorMessage: String) {
        binding?.apply {
            progressBar.isVisible = false
            majorImageView.isVisible = false
            textViewMajorName.isVisible = false
            textViewMajorDescription.isVisible = false
            universityListTextView.isVisible = false
            universityRecyclerView.isVisible = false
            potentialJobsTextView.isVisible = false
            jobsRecyclerView.isVisible = false
            opinionsTextView.isVisible = false
            opinionsRecyclerView.isVisible = false

            errorTextView.isVisible = true
            errorTextView.text = errorMessage
        }
    }

    private fun setupSuccess(response: DetailMajorResponse) {
        binding?.apply {
            progressBar.isVisible = false
            errorTextView.isVisible = false

            majorImageView.isVisible = true
            Glide.with(requireView())
                .load(response.major?.majorCover)
                .into(majorImageView)

            Log.d("CatalogDetailFragment", response.toString())
            textViewMajorName.isVisible = true
            textViewMajorName.text = response.major?.majorName ?: ""

            textViewMajorDescription.isVisible = true
            textViewMajorDescription.text = response.major?.majorDescription ?: ""

            universityListTextView.isVisible = true
            universityRecyclerView.isVisible = true
            val adapterUniversity = ListUniversityAdapter()
            adapterUniversity.submitList(response.majorUniversity)
            universityRecyclerView.adapter = adapterUniversity

            potentialJobsTextView.isVisible = true
            jobsRecyclerView.isVisible = true
            val adapterJob = ListJobAdapter()
            adapterJob.submitList(response.majorJob)
            jobsRecyclerView.adapter = adapterJob

            opinionsTextView.isVisible = true
            opinionsRecyclerView.isVisible = true
            val adapterOpinion = ListOpinionAdapter()
            adapterOpinion.submitList(response.majorOpinion)
            opinionsRecyclerView.adapter = adapterOpinion
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as MainActivity).setBottomNavigationVisibility(true)
        (requireActivity() as MainActivity).setStatusBarTextColor(true)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setBottomNavigationVisibility(false)
        (requireActivity() as MainActivity).setStatusBarTextColor(false)
    }
}