package com.bangkit.nest.ui.main.task.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.graphics.ColorFilter
import com.bangkit.nest.R
import com.bangkit.nest.ui.bodyTextStyleMedium
import com.bangkit.nest.ui.subTitleFormStyle

@Composable
fun CompletedComponent(completedCount: Int, totalCount: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(colorResource(R.color.purple), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
            .height(70.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Completed",
                    style = subTitleFormStyle,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_completed),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$completedCount/$totalCount Tasks",
                        style = bodyTextStyleMedium,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.ic_forward_arrow),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color.White)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompletedComponentPreview() {
    CompletedComponent(completedCount = 2, totalCount = 6) {
        // onClick
    }
}
