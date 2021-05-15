package ru.awawa.clockutils.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.awawa.clockutils.ui.theme.ClockUtilsTheme

@Composable
fun TimerView(
    modifier: Modifier = Modifier,
    label: String,
    currentTime: Long,
    isRunning: Boolean,
    onSetTime: (Long) -> Unit,
    onStartTimer: () -> Unit,
    onPauseTimer: () -> Unit,
    onStopTimer: () -> Unit
) {

    ConstraintLayout(modifier = modifier) {

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
            isEnabled = !isRunning,
            onValueChange = onSetTime,
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 38.sp,
                color = MaterialTheme.colors.onPrimary
            )
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
                    .size(50.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null
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
                .size(50.dp)
                .clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Stop,
                contentDescription = null
            )
        }
    }
}

@Composable
fun TimeInputView(
    modifier: Modifier = Modifier,
    currentTime: Long = 0L,
    isEnabled: Boolean = true,
    onValueChange: (Long) -> Unit = {},
    textStyle: TextStyle = TextStyle(
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        color = MaterialTheme.colors.onPrimary
    )
) {
    val seconds = "%02d".format(currentTime / 1000 % 60)
    val minutes = "%02d".format(currentTime / 1000 / 60 % 60)
    val hours = "%02d".format(currentTime / 1000 / 60 / 60)
    val transparentColor = remember { Color(0, 0, 0, alpha = 0)}

    Row(modifier = modifier) {
        BasicTextField(
            value = TextFieldValue("$hours : $minutes : $seconds", TextRange(12, 12)),
            onValueChange = {
                val inp = it.text
                if (!inp.last().isDigit() || inp.length == 12) return@BasicTextField
                val nums = inp.filter { char -> char.isDigit() }
                if (nums.length < 6) {
                    val h = "0${nums[0]}".toLong() * 1000 * 60 * 60
                    val m = nums.substring(1..2).toLong() * 1000 * 60
                    val s = nums.substring(3..4).toLong() * 1000
                    onValueChange(h + m + s)
                } else {
                    val h = nums.substring(1..2).toLong() * 1000 * 60 * 60
                    val m = nums.substring(3..4).toLong() * 1000 * 60
                    val s = nums.substring(5..6).toLong() * 1000
                    onValueChange(h + m + s)
                }
            },
            textStyle = textStyle,
            maxLines = 1,
            enabled = isEnabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = Brush.verticalGradient(listOf(transparentColor, transparentColor))
        )
    }
}

@Preview
@Composable
fun PreviewTimerView() {
    ClockUtilsTheme {
        TimerView(
            label = "Timer",
            currentTime = 0L,
            isRunning = false,
            onSetTime = {},
            onStartTimer = {},
            onPauseTimer = {},
            onStopTimer = {}
        )
    }
}