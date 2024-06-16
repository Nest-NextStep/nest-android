package com.bangkit.nest.ui.main.assess

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nest.R
import com.bangkit.nest.data.remote.response.ResultsResponseItem

class TestResultsAdapter(private val testResults: List<ResultsResponseItem?>) :
    RecyclerView.Adapter<TestResultsAdapter.TestResultsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestResultsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_test_result, parent, false)
        return TestResultsViewHolder(view)
    }

    override fun onBindViewHolder(holder: TestResultsViewHolder, position: Int) {
        val testResult = testResults[position]
        holder.titleTextView.text = "Recommended Major ${position + 1}"
        holder.recommendedMajorTextView.text = testResult?.majorName
        holder.dateTextView.text = testResult?.userMajorDate
    }

    override fun getItemCount(): Int {
        return testResults.size
    }

    class TestResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.text_assessment_title)
        val recommendedMajorTextView: TextView = itemView.findViewById(R.id.text_recommended_major)
        val dateTextView: TextView = itemView.findViewById(R.id.text_assessment_date)
    }
}
