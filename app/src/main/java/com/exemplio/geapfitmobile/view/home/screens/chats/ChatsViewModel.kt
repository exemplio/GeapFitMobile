package com.exemplio.geapfitmobile.view.home.screens.client

import ChatErrorProductState
import ChatInitialState
import ChatLoadedProductState
import ChatLoadingProductState
import ChatState
import People
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exemplio.geapfitmobile.data.repository.ClientRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(val repository: ClientRepositoryImpl) : ViewModel() {

    var state by mutableStateOf<ChatState>(ChatInitialState)

    var peopleList by mutableStateOf(listOf<People>())

    fun init() {
        state = ChatLoadingProductState
        viewModelScope.launch {
            // Simulating API delay
            delay(1200)
            val people = loadRandomPeople()
            peopleList = people
            state = if (people.isNotEmpty()) ChatLoadedProductState(people) else ChatErrorProductState()
        }
    }

    fun refresh() {
        init()
    }

    private fun loadRandomPeople(): List<People> {
        // Replace this with real API call
        return listOf(
            People(
                name = "Alice Smith",
                avatarUrl = "https://randomuser.me/api/portraits/women/1.jpg",
                lastMessage = "Hey there!",
                dateTime = "09:00",
                unreadCount = 1
            ),
            People(
                name = "Bob Johnson",
                avatarUrl = "https://randomuser.me/api/portraits/men/2.jpg",
                lastMessage = "Let's catch up.",
                dateTime = "Yesterday",
                unreadCount = 0
            ),
            People(
                name = "Charlie Davis",
                avatarUrl = "https://randomuser.me/api/portraits/men/3.jpg",
                lastMessage = "How are you?",
                dateTime = "Sun",
                unreadCount = 2
            )
        )
    }

}