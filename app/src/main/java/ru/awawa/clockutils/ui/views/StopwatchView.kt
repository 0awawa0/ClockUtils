package ru.awawa.clockutils.ui.views

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.awawa.clockutils.MainViewModel
import ru.awawa.clockutils.ui.theme.*


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StopwatchView(
    modifier: Modifier = Modifier,
    currentTime: Long,
    isRunning: Boolean,
    checkPoints: HashSet<MainViewModel.CheckPoint>,
    onSwitchStopwatch: () -> Unit,
    onResetStopwatch: () -> Unit,
    onAddCheckPoint: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val blink by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, delayMillis = 2000),
            RepeatMode.Reverse
        )
    )
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        val spacerWeight = 2f
        val buttonsWeight = 8f
        val boxWeight: Float = 100f - spacerWeight * 3 - buttonsWeight
        Spacer(modifier = Modifier.weight(spacerWeight))
        BoxWithConstraints(modifier = Modifier
            .weight(boxWeight)
            .padding(16.dp)
        ) {
            val spacerHeight = 32.dp
            var archHeight by remember { mutableStateOf(maxHeight) }
            var checksHeight by remember { mutableStateOf(0.dp) }
            if (checkPoints.isNotEmpty()) {
                archHeight = maxHeight / 2
                checksHeight = maxHeight - archHeight - spacerHeight
            } else {
                archHeight = maxHeight
                checksHeight = 0.dp
            }
            val archSize: Dp by animateDpAsState(archHeight, animationSpec = tween(durationMillis = 250))
            val checksSize: Dp by animateDpAsState(checksHeight, animationSpec = tween(durationMillis = 250))
            TimeArcView(
                modifier = Modifier
                    .align(TopCenter)
                    .height(archSize)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onSwitchStopwatch
                    )
                    .alpha(if (isRunning) 1f else blink),
                currentTime = currentTime,
                primaryColor = Teal300,
                secondaryColor = Grey700,
                primaryStrokeWidth = 10f,
                secondaryStrokeWidth = 5f,
                pointerColor = Red400
            )

            LazyColumn(modifier = Modifier
                .align(BottomCenter)
                .height(checksSize),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                checkPoints.reversed().forEach {
                    item {
                        CheckPointView(
                            modifier = Modifier.fillMaxWidth(),
                            checkPoint = it
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(spacerWeight))
        AnimatedVisibility(modifier = Modifier
            .weight(buttonsWeight)
            .fillMaxWidth()
            .align(CenterHorizontally),
            visible = currentTime != 0L,
            enter = slideInHorizontally(animationSpec = tween(250)) + fadeIn(),
            exit = slideOutHorizontally({ it + it / 2}, animationSpec = tween(250)) + fadeOut()
        ) {
            BoxWithConstraints {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = if (isRunning) onAddCheckPoint else onResetStopwatch,
                        modifier = Modifier
                            .size(this@BoxWithConstraints.maxHeight)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isRunning) Icons.Default.Flag else Icons.Default.Refresh,
                            contentDescription = null,
                            tint = Teal300
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(spacerWeight))
    }
}

@Composable
fun CheckPointView(
    modifier: Modifier = Modifier,
    checkPoint: MainViewModel.CheckPoint
) {
    val milliseconds = "%03d".format(checkPoint.timestamp % 1000)
    val seconds = "%02d".format(checkPoint.timestamp / 1000 % 60)
    val minutes = "%02d".format(checkPoint.timestamp / 1000 / 60 % 60)
    val hours = "%02d".format(checkPoint.timestamp / 1000 / 60 / 60)

    Card(
        modifier = modifier,
        elevation = 10.dp,
        backgroundColor = Grey700
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "$hours:$minutes:$seconds:$milliseconds",
                color = Color.White,
                fontSize = 16.sp
            )

            if (checkPoint.difference != -1L) {
                val diffMillis = "%03d".format(checkPoint.difference % 1000)
                val diffSeconds = "%02d".format(checkPoint.difference / 1000 % 60)
                val diffMins = "%02d".format(checkPoint.difference / 1000 / 60 % 60)
                val diffHours = "%02d".format(checkPoint.difference / 1000 / 60 / 60)
                Text(
                    "+$diffHours:$diffMins:$diffSeconds:$diffMillis",
                    color = Red400,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCheckPointView() {
    CheckPointView(checkPoint = MainViewModel.CheckPoint(1000, 500))
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Preview
@Composable
fun PreviewStopwatchView() {
    ClockUtilsTheme {
        StopwatchView(
            modifier = Modifier.fillMaxSize(),
            currentTime = 30L * 1000,
            isRunning = true,
            hashSetOf(
                MainViewModel.CheckPoint(1000, 0),
                MainViewModel.CheckPoint(2000, 1000),
                MainViewModel.CheckPoint(40000, 40000)
            ),
            onSwitchStopwatch = { },
            onResetStopwatch = { },
            onAddCheckPoint = { }
        )
    }
}
