package com.bangkit.nest.ui.main.task

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.bangkit.nest.ui.urbanistFontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.nest.R
import com.bangkit.nest.data.remote.response.Task
import com.bangkit.nest.ui.Typography
import com.bangkit.nest.ui.h3TextStyle
import com.bangkit.nest.ui.main.MainActivity
import com.bangkit.nest.ui.main.task.components.CustomDurationDialogComponent
import com.bangkit.nest.ui.task.AddTaskViewModel
import com.bangkit.nest.ui.main.task.components.DurationComponent
import com.bangkit.nest.ui.main.task.components.Priority
import com.bangkit.nest.ui.main.task.components.PriorityComponent
import com.bangkit.nest.ui.subTitleFormStyle
import com.bangkit.nest.ui.task.components.TimePickerComponent
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar

class AddTaskFragment : Fragment() {

    private val addTaskViewModel: AddTaskViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme(
                    typography = Typography
                ){
                    AddTaskScreen()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setBottomNavigationVisibility(false)
    }


    @SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun AddTaskScreen() {
        val calendar = Calendar.getInstance()
        var dateText by remember { mutableStateOf("Select Date") }
        var taskStartTime by remember { mutableStateOf(LocalTime.now()) }
        var taskEndTime by remember { mutableStateOf(taskStartTime.plusHours(1)) }
        var titleText by remember { mutableStateOf(TextFieldValue("")) }
        var focusTimeText by remember { mutableStateOf(TextFieldValue("30")) }
        var breakTimeText by remember { mutableStateOf(TextFieldValue("10")) }
        var repeatSwitch by remember { mutableStateOf(false) }
        var taskDuration by remember { mutableLongStateOf(60) }

        var isTimeUpdated by remember { mutableStateOf(false) }
        var selectedPriority by mutableStateOf(Priority("Low", colorResource(R.color.green)))
        var isFocusedTitle by remember { mutableStateOf(false) }
        var isFocusedFocusTime by remember { mutableStateOf(false) }
        var isFocusedBreakTime by remember { mutableStateOf(false) }
        var showCustomDurationDialog by remember { mutableStateOf(false) }

        val focusRequester = FocusRequester()


        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Create New Task",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = h3TextStyle
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { /* Handle back navigation */ },
                            modifier = Modifier
                                .background(colorResource(R.color.purple), CircleShape)
                                .width(36.dp)
                                .height(36.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back_arrow),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.width(20.dp).height(20.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier.padding(16.dp).background(Color.White),
                    actions = {},
                )
            },
            modifier = Modifier.background(Color.White)
        ) {
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
                    // Date Picker
                    Text(
                        text = "Date",
                        style = subTitleFormStyle
                    )

                    Text(
                        text = "Select Date",
                        style = subTitleFormStyle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
                            .clickable {
                                DatePickerDialog(
                                    requireContext(),
                                    { _, year, month, dayOfMonth ->
                                        calendar.set(year, month, dayOfMonth)
                                        addTaskViewModel.setDate(calendar.timeInMillis)
                                        dateText = "${dayOfMonth}/${month + 1}/${year}"
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            }
                            .padding(8.dp)
                    )

                    // Title
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .border(
                                    width = 1.dp,
                                    color = if (isFocusedTitle) colorResource(R.color.purple_faded) else colorResource(R.color.gray_400),
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
                                    titleText = it
                                },
                                placeholder = {
                                    Text(
                                        text = "e.g. Study",
                                        color = colorResource(R.color.gray_400)
                                    )
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp)
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
                                is24hourFormat = false
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
                                is24hourFormat = false,
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
                                        color = if (isFocusedFocusTime) colorResource(R.color.purple_faded) else colorResource(R.color.gray_400),
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
                                    onValueChange = { focusTimeText = it },
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
                                        color = if (isFocusedBreakTime) colorResource(R.color.purple_faded) else colorResource(R.color.gray_400),
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
                                    onValueChange = { breakTimeText = it },
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
                            fontFamily = urbanistFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Switch(
                            checked = repeatSwitch,
                            onCheckedChange = { repeatSwitch = it },
                            colors = SwitchDefaults.colors(
                                uncheckedThumbColor = colorResource(R.color.gray_400), // Change color when unchecked
                                checkedThumbColor = colorResource(R.color.purple), // Change color when checked
                                uncheckedTrackColor = colorResource(R.color.purple_300), // Change track color when unchecked
                                checkedTrackColor = colorResource(R.color.purple_faded) // Change track color when checked
                            ),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    // Add Task Button
                    Button(
                        onClick = {
                            val task = Task(
                                id = 1,
                                title = titleText.text,
                                date = LocalDate.now(),
                                startTime = taskStartTime,
                                endTime = taskEndTime,
                                duration = addTaskViewModel.duration.value ?: 60,
                                focusTime = focusTimeText.text.toIntOrNull() ?: 30,
                                breakTime = breakTimeText.text.toIntOrNull() ?: 10,
                                priority = selectedPriority.displayText,
                                isRepeated = repeatSwitch,
                                isCompleted = false
                            )
                            addTaskViewModel.addTask(task)
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
}