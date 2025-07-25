package com.exemplio.geapfitmobile.view.core.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector
import com.exemplio.geapfitmobile.R

sealed class BottomNavigation(
    @StringRes val label: Int, val icon: ImageVector, val tabScreens: TabScreens
) {

    companion object {
        val tabBottomItemsList = listOf(TabHome, TabAgenda, TabBusiness, TabLibrary, TabProfile)
    }

    data object TabHome : BottomNavigation(
        label = R.string.tab_clients, icon = Icons.Default.Person, tabScreens = TabScreens.TabHome
    )

    data object TabProfile : BottomNavigation(
        label = R.string.tab_chat,
        icon = Icons.Default.Email,
        tabScreens = TabScreens.TabProfile
    )

    data object TabLibrary : BottomNavigation(
        label = R.string.tab_library,
        icon = Icons.Default.PlayArrow,
        tabScreens = TabScreens.TabLibrary
    )

    data object TabBusiness : BottomNavigation(
        label = R.string.tab_business, icon = Icons.Default.Info, tabScreens = TabScreens.TabBusiness
    )

    data object TabAgenda : BottomNavigation(
        label = R.string.tab_agenda, icon = Icons.Default.DateRange, tabScreens = TabScreens.TabAgenda
    )
}