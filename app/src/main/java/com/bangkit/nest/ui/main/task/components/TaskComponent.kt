package com.bangkit.nest.ui.main.task.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bangkit.nest.R
import com.bangkit.nest.data.remote.response.Task
import com.bangkit.nest.ui.bodyTextStyle
import com.bangkit.nest.ui.subBodyTextStyle

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskComponent(
    task: Task,
    onEdit: (Int) -> Unit,
    onComplete: (Int) -> Unit,
    onPomodoro: (Int) -> Unit,
    animDelay: Int = 100
) {
    val alphaAnimation = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(animDelay) {
        alphaAnimation.animateTo(targetValue = 1f, animationSpec = tween(1000, animDelay))
    }

    val priorityColor = when (task.priority) {
        "low" -> colorResource(R.color.green)
        "medium" -> colorResource(R.color.yellow)
        "high" -> colorResource(R.color.soft_red)
        else -> Color.Gray
    }

    val startTime = task.startTime
    val endTime = task.endTime

    Box(
        modifier = Modifier
            .graphicsLayer {
                alpha = alphaAnimation.value
            }
            .fillMaxWidth()
            .height(85.dp)
            .background(
                priorityColor,
                RoundedCornerShape(
                    topStart = 8.dp,
                    bottomStart = 8.dp,
                    topEnd = 20.dp,
                    bottomEnd = 20.dp
                )
            )
            .border(
                width = 1.dp,
                color = colorResource(R.color.gray_400),
                shape = RoundedCornerShape(
                    topStart = 8.dp,
                    bottomStart = 8.dp,
                    topEnd = 8.dp,
                    bottomEnd = 8.dp
                )
            )
            .padding(start = 16.dp)
            .clickable {
                onEdit(task.id)
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
                .background(
                    Color.White,
                    RoundedCornerShape(
                        topEnd = 8.dp,
                        bottomEnd = 8.dp
                    )
                )
                .padding(start = 8.dp, top = 10.dp, end = 16.dp, bottom = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { onComplete(task.id) },
                    modifier = Modifier
                        .size(32.dp)
                        .weight(0.1f)
                ) {
                    if (task.isCompleted == 1) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check_circle),
                            contentDescription = null,
                            tint = colorResource(R.color.purple),
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .border(
                                    width = 2.dp,
                                    color = colorResource(R.color.purple),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center,
                            content = {}
                        )
                    }
                }
                Row(
                    modifier = Modifier.weight(0.8f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(verticalArrangement = Arrangement.Center) {
                        task.title?.let {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .basicMarquee(delayMillis = 1000),
                                text = it,
                                style = bodyTextStyle,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_assess_date),
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = colorResource(R.color.gray_400)
                            )
                            task.date?.let {
                                Text(
                                    text = it,
                                    style = subBodyTextStyle,
                                    color = colorResource(R.color.gray_400)
                                )
                            }
                            Divider(
                                modifier = Modifier
                                    .height(16.dp)
                                    .padding(horizontal = 4.dp)
                                    .width(1.dp),
                                color = colorResource(R.color.gray_400)
                            )
                            Text(
                                text = "$startTime - $endTime",
                                style = subBodyTextStyle,
                                color = colorResource(R.color.gray_400)
                            )
                            if (task.isRepeated == 1) {
                                Divider(
                                    modifier = Modifier
                                        .height(16.dp)
                                        .padding(horizontal = 4.dp)
                                        .width(1.dp),
                                    color = colorResource(R.color.gray_400)
                                )
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp),
                                    tint = colorResource(R.color.gray_400)
                                )
                            }
                        }
                    }
                }
                if (task.isCompleted == 0) {
                    IconButton(
                        onClick = { onPomodoro(task.id) },
                        modifier = Modifier.weight(0.1f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_timer),
                            tint = colorResource(R.color.black),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}
