package com.example.parkpal.presentation.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
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
import com.example.parkpal.ui.theme.CircularProgressIndicatorOffset
import com.example.parkpal.ui.theme.LogoOffset
import com.example.parkpal.ui.theme.LogoSize
import com.example.parkpal.ui.theme.MarginMedium
import com.example.parkpal.ui.theme.ParkpalTheme
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(onContinueClicked: () -> Unit ) {

    LaunchedEffect(key1 = true) {
        delay(3000)
        onContinueClicked()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MarginMedium)
            .offset(y = LogoOffset)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.parkpal_logo),
            contentDescription = stringResource(id = R.string.app_logo_content_description),
            modifier = Modifier.size(LogoSize)
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MarginMedium)
            .offset(y = CircularProgressIndicatorOffset),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
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