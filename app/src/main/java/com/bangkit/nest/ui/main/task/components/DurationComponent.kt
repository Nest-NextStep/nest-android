package com.bangkit.nest.ui.main.task.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bangkit.nest.R

@Composable
fun DurationComponent(
    modifier: Modifier = Modifier,
    durationList: List<Long>,
    defaultDuration: Long = 60,
    onSelect: (Long) -> Unit
) {
    val lastIndex = durationList.lastIndex

    var defaultDurationIndex = durationList.indexOf(defaultDuration)
    if (defaultDurationIndex == -1) defaultDurationIndex = lastIndex

    var selectedOption by remember { mutableIntStateOf(defaultDurationIndex) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(R.color.purple_faded), RoundedCornerShape(8.dp))
            .border(2.dp, colorResource(R.color.purple_faded), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            durationList.forEachIndexed { index, it ->
                var durationText = "$it min"

                val shape = when (index) {
                    0 -> {
                        RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                    }

                    lastIndex -> {
                        durationText = "Custom"
                        RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                    }

                    else -> {
                        RectangleShape
                    }
                }

                DurationItemComponent(
                    modifier = Modifier.weight(1f),
                    text = durationText,
                    isSelected = selectedOption == index,
                    shape = shape
                ) {
                    onSelect(it)
                    selectedOption = index
                }

                if (index != lastIndex) {
                    Spacer(modifier = Modifier.width(2.dp))
                }
            }
        }
    }
}



@Composable
fun DurationItemComponent(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean = false,
    shape: Shape = RectangleShape,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected)
        colorResource(R.color.purple_faded)
    else
        colorResource(R.color.purple_300)

    Box(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .background(bgColor, shape)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = colorResource(R.color.black), fontSize = 14.sp)
    }
}


@Preview
@Composable
fun DurationComponentPreview() {
    MaterialTheme {
        DurationComponent(durationList = listOf(30, 60, 90, 0), onSelect = {})
    }
}

@Preview
@Composable
fun DurationItemComponentPreview() {
    MaterialTheme {
        DurationItemComponent(text = "60 min", onClick = {})
    }
}