package com.example.parkpal.presentation.screens.main

import androidx.compose.ui.graphics.Color
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun AccountScreen(
    userName: String,
    onPersonalInfoClick: () -> Unit,
    onMyVehicleClick: () -> Unit,
    onSecurityClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    Log.d("ProfileScreen", "User name: $userName")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            // Circular Frame with User Initials
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.split(" ").joinToString("") { it.first().toString() }.uppercase(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        HorizontalDivider(thickness = 2.dp)
        Spacer(modifier = Modifier.height(24.dp))

        AccountRow("Personal Info", onPersonalInfoClick )
        AccountRow("My Vehicle", onMyVehicleClick )

        HorizontalDivider(thickness = 2.dp)
        Spacer(modifier = Modifier.height(16.dp))

        AccountRow("Security", onSecurityClick)
        AccountRow("Language", {})
        AccountRow("Dark Mode", {})

        HorizontalDivider(thickness = 2.dp)
        Spacer(modifier = Modifier.height(16.dp))

        AccountRow("Sign Out", onSignOutClick, textColor = MaterialTheme.colorScheme.error)
    }

}

@Composable
fun AccountRow(
    title: String,
    onClick: () -> Unit,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    leadingIcon: ImageVector = Icons.Default.AccountCircle,
    trailingIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowForward
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.padding(start = 16.dp)
        )

        Text(
            text = title,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp)
        )

        Icon(
            imageVector = trailingIcon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}