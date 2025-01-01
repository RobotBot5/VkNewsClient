package com.robotbot.vknewsclient.domain

sealed interface AuthState {

    data object NotAuthorized : AuthState

    data object Authorized : AuthState

    data object Initial : AuthState
}