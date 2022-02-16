package ru.awawa.clockutils.ui.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun RoundRangePickView(
    modifier: Modifier = Modifier,
    skipAngle: Float = 45f
) {
    BoxWithConstraints(modifier = modifier) {
        val radius = minOf(maxHeight, maxWidth)
        val strokeWidth = 5f
        Canvas(
            modifier = Modifier
                .width(radius)
                .height(radius)
                .align(Alignment.Center)

        ) {
            val innerRadius = (size.minDimension - strokeWidth) / 2
            val halfSize = size / 2f
            val size = Size(innerRadius * 2, innerRadius * 2)
            val topLeft = Offset(halfSize.width - innerRadius, halfSize.height - innerRadius)
            val startAngle = 90 + skipAngle / 2f
            val sweepAngle = 360 - skipAngle
            val selectorRadius = 50f
            val selectorXOffset = innerRadius - innerRadius * Math.sin(
                Math.toRadians(startAngle.toDouble() + skipAngle.toDouble())
            )
            val selectorYOffset = innerRadius - innerRadius * Math.cos(
                Math.toRadians(startAngle.toDouble() + skipAngle.toDouble())
            )
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
fun PreviewRoundRangePickView() {
    RoundRangePickView()
}