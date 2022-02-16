package ru.awawa.clockutils.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.awawa.clockutils.ui.theme.ClockUtilsTheme
import ru.awawa.clockutils.ui.theme.Grey700
import ru.awawa.clockutils.ui.theme.Teal300

@Composable
fun TimerView(
    modifier: Modifier = Modifier,
    label: String,
    currentTime: Long,
    totalTime: Long,
    isRunning: Boolean,
    onSetTime: (Long) -> Unit,
    onStartTimer: () -> Unit,
    onPauseTimer: () -> Unit,
    onStopTimer: () -> Unit
) {

    ConstraintLayout(modifier = modifier.fillMaxSize().padding(16.dp)) {

        val (btnStart, btnStop, text, header) = createRefs()

        Text(
            modifier = Modifier.constrainAs(header) {
                start.linkTo(parent.start, margin = 8.dp)
                end.linkTo(parent.end, margin = 8.dp)
                top.linkTo(parent.top, margin = 8.dp)
            },
            fontSize = 32.sp,
            text = label
        )

        TimeInputView(
            modifier = Modifier.constrainAs(text) {
                start.linkTo(parent.start, margin = 8.dp)
                end.linkTo(parent.end, margin = 8.dp)
                top.linkTo(header.bottom, margin = 8.dp)
                bottom.linkTo(btnStop.top, margin = 8.dp)
            },
            currentTime = currentTime,
            totalTime = totalTime,
            isEnabled = !isRunning,
            onValueChange = onSetTime,
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 50.sp,
                color = Color.White
            ),
            primaryColor = Teal300,
            secondaryColor = Grey700,
            primaryStrokeWidth = 10f,
            secondaryStrokeWidth = 5f
        )

        if (!isRunning || currentTime != 0L) {
            Button(
                onClick = { if (isRunning) onPauseTimer() else onStartTimer() },
                modifier = Modifier
                    .constrainAs(btnStart) {
                        start.linkTo(parent.start, margin = 8.dp)
                        end.linkTo(btnStop.start, margin = 8.dp)
                        bottom.linkTo(parent.bottom, margin = 32.dp)
                    }
                    .size(60.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Teal300
                )
            }
        }

        Button(
            onClick = onStopTimer,
            modifier = Modifier
                .constrainAs(btnStop) {
                    start.linkTo(
                        if (!isRunning || currentTime != 0L)
                            btnStart.end
                        else
                            parent.start,
                        margin = 8.dp
                    )
                    end.linkTo(parent.end, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 32.dp)
                }
                .size(60.dp)
                .clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Stop,
                contentDescription = null,
                tint = Teal300
            )
        }
    }
}

@Preview
@Composable
fun PreviewTimerView() {
    ClockUtilsTheme {
        TimerView(
            label = "Timer",
            currentTime = 1L,
            totalTime = 1L,
            isRunning = false,
            onSetTime = {},
            onStartTimer = {},
            onPauseTimer = {},
            onStopTimer = {}
        )
    }
}