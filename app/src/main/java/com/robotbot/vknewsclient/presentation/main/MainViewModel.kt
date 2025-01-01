package com.robotbot.vknewsclient.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.vknewsclient.domain.usecases.CheckAuthStateUseCase
import com.robotbot.vknewsclient.domain.usecases.GetAuthStateFlowUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    getAuthStateFlowUseCase: GetAuthStateFlowUseCase,
    private val checkAuthStateUseCase: CheckAuthStateUseCase
) : ViewModel() {

    val authState = getAuthStateFlowUseCase()

    fun performAuthResult() {
        viewModelScope.launch {
            checkAuthStateUseCase()
        }
    }

}