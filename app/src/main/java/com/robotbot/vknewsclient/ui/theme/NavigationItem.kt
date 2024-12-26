package com.robotbot.vknewsclient.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.robotbot.vknewsclient.R
import com.robotbot.vknewsclient.navigation.Screen

sealed class NavigationItem(
    val titleResId: Int,
    val icon: ImageVector,
    val screen: Screen
) {

    data object Home : NavigationItem(
        titleResId = R.string.navigation_item_name,
        icon = Icons.Outlined.Home,
        screen = Screen.Home
    )

    data object Favourite : NavigationItem(
        titleResId = R.string.navigation_item_favourite,
        icon = Icons.Outlined.Favorite,
        screen = Screen.Favourite
    )

    data object Profile : NavigationItem(
        titleResId = R.string.navigation_item_profile,
        icon = Icons.Outlined.Person,
        screen = Screen.Profile
    )



}