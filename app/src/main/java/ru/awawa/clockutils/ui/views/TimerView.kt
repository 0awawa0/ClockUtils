package ru.awawa.clockutils.ui.views


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.awawa.clockutils.ui.theme.ClockUtilsTheme


@Composable
fun TimerView(
    modifier: Modifier = Modifier,
    currentTime: Long,
    isRunning: Boolean,
    onStartTimer: () -> Unit,
    onPauseTimer: () -> Unit,
    onStopTimer: () -> Unit
) {
    val milliseconds = "%03d".format(currentTime % 1000)
    val seconds = "%02d".format(currentTime / 1000 % 60)
    val minutes = "%02d".format(currentTime / 1000 / 60 % 60)
    val hours = "%02d".format(currentTime / 1000 / 60 / 60)

    Column(modifier = modifier) {
        TimeInputView(modifier.align(CenterHorizontally))
        TimeInputView(modifier.align(CenterHorizontally), textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 30.sp))
        TimeInputView(modifier.align(CenterHorizontally), textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 10.sp))
    }
}

@Composable
fun TimeInputView(
    modifier: Modifier = Modifier,
    value: String = "00:00:00",
    textStyle: TextStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 20.sp)
) {
    val split = value.split(":")
    val hours = split[0]
    val minutes = split[1]
    val seconds = split[2]
    val cellSize = maxOf(textStyle.fontSize.value * 2f, 60f)
    Row(modifier = modifier) {
        BasicTextField(
            value = hours,
            onValueChange = {},
            modifier = Modifier.size(cellSize.dp),
            textStyle = textStyle,
            maxLines = 1
        )
        BasicTextField(
            value = minutes,
            onValueChange = {},
            modifier = Modifier.size(cellSize.dp),
            textStyle = textStyle,
            maxLines = 1
        )
        BasicTextField(
            value = seconds,
            onValueChange = {},
            modifier = Modifier.size(cellSize.dp),
            textStyle = textStyle,
            maxLines = 1
        )
    }
}

@Preview
@Composable
fun PreviewTimerView() {
    ClockUtilsTheme {
        TimerView(
            currentTime = 0L,
            isRunning = false,
            onStartTimer = { /*TODO*/ },
            onPauseTimer = { /*TODO*/ }) {
        }
    }
}