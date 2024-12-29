package com.robotbot.vknewsclient.presentation.main

sealed interface AuthState {

    data object NotAuthorized : AuthState

    data object Authorized : AuthState

    data object Initial : AuthState
}