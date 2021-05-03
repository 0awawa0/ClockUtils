package ru.awawa.clockutils.ui.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.awawa.clockutils.ui.theme.ClockUtilsTheme


@Composable
fun StopwatchView(
    modifier: Modifier = Modifier,
    currentTime: Long,
    isRunning: Boolean,
    onStartStopwatch: () -> Unit,
    onPauseStopwatch: () -> Unit,
    onStopStopwatch: () -> Unit
) {
    val milliseconds = "%03d".format(currentTime % 1000)
    val seconds = "%02d".format(currentTime / 1000 % 60)
    val minutes = "%02d".format(currentTime / 1000 / 60 % 60)
    val hours = "%02d".format(currentTime / 1000 / 60 / 60)

    ConstraintLayout(modifier = modifier) {
        val (btnStart, btnStop, text) = createRefs()

        Text(
            text = "$hours:$minutes:$seconds:$milliseconds",
            modifier = Modifier.constrainAs(text) {
                start.linkTo(parent.start, margin = 8.dp)
                end.linkTo(parent.end, margin = 8.dp)
                top.linkTo(parent.top, margin = 8.dp)
                bottom.linkTo(btnStart.top, margin = 8.dp)
            },
            fontSize = 32.sp,
            fontFamily = FontFamily.Monospace
        )

        Button(
            onClick = {
                if (isRunning) onPauseStopwatch() else onStartStopwatch()
            },
            modifier = Modifier.constrainAs(btnStart) {
                start.linkTo(parent.start, margin = 8.dp)
                end.linkTo(btnStop.start, margin = 8.dp)
                bottom.linkTo(parent.bottom, margin = 32.dp)
            }
        ) {
            Text(text = if (isRunning) "Pause" else "Start")
        }

        Button(
            onClick = onStopStopwatch,
            modifier = Modifier.constrainAs(btnStop) {
                start.linkTo(btnStart.end, margin = 8.dp)
                end.linkTo(parent.end, margin = 8.dp)
                bottom.linkTo(parent.bottom, margin = 32.dp)
            }
        ) {
            Text(text = "Stop")
        }
    }
}

@Preview
@Composable
fun PreviewStopwatchView() {
    ClockUtilsTheme {
        StopwatchView(
            modifier = Modifier.fillMaxSize(),
            currentTime = 0L,
            isRunning = false,
            onStartStopwatch = { },
            onPauseStopwatch = { },
            onStopStopwatch = { }
        )
    }
}