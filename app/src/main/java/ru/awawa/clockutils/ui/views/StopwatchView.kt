package ru.awawa.clockutils.ui.views

import androidx.compose.animation.*
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.awawa.clockutils.ui.theme.ClockUtilsTheme
import ru.awawa.clockutils.ui.theme.Teal200
import ru.awawa.clockutils.ui.theme.Teal500


@ExperimentalAnimationApi
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
        val (btnsBox, text, header, circle) = createRefs()

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
            bottom.linkTo(btnsBox.top)
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

        StopwatchButtons(
            Modifier
                .fillMaxWidth()
                .constrainAs(btnsBox) {
                    bottom.linkTo(parent.bottom, margin = 32.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            isRunning,
            onStartStopwatch = onStartStopwatch,
            onPauseStopwatch = onPauseStopwatch,
            onStopStopwatch = onStopStopwatch
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun StopwatchButtons(
    modifier: Modifier = Modifier,
    isRunning: Boolean = false,
    onStartStopwatch: () -> Unit,
    onPauseStopwatch: () -> Unit,
    onStopStopwatch: () -> Unit
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { if (isRunning) onPauseStopwatch() else onStartStopwatch() },
            modifier = Modifier
                .size(75.dp)
                .clip(CircleShape)
        ) {
            Icon(
                imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Teal200
            )
        }

        Button(
            onClick = onStopStopwatch,
            modifier = Modifier
                .size(75.dp)
                .clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Stop,
                contentDescription = null,
                tint = Teal200
            )
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun PreviewStopwatchView() {
    ClockUtilsTheme {
        StopwatchView(
            modifier = Modifier.fillMaxSize(),
            label = "Stopwatch",
            currentTime = 30L * 1000,
            isRunning = true,
            onStartStopwatch = { },
            onPauseStopwatch = { },
            onStopStopwatch = { }
        )
    }
}
