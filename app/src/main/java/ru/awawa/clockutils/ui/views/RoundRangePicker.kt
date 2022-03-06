package ru.awawa.clockutils.ui.views

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.sp
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
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
    subtextFormatter: (Float) -> String = { "" },
    onValueChanged: (Float) -> Unit = {},
    filledColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Gray,
    unfilledColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.White,
    selectorColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Black,
    selectorSize: Dp? = null,
    textColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Black,
    subtextColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.5f),
    filledStroke: Stroke = Stroke(5f, miter = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round, PathEffect.dashPathEffect(floatArrayOf(5f, 15f))),
    unfilledStroke: Stroke = Stroke(5f, miter = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round, PathEffect.dashPathEffect(floatArrayOf(5f, 15f))),
    textSize: TextUnit = 96.sp,
    subtextSize: TextUnit = 48.sp
) {
    val startAngle = 90 + skipAngle / 2f
    val sweepAngle = 360 - skipAngle
    val selectorRadius = selectorSize?.value
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
            val innerRadius = (size.minDimension - filledStroke.width) / 2
            val halfSize = size / 2f
            val size = Size(innerRadius * 2, innerRadius * 2)
            val topLeft = Offset(halfSize.width - innerRadius, halfSize.height - innerRadius)

            selectorYOffset = innerRadius - innerRadius * cos(Math.toRadians(selectorDegrees.toDouble()))
            selectorXOffset = innerRadius - innerRadius * sin(Math.toRadians(selectorDegrees.toDouble()))

            drawArc(
                color = filledColor,
                startAngle = startAngle,
                sweepAngle = selectorAngle,
                useCenter = false,
                style = filledStroke,
                size = size,
                topLeft = topLeft
            )

            drawArc(
                color = unfilledColor,
                startAngle = startAngle + selectorAngle,
                sweepAngle = sweepAngle - selectorAngle,
                useCenter = false,
                style = unfilledStroke,
                size = size,
                topLeft = topLeft
            )

            drawCircle(
                color = selectorColor,
                radius = selectorRadius ?: innerRadius / 15,
                center = Offset(selectorXOffset.toFloat(), selectorYOffset.toFloat())
            )

            val valueText = textFormatter(value)
            if (valueText.isNotBlank()) {
                drawContext.canvas.nativeCanvas.apply {

                    val valuePaint = Paint()
                    val argbColor = textColor.toArgb()
                    valuePaint.color = Color.argb(argbColor.alpha, argbColor.red, argbColor.green, argbColor.blue)
                    valuePaint.textSize = textSize.toPx()
                    val valueTextRect = Rect()
                    valuePaint.getTextBounds(valueText, 0, valueText.length, valueTextRect)

                    val valueTextPositionX = size.center.x - (valueTextRect.width() / 2)
                    val valueTextPositionY = size.center.y

                    drawText(
                        valueText,
                        valueTextPositionX,
                        valueTextPositionY,
                        valuePaint
                    )
                }
            }

            val subtext = subtextFormatter(value)
            if (subtext.isNotBlank()) {
                drawContext.canvas.nativeCanvas.apply {
                    val paint = Paint()
                    val argbColor = subtextColor.toArgb()
                    paint.color = Color.argb(argbColor.alpha, argbColor.red, argbColor.green, argbColor.blue)
                    paint.textSize = subtextSize.toPx()
                    val textRect = Rect()
                    paint.getTextBounds(subtext, 0, subtext.length, textRect)

                    val textPositionX = size.center.x - (textRect.width() / 2)
                    val textPositionY = size.center.y + 2 * textRect.height()

                    drawText(
                        subtext,
                        textPositionX,
                        textPositionY,
                        paint
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewRoundRangePickView0() {
    RoundRangePickView(
        startValue = 0.5f,
        skipAngle = 60f,
        onValueChanged = { },
        textFormatter = { String.format("%02.02f", it * 100) },
        subtextFormatter = { "percents" },
    )
}