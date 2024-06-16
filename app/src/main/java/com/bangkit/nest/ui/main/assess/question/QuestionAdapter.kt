package com.bangkit.nest.ui.main.assess.question

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nest.databinding.ItemQuestionBinding
import com.bangkit.nest.data.remote.response.OptionDataItem
import com.bangkit.nest.data.remote.response.QuestionsDataItem

class QuestionAdapter(
    private val questions: List<Pair<QuestionsDataItem, List<OptionDataItem>>>,
    private val selectedAnswers: Map<String, Int>,
    private val onOptionSelected: (String, Int) -> Unit
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(private val binding: ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(questionWithOptions: Pair<QuestionsDataItem, List<OptionDataItem>>) {
            val (question, options) = questionWithOptions
            binding.questionTextView.text = question.questionText
            val selectedOptionCode = selectedAnswers[question.questionId]
            binding.optionsRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
            binding.optionsRecyclerView.adapter = OptionAdapter(options, selectedOptionCode) { selectedOption ->
                question.questionId?.let { selectedOption.optionCode?.let { it1 ->
                    onOptionSelected(it,
                        it1
                    )
                } }
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
