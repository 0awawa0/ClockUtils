package ru.awawa.clockutils.ui.views

import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.center
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RoundRangePickView(
    modifier: Modifier = Modifier,
    skipAngle: Float = 45f,
    minValue: Float = 0f,
    maxValue: Float = 1f,
    startValue: Float = 0f,
    textFormatter: (Float) -> String = { "" },
    onValueChanged: (Float) -> Unit = {},
    onButtonClick: () -> Unit = {}
) {
    val startAngle = 90 + skipAngle / 2f
    val sweepAngle = 360 - skipAngle
    val selectorRadius = 50f
    val touchSlop = 100f
    val startValueNormalized = (startValue.coerceIn(minValue, maxValue) - minValue) / (maxValue - minValue)
    var value by remember { mutableStateOf(startValueNormalized) }

    val selectorDegrees = 180.0f - skipAngle / 2 - sweepAngle * value
    
    var dragStartedAngle by remember { mutableStateOf(selectorDegrees) }
    var selectorAngle by remember { mutableStateOf((360 - skipAngle) / 2 - selectorDegrees) }
    var previousAngle by remember { mutableStateOf(selectorAngle) }

    var selectorXOffset by remember { mutableStateOf(0.0) }
    var selectorYOffset by remember { mutableStateOf(0.0) }
    var isDraggingSelector by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier) {
        val radius = minOf(maxHeight, maxWidth)
        val strokeWidth = 5f
        Canvas(
            modifier = Modifier
                .size(radius)
                .align(Alignment.Center)
                .pointerInput(true) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            isDraggingSelector = selectorXOffset - touchSlop < offset.x
                                && offset.x < selectorXOffset + touchSlop
                                && selectorYOffset - touchSlop < offset.y
                                && offset.y < selectorYOffset + touchSlop

                            dragStartedAngle = atan2(
                                y = size.center.x - offset.x,
                                x = size.center.y - offset.y
                            ) * (180 / Math.PI.toFloat()) * -1
                        },

                        onDragEnd = {
                            previousAngle = selectorAngle
                        }
                    ) { change, _ ->
                        if (!isDraggingSelector) return@detectDragGestures

                        change.consumeAllChanges()
                        val touchAngle = atan2(
                            y = size.center.x - change.position.x,
                            x = size.center.y - change.position.y
                        ) * (180 / Math.PI.toFloat()) * -1


                        val old = selectorAngle
                        val x = previousAngle + touchAngle - dragStartedAngle
                        if (abs(old - x) < 180f) selectorAngle = x.coerceIn(0f, 360f - skipAngle)

                        val newValue = selectorAngle / (360f - skipAngle)
                        value = newValue
                        onValueChanged(newValue * (maxValue - minValue) + minValue)
                    }
                }

        ) {
            val innerRadius = (size.minDimension - strokeWidth) / 2
            val halfSize = size / 2f
            val size = Size(innerRadius * 2, innerRadius * 2)
            val topLeft = Offset(halfSize.width - innerRadius, halfSize.height - innerRadius)

            selectorYOffset = innerRadius - innerRadius * cos(Math.toRadians(selectorDegrees.toDouble()))
            selectorXOffset = innerRadius - innerRadius * sin(Math.toRadians(selectorDegrees.toDouble()))
            drawArc(
                color = Color.White,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth),
                size = size,
                topLeft = topLeft
            )

            drawCircle(
                color = Color.Black,
                radius = selectorRadius,
                center = Offset(selectorXOffset.toFloat(), selectorYOffset.toFloat())
            )

            val valueText = textFormatter(value)
            if (valueText.isNotBlank()) {
                drawContext.canvas.nativeCanvas.apply {

                    val valuePaint = Paint()
                    valuePaint.color = android.graphics.Color.parseColor("#000000")
                    valuePaint.textSize = (innerRadius / 2)
                    val valueTextRect = Rect()
                    valuePaint.getTextBounds(valueText, 0, valueText.length, valueTextRect)

                    val valueTextPositionX = size.center.x - (valueTextRect.width() / 2)
                    val valueTextPositionY = size.center.y - (valueTextRect.height() / 2)

                    drawText(
                        valueText,
                        valueTextPositionX,
                        valueTextPositionY,
                        valuePaint
                    )
                }
            }
        }
        
        Button(onClick = onButtonClick) {
            Text(text = "Click")
        }
    }
}

@Composable
fun MetronomeView(
    modifier: Modifier = Modifier,
    ticksPerSecond: Int,
    valueUpdated: (Int) -> Unit = {}
) {

    NumberInputView(
        modifier = modifier.fillMaxSize(),
        currentValue = ticksPerSecond,
        maxValue = 300,
        numbersCount = 3,
        onValueChange = valueUpdated
    )
}

@Preview
@Composable
fun PreviewRoundRangePickView0() {
    var percentage by remember { mutableStateOf(0f) }
    RoundRangePickView(
        startValue = percentage,
        onValueChanged = { percentage = it },
        textFormatter = { String.format("%02.02f", it * 100) }
    )
}

@Preview
@Composable
fun PreviewRoundRangePickView25() {
    RoundRangePickView(
        startValue = 0.25f,
        textFormatter = { String.format("%02d", (it * 100).toInt()) }
    )
}

@Preview
@Composable
fun PreviewRoundRangePickView50() {
    var percentage by remember { mutableStateOf(0f) }
    RoundRangePickView(
        startValue = 0.5f,
        onValueChanged =  { percentage = it }
    )
}

@Preview
@Composable
fun PreviewRoundRangePickView75() {
    RoundRangePickView(
        startValue = 0.75f
    )
}

@Preview
@Composable
fun PreviewRoundRangePickView100() {
    RoundRangePickView(
        startValue = 1f
    )
}