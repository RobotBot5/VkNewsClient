package com.robotbot.vknewsclient.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.robotbot.vknewsclient.R
import com.robotbot.vknewsclient.ui.theme.DarkBlue
import com.robotbot.vknewsclient.ui.theme.VkNewsClientTheme

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.drawable.vk_logo),
                contentDescription = null
            )
            Spacer(Modifier.height(100.dp))
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkBlue,
                    contentColor = Color.White
                ),
                onClick = { onLoginClick() }
            ) {
                Text(text = stringResource(R.string.button_login))
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    VkNewsClientTheme {
        LoginScreen {  }
    }
}