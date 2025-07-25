import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.CheckCircle
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
import com.exemplio.geapfitmobile.view.home.screens.agenda.LibraryViewModel
import com.exemplio.geapfitmobile.view.home.screens.client.ClientViewModel

// Data Model
data class Client(
    val initials: String,
    val name: String,
    val lastActivity: String,
    val status: String = "Invitado"
)

val mockClients = listOf(
    Client("EP", "Emiliano Pistone", "Última actividad hace 27 días"),
    Client("FE", "Francelis Esteves", "Última actividad hace 41 días"),
    Client("MO", "Mateo Oppen", "Sin actividad"),
    Client("LL", "Lautaro Lopez Fer...", "Sin actividad"),
)

@Composable
fun ClientScreen(
    clientViewModel: ClientViewModel = hiltViewModel(),
    onCloseSession: () -> Unit
) {
    Scaffold(
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
        ) {
            ErrorBanner()
            InfoCard()
            ClientAssignmentDropdown()
            SearchAndFilterSection()
            ClientListSection(mockClients)
        }
    }
}

@Composable
fun ErrorBanner() {
    Box(
        Modifier
            .fillMaxWidth()
            .background(Color(0xFFE74C3C))
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Parece que ha habido un error en el pago de tu factura 💸",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { /* Revisar facturas */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF424242)),
                shape = RoundedCornerShape(18.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("Revisar facturas", color = Color.White, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun InfoCard() {
    Box(
        Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE8F0FE))
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF5678C7))
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Así ven tus clientes su app",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    "Comprueba cómo ven tus clientes el contenido que has creado para ellos.",
                    fontSize = 13.sp,
                    color = Color(0xFF505050)
                )
            }
            TextButton(onClick = { /* Ver capturas */ }) {
                Text("Ver capturas →", fontSize = 13.sp, color = Color(0xFF5678C7))
            }
        }
    }
}

@Composable
fun ClientAssignmentDropdown() {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("10 Clientes asignados") }
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { expanded = true }
            .padding(vertical = 12.dp, horizontal = 14.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(selectedText, fontSize = 15.sp, color = Color(0xFF505050), modifier = Modifier.weight(1f))
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFFB0BEC5))
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text("10 Clientes asignados") },
                    onClick = {
                        selectedText = "10 Clientes asignados"
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Todos los clientes") },
                    onClick = {
                        selectedText = "Todos los clientes"
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SearchAndFilterSection() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Nombre o email") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(40.dp),
            singleLine = true
        )
        Spacer(Modifier.width(8.dp))
        IconButton(onClick = { /* Filtrar */ }) {
            Icon(Icons.Default.Check, contentDescription = "Filtrar")
        }
        IconButton(onClick = { /* Selección múltiple */ }) {
            Icon(Icons.Default.CheckCircle, contentDescription = "Selección múltiple")
        }
    }
}

@Composable
fun ClientListSection(clients: List<Client>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp)
    ) {
        items(clients) { client ->
            ClientListItem(client)
        }
    }
}

@Composable
fun ClientListItem(client: Client) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFF703EFE)),
            contentAlignment = Alignment.Center
        ) {
            Text(client.initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(client.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(client.lastActivity, fontSize = 13.sp, color = Color(0xFF757575))
        }
        Text(
            client.status,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE8E8E8))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 13.sp,
            color = Color.Black
        )
        IconButton(onClick = { /* More options */ }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Más")
        }
    }
}