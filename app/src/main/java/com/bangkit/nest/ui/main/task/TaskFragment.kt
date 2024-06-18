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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bangkit.nest.R
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.response.Task
import com.bangkit.nest.databinding.FragmentTaskBinding
import com.bangkit.nest.ui.main.task.components.TaskComponent
import com.bangkit.nest.ui.main.task.components.CompletedComponent
import com.bangkit.nest.utils.ViewModelFactory

class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val taskViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[TaskViewModel::class.java]

        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addTaskButton.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_addTaskFragment)
        }

        // Fetch tasks when the view is created
        taskViewModel.fetchUserTasks()

        taskViewModel.tasks.observe(viewLifecycleOwner) { result ->
            binding.progressBar.isVisible = result is Result.Loading
            binding.textTask.isVisible = result !is Result.Loading
            binding.addTaskButton.isVisible = result !is Result.Loading
            binding.composeView.isVisible = result !is Result.Loading

            if (result is Result.Success) {
                val data = result.data.tasks?.filterNotNull() ?: emptyList()
                binding.imgNoTasks.isVisible = data.isEmpty()
                binding.composeView.isVisible = data.isNotEmpty()
            } else {
                binding.imgNoTasks.isVisible = false
            }
        }

        binding.icCalendarToday.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_calendarTaskFragment)
        }

        val completedComponentView: ComposeView = binding.completedComponentView
        completedComponentView.setContent {
            CompletedComponentView(taskViewModel) {
                findNavController().navigate(R.id.action_taskFragment_to_completedTaskFragment)
            }
        }

        val composeView: ComposeView = binding.composeView
        composeView.setContent {
            TaskList(taskViewModel, onEditClick = { taskId ->
                findNavController().navigate(R.id.action_taskFragment_to_updateTaskFragment, Bundle().apply {
                    putInt("taskId", taskId)
                })
            }, onPomodoroClick = { task ->
                findNavController().navigate(R.id.action_taskFragment_to_pomodoroFragment, Bundle().apply {
                    putInt("taskId", task.id)
                    putInt("focusTime", task.focusTime)
                    putInt("breakTime", task.breakTime)
                    putInt("duration", task.duration)
                    putString("taskTitle", task.title)
                })
            })
        }

        // Observe the result of setting a task as completed
        taskViewModel.setTaskCompletedResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(context, "Task marked as completed!", Toast.LENGTH_SHORT).show()
                    // Refresh tasks
                    taskViewModel.fetchUserTasks()
                    // Clear the result
                    taskViewModel.clearSetTaskCompletedResult()
                }
                is Result.Error -> {
                    Toast.makeText(context, "Failed to mark task as completed: ${result.error}", Toast.LENGTH_SHORT).show()
                    // Clear the result
                    taskViewModel.clearSetTaskCompletedResult()
                }
                else -> {}
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CompletedComponentView(taskViewModel: TaskViewModel, onClick: () -> Unit) {
    val taskResult = taskViewModel.tasks.observeAsState()

    when (val result = taskResult.value) {
        is Result.Success -> {
            val completedCount = result.data.completedCount ?: 0
            val totalCount = result.data.totalCount ?: 0
            CompletedComponent(completedCount = completedCount, totalCount = totalCount, onClick = onClick)
        }
        else -> {}
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskList(taskViewModel: TaskViewModel, onEditClick: (Int) -> Unit, onPomodoroClick: (Task) -> Unit) {
    val taskResult = taskViewModel.tasks.observeAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        when (val result = taskResult.value) {
            is Result.Loading -> {
                // Handled in fragment
            }
            is Result.Success -> {
                val tasks = result.data.tasks?.filterNotNull() ?: emptyList()
                LazyColumn {
                    items(tasks) { task ->
                        TaskComponent(
                            task = task,
                            onEdit = { taskId -> onEditClick(taskId) },
                            onComplete = { taskId -> taskViewModel.setTaskCompleted(taskId) },
                            onPomodoro = { _ -> onPomodoroClick(task) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
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
