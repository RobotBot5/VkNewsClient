package com.robotbot.vknewsclient.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import com.robotbot.vknewsclient.MainViewModel
import com.robotbot.vknewsclient.navigation.AppNavGraph
import com.robotbot.vknewsclient.navigation.rememberNavigationState

@Composable
fun MainScreen(viewModel: MainViewModel) {

    val navigationState = rememberNavigationState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val items = listOf(
                    NavigationItem.Home,
                    NavigationItem.Favourite,
                    NavigationItem.Profile
                )
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.screen.route,
                        onClick = { navigationState.navigateTo(item.screen.route) },
                        icon = {
                            Icon(item.icon, contentDescription = null)
                        },
                        label = {
                            Text(text = stringResource(id = item.titleResId))
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                            unselectedTextColor = MaterialTheme.colorScheme.onSecondary,
                            indicatorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        AppNavGraph(
            navHostController = navigationState.navHostController,
            homeScreenContent = {
                HomeScreen(viewModel = viewModel, paddingValues = paddingValues)
            },
            favouriteScreenContent = {
                TextCounter(
                    name = "Favourite",
                    modifier = Modifier.padding(paddingValues)
                )
            },
            profileScreenContent = {
                TextCounter(
                    name = "Profile",
                    modifier = Modifier.padding(paddingValues)
                )
            }
        )
    }
}

@Composable
private fun TextCounter(name: String, modifier: Modifier = Modifier) {
    var counter by rememberSaveable {
        mutableStateOf(0)
    }

    Text(
        modifier = modifier.clickable { counter++ },
        text = "$name Count: $counter",
        color = Color.Black
    )
}