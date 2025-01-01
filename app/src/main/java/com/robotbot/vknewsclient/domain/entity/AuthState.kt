package com.robotbot.vknewsclient.domain.entity

sealed interface AuthState {

    data object NotAuthorized : AuthState

    data object Authorized : AuthState

    data object Initial : AuthState
}