import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.exemplio.geapfitmobile.view.home.screens.client.ChatsViewModel

// --- Data Models ---
data class People(
    val name: String,
    val avatarUrl: String,
    val lastMessage: String,
    val dateTime: String,
    val unreadCount: Int
)

// --- State Classes ---
sealed class ChatState
object ChatInitialState : ChatState()
object ChatLoadingProductState : ChatState()
data class ChatLoadedProductState(val chat: List<People>) : ChatState()
data class ChatErrorProductState(val errorMessage: String = "NO HAY CHATS DISPONIBLE") : ChatState()

@Composable
fun ChatsScreen(
    chatsViewModel: ChatsViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    var showErrorDialog by remember { mutableStateOf<String?>(null) }
    val state by remember { derivedStateOf { chatsViewModel.state } }
    val peopleList by remember { derivedStateOf { chatsViewModel.peopleList } }

    LaunchedEffect(Unit) {
        chatsViewModel.init()
    }

    Scaffold(
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF1F1F1))
        ) {
            when (state) {
                is ChatInitialState, is ChatLoadingProductState -> {
                    LoadingCenter()
                }
                is ChatErrorProductState -> {
                    ShowErrorMessageService((state as ChatErrorProductState).errorMessage)
                }
                is ChatLoadedProductState -> {
                    val chat = (state as ChatLoadedProductState).chat
                    if (chat.isEmpty()) {
                        ShowErrorMessage()
                    } else {
                        ChatList(peopleList = chat)
                    }
                }

                else -> {
                    ShowErrorMessage("Unexpected state")
                }
            }
        }
    }

    // Error dialog if needed
    showErrorDialog?.let { message ->
        AlertDialog(
            onDismissRequest = { showErrorDialog = null },
            title = { Text(message) },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = null }) { Text("OK") }
            }
        )
    }
}

@Composable
fun ShowErrorMessage(errorMessage: String = "NO HAY CHATS DISPONIBLE") {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Provide your warning image here if desired
        Spacer(modifier = Modifier.height(10.dp))
        Text(errorMessage)
    }
}

@Composable
fun ShowErrorMessageService(errorMessage: String = "Test screen") {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(errorMessage)
    }
}

@Composable
fun ChatList(peopleList: List<People>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 5.dp)
    ) {
        items(peopleList) { person ->
            PeopleItem(person)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun PeopleItem(people: People) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(12.dp)
            .clickable { /* Navigate to chat */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = people.avatarUrl,
            contentDescription = people.name,
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                people.name,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
            Text(
                people.lastMessage,
                fontSize = 15.sp,
                color = Color(0xFF757575),
                maxLines = 1
            )
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                people.dateTime,
                fontSize = 13.sp,
                color = Color(0xFFB0BEC5)
            )
            if (people.unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF703EFE)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        people.unreadCount.toString(),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// --- Bottom Bar ---
@Composable
fun ChatBottomBar() {
    val context = LocalContext.current
    Row(
        Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(Color.White, shape = RoundedCornerShape(topStart = 45.dp, topEnd = 45.dp)),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            // open URL
            openUrl(context, "https://github.com/siracyakut/bytebuilders-chat-app/")
        }) {
            Icon(Icons.Default.Star, contentDescription = "Language", tint = Color(0xFF808080), modifier = Modifier.size(30.dp))
        }
        IconButton(onClick = {
            openUrl(context, "sms:+905538543421?body=Hello+ByteBuilders!")
        }) {
            Icon(Icons.Default.Person, contentDescription = "Forum", tint = Color(0xFF808080), modifier = Modifier.size(30.dp))
        }
        Box(
            modifier = Modifier
                .size(65.dp)
                .clip(CircleShape)
                .background(Color(0xFF703EFE))
                .clickable { /* Add Chat */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(45.dp))
        }
        IconButton(onClick = {
            openUrl(context, "tel:+905538543421")
        }) {
            Icon(Icons.Default.Call, contentDescription = "Call", tint = Color(0xFF808080), modifier = Modifier.size(30.dp))
        }
        IconButton(onClick = {
            // navigate to profile
            // e.g., navController.navigate("profile")
        }) {
            Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color(0xFF808080), modifier = Modifier.size(30.dp))
        }
    }
}

fun openUrl(context: android.content.Context, url: String) {
    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
    context.startActivity(intent)
}