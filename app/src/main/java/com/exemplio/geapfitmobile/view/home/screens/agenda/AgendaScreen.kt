import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exemplio.geapfitmobile.view.home.screens.agenda.AgendaUiState
import com.exemplio.geapfitmobile.view.home.screens.agenda.AgendaViewModel
import java.time.LocalDate
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    agendaViewModel: AgendaViewModel = hiltViewModel(), // Use hiltViewModel() if using Hilt
    onCloseSession: () -> Unit
) {
    val state by agendaViewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf<String?>(null) }
    var selectedDay by remember { mutableStateOf<LocalDate?>(null) }

    Scaffold(
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
//            when (state) {
//                is AgendaUiState.Initial, is AgendaUiState.Loading -> {
//                    LaunchedEffect(Unit) {  }
//                    LoadingCenter()
//                }
//                is AgendaUiState.Error -> {
//                    ShowErrorMessageService((state as AgendaUiState.AgendaErrorProductState).errorMessage)
//                }
//                is AgendaUiState.Loaded -> {
//                    val agenda = (state as AgendaUiState.AgendaLoadedProductState).agenda
//                    if (agenda.isEmpty()) {
//                        ShowErrorMessage()
//                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            SimpleCalendar(
                                selectedDay = selectedDay,
                                onDaySelected = { selectedDay = it }
                            )
                            // Show agenda items here...
                        }
//                    }
//                }
//
//                else -> {
//                    ShowErrorMessage("Unexpected state")
//                }
//            }
        }
    }

    // Error dialog
    showDialog?.let { message ->
        AlertDialog(
            onDismissRequest = { showDialog = null },
            title = { Text(message) },
            confirmButton = {
                TextButton(onClick = { showDialog = null }) { Text("OK") }
            }
        )
    }
}

@Composable
fun SimpleCalendar(
    selectedDay: LocalDate?,
    onDaySelected: (LocalDate) -> Unit,
    month: YearMonth = YearMonth.now(),
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val firstDayOfMonth = month.atDay(1)
    val lastDayOfMonth = month.atEndOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 0 (Sun) to 6 (Sat)

    val daysInMonth = (1..lastDayOfMonth.dayOfMonth).map { day ->
        month.atDay(day)
    }

    val calendarDays = buildList<LocalDate?> {
        // Fill empty days at the start
        repeat(firstDayOfWeek) { add(null) }
        // Days of month
        addAll(daysInMonth)
        // Fill empty days at the end to complete the last week
        val remaining = (7 - (size % 7)) % 7
        repeat(remaining) { add(null) }
    }

    Column(modifier = modifier.padding(8.dp)) {
        // Days of week header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            java.time.DayOfWeek.values().forEach { dayOfWeek ->
                Text(
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    modifier = Modifier.weight(1f),
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Calendar grid
        calendarDays.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                week.forEach { day ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .let {
                                if (day != null) {
                                    it.clickable { onDaySelected(day) }
                                } else it
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            day == null -> Box(modifier = Modifier)
                            day == selectedDay -> Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary
                            ) {
                                Text(
                                    text = "${day.dayOfMonth}",
                                    color = Color.White,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            day == today -> Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            ) {
                                Text(
                                    text = "${day.dayOfMonth}",
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            else -> Text(
                                text = "${day.dayOfMonth}",
                                color = Color.Unspecified,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}