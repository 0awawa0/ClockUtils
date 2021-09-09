package ru.awawa.clockutils.ui.views

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.awawa.clockutils.R
import ru.awawa.clockutils.ui.theme.ClockUtilsTheme
import ru.awawa.clockutils.ui.theme.Teal300
import ru.awawa.clockutils.ui.theme.Teal400


enum class NavigationItem(val route: String, val icon: ImageVector, val label: Int) {
    Stopwatch("stopwatch", Icons.Default.Timer, R.string.stopwatch),
    Timer("timer", Icons.Default.AvTimer, R.string.timer),
    Alarm("alarm", Icons.Default.Alarm, R.string.alarm)
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    items: List<NavigationItem>,
    currentItem: NavigationItem,
    onItemSelected: (Int) -> Unit
) {
    BottomNavigation(modifier = modifier) {
        items.forEachIndexed { index, navigationItem ->
            BottomNavigationItem(
                icon = { Icon(navigationItem.icon, contentDescription = null) },
                selected = navigationItem.route == currentItem.route,
                onClick = { onItemSelected(index) },
                selectedContentColor = Teal300,
                unselectedContentColor = Color.White,
                label = { Text(stringResource(navigationItem.label)) }
            )
        }
    }
}

@Preview
@Composable
fun PreviewBottomBar() {
    val items = listOf(
        NavigationItem.Stopwatch,
        NavigationItem.Timer,
        NavigationItem.Alarm
    )
    ClockUtilsTheme {
        BottomBar(
            items = items,
            currentItem = items[0],
            onItemSelected = {}
        )
    }
}