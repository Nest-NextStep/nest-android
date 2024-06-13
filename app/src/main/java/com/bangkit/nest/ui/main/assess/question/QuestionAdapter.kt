package com.bangkit.nest.ui.main.assess.question

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nest.databinding.ItemQuestionBinding

class QuestionAdapter(
    private val questions: List<Question>
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(private val binding: ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: Question) {
            binding.questionTextView.text = question.questionText
            binding.optionsRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
            binding.optionsRecyclerView.adapter = OptionAdapter(question.options) {
                // Handle option click
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    override fun getItemCount() = questions.size
}
