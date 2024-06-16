package com.bangkit.nest.ui.main.task.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bangkit.nest.R
import com.bangkit.nest.ui.bodyTextStyleMedium
import com.bangkit.nest.ui.h3TextStyle
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomDurationDialogComponent(
    duration: Int = 120,
    onClose: () -> Unit,
    onSelect: (LocalTime) -> Unit
) {
    Dialog(onDismissRequest = { onClose() }) {


        val hours = duration / 60
        val minutes = duration % 60

        var customDuration = LocalTime.of(hours, minutes)

        Card(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Text(
                text = "Custom Duration",
                color = Color.Black,
                style = h3TextStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        24.dp,
                        20.dp
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        32.dp,
                        16.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                WheelTimePicker(
                    startTime = LocalTime.of(hours, minutes),
                    textColor = Color.Black,
                    textStyle = bodyTextStyleMedium,
                    selectorProperties = WheelPickerDefaults.selectorProperties(color = colorResource(
                        R.color.white), border = BorderStroke(1.dp, colorResource(R.color.purple_400))
                    ),
                    onSnappedTime = { customDuration = it }
                )
                Surface(
                    modifier = Modifier.padding(bottom = 8.dp)
                        .clickable {
                            onSelect(customDuration)
                            onClose()
                        }
                        .align(Alignment.End),
                    shape = RoundedCornerShape(8.dp),
                    color = colorResource(R.color.purple)
                ) {
                    Text(
                        text = "Done",
                        style = bodyTextStyleMedium,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewCustomDurationDialogComponent() {
    CustomDurationDialogComponent(
        duration = 120,
        onClose = {},
        onSelect = {}
    )
}