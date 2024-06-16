package com.bangkit.nest.ui.main.task.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
fun CalendarComponent(
    modifier: Modifier = Modifier,
    initialDate: Date = Calendar.getInstance().time,
    onDateSelected: (Date) -> Unit
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    val calendar = Calendar.getInstance().apply { time = selectedDate }
    var currentWeekStart by remember { mutableStateOf(getStartOfWeek(calendar)) }
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        CalendarHeader(
            currentWeekStart = currentWeekStart,
            onPreviousWeek = { currentWeekStart = changeWeek(currentWeekStart, -1) },
            onNextWeek = { currentWeekStart = changeWeek(currentWeekStart, 1) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Spacer(modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(Color.Gray))

        Spacer(modifier = Modifier.height(8.dp))

        DaysOfWeekHeader(daysOfWeek, selectedDate, currentWeekStart)

        CalendarDays(
            currentWeekStart = currentWeekStart,
            selectedDate = selectedDate,
            onDateSelected = {
                selectedDate = it
                onDateSelected(it)
            }
        )
    }
}

@Composable
fun CalendarHeader(
    currentWeekStart: Date,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit
) {
    val calendar = Calendar.getInstance().apply { time = currentWeekStart }
    val monthYearFormat = SimpleDateFormat("MMMM, yyyy", Locale.getDefault())
    val monthYear = monthYearFormat.format(calendar.time)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousWeek) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = "Previous Week",
                modifier = Modifier.size(16.dp)
            )
        }

        Text(
            text = monthYear,
            style = subTitleFormStyle,
            color = Color(0xFF6463FF),
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        IconButton(onClick = onNextWeek) {
            Icon(
                painter = painterResource(id = R.drawable.ic_forward_arrow),
                contentDescription = "Next Week",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun DaysOfWeekHeader(daysOfWeek: List<String>, selectedDate: Date, currentWeekStart: Date) {
    val selectedDayOfWeek = Calendar.getInstance().apply { time = selectedDate }.get(Calendar.DAY_OF_WEEK)
    val calendar = Calendar.getInstance().apply { time = currentWeekStart }
    val weekDays = List(7) { calendar.get(Calendar.DAY_OF_WEEK).also { calendar.add(Calendar.DAY_OF_MONTH, 1) } }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        daysOfWeek.forEachIndexed { index, day ->
            val isSelectedDay = weekDays[index] == selectedDayOfWeek

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
                    color = if (isSelectedDay) colorResource(R.color.purple) else Color.Black
                )
            }
        }
    }
}

@Composable
private fun CalendarDays(
    currentWeekStart: Date,
    selectedDate: Date,
    onDateSelected: (Date) -> Unit
) {
    val calendar = Calendar.getInstance()
    calendar.time = currentWeekStart

    val daysInWeek = List(7) { index ->
        val date = calendar.time
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        date
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(2.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(daysInWeek) { index, date ->
            val isSelected = date == selectedDate
            val isToday = isSameDay(date, Date())
            val isInCurrentMonth = isSameMonth(date, currentWeekStart)

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
                        onDateSelected(date)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = SimpleDateFormat("d", Locale.getDefault()).format(date),
                    style = bodyTextStyle,
                    color = when {
                        !isInCurrentMonth -> Color.Gray
                        else -> Color.Black
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarComponentPreview() {
    MaterialTheme {
        CalendarComponent(onDateSelected = {})
    }
}

// Helper functions
fun getStartOfWeek(calendar: Calendar): Date {
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    return calendar.time
}

fun changeWeek(date: Date, weeks: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.WEEK_OF_YEAR, weeks)
    return calendar.time
}

fun isSameDay(date1: Date, date2: Date): Boolean {
    val calendar1 = Calendar.getInstance().apply { time = date1 }
    val calendar2 = Calendar.getInstance().apply { time = date2 }
    return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
}

fun isSameMonth(date1: Date, date2: Date): Boolean {
    val calendar1 = Calendar.getInstance().apply { time = date1 }
    val calendar2 = Calendar.getInstance().apply { time = date2 }
    return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
}
