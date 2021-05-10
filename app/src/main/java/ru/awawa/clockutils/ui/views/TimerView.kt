package ru.awawa.clockutils.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
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
import ru.awawa.clockutils.ui.theme.ClockUtilsTheme

@Composable
fun TimerView(
    modifier: Modifier = Modifier,
    currentTime: Long,
    isRunning: Boolean,
    onSetTime: (Long) -> Unit,
    onStartTimer: () -> Unit,
    onPauseTimer: () -> Unit,
    onStopTimer: () -> Unit
) {

    Column(modifier = modifier) {
        TimeInputView(
            modifier.align(CenterHorizontally),
            currentTime = currentTime,
            isEnabled = !isRunning,
            onValueChange = onSetTime,
            textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 38.sp)
        )
    }
}

@Composable
fun TimeInputView(
    modifier: Modifier = Modifier,
    currentTime: Long = 0L,
    isEnabled: Boolean = true,
    onValueChange: (Long) -> Unit = {},
    textStyle: TextStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 20.sp)
) {
    val seconds = "%02d".format(currentTime / 1000 % 60)
    val minutes = "%02d".format(currentTime / 1000 / 60 % 60)
    val hours = "%02d".format(currentTime / 1000 / 60 / 60)
    val cellSize = maxOf(textStyle.fontSize.value * 2f, 60f)
    val transparentColor = remember { Color(0, 0, 0, alpha = 0)}

    Row(modifier = modifier) {
        BasicTextField(
            value = TextFieldValue(hours, selection = TextRange(2)),
            onValueChange = {
                val inp = if (it.text.length > 2)
                    it.text.substring(it.text.length - 2 until it.text.length)
                else
                    it.text
                val h = inp.toLongOrNull() ?: return@BasicTextField
                val m = minutes.toLongOrNull() ?: return@BasicTextField
                val s = seconds.toLongOrNull() ?: return@BasicTextField
                val newValue = h * 1000 * 60 * 60 + m * 1000 * 60 + s * 1000
                onValueChange(newValue)
            },
            modifier = Modifier.size(cellSize.dp),
            textStyle = textStyle,
            maxLines = 1,
            enabled = isEnabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = Brush.verticalGradient(listOf(transparentColor, transparentColor))
        )
        BasicTextField(
            value = TextFieldValue(minutes, selection = TextRange(2)),
            onValueChange = {
                val inp = if (it.text.length > 2)
                    it.text.substring(it.text.length - 2 until it.text.length)
                else
                    it.text
                val h = hours.toLongOrNull() ?: return@BasicTextField
                val m = inp.toLongOrNull() ?: return@BasicTextField
                val s = seconds.toLongOrNull() ?: return@BasicTextField
                val newValue = h * 1000 * 60 * 60 + m * 1000 * 60 + s * 1000
                onValueChange(newValue)
            },
            modifier = Modifier.size(cellSize.dp),
            textStyle = textStyle,
            maxLines = 1,
            enabled = isEnabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = Brush.verticalGradient(listOf(transparentColor, transparentColor))
        )
        BasicTextField(
            value = TextFieldValue(seconds, selection = TextRange(2)),
            onValueChange = {
                val inp = if (it.text.length > 2)
                    it.text.substring(it.text.length - 2 until it.text.length)
                else
                    it.text
                val h = hours.toLongOrNull() ?: return@BasicTextField
                val m = minutes.toLongOrNull() ?: return@BasicTextField
                val s = inp.toLongOrNull() ?: return@BasicTextField
                val newValue = h * 1000 * 60 * 60 + m * 1000 * 60 + s * 1000
                onValueChange(newValue)
            },
            modifier = Modifier.size(cellSize.dp),
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
            currentTime = 0L,
            isRunning = false,
            onSetTime = {},
            onStartTimer = {},
            onPauseTimer = {},
            onStopTimer = {}
        )
    }
}