import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exemplio.geapfitmobile.view.home.screens.agenda.AgendaViewModel
import com.exemplio.geapfitmobile.view.home.screens.client.ChatsViewModel

data class ChatItem(
    val initials: String,
    val name: String,
    val date: String,
    val lastMessage: String
)

val chats = listOf(
    ChatItem("MO", "MATEO OPPEN", "01-03-2025", "Hola, bienvenido a GEAP ACADEMY! Es..."),
    ChatItem("LL", "LAUTARO LOPEZ FERNANDEZ", "17-02-2025", "entrenamiento elite grupal"),
    ChatItem("UH", "URIEL HENDLER", "12-02-2025", "Tenkiu"),
    ChatItem("AB", "ADRIAN BLUM", "12-02-2025", "Welcome to the new app"),
    ChatItem("SL", "SEBASTIÁN LIBERMAN", "11-02-2025", "Tipo medidas físicas, fotos bla bla"),
    ChatItem("AG", "ALEJANDRO GONZÁLEZ ...", "10-02-2025", "entrenamiento elite grupal"),
)

@Composable
fun ChatsScreen(
    chatsViewModel: ChatsViewModel = hiltViewModel(),
    onCloseSession: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    var showOnlyAssigned by remember { mutableStateOf(true) }
    Scaffold(
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
        ) {
            Tabs(selectedTab) { selectedTab = it }
            CheckboxRow(showOnlyAssigned) { showOnlyAssigned = it }
            MassMessageButton()
            SearchField()
            UnreadBanner(unreadCount = 0)
            ChatListSection(chats)
        }
    }
}

@Composable
fun Tabs(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .height(44.dp)
    ) {
        Box(
            Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(20.dp))
                .background(if (selectedTab == 0) Color.Black else Color.White)
                .clickable { onTabSelected(0) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Clientes",
                color = if (selectedTab == 0) Color.White else Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Box(
            Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(20.dp))
                .background(if (selectedTab == 1) Color.Black else Color.White)
                .clickable { onTabSelected(1) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Colaboradores",
                color = if (selectedTab == 1) Color.White else Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun CheckboxRow(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = Color.Black)
        )
        Text(
            "Ver solo mis clientes asignados",
            fontSize = 15.sp,
            color = Color.Black
        )
    }
}

@Composable
fun MassMessageButton() {
    Button(
        onClick = { /* Mass message */ },
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(48.dp)
    ) {
        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.Black)
        Text(
            "Mensaje masivo",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}

@Composable
fun SearchField() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text("Buscar por nombre") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(40.dp),
        singleLine = true
    )
}

@Composable
fun UnreadBanner(unreadCount: Int) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE8F0FE))
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🎂", fontSize = 17.sp)
            Spacer(Modifier.width(10.dp))
            Text(
                "Tienes $unreadCount chats sin leer",
                fontSize = 15.sp,
                color = Color(0xFF505050)
            )
        }
    }
}

@Composable
fun ChatListSection(chats: List<ChatItem>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp)
    ) {
        items(chats) { chat ->
            ChatListItem(chat)
        }
    }
}

@Composable
fun ChatListItem(chat: ChatItem) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(12.dp)
            .clickable { /* Navigate to chat */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFF703EFE)),
            contentAlignment = Alignment.Center
        ) {
            Text(chat.initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(chat.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(chat.lastMessage, fontSize = 14.sp, color = Color(0xFF757575), maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Text(
            chat.date,
            fontSize = 13.sp,
            color = Color(0xFFB0BEC5),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}