package com.robotbot.vknewsclient.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.robotbot.vknewsclient.domain.entity.AuthState
import com.robotbot.vknewsclient.presentation.NewsFeedApplication
import com.robotbot.vknewsclient.presentation.ViewModelFactory
import com.robotbot.vknewsclient.presentation.getApplicationComponent
import com.robotbot.vknewsclient.ui.theme.VkNewsClientTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val component = getApplicationComponent()
            val viewModel: MainViewModel = viewModel(factory = component.getViewModelFactory())
            val authState = viewModel.authState.collectAsState(AuthState.Initial)

            val launcher = rememberLauncherForActivityResult(
                contract = VK.getVKAuthActivityResultContract()
            ) {
                viewModel.performAuthResult()
            }

            VkNewsClientTheme {
                when (authState.value) {
                    AuthState.Authorized -> MainScreen()
                    AuthState.NotAuthorized -> LoginScreen { launcher.launch(listOf(VKScope.WALL, VKScope.FRIENDS)) }
                    AuthState.Initial -> {  }
                }
            }
        }
    }
}