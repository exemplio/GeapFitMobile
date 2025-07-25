package com.exemplio.geapfitmobile.view.core.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.exemplio.geapfitmobile.R

sealed class BottomNavigation(
    @StringRes val label: Int, @DrawableRes val icon: Int, val tabScreens: TabScreens
) {

    companion object {
        val tabBottomItemsList = listOf(TabHome, TabAgenda, TabBusiness, TabLibrary, TabProfile)
    }

    data object TabHome : BottomNavigation(
        label = R.string.tab_clients, icon = R.drawable.ic_home, tabScreens = TabScreens.TabHome
    )

    data object TabProfile : BottomNavigation(
        label = R.string.tab_chat,
        icon = R.drawable.ic_profile,
        tabScreens = TabScreens.TabProfile
    )

    data object TabLibrary : BottomNavigation(
        label = R.string.tab_library,
        icon = R.drawable.ic_add,
        tabScreens = TabScreens.TabBusiness
    )

    data object TabBusiness : BottomNavigation(
        label = R.string.tab_business, icon = R.drawable.ic_reels, tabScreens = TabScreens.TabLibrary
    )

    data object TabAgenda : BottomNavigation(
        label = R.string.tab_agenda, icon = R.drawable.ic_search, tabScreens = TabScreens.TabAgenda
    )
}