package com.robotbot.vknewsclient.ui.theme

sealed interface AuthState {

    data object NotAuthorized : AuthState

    data object Authorized : AuthState

    data object Initial : AuthState
}