package com.bangkit.nest.ui.main.assess.question

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nest.R

class OptionAdapter(
    private val options: List<String>,
    private val onOptionClicked: (String) -> Unit
) : RecyclerView.Adapter<OptionAdapter.OptionViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val optionButton: Button = itemView.findViewById(R.id.optionButton)

        init {
            optionButton.setOnClickListener {
                // Update the selected position and notify the adapter to refresh the views
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(selectedPosition)
                    selectedPosition = adapterPosition
                    notifyItemChanged(selectedPosition)
                }
                onOptionClicked(options[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_option, parent, false)
        return OptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.optionButton.text = options[position]
        holder.optionButton.isSelected = position == selectedPosition
    }

    override fun getItemCount() = options.size
}
