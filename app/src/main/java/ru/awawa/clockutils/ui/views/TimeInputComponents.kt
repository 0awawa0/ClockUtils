package ru.awawa.clockutils.ui.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly


@Composable
fun NumberInputView(
    modifier: Modifier = Modifier,
    currentValue: Int = 0,
    maxValue: Int = 9,
    numbersCount: Int = 1,
    isEnabled: Boolean = true,
    onValueChange: (Int) -> Unit = {},
    textStyle: TextStyle = TextStyle(
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        color = MaterialTheme.colors.onPrimary
    )
) {

    val numsCount = if (numbersCount < 1) 1 else numbersCount
    val value = "%0${numsCount}d".format(currentValue % maxValue)
    val width = textStyle.fontSize.value * numsCount
    val transparentColor = Color(0, 0, 0, 0)
    val focusManager = LocalFocusManager.current

    BasicTextField(
        modifier = modifier.width(width.dp),
        value = TextFieldValue(value, TextRange(numbersCount)),
        textStyle = textStyle,
        maxLines = 1,
        enabled = isEnabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions { focusManager.clearFocus(true) },
        onValueChange = {
            if (!it.text.isDigitsOnly()) return@BasicTextField

            val newValue = if (it.text.length > numsCount) {
                it.text.substring(1..2).toInt()
            } else {
                it.text.toInt()
            }
            if (newValue < maxValue)
                onValueChange(newValue)
            else
                onValueChange(maxValue - 1)
        },
        cursorBrush = Brush.verticalGradient(arrayListOf(transparentColor, transparentColor))
    )
}

@Composable
fun TimeInputView(
    modifier: Modifier = Modifier,
    currentTime: Long = 0L,
    totalTime: Long = 0L,
    isEnabled: Boolean = true,
    onValueChange: (Long) -> Unit = {},
    useSeconds: Boolean = true,
    primaryColor: Color = Color.White,
    secondaryColor: Color = Color.Black,
    primaryStrokeWidth: Float = 5f,
    secondaryStrokeWidth: Float = 3f,
    textStyle: TextStyle = TextStyle(
        color = Color.White,
        textAlign = TextAlign.Center
    )
) {
    val seconds = currentTime / 1000 % 60
    val minutes = currentTime / 1000 / 60 % 60
    val hours = currentTime / 1000 / 60/ 60 % 24

    val filled = if (totalTime != 0L) currentTime / totalTime.toFloat() else 1f

    BoxWithConstraints(modifier = modifier) {
        val radius = minOf(maxHeight, maxWidth)
        Canvas(modifier = Modifier
            .width(radius)
            .height(radius)
            .align(Alignment.Center)
        ) {
            val innerRadius = (size.minDimension - primaryStrokeWidth) / 2
            val halfSize = size / 2f
            val topLeft = Offset(
                halfSize.width - innerRadius,
                halfSize.height - innerRadius
            )

            val size = Size(innerRadius * 2, innerRadius * 2)
            val primaryStartAngle = -90f
            val primarySweepAngle = 360f * filled
            val secondaryStartAngle = primaryStartAngle + primarySweepAngle
            val secondarySweepAngle = 360f - primarySweepAngle

            drawArc(
                color = primaryColor,
                startAngle = primaryStartAngle,
                sweepAngle = primarySweepAngle,
                useCenter = false,
                style = Stroke(width = primaryStrokeWidth),
                size = size,
                topLeft = topLeft
            )

            drawArc(
                color = secondaryColor,
                startAngle =  secondaryStartAngle,
                sweepAngle = secondarySweepAngle,
                useCenter = false,
                style = Stroke(width = secondaryStrokeWidth),
                size = size,
                topLeft = topLeft
            )


        }

        val fontSize = ((radius - 10.dp) / 7).value.sp

        Row(
            modifier = Modifier.align(Alignment.Center)
        ) {
            NumberInputView(
                currentValue = hours.toInt(),
                numbersCount = 2,
                maxValue = 24,
                isEnabled = isEnabled,
                onValueChange = {
                    onValueChange(it * 1000 * 60 * 60 + minutes * 1000 * 60 + seconds * 1000)
                },
                textStyle = textStyle.copy(fontSize = fontSize)
            )
            Text(
                text = ":",
                fontSize = fontSize,
                color = textStyle.color,
                textAlign = textStyle.textAlign
            )
            NumberInputView(
                currentValue = minutes.toInt(),
                numbersCount = 2,
                maxValue = 60,
                isEnabled = isEnabled,
                onValueChange = {
                    onValueChange(hours * 1000 * 60 * 60 + it * 1000 * 60 + seconds * 1000)
                },
                textStyle = textStyle.copy(fontSize = fontSize)
            )
            if (useSeconds) {
                Text(
                    text = ":",
                    fontSize = fontSize,
                    color = textStyle.color,
                    textAlign = textStyle.textAlign
                )
                NumberInputView(
                    currentValue = seconds.toInt(),
                    numbersCount = 2,
                    maxValue = 60,
                    isEnabled = isEnabled,
                    onValueChange = {
                        onValueChange(hours * 1000 * 60 * 60 + minutes * 1000 * 60 + it * 1000)
                    },
                    textStyle = textStyle.copy(fontSize = fontSize)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewNumberInput() {
    NumberInputView(
        numbersCount = 2,
        maxValue = 100,
        currentValue = 10
    )
}

@Preview
@Composable
fun PreviewTimeInput() {
    TimeInputView(
        currentTime = 0,
        totalTime = 0,

    )
}