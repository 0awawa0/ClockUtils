package ru.awawa.clockutils.ui.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.awawa.clockutils.ui.theme.ClockUtilsTheme
import ru.awawa.clockutils.ui.theme.Teal200
import ru.awawa.clockutils.ui.theme.Teal500

@Composable
fun StopwatchView(
    modifier: Modifier = Modifier,
    label: String,
    currentTime: Long,
    isRunning: Boolean,
    onStartStopwatch: () -> Unit,
    onPauseStopwatch: () -> Unit,
    onStopStopwatch: () -> Unit
) {
    ConstraintLayout(modifier = modifier) {
        val (btnStart, btnStop, text, header, circle) = createRefs()

        Text(
            modifier = Modifier.constrainAs(header) {
                start.linkTo(parent.start, margin = 8.dp)
                end.linkTo(parent.end, margin = 8.dp)
                top.linkTo(parent.top, margin = 8.dp)
            },
            fontSize = 32.sp,
            text = label
        )

        Box(modifier = Modifier.constrainAs(circle) {
            top.linkTo(header.bottom)
            bottom.linkTo(btnStart.top)
            start.linkTo(text.start)
            end.linkTo(text.end)
        }) {
            TimeArcView(
                modifier = Modifier.fillMaxWidth(),
                currentTime = currentTime,
                primaryColor = Teal200,
                secondaryColor = Teal500,
                primaryStrokeWidth = 10f,
                secondaryStrokeWidth = 5f,
                fontSize = 40.sp
            )
        }

        Button(
            onClick = { if (isRunning) onPauseStopwatch() else onStartStopwatch() },
            modifier = Modifier
                .constrainAs(btnStart) {
                    start.linkTo(parent.start, margin = 8.dp)
                    end.linkTo(btnStop.start, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 32.dp)
                }
                .size(50.dp)
                .clip(CircleShape)
        ) {
            Icon(
                imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = null
            )
        }

        Button(
            onClick = onStopStopwatch,
            modifier = Modifier
                .constrainAs(btnStop) {
                    start.linkTo(btnStart.end, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 32.dp)
                }
                .size(50.dp)
                .clip(CircleShape)
        ) {
            Icon(imageVector = Icons.Default.Stop, contentDescription = null)
        }
    }
}

@Preview
@Composable
fun PreviewStopwatchView() {
    ClockUtilsTheme {
        StopwatchView(
            modifier = Modifier.fillMaxSize(),
            label = "Stopwatch",
            currentTime = 30L * 1000,
            isRunning = false,
            onStartStopwatch = { },
            onPauseStopwatch = { },
            onStopStopwatch = { }
        )
    }
}