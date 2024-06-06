package com.bangkit.nest.ui.main.task.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bangkit.nest.R

data class Priority(
    val displayText: String,
    val color: Color
)

@Composable
fun getPriorities(): List<Priority> {
    return listOf(
        Priority("Low", colorResource(R.color.green)),
        Priority("Medium", colorResource(R.color.yellow)),
        Priority("High", colorResource(R.color.soft_red))
    )
}

@Composable
fun PriorityComponent(
    defaultSortTask: Priority = getPriorities().first(),
    onSelect: (Priority) -> Unit
) {
    var selectedOption by remember {
        mutableStateOf(defaultSortTask)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        getPriorities().forEach {
            PriorityItemComponent(
                title = it.displayText,
                backgroundColor = it.color,
                isSelected = selectedOption == it,
                modifier = Modifier.weight(1f)
            ) {
                onSelect(it)
                selectedOption = it
            }
        }
    }
}

@Composable
fun PriorityItemComponent(
    title: String,
    backgroundColor: Color,
    isSelected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var priorityBackground = colorResource(R.color.lighter_gray)
        var textColor = colorResource(R.color.white)

        if (isSelected) {
            priorityBackground = backgroundColor
            textColor = Color.Black
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(priorityBackground, RoundedCornerShape(8.dp))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = textColor,
                modifier = Modifier.padding(4.dp, 16.dp)
            )
        }

        if (isSelected) {
            val animValue = remember { Animatable(initialValue = 0f) }

            LaunchedEffect(Unit) {
                animValue.animateTo(1f, tween(300))
            }

            Box(
                modifier = Modifier
                    .width(50.dp * animValue.value)
                    .height(4.dp)
                    .background(backgroundColor, RoundedCornerShape(8.dp))
            )
        }
    }
}

@Preview
@Composable
fun PriorityComponentPreview() {
    MaterialTheme {
        PriorityComponent(defaultSortTask = getPriorities().first(), onSelect = {})
    }
}
