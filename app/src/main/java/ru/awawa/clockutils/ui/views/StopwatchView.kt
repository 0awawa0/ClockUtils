package ru.awawa.clockutils.ui.views

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.awawa.clockutils.MainViewModel
import ru.awawa.clockutils.ui.theme.ClockUtilsTheme
import ru.awawa.clockutils.ui.theme.Red
import ru.awawa.clockutils.ui.theme.Teal200
import ru.awawa.clockutils.ui.theme.Teal500

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun StopwatchView(
    modifier: Modifier = Modifier,
    label: String,
    currentTime: Long,
    isRunning: Boolean,
    checkPoints: HashSet<MainViewModel.CheckPoint>,
    onStartStopwatch: () -> Unit,
    onPauseStopwatch: () -> Unit,
    onStopStopwatch: () -> Unit,
    onAddCheckPoint: () -> Unit,
    onSaveCheckPoint: (MainViewModel.CheckPoint) -> Unit,
    onRemoveCheckPoint: (MainViewModel.CheckPoint) -> Unit
) {
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        val spacerWeight = 3f
        val buttonsWeight = 10f
        val boxWeight: Float = 100f - spacerWeight * 3 - buttonsWeight
        Spacer(modifier = Modifier.weight(spacerWeight))
        BoxWithConstraints(modifier = Modifier.weight(boxWeight).padding(16.dp)) {
            var archHeight by remember { mutableStateOf(maxHeight) }
            var checksHeight by remember { mutableStateOf(0.dp) }
            if (checkPoints.any { it.isVisible }) {
                archHeight = maxHeight / 2
                checksHeight = maxHeight - archHeight
            } else {
                archHeight = maxHeight
                checksHeight = 0.dp
            }
            val archSize: Dp by animateDpAsState(archHeight, animationSpec = tween(durationMillis = 250))
            val checksSize: Dp by animateDpAsState(checksHeight, animationSpec = tween(durationMillis = 250))
            TimeArcView(
                modifier = Modifier
                    .align(TopCenter)
                    .height(archSize),
                currentTime = currentTime,
                primaryColor = Teal200,
                secondaryColor = Teal500,
                primaryStrokeWidth = 10f,
                secondaryStrokeWidth = 5f
            )
            LazyColumn(modifier = Modifier
                .align(BottomCenter)
                .height(checksSize)
            ) {

                checkPoints.reversed().forEach {
                    item {
                        androidx.compose.animation.AnimatedVisibility(visible = it.isVisible, exit = fadeOut())
                        {
                            CheckPointView(
                                modifier = Modifier.fillMaxWidth(),
                                checkPoint = it,
                                onSaveCheckPoint = onSaveCheckPoint,
                                onRemoveCheckPoint = onRemoveCheckPoint
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(spacerWeight))
        StopwatchButtons(
            modifier = Modifier
                .weight(buttonsWeight)
                .fillMaxWidth(),
            isRunning = isRunning,
            onStartStopwatch = onStartStopwatch,
            onPauseStopwatch = onPauseStopwatch,
            onStopStopwatch = onStopStopwatch,
            onAddCheckPoint = onAddCheckPoint
        )
        Spacer(modifier = Modifier.weight(spacerWeight))
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
fun CheckPointView(
    modifier: Modifier = Modifier,
    checkPoint: MainViewModel.CheckPoint,
    onSaveCheckPoint: (MainViewModel.CheckPoint) -> Unit,
    onRemoveCheckPoint: (MainViewModel.CheckPoint) -> Unit
) {
    val milliseconds = "%03d".format(checkPoint.timestamp % 1000)
    val seconds = "%02d".format(checkPoint.timestamp / 1000 % 60)
    val minutes = "%02d".format(checkPoint.timestamp / 1000 / 60 % 60)
    val hours = "%02d".format(checkPoint.timestamp / 1000 / 60 / 60)

    Row(modifier = modifier, horizontalArrangement = Arrangement.End) {
        Text(
            "$hours:$minutes:$seconds:$milliseconds",
            modifier = Modifier
                .align(CenterVertically)
                .fillMaxWidth(0.75f),
            color = Color.White,
            fontSize = 18.sp
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
        CheckPointView(checkPoint = MainViewModel.CheckPoint(1000, false), onSaveCheckPoint = {}, onRemoveCheckPoint = {})
    }
}

@ExperimentalFoundationApi
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
            hashSetOf(
                MainViewModel.CheckPoint(1000),
                MainViewModel.CheckPoint(2000),
                MainViewModel.CheckPoint(40000)
            ),
            onStartStopwatch = { },
            onPauseStopwatch = { },
            onStopStopwatch = { },
            onAddCheckPoint = { },
            onSaveCheckPoint = { },
            onRemoveCheckPoint = { }
        )
    }
}
