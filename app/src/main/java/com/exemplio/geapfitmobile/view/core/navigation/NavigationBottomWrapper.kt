package com.exemplio.geapfitmobile.view.core.navigation

import AgendaScreen
import BusinessScrenn
import ChatsScreen
import ClientScreen
import LibraryScreen
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.exemplio.geapfitmobile.data.repository.ClientRepositoryImpl
import com.exemplio.geapfitmobile.domain.repository.ClientApiService
import com.exemplio.geapfitmobile.view.auth.login.LoginViewModel
import com.exemplio.geapfitmobile.view.core.navigation.TabScreens.*
import com.exemplio.geapfitmobile.view.home.screens.agenda.AgendaViewModel

@Composable
fun NavigationBottomWrapper(modifier: Modifier = Modifier, navHostController: NavHostController) {
    NavHost(modifier = modifier, navController = navHostController, startDestination = TabHome) {
        composable<TabHome> {
            ClientScreen(onCloseSession = {
            })
        }
        composable<TabAgenda> {
            AgendaScreen(onCloseSession = {
            })
        }
        composable<TabLibrary> {
            LibraryScreen(onCloseSession = {
            })
        }
        composable<TabBusiness> {
            BusinessScrenn(onCloseSession = {
            })
        }
        composable<TabProfile> {
            ChatsScreen(onCloseSession = {
            })
        }
    }
}
