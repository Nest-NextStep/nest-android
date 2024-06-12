package com.bangkit.nest.ui.main.task.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.bangkit.nest.R
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@Composable
fun CalendarComponent(
    modifier: Modifier = Modifier,
    initialDate: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    var currentWeekStart by remember { mutableStateOf(selectedDate.with(DayOfWeek.MONDAY)) }
    val daysOfWeek = DayOfWeek.values().map { it.getDisplayName(TextStyle.SHORT, Locale.getDefault()) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        CalendarHeader(
            currentWeekStart = currentWeekStart,
            onPreviousWeek = { currentWeekStart = currentWeekStart.minusWeeks(1) },
            onNextWeek = { currentWeekStart = currentWeekStart.plusWeeks(1) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DaysOfWeekHeader(daysOfWeek, selectedDate)

        Spacer(modifier = Modifier.height(8.dp))

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
    currentWeekStart: LocalDate,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousWeek) {
            Icon(painter = painterResource(id = R.drawable.ic_back_arrow), contentDescription = "Previous Week")
        }

        Text(
            text = "${currentWeekStart.month.getDisplayName(TextStyle.FULL, Locale.getDefault())}, ${currentWeekStart.year}",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6463FF),
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        IconButton(onClick = onNextWeek) {
            Icon(painter = painterResource(id = R.drawable.ic_forward_arrow), contentDescription = "Next Week")
        }
    }
}

@Composable
fun DaysOfWeekHeader(daysOfWeek: List<String>, selectedDate: LocalDate) {
    val selectedDayOfWeek = selectedDate.dayOfWeek

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        daysOfWeek.forEachIndexed { index, day ->
            val dayOfWeek = DayOfWeek.of(index + 1)
            val isSelectedDay = dayOfWeek == selectedDayOfWeek

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
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (isSelectedDay) Color.Red else Color.Black
                )
            }
        }
    }
}

@Composable
fun CalendarDays(
    currentWeekStart: LocalDate,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now()
    val daysInWeek = 7
    val currentMonth = currentWeekStart.month

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(2.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(daysInWeek) { index ->
            val date = currentWeekStart.plusDays(index.toLong())
            val isSelected = date == selectedDate
            val isToday = date == today
            val isInCurrentMonth = date.month == currentMonth

            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .background(
                        if (isSelected) Color(0xFFEF9A9A) else Color.Transparent,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .border(
                        width = if (isToday) 2.dp else 0.dp,
                        color = if (isToday) Color.Black else Color.Transparent,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable {
                        onDateSelected(date)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    color = when {
                        !isInCurrentMonth -> Color.Gray
                        else -> Color.Black
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
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
