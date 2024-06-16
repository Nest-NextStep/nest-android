package com.bangkit.nest.ui.main.task

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.fragment.findNavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bangkit.nest.R
import com.bangkit.nest.data.Result
import com.bangkit.nest.databinding.FragmentCompletedTaskBinding
import com.bangkit.nest.ui.main.MainActivity
import com.bangkit.nest.ui.main.task.components.TaskComponent
import com.bangkit.nest.utils.ViewModelFactory

class CompletedTaskFragment : Fragment() {

    private var _binding: FragmentCompletedTaskBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val taskViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[TaskViewModel::class.java]

        _binding = FragmentCompletedTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.toolbar.findViewById<View>(R.id.back_button).setOnClickListener {
            findNavController().popBackStack()
        }

        // Fetch completed tasks when the view is created
        taskViewModel.fetchUserCompletedTasks()

        taskViewModel.completedTasks.observe(viewLifecycleOwner) { result ->
            binding.progressBar.isVisible = result is Result.Loading
            binding.composeView.isVisible = result !is Result.Loading

            if (result is Result.Success) {
                val data = result.data
                binding.imgNoCompletedTasks.isVisible = data.isEmpty()
                binding.composeView.isVisible = data.isNotEmpty()
            } else {
                binding.imgNoCompletedTasks.isVisible = false
            }
        }

        val composeView: ComposeView = binding.composeView
        composeView.setContent {
            CompletedTaskList(taskViewModel)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setBottomNavigationVisibility(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (activity as? MainActivity)?.setBottomNavigationVisibility(true)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CompletedTaskList(taskViewModel: TaskViewModel) {
    val taskResult = taskViewModel.completedTasks.observeAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        when (val result = taskResult.value) {
            is Result.Loading -> {
                // Handled in fragment
            }
            is Result.Success -> {
                val tasks = result.data
                LazyColumn {
                    items(tasks) { task ->
                        TaskComponent(
                            task = task,
                            onEdit = { taskId -> /* can not be triggered due to isCompleted true */ },
                            onComplete = { taskId -> /* can not be triggered due to isCompleted true */ },
                            onPomodoro = { taskId -> /* can not be triggered due to isCompleted true */ }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
            is Result.Error -> {
                // Display an error message
                Text("Error: ${result.error}")
            }
            else -> {}
        }
    }
}
