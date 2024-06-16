package com.bangkit.nest.ui.main.assess.question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bangkit.nest.R
import com.bangkit.nest.ui.main.MainActivity
import com.google.android.material.button.MaterialButton

class BeginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_begin, container, false)

        view.findViewById<MaterialButton>(R.id.cancelButton).setOnClickListener {
            // Handle cancel button click
            findNavController().popBackStack()
        }

        view.findViewById<MaterialButton>(R.id.beginButton).setOnClickListener {
            // Navigate to QuestionFragment
            findNavController().navigate(R.id.action_beginFragment_to_questionFragment)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setBottomNavigationVisibility(false)
    }

    override fun onPause() {
        super.onPause()
        (activity as? MainActivity)?.setBottomNavigationVisibility(true)
    }
}

