package ru.awawa.clockutils.ui.views

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.awawa.clockutils.ui.theme.ClockUtilsTheme
import ru.awawa.clockutils.ui.theme.Red
import ru.awawa.clockutils.ui.theme.Teal200
import ru.awawa.clockutils.ui.theme.Teal500


@ExperimentalAnimationApi
@Composable
fun StopwatchView(
    modifier: Modifier = Modifier,
    label: String,
    currentTime: Long,
    isRunning: Boolean,
    checkPoints: Set<Long>,
    onStartStopwatch: () -> Unit,
    onPauseStopwatch: () -> Unit,
    onStopStopwatch: () -> Unit,
    onAddCheckPoint: () -> Unit,
    onSaveCheckPoint: (Long) -> Unit,
    onRemoveCheckPoint: (Long) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {

        val constraints = this.constraints

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(5f),
                fontSize = 32.sp,
                text = label
            )

            Box(modifier = Modifier.weight(50f), contentAlignment = Center) {
                TimeArcView(
                    currentTime = currentTime,
                    primaryColor = Teal200,
                    secondaryColor = Teal500,
                    primaryStrokeWidth = 10f,
                    secondaryStrokeWidth = 5f,
                    fontSize = 40.sp
                )
            }

            CheckPointsListView(
                modifier = Modifier.weight(35f),
                checkPoints = checkPoints,
                onSaveCheckPoint = onSaveCheckPoint,
                onRemoveCheckPoint = onRemoveCheckPoint
            )

            StopwatchButtons(
                modifier = Modifier.weight(10f).fillMaxWidth(),
                isRunning = isRunning,
                onStartStopwatch = onStartStopwatch,
                onPauseStopwatch = onPauseStopwatch,
                onStopStopwatch = onStopStopwatch,
                onAddCheckPoint = onAddCheckPoint
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun StopwatchButtons(
    modifier: Modifier = Modifier,
    isRunning: Boolean = false,
    onStartStopwatch: () -> Unit,
    onPauseStopwatch: () -> Unit,
    onStopStopwatch: () -> Unit,
    onAddCheckPoint: () -> Unit
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { if (isRunning) onPauseStopwatch() else onStartStopwatch() },
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        ) {
            Icon(
                imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Teal200
            )
        }

        Button(
            onClick = if (isRunning) onAddCheckPoint else onStopStopwatch,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        ) {
            Icon(
                imageVector = if (isRunning) Icons.Default.Flag else Icons.Default.Stop,
                contentDescription = null,
                tint = Teal200
            )
        }
    }
}

@Composable
fun CheckPointsListView(
    modifier: Modifier = Modifier,
    checkPoints: Set<Long>,
    onSaveCheckPoint: (Long) -> Unit,
    onRemoveCheckPoint: (Long) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.Top){
        items(checkPoints.size) { index ->
            CheckPointView(
                modifier = Modifier.fillMaxWidth(),
                checkPoint = checkPoints.elementAt(index),
                onSaveCheckPoint = onSaveCheckPoint,
                onRemoveCheckPoint = onRemoveCheckPoint
            )
        }

    }
}

@Composable
fun CheckPointView(
    modifier: Modifier = Modifier,
    checkPoint: Long,
    onSaveCheckPoint: (Long) -> Unit,
    onRemoveCheckPoint: (Long) -> Unit
) {
    val milliseconds = "%03d".format(checkPoint % 1000)
    val seconds = "%02d".format(checkPoint / 1000 % 60)
    val minutes = "%02d".format(checkPoint / 1000 / 60 % 60)
    val hours = "%02d".format(checkPoint / 1000 / 60 / 60)

    Row(modifier = modifier, horizontalArrangement = Arrangement.End) {
        Text(
            "$hours:$minutes:$seconds:$milliseconds",
            modifier = Modifier
                .align(CenterVertically)
                .fillMaxWidth(0.75f),
            color = Color.White,
            fontSize = 16.sp
        )
        IconButton(onClick = { onSaveCheckPoint(checkPoint) }) {
            Icon(imageVector = Icons.Default.Save, contentDescription = null, tint = Color.White)
        }
        IconButton(onClick = { onRemoveCheckPoint(checkPoint) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Red)
        }
    }
}

@Preview
@Composable
fun PreviewCheckPointView() {
    ClockUtilsTheme {
        CheckPointView(checkPoint = 1000, onSaveCheckPoint = {}, onRemoveCheckPoint = {})
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
            setOf(1000, 2000, 40000),
            onStartStopwatch = { },
            onPauseStopwatch = { },
            onStopStopwatch = { },
            onAddCheckPoint = { },
            onSaveCheckPoint = { },
            onRemoveCheckPoint = { }
        )
    }
}
