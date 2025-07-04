package com.example.parkpal.presentation.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.parkpal.R
import com.example.parkpal.ui.theme.LogoSize
import com.example.parkpal.ui.theme.SpaceMedium
import com.example.parkpal.ui.theme.ParkpalTheme
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(onContinueClicked: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000)
        onContinueClicked()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(SpaceMedium),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.parkpal_logo),
            contentDescription = stringResource(id = R.string.app_logo_content_description),
            modifier = Modifier.size(LogoSize)
        )

        Spacer(modifier = Modifier.weight(1f))

        CircularProgressIndicator(
            modifier = Modifier.padding(bottom = SpaceMedium)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    ParkpalTheme {
        WelcomeScreen(
            onContinueClicked = {}
        )
    }
}