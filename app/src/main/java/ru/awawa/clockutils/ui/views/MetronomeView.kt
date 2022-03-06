package ru.awawa.clockutils.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MetronomeView(
    modifier: Modifier = Modifier,
    separateBars: Boolean = false,
    onChangeSeparateBars: (Boolean) -> Unit = {},
    valueUpdated: (Int) -> Unit = {}
) {

    Column(modifier = modifier) {
        RoundRangePickView()
        Row(
            Modifier
                .align(CenterHorizontally)
                .fillMaxWidth()) {
            Text("Separate bars")
            Spacer(modifier = Modifier.fillMaxWidth(0.66f))
            Checkbox(checked = separateBars, onCheckedChange = onChangeSeparateBars)
        }
    }

}

@Preview
@Composable
fun PreviewMetronomeView() {
    MetronomeView()
}
