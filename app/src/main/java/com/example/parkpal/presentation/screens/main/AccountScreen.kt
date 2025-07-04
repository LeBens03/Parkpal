package com.example.parkpal.presentation.screens.main

import androidx.compose.ui.graphics.Color
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.parkpal.R
import com.example.parkpal.ui.theme.*

@Composable
fun AccountScreen(
    userName: String,
    onPersonalInfoClick: () -> Unit,
    onMyVehicleClick: () -> Unit,
    onSecurityClick: () -> Unit,
    onSignOutClick: () -> Unit,
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit
) {
    Log.d("ProfileScreen", "User name: $userName")

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = SpaceExtraLarge)
                .padding(SpaceMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar with initials
            Box(
                modifier = Modifier
                    .size(SpaceExtraLarge.times(2)) // 32.dp * 2 = 64.dp, slightly smaller avatar for modern feel
                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.split(" ").joinToString("") { it.first().toString() }.uppercase(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(SpaceExtraLarge))

            HorizontalDivider(thickness = StrokeWidthThick)

            Spacer(modifier = Modifier.height(ContentSpacing))

            // Section 1
            AccountRow(stringResource(id = R.string.personal_info), onPersonalInfoClick, leadingIcon = Icons.Default.Person)
            AccountRow(stringResource(id = R.string.my_vehicle), onMyVehicleClick, leadingPainter = R.drawable.baseline_directions_car_24)

            Spacer(modifier = Modifier.height(ContentSpacing))

            HorizontalDivider(thickness = StrokeWidthThick)

            Spacer(modifier = Modifier.height(ContentSpacing))

            // Section 2
            AccountRow(stringResource(id = R.string.security), onSecurityClick, leadingIcon = Icons.Default.Settings)
            AccountRow(
                title = stringResource(id = R.string.dark_mode),
                onClick = { },
                leadingPainter = R.drawable.outline_dark_mode_24,
                trailingContent = {
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = onDarkModeToggle,
                        modifier = Modifier.padding(end = SpaceMedium)
                    )
                }
            )

            Spacer(modifier = Modifier.height(ContentSpacing))

            HorizontalDivider(thickness = StrokeWidthThick)

            Spacer(modifier = Modifier.height(ContentSpacing))

            // Sign out (highlighted)
            AccountRow(
                title = stringResource(id = R.string.sign_out),
                onClick = onSignOutClick,
                textColor = MaterialTheme.colorScheme.error,
                leadingIcon = Icons.AutoMirrored.Filled.ExitToApp
            )
        }
    }
}

@Composable
fun AccountRow(
    title: String,
    onClick: () -> Unit,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    leadingIcon: ImageVector? = null,
    leadingPainter: Int? = null,
    trailingIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowForward,
    trailingContent: (@Composable () -> Unit)? = null,  // optional trailing composable
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = ElementSpacing)
            .clickable(enabled = trailingContent == null) { onClick() },  // disable click if trailingContent (like switch) handles it
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.padding(start = SpaceMedium)
            )
        } else if (leadingPainter != null) {
            Icon(
                painter = painterResource(id = leadingPainter),
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.padding(start = SpaceMedium)
            )
        } else {
            Spacer(modifier = Modifier.width(SpaceLarge)) // space placeholder for alignment
        }

        Text(
            text = title,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = SpaceMedium)
        )

        if (trailingContent != null) {
            trailingContent()
        } else {
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.padding(end = SpaceMedium)
            )
        }
    }
}
