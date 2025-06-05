package com.example.parkpal.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parkpal.R
import com.example.parkpal.ui.theme.LogoSize
import com.example.parkpal.ui.theme.MarginMedium
import com.example.parkpal.ui.theme.ParkpalTheme
import com.example.parkpal.ui.theme.SpaceMedium

@Composable
fun WelcomeScreen(onContinueClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MarginMedium)
            .offset(y = (128).dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Logo
        Image(
            painter = painterResource(id = R.drawable.parkpal_logo),
            contentDescription = stringResource(id = R.string.app_logo_content_description),
            modifier = Modifier.size(LogoSize)
        )

        Spacer(modifier = Modifier.height(SpaceMedium))

        // Welcome Text
        Text(
            text = stringResource(id = R.string.welcome_message),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(SpaceMedium))

        Text(
            text = stringResource(id = R.string.onboarding_subtitle),
            style = MaterialTheme.typography.bodyLarge
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MarginMedium)
            .offset(y = (-24).dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Continue Button
        Button(onClick = onContinueClicked) {
            Text(text = stringResource(id = R.string.continue_button_text))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    ParkpalTheme {
        WelcomeScreen(onContinueClicked = {})
    }
}