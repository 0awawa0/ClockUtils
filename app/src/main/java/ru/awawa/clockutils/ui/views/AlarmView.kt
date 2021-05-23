package ru.awawa.clockutils.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.awawa.clockutils.database.Alarm

@Composable
fun AlarmView(
    modifier: Modifier = Modifier,
    label: String,
    alarms: List<Alarm>
) {
    Column(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(alarms) { alarm ->
                SingleAlarmRow(
                    modifier = Modifier.fillMaxWidth(),
                    alarm = alarm
                )
            }
        }

        Button(onClick = { }) {
            Text("Add new")
        }
    }
}

@Composable
fun SingleAlarmRow(
    modifier: Modifier = Modifier,
    alarm: Alarm
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(alarm.label)
            Text(alarm.time.toString())
        }
    }
}

@Preview
@Composable
fun PreviewAlarmView() {
    val alarms = listOf(
        Alarm(
            time = 12345,
            repeat = "",
            ringtone = "",
            vibrate = true,
            label = "Alarm1"
        ),
        Alarm(
            time = 873492734,
            repeat = "",
            ringtone = "",
            vibrate = true,
            label = "Alarm2"
        ),
        Alarm(
            time = 987234,
            repeat = "",
            ringtone = "",
            vibrate = true,
            label = "Alarm3"
        )
    )

    AlarmView(label = "Alarm", alarms = alarms)
}