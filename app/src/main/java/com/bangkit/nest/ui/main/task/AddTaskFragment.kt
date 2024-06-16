package com.bangkit.nest.ui.main.task

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bangkit.nest.R
import com.bangkit.nest.data.remote.request.TaskRequest
import com.bangkit.nest.ui.Typography
import com.bangkit.nest.ui.h3TextStyle
import com.bangkit.nest.ui.main.MainActivity
import com.bangkit.nest.ui.main.task.components.*
import com.bangkit.nest.ui.subTitleFormStyle
import com.bangkit.nest.ui.main.task.components.TimePickerComponent
import com.bangkit.nest.data.Result
import com.bangkit.nest.utils.ViewModelFactory
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*

class AddTaskFragment : Fragment() {

    private val taskViewModel: TaskViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme(
                    typography = Typography
                ) {
                    AddTaskScreen(taskViewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setBottomNavigationVisibility(false)
    }

    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun AddTaskScreen(viewModel: TaskViewModel) {
        val context = LocalContext.current
        val createTaskResult by viewModel.createTaskResult.observeAsState()
        val calendar = Calendar.getInstance()
        var taskStartTime by remember { mutableStateOf(LocalTime.now()) }
        var taskEndTime by remember { mutableStateOf(taskStartTime.plusHours(1)) }
        var titleText by remember { mutableStateOf(TextFieldValue("")) }
        var focusTimeText by remember { mutableStateOf(TextFieldValue("30")) }
        var breakTimeText by remember { mutableStateOf(TextFieldValue("10")) }
        var repeatSwitch by remember { mutableIntStateOf(0) }
        var taskDuration by remember { mutableLongStateOf(60) }
        var isTimeUpdated by remember { mutableStateOf(false) }
        var selectedPriority by remember { mutableStateOf(Priority("low", Color(0xFFBEE797))) }
        var isFocusedTitle by remember { mutableStateOf(false) }
        var isFocusedFocusTime by remember { mutableStateOf(false) }
        var isFocusedBreakTime by remember { mutableStateOf(false) }
        var showCustomDurationDialog by remember { mutableStateOf(false) }
        var selectedDate by remember { mutableStateOf(calendar.time) }

        val focusRequester = FocusRequester()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Create New Task",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 64.dp),
                            style = h3TextStyle
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { findNavController().popBackStack() },
                            modifier = Modifier
                                .background(colorResource(R.color.purple), CircleShape)
                                .width(36.dp)
                                .height(36.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back_arrow),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .background(Color.White),
                    actions = {},
                )
            },
            modifier = Modifier.background(Color.White)
        ) { it ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding())
                    .background(colorResource(R.color.white)),
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                        .padding(16.dp)
                        .background(colorResource(R.color.white)),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Calendar Component
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(8.dp)
                    ) {
                        CalendarComponent(
                            initialDate = selectedDate,
                            onDateSelected = {
                                selectedDate = it
                            }
                        )
                    }

                    // Title
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text(
                            text = "Title",
                            style = subTitleFormStyle,
                            modifier = Modifier.padding(start = 8.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .border(
                                    width = 1.dp,
                                    color = if (isFocusedTitle) colorResource(R.color.purple_500) else colorResource(R.color.gray_400),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(selectedPriority.color, RoundedCornerShape(8.dp))
                        ) {
                            val containerColor = colorResource(R.color.white)
                            TextField(
                                value = titleText,
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = containerColor,
                                    unfocusedContainerColor = containerColor,
                                    disabledContainerColor = containerColor,
                                    cursorColor = colorResource(R.color.purple),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                ),
                                onValueChange = {
                                    if (it.text.length <= 50) {
                                        titleText = it
                                    }
                                },
                                placeholder = {
                                    Text(
                                        text = "e.g. Study",
                                        color = colorResource(R.color.gray_400)
                                    )
                                },
                                shape = RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 8.dp,
                                    bottomEnd = 8.dp,
                                    bottomStart = 0.dp
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp)
                                    .onFocusChanged {
                                        isFocusedTitle = it.isFocused
                                    }
                                    .focusRequester(focusRequester),
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Sentences,
                                    imeAction = ImeAction.Done
                                )
                            )
                        }
                    }

                    // Start Time and End Time Picker
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp, 8.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Start Time",
                                color = Color(0xFF66A928),
                                style = subTitleFormStyle
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TimePickerComponent(
                                time = taskStartTime,
                                is24hourFormat = true
                            ) { snappedTime ->
                                taskStartTime = snappedTime
                            }
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "End Time",
                                color = colorResource(R.color.soft_red),
                                style = subTitleFormStyle
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TimePickerComponent(
                                time = taskEndTime,
                                is24hourFormat = true,
                                isTimeUpdated = isTimeUpdated
                            ) { snappedTime ->
                                taskEndTime = snappedTime
                            }
                        }
                    }

                    // Custom Duration Dialog
                    if (showCustomDurationDialog) {
                        CustomDurationDialogComponent(
                            onClose = { showCustomDurationDialog = false },
                            onSelect = { time ->
                                val duration = time.toSecondOfDay() / 60L
                                taskEndTime = taskStartTime.plusMinutes(duration)
                                isTimeUpdated = !isTimeUpdated
                            }
                        )
                    }

                    // Duration
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Duration",
                            style = subTitleFormStyle
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
                            showCustomDurationDialog = true
                        } else {
                            taskEndTime = taskStartTime.plusMinutes(duration)
                            isTimeUpdated = !isTimeUpdated
                        }
                    }

                    // Focus and Break Time
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Focus Time",
                                style = subTitleFormStyle,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = if (isFocusedFocusTime) colorResource(R.color.purple_500) else colorResource(R.color.gray_400),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(start = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.menu_task_secondary),
                                    contentDescription = null,
                                    modifier = Modifier.scale(0.5f),
                                    tint = colorResource(R.color.gray_400)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                TextField(
                                    value = focusTimeText,
                                    onValueChange = {
                                        focusTimeText = it
                                    },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        disabledContainerColor = Color.Transparent,
                                        cursorColor = colorResource(R.color.purple),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .onFocusChanged {
                                            isFocusedFocusTime = it.isFocused
                                        }
                                        .focusRequester(focusRequester)
                                        .height(50.dp),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        imeAction = ImeAction.Done
                                    )
                                )
                                Text(
                                    text = "min",
                                    color = colorResource(R.color.gray_400),
                                    modifier = Modifier.padding(end = 25.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Break Time",
                                style = subTitleFormStyle,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isFocusedBreakTime) colorResource(R.color.purple_500) else colorResource(R.color.gray_400),
                                    )
                                    .padding(start = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.menu_task_secondary),
                                    contentDescription = null,
                                    modifier = Modifier.scale(0.5f),
                                    tint = colorResource(R.color.gray_400)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                TextField(
                                    value = breakTimeText,
                                    onValueChange = {
                                        breakTimeText = it
                                    },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        disabledContainerColor = Color.Transparent,
                                        cursorColor = colorResource(R.color.purple),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .onFocusChanged {
                                            isFocusedBreakTime = it.isFocused
                                        }
                                        .focusRequester(focusRequester)
                                        .height(50.dp),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        imeAction = ImeAction.Done
                                    )
                                )
                                Text(
                                    text = "min",
                                    color = colorResource(R.color.gray_400),
                                    modifier = Modifier.padding(end = 25.dp)
                                )
                            }
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
                            style = subTitleFormStyle
                        )
                    }
                    PriorityComponent(
                        defaultSortTask = selectedPriority,
                        onSelect = { selectedPriority = it }
                    )

                    // Repeat
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Repeat",
                            style = subTitleFormStyle
                        )
                        Switch(
                            checked = repeatSwitch == 1,
                            onCheckedChange = { repeatSwitch = if (it) 1 else 0 },
                            colors = SwitchDefaults.colors(
                                uncheckedThumbColor = colorResource(R.color.gray_400),
                                checkedThumbColor = colorResource(R.color.purple),
                                uncheckedTrackColor = colorResource(R.color.purple_300),
                                checkedTrackColor = colorResource(R.color.purple_400)
                            ),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    // Add Task Button
                    Button(
                        onClick = {
                            if (validateInputs(titleText.text, focusTimeText.text, breakTimeText.text, taskStartTime, taskEndTime, context)) {
                                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                val taskRequest = TaskRequest(
                                    taskName = titleText.text,
                                    taskDate = dateFormat.format(selectedDate),
                                    taskStartTime = "${taskStartTime.hour}:${taskStartTime.minute}:00",
                                    taskEndTime = "${taskEndTime.hour}:${taskEndTime.minute}:00",
                                    taskDuration = taskDuration.toInt(),
                                    taskFocusTime = focusTimeText.text.toIntOrNull() ?: 30,
                                    taskBreakTime = breakTimeText.text.toIntOrNull() ?: 10,
                                    taskPriority = selectedPriority.displayText,
                                    taskRepeat = repeatSwitch,
                                    isCompleted = 0
                                )
                                viewModel.submitNewTask(taskRequest)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.purple)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Add Task",
                            style = subTitleFormStyle
                        )
                    }
                }
            }
        }

        // Handle createTaskResult state
        createTaskResult?.let { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(context, "Task created successfully!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                    viewModel.clearCreateTaskResult() // Clear the result to avoid showing the toast again
                }
                is Result.Error -> {
                    Toast.makeText(context, "Failed to create task: ${result.error}", Toast.LENGTH_SHORT).show()
                    viewModel.clearCreateTaskResult() // Clear the result to avoid showing the toast again
                }
                is Result.Loading -> {
                    // Show loading indicator if needed
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as MainActivity).setBottomNavigationVisibility(true)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun validateInputs(
        title: String,
        focusTime: String,
        breakTime: String,
        startTime: LocalTime,
        endTime: LocalTime,
        context: android.content.Context
    ): Boolean {
        when {
            title.isEmpty() -> {
                Toast.makeText(context, "Title must be filled.", Toast.LENGTH_SHORT).show()
                return false
            }
            title.length > 50 -> {
                Toast.makeText(context, "Title must be less than 50 characters.", Toast.LENGTH_SHORT).show()
                return false
            }
            focusTime.isEmpty() -> {
                Toast.makeText(context, "Focus Time must be filled.", Toast.LENGTH_SHORT).show()
                return false
            }
            focusTime.toIntOrNull() == null || focusTime.toInt() < 5 -> {
                Toast.makeText(context, "Focus Time must be at least 5 minutes.", Toast.LENGTH_SHORT).show()
                return false
            }
            breakTime.isEmpty() -> {
                Toast.makeText(context, "Break Time must be filled.", Toast.LENGTH_SHORT).show()
                return false
            }
            breakTime.toIntOrNull() == null || breakTime.toInt() < 5 -> {
                Toast.makeText(context, "Break Time must be at least 5 minutes.", Toast.LENGTH_SHORT).show()
                return false
            }
            startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) < 5 -> {
                Toast.makeText(context, "Duration must be at least 5 minutes.", Toast.LENGTH_SHORT).show()
                return false
            }
            else -> return true
        }
    }
}