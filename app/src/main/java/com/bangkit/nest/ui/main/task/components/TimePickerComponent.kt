package com.bangkit.nest.ui.task.components

import android.os.Build
import androidx.annotation.RequiresApi
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bangkit.nest.R
import com.bangkit.nest.ui.bodyTextStyle
import com.bangkit.nest.ui.bodyTextStyleMedium
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import java.time.LocalTime

@Composable
fun TimePickerComponent(
    time: LocalTime,
    isTimeUpdated: Boolean = false,
    is24hourFormat: Boolean = false,
    onSelect: (LocalTime) -> Unit
) {
    AnimatedVisibility(
        isTimeUpdated,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(tween(0))
    ) {
        WheelTimePicker(
            timeFormat = if (is24hourFormat) TimeFormat.HOUR_24 else TimeFormat.AM_PM,
            startTime = time,
            textColor = Color.Black,
            textStyle = bodyTextStyleMedium,
            selectorProperties = WheelPickerDefaults.selectorProperties(color = colorResource(R.color.white), border = BorderStroke(1.dp, colorResource(R.color.purple_400))),
            onSnappedTime = onSelect
        )
    }
    AnimatedVisibility(
        !isTimeUpdated,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(tween(0))
    ) {
        WheelTimePicker(
            timeFormat = if (is24hourFormat) TimeFormat.HOUR_24 else TimeFormat.AM_PM,
            startTime = time,
            textColor = Color.Black,
            textStyle = bodyTextStyleMedium,
            selectorProperties = WheelPickerDefaults.selectorProperties(color = colorResource(R.color.white), border = BorderStroke(1.dp, colorResource(R.color.purple_400))),
            onSnappedTime = onSelect
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TimePickerComponentPreview() {
    TimePickerComponent(
        time = LocalTime.now(),
        isTimeUpdated = false,
        is24hourFormat = false,
        onSelect = {}
    )
}