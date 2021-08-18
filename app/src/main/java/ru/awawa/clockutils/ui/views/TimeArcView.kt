package ru.awawa.clockutils.ui.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.awawa.clockutils.ui.theme.ClockUtilsTheme


@Composable
fun TimeArcView(
    modifier: Modifier = Modifier,
    currentTime: Long,
    primaryColor: Color = Color.White,
    secondaryColor: Color = Color.Black,
    pointerColor: Color = Color.Red,
    primaryStrokeWidth: Float = 5f,
    secondaryStrokeWidth: Float = 3f,
    fontSize: TextUnit = 32.sp,
    textColor: Color = Color.White
) {

    val milliseconds = "%03d".format(currentTime % 1000)
    val seconds = "%02d".format(currentTime / 1000 % 60)
    val minutes = "%02d".format(currentTime / 1000 / 60 % 60)
    val hours = "%02d".format(currentTime / 1000 / 60 / 60)

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier.width(fontSize.value.dp * 8)
                .height(fontSize.value.dp * 8)
                .align(Alignment.Center)
        ) {
            val switchedColors = (currentTime / 1000 / 60) % 2 == 1L
            val innerRadius = (size.minDimension - primaryStrokeWidth) / 2
            val halfSize = size / 2f
            val topLeft = Offset(
                halfSize.width - innerRadius,
                halfSize.height - innerRadius
            )
            val size = Size(innerRadius * 2, innerRadius * 2)
            val pointerWidth = 1f
            val primaryStartAngle = -90f
            val primarySweepAngle = 360f * ((currentTime % 60000) / 60000f)
            val secondaryStartAngle = primaryStartAngle + primarySweepAngle + pointerWidth
            val secondarySweepAngle = 360f - primarySweepAngle - pointerWidth
            drawArc(
                color = if (switchedColors) secondaryColor else primaryColor,
                startAngle = primaryStartAngle,
                sweepAngle = primarySweepAngle,
                useCenter = false,
                style = Stroke(width = if (switchedColors) primaryStrokeWidth else secondaryStrokeWidth),
                size = size,
                topLeft = topLeft
            )
            drawArc(
                color = pointerColor,
                startAngle = secondaryStartAngle - pointerWidth,
                sweepAngle = pointerWidth,
                useCenter = false,
                style = Stroke(width = primaryStrokeWidth),
                size = size,
                topLeft = topLeft
            )
            drawArc(
                color = if (switchedColors) primaryColor else secondaryColor,
                startAngle = secondaryStartAngle,
                sweepAngle = secondarySweepAngle,
                useCenter = false,
                style = Stroke(width = if (switchedColors) secondaryStrokeWidth else primaryStrokeWidth),
                size = size,
                topLeft = topLeft
            )
        }
        Text(
            text = "$hours:$minutes:$seconds:$milliseconds",
            modifier = Modifier.align(Alignment.Center),
            fontSize = fontSize,
            fontFamily = FontFamily.Monospace,
            color = textColor
        )
    }

}

@Preview
@Composable
fun PreviewTimeArcView() {
    ClockUtilsTheme {
        TimeArcView(
            modifier = Modifier.fillMaxSize(),
            currentTime = 30000
        )
    }
}