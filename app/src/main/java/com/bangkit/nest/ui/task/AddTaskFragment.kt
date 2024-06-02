package com.bangkit.nest.ui.task

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.nest.ui.task.components.DurationComponent
import com.bangkit.nest.ui.task.components.Priority
import com.bangkit.nest.ui.task.components.PriorityComponent
import com.bangkit.nest.ui.task.components.TimePickerComponent
import java.time.LocalTime
import java.util.Calendar

class AddTaskFragment : Fragment() {

    private val addTaskViewModel: AddTaskViewModel by viewModels()
    private var selectedPriority by mutableStateOf(Priority("Low", Color.Green))

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    AddTaskScreen()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun AddTaskScreen() {
        val calendar = Calendar.getInstance()
        var dateText by remember { mutableStateOf("Select Date") }
        var taskStartTime by remember { mutableStateOf(LocalTime.now()) }
        var taskEndTime by remember { mutableStateOf(taskStartTime.plusHours(1))}
        var titleText by remember { mutableStateOf(TextFieldValue("")) }
        var focusTimeText by remember { mutableStateOf(TextFieldValue("30")) }
        var breakTimeText by remember { mutableStateOf(TextFieldValue("10")) }
        var repeatSwitch by remember { mutableStateOf(false) }
        val taskDuration by remember { mutableLongStateOf(60) }
        var isTimeUpdated by remember { mutableStateOf(false) }


        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Create New Task")

            // Date Picker
            Text(text = "Date")
            Text(
                text = dateText,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
                    .clickable {
                        DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                            calendar.set(year, month, dayOfMonth)
                            addTaskViewModel.setDate(calendar.timeInMillis)
                            dateText = "${dayOfMonth}/${month + 1}/${year}"
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
                    }
                    .padding(8.dp)
            )

            // Title
            Text(text = "Title")
            TextField(
                value = titleText,
                onValueChange = { titleText = it },
                placeholder = { Text(text = "e.g. Study") },
                modifier = Modifier.fillMaxWidth()
            )

            // Start Time and End Time Picker
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Start Time"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TimePickerComponent(
                        time = taskStartTime,
                        is24hourFormat = false
                    ) { snappedTime ->
                        taskStartTime = snappedTime
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "End Time"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TimePickerComponent(
                        time = taskEndTime,
                        is24hourFormat = false,
                        isTimeUpdated = isTimeUpdated
                    ) { snappedTime ->
                        taskEndTime = snappedTime
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Duration",
                )

                Text(
                    text = getFormattedDuration(taskStartTime, taskEndTime)
                )
            }

            DurationComponent(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                durationList = listOf(30, 60, 90, 0),
                defaultDuration = taskDuration
            ) { duration ->
                if (duration == 0L) {
                    //showDialogCustomDuration = true
                } else {
                    taskEndTime = taskStartTime.plusMinutes(duration)
                    isTimeUpdated = !isTimeUpdated
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Focus Time
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Focus Time")
                    TextField(
                        value = focusTimeText,
                        onValueChange = { focusTimeText = it },
                        placeholder = { Text(text = "30 min") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Spacer
                Spacer(modifier = Modifier.width(16.dp))

                // Break Time
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Break Time")
                    TextField(
                        value = breakTimeText,
                        onValueChange = { breakTimeText = it },
                        placeholder = { Text(text = "10 min") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Priority
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            ) {
                Text(
                    text = "Priority",
                )
            }
            PriorityComponent(
                defaultSortTask = selectedPriority,
                onSelect = { selectedPriority = it }
            )

            // Repeat
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Repeat")
                Switch(
                    checked = repeatSwitch,
                    onCheckedChange = { repeatSwitch = it },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Add Task Button
            Button(
                onClick = {
                    val task = Task(
                        title = titleText.text,
                        date = addTaskViewModel.date.value ?: calendar.timeInMillis,
                        startTime = taskStartTime,
                        endTime = taskEndTime,
                        duration = addTaskViewModel.duration.value ?: 60,
                        focusTime = focusTimeText.text.toIntOrNull() ?: 30,
                        breakTime = breakTimeText.text.toIntOrNull() ?: 10,
                        priority = selectedPriority.displayText,
                        repeat = repeatSwitch
                    )
                    addTaskViewModel.addTask(task)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Add Task")
            }
        }
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormattedDuration(
        startTime: LocalTime,
        endTime: LocalTime
    ): String {
        val taskDuration = endTime.toSecondOfDay() - startTime.toSecondOfDay()

        val hours = taskDuration / 3600
        val minutes = (taskDuration % 3600) / 60

        val hoursString = if (hours == 1) "hour" else "hours"

        return when {
            hours > 0 && minutes > 0 -> String.format("%dh %02dm", hours, minutes)
            hours > 0 -> String.format("%d $hoursString", hours)
            else -> String.format("%dmin", minutes)
        }

    }
}
