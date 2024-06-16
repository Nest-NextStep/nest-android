package com.bangkit.nest.ui.main.task.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.bangkit.nest.R
import com.bangkit.nest.ui.bodyTextStyle
import com.bangkit.nest.ui.bodyTextStyleMedium
import com.bangkit.nest.ui.subTitleFormStyle
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MonthlyCalendarComponent(
    modifier: Modifier = Modifier,
    initialDate: Date = Calendar.getInstance().time,
    tasks: List<Date> = emptyList(),
    onDateSelected: (Date) -> Unit
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    val calendar = Calendar.getInstance().apply { time = selectedDate }
    var currentMonthStart by remember { mutableStateOf(getStartOfMonth(calendar)) }
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        MonthlyCalendarHeader(
            currentMonthStart = currentMonthStart,
            onPreviousMonth = { currentMonthStart = changeMonth(currentMonthStart, -1) },
            onNextMonth = { currentMonthStart = changeMonth(currentMonthStart, 1) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Spacer(modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(Color.Gray))

        Spacer(modifier = Modifier.height(8.dp))

        DaysOfWeekHeader(daysOfWeek)

        CalendarDays(
            currentMonthStart = currentMonthStart,
            selectedDate = selectedDate,
            tasks = tasks,
            onDateSelected = {
                selectedDate = it
                onDateSelected(it)
            }
        )
    }
}

@Composable
fun MonthlyCalendarHeader(
    currentMonthStart: Date,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val calendar = Calendar.getInstance().apply { time = currentMonthStart }
    val monthYearFormat = SimpleDateFormat("MMMM, yyyy", Locale.getDefault())
    val monthYear = monthYearFormat.format(calendar.time)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = "Previous Month",
                modifier = Modifier.size(16.dp)
            )
        }

        Text(
            text = monthYear,
            style = subTitleFormStyle,
            color = Color(0xFF6463FF),
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        IconButton(onClick = onNextMonth) {
            Icon(
                painter = painterResource(id = R.drawable.ic_forward_arrow),
                contentDescription = "Next Month",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun DaysOfWeekHeader(daysOfWeek: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        daysOfWeek.forEach { day ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .background(Color.Transparent)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    style = bodyTextStyleMedium,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
private fun CalendarDays(
    currentMonthStart: Date,
    selectedDate: Date,
    tasks: List<Date>,
    onDateSelected: (Date) -> Unit
) {
    val calendar = Calendar.getInstance()
    calendar.time = currentMonthStart

    val daysInMonth = mutableListOf<Date>()
    val startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
    val precedingEmptyDays = (1..startDayOfWeek).map { Date(0) }  // Dates to fill the grid for empty days

    while (calendar.get(Calendar.MONTH) == currentMonthStart.month) {
        daysInMonth.add(calendar.time)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    val totalDays = precedingEmptyDays + daysInMonth

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(2.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(totalDays) { date ->
            val isSelected = date == selectedDate
            val isToday = isSameDay(date, Date())
            val isInCurrentMonth = isSameMonth(date, currentMonthStart)
            val hasTask = tasks.any { isSameDay(it, date) }

            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .background(
                        if (isSelected) colorResource(R.color.purple_400) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = if (isToday) 1.dp else 0.dp,
                        color = if (isToday) colorResource(R.color.purple_400) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        if (date.time != 0L) {
                            onDateSelected(date)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (date.time != 0L) {
                        Text(
                            text = SimpleDateFormat("d", Locale.getDefault()).format(date),
                            style = bodyTextStyle,
                            color = when {
                                !isInCurrentMonth -> Color.Gray
                                else -> Color.Black
                            },
                        )
                        if (hasTask) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .background(colorResource(R.color.purple), CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MonthlyCalendarComponentPreview() {
    MaterialTheme {
        MonthlyCalendarComponent(onDateSelected = {})
    }
}

@Preview(showBackground = true)
@Composable
fun MonthlyCalendarComponentWithTasksPreview() {
    val sampleTasks = listOf(
        Calendar.getInstance().apply { set(2023, Calendar.JUNE, 10) }.time,
        Calendar.getInstance().apply { set(2023, Calendar.JUNE, 15) }.time,
        Calendar.getInstance().apply { set(2023, Calendar.JUNE, 20) }.time
    )

    MaterialTheme {
        MonthlyCalendarComponent(
            initialDate = Calendar.getInstance().apply { set(2023, Calendar.JUNE, 1) }.time,
            tasks = sampleTasks,
            onDateSelected = {}
        )
    }
}


// Helper functions
fun getStartOfMonth(calendar: Calendar): Date {
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    return calendar.time
}

fun changeMonth(date: Date, months: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.MONTH, months)
    return calendar.time
}

private val Date.month: Int
    get() = Calendar.getInstance().apply { time = this@month }.get(Calendar.MONTH)

private val Date.dayOfWeek: Int
    get() = Calendar.getInstance().apply { time = this@dayOfWeek }.get(Calendar.DAY_OF_WEEK)

