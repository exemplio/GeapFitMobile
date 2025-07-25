import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exemplio.geapfitmobile.view.home.HomeViewModel
import com.exemplio.geapfitmobile.view.home.screens.agenda.AgendaViewModel
import com.exemplio.geapfitmobile.view.home.screens.client.ClientViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

// --- Data Models ---
data class CalendarEvent(val day: Int, val label: String)

val sampleEvents = listOf(
    CalendarEvent(2, "Cumple"),
    CalendarEvent(2, "Cumple"),
    CalendarEvent(9, "Evento especial")
)

@Composable
fun AgendaScreen(agendaViewModel: AgendaViewModel = hiltViewModel(), onCloseSession: () -> Unit) {
    var currentMonth by remember { mutableStateOf(YearMonth.of(2025, 3)) }
    var selectedDay by remember { mutableStateOf<LocalDate?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Add new event */ },
                containerColor = Color.Black,
                shape = CircleShape
            ) { Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.Red) }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            ErrorBanner()
            CalendarTopBar(
                currentMonth = currentMonth,
                onPrevMonth = { currentMonth = currentMonth.minusMonths(1) },
                onNextMonth = { currentMonth = currentMonth.plusMonths(1) },
                onToday = {
                    val today = LocalDate.now()
                    currentMonth = YearMonth.of(today.year, today.month)
                    selectedDay = today
                }
            )
            CalendarKPIs()
            AgendaCalendar(
                currentMonth = currentMonth,
                selectedDay = selectedDay,
                onDaySelected = { selectedDay = it },
                events = sampleEvents
            )
        }
    }
}

@Composable
fun CalendarTopBar(
    currentMonth: YearMonth,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onToday: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 14.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "${currentMonth.month.getDisplayName(TextStyle.SHORT, Locale("es")).replaceFirstChar { it.uppercase() }} ${currentMonth.year}",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable { /* Show month picker */ }
        )
        Spacer(Modifier.width(12.dp))
        IconButton(onClick = onPrevMonth) {
            Icon(Icons.Default.Person, contentDescription = "Mes anterior")
        }
        IconButton(onClick = onNextMonth) {
            Icon(Icons.Default.Person, contentDescription = "Mes siguiente")
        }
        Button(
            onClick = onToday,
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            border = ButtonDefaults.outlinedButtonBorder,
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 0.dp),
            modifier = Modifier.height(36.dp)
        ) {
            Text("Hoy", color = Color.Black, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(6.dp))
        IconButton(onClick = { /* Filtrar */ }) {
            Icon(Icons.Default.Person, contentDescription = "Filtrar")
        }
    }
}

@Composable
fun CalendarKPIs() {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        KpiItem(icon = Icons.Default.Person, value = "0", label = "Total sesiones")
        KpiItem(icon = Icons.Default.Notifications, value = "0", label = "Clientes en cola")
        KpiItem(icon = Icons.Default.DateRange, value = "0", label = "Asistencia", rate = "0%")
    }
}

@Composable
fun KpiItem(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String, rate: String? = null) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = Color(0xFFB0BEC5), modifier = Modifier.size(28.dp))
        Spacer(Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            if (rate != null) {
                Spacer(Modifier.width(2.dp))
                Text(rate, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF3983E8))
            }
        }
        Text(label, fontSize = 13.sp, color = Color(0xFFB0BEC5))
    }
}

@Composable
fun AgendaCalendar(
    currentMonth: YearMonth,
    selectedDay: LocalDate?,
    onDaySelected: (LocalDate) -> Unit,
    events: List<CalendarEvent>
) {
    val today = LocalDate.now()
    val firstDayOfMonth = currentMonth.atDay(1)
    val lastDayOfMonth = currentMonth.atEndOfMonth()
    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.value % 7) // 0 (Sun) to 6 (Sat)

    val daysInMonth = (1..lastDayOfMonth.dayOfMonth).map { currentMonth.atDay(it) }
    val calendarDays = buildList<LocalDate?> {
        repeat(firstDayOfWeek) { add(null) }
        addAll(daysInMonth)
        val remaining = (7 - (size % 7)) % 7
        repeat(remaining) { add(null) }
    }

    val eventsByDay = events.groupBy { it.day }

    Spacer(Modifier.height(8.dp))
    // Day headers
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf("L", "M", "X", "J", "V", "S", "D").forEach { dayName ->
            Text(dayName, fontWeight = FontWeight.Bold, color = Color(0xFFB0BEC5), fontSize = 16.sp, modifier = Modifier.weight(1f))
        }
    }
    // Calendar grid
    Column(Modifier.padding(horizontal = 2.dp)) {
        calendarDays.chunked(7).forEach { week ->
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                week.forEach { day ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(1.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                when {
                                    day == selectedDay -> Color(0xFF703EFE)
                                    day == today -> Color(0xFF703EFE).copy(alpha = 0.08f)
                                    else -> Color.Transparent
                                }
                            )
                            .clickable(day != null) { day?.let { onDaySelected(it) } },
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (day == null) {
                                Spacer(Modifier.height(24.dp))
                            } else {
                                Text(
                                    day.dayOfMonth.toString(),
                                    color = if (day == selectedDay) Color.White else Color(0xFF303030),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                // Events
                                eventsByDay[day.dayOfMonth]?.forEach {
                                    Box(
                                        Modifier
                                            .padding(top = 2.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFFE8F0FE))
                                            .border(1.dp, Color(0xFFB6D0FE), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            it.label,
                                            fontSize = 11.sp,
                                            color = Color(0xFF5678C7)
                                        )
                                    }
                                }
                                if (day == LocalDate.of(2025, 3, 9)) {
                                    Spacer(Modifier.height(12.dp))
                                    Box(
                                        Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF303030)),
                                        contentAlignment = Alignment.Center
                                    ) {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}