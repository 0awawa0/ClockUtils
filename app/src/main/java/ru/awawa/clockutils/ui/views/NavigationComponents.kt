package ru.awawa.clockutils.ui.views

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import ru.awawa.clockutils.ui.theme.ClockUtilsTheme
import ru.awawa.clockutils.ui.theme.Teal200


enum class NavigationItem(val route: String, val icon: ImageVector) {
    Stopwatch("stopwatch", Icons.Default.Timer),
    Timer("timer", Icons.Default.AvTimer)
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
                selectedContentColor = Teal200,
                unselectedContentColor = Color.White
            )
        }
    }
}

@Preview
@Composable
fun PreviewBottomBar() {
    val items = listOf(
        NavigationItem.Stopwatch,
        NavigationItem.Timer
    )
    ClockUtilsTheme {
        BottomBar(
            items = items,
            currentItem = items[0],
            onItemSelected = {}
        )
    }
}