package com.robotbot.vknewsclient.presentation

import android.app.Application
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.robotbot.vknewsclient.di.ApplicationComponent
import com.robotbot.vknewsclient.di.DaggerApplicationComponent

class NewsFeedApplication : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(
            context = this
        )
    }

}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    Log.d("RECOMPOSITION_TAG", "getApplicationComponent")
    return (LocalContext.current.applicationContext as NewsFeedApplication).component
}