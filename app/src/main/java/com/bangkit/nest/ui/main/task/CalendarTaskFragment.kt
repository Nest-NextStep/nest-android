package com.bangkit.nest.ui.main.task

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bangkit.nest.R
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.response.Task
import com.bangkit.nest.databinding.FragmentCalendarTaskBinding
import com.bangkit.nest.ui.main.MainActivity
import com.bangkit.nest.ui.main.task.components.TaskComponent
import com.bangkit.nest.ui.main.task.components.MonthlyCalendarComponent
import com.bangkit.nest.utils.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class CalendarTaskFragment : Fragment() {

    private var _binding: FragmentCalendarTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskViewModel: TaskViewModel
    private val groupedTasks = mutableMapOf<String, List<Task>>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        taskViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[TaskViewModel::class.java]

        _binding = FragmentCalendarTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.toolbar.findViewById<View>(R.id.back_button).setOnClickListener {
            findNavController().popBackStack()
        }

        // Fetch user tasks when the view is created
        taskViewModel.fetchUserTasks()

        taskViewModel.tasks.observe(viewLifecycleOwner) { result ->
            binding.progressBar.isVisible = result is Result.Loading
            binding.calendarView.isVisible = result !is Result.Loading
            binding.composeView.isVisible = result !is Result.Loading

            when (result) {
                is Result.Success -> {
                    val tasks = result.data.tasks?.sortedBy { it?.date } ?: emptyList()
                    groupedTasks.clear()
                    tasks.forEach { task ->
                        val dateStr = task?.date // Date as string
                        if (dateStr != null) {
                            val taskList = groupedTasks[dateStr]?.toMutableList() ?: mutableListOf()
                            taskList.add(task)
                            groupedTasks[dateStr] = taskList
                        }
                    }
                    if (tasks.isEmpty()) {
                        binding.imgNoTasks.isVisible = true
                        binding.composeView.isVisible = false
                    } else {
                        binding.imgNoTasks.isVisible = false
                        updateTaskListForDate(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
                    }
                }
                is Result.Error -> {
                    binding.imgNoTasks.isVisible = true
                    binding.composeView.isVisible = false
                }
                else -> {}
            }

            val calendarView: ComposeView = binding.calendarView
            calendarView.setContent {
                MonthlyCalendarComponent(
                    tasks = groupedTasks.keys.mapNotNull { dateStr ->
                        try {
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            null
                        }
                    },
                    onDateSelected = { date ->
                        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
                        updateTaskListForDate(formattedDate)
                    }
                )
            }
        }

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTaskListForDate(date: String) {
        val tasks = groupedTasks[date] ?: emptyList()
        val composeView: ComposeView = binding.composeView
        composeView.setContent {
            TaskList(tasks)
        }
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
fun TaskList(tasks: List<Task>) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(tasks) { task ->
                TaskComponent(
                    task = task,
                    onEdit = { /* No edit in this view */ },
                    onComplete = { /* No complete in this view */ },
                    onPomodoro = { /* No pomodoro in this view */ }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
