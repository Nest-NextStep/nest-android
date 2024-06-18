package com.bangkit.nest.ui.main.task

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.fragment.findNavController
import com.bangkit.nest.ui.main.MainActivity
import com.bangkit.nest.R
import com.bangkit.nest.ui.h3TextStyle
import com.bangkit.nest.ui.main.task.components.PomodoroProgressBar
import com.bangkit.nest.ui.pomodoroTimerText
import com.bangkit.nest.ui.subTitleFormStyle
import kotlinx.coroutines.delay

class PomodoroFragment : Fragment() {

    private var taskId: Int = 0
    private var focusTime: Int = 0
    private var breakTime: Int = 0
    private var duration: Int = 0
    private var taskTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            taskId = it.getInt("taskId")
            focusTime = it.getInt("focusTime")
            breakTime = it.getInt("breakTime")
            duration = it.getInt("duration")
            taskTitle = it.getString("taskTitle")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Hide bottom navigation bar
        (activity as? MainActivity)?.setBottomNavigationVisibility(false)

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    PomodoroContent(
                        taskId = taskId,
                        focusTime = focusTime,
                        breakTime = breakTime,
                        duration = duration,
                        taskTitle = taskTitle,
                        onBack = {
                            findNavController().popBackStack()
                        }
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.setBottomNavigationVisibility(true)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroContent(
    taskId: Int,
    focusTime: Int,
    breakTime: Int,
    duration: Int,
    taskTitle: String?,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val currentView = LocalView.current

    var isTimerCompleted by remember { mutableStateOf(false) }
    var totalTime by remember { mutableLongStateOf(duration.toLong() * 60L) }
    var timeLeft by remember { mutableLongStateOf(totalTime) }
    var isPaused by remember { mutableStateOf(false) }
    var isReset by remember { mutableStateOf(false) }
    var isFocusTime by remember { mutableStateOf(true) }
    val focusDuration = focusTime.toLong() * 60L
    val breakDuration = breakTime.toLong() * 60L
    var remainingFocusTime by remember { mutableLongStateOf(focusDuration) }
    var remainingBreakTime by remember { mutableLongStateOf(breakDuration) }

    // Keep screen on
    DisposableEffect(Unit) {
        currentView.keepScreenOn = true
        onDispose {
            currentView.keepScreenOn = false
        }
    }

    // Flicker animation + toggle keep screen on
    val alphaValue = remember { Animatable(1f) }
    LaunchedEffect(isPaused) {
        currentView.keepScreenOn = !isPaused

        if (isPaused && (timeLeft != totalTime)) {
            alphaValue.animateTo(
                targetValue = 0.2f,
                animationSpec = infiniteRepeatable(
                    tween(
                        1000,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            alphaValue.snapTo(1f)
        }
    }

    // Timer logic
    LaunchedEffect(isReset) {
        if (isReset) {
            isPaused = true
            isFocusTime = true
            remainingFocusTime = focusDuration
            remainingBreakTime = breakDuration
            timeLeft = totalTime
            alphaValue.snapTo(1f)
        }
    }

    LaunchedEffect(
        key1 = timeLeft,
        key2 = isPaused
    ) {
        while (timeLeft > 0 && !isPaused) {
            delay(1000L)
            timeLeft--

            if (isFocusTime) {
                remainingFocusTime--
                if (remainingFocusTime == 0L) {
                    isFocusTime = false
                    remainingBreakTime = breakDuration
                }
            } else {
                remainingBreakTime--
                if (remainingBreakTime == 0L) {
                    isFocusTime = true
                    remainingFocusTime = focusDuration
                }
            }
        }

        if (timeLeft == 0L && !isPaused) {
            isTimerCompleted = true
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
            ),
            title = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center),
                    text = taskTitle ?: "Pomodoro Timer",
                    style = h3TextStyle
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { onBack() },
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
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp)
                .background(Color.White),
            actions = {
                IconButton(onClick = {
                    onBack()
                }) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        )
    }) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White), // Set background color to white
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isTimerCompleted) {
                // Rounded pill indicator
                RoundedPillIndicator(isFocusTime = isFocusTime)

                Spacer(modifier = Modifier.height(48.dp))
            }

            Box(contentAlignment = Alignment.Center) {
                Text(
                    modifier = Modifier.alpha(alphaValue.value),
                    text = if (isTimerCompleted) {
                        stringResource(R.string.completed)
                    } else {
                        getDurationTimeStamp(timeLeft)
                    },
                    style = pomodoroTimerText,
                    color = colorResource(R.color.black)
                )
                val calcProgress = 100f - ((timeLeft.toFloat() / totalTime.toFloat()) * 100f)
                if (calcProgress >= 99) {
                    isTimerCompleted = true
                }
                if (!isTimerCompleted) {
                    PomodoroProgressBar(
                        progress = calcProgress,
                        progressBarColor = if (isFocusTime) colorResource(R.color.soft_red) else colorResource(R.color.green),
                        backgroundProgressBarColor = if (isFocusTime) colorResource(R.color.faded_red) else colorResource(R.color.faded_green)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            AnimatedVisibility(!isTimerCompleted) {
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    FloatingActionButton(
                        onClick = {
                            isReset = true
                        },
                        shape = RoundedCornerShape(16.dp),
                        elevation = FloatingActionButtonDefaults.elevation(4.dp),
                        containerColor = if (isFocusTime) colorResource(R.color.faded_red) else colorResource(R.color.faded_green),
                        contentColor = Color.LightGray
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            tint = colorResource(R.color.black),
                            contentDescription = null
                        )
                    }

                    FloatingActionButton(
                        onClick = {
                            isPaused = !isPaused
                            isReset = false
                        },
                        shape = RoundedCornerShape(16.dp),
                        elevation = FloatingActionButtonDefaults.elevation(4.dp),
                        containerColor = if (isFocusTime) colorResource(R.color.soft_red) else colorResource(R.color.green),
                        contentColor = Color.LightGray
                    ) {
                        Icon(
                            imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            tint = colorResource(R.color.black),
                            contentDescription = null
                        )
                    }

                    // Skip Button
                    FloatingActionButton(
                        onClick = {
                            if (isFocusTime) {
                                timeLeft -= remainingFocusTime
                                remainingFocusTime = 0L
                                isFocusTime = false
                                remainingBreakTime = breakDuration
                            } else {
                                timeLeft -= remainingBreakTime
                                remainingBreakTime = 0L
                                isFocusTime = true
                                remainingFocusTime = focusDuration
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        elevation = FloatingActionButtonDefaults.elevation(4.dp),
                        containerColor = if (isFocusTime) colorResource(R.color.faded_red) else colorResource(R.color.faded_green),
                        contentColor = Color.LightGray
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_skip),
                            tint = colorResource(R.color.black),
                            contentDescription = null
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(innerPadding.calculateTopPadding()))
        }
    }
}

@Composable
fun RoundedPillIndicator(isFocusTime: Boolean) {
    val backgroundColor = if (isFocusTime) colorResource(R.color.faded_red) else colorResource(R.color.faded_green)
    val borderColor = if (isFocusTime) colorResource(R.color.soft_red) else colorResource(R.color.green)
    val text = if (isFocusTime) "Focus" else "Break"

    Box(
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(50))
            .border(2.dp, borderColor, shape = RoundedCornerShape(50))
            .padding(horizontal = 24.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = colorResource(R.color.black), style = subTitleFormStyle)
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun getDurationTimeStamp(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
}
