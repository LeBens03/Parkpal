package com.example.parkpal

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.parkpal.presentation.BottomNavDestination
import com.example.parkpal.presentation.BottomNavigationBar
import com.example.parkpal.presentation.screens.main.AccountScreen
import com.example.parkpal.presentation.screens.main.HomeScreen
import com.example.parkpal.presentation.screens.main.SignInScreen
import com.example.parkpal.presentation.screens.main.MyVehicleScreen
import com.example.parkpal.presentation.screens.main.ParkingHistoryScreen
import com.example.parkpal.presentation.screens.main.PersonalInfoScreen
import com.example.parkpal.presentation.screens.main.SettingsScreen
import com.example.parkpal.presentation.screens.onboarding.CarInfoScreen
import com.example.parkpal.presentation.screens.onboarding.UserInfoScreen
import com.example.parkpal.presentation.screens.onboarding.WelcomeScreen
import com.example.parkpal.presentation.viewmodel.AuthState
import com.example.parkpal.presentation.viewmodel.AuthViewModel
import com.example.parkpal.presentation.viewmodel.CarViewModel
import com.example.parkpal.presentation.viewmodel.ParkingHistoryViewModel
import com.example.parkpal.presentation.viewmodel.UserViewModel

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    val userViewModel: UserViewModel = hiltViewModel()
    val carViewModel: CarViewModel = hiltViewModel()
    val parkingHistoryViewModel: ParkingHistoryViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = viewModel()
    var context = LocalContext.current

    val authState = authViewModel.authState.collectAsState()
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> {
                userViewModel.clearCurrentUser()
                carViewModel.clearCarOfCurrentUser()
            }
            is AuthState.Authenticated -> {
                userViewModel.fetchCurrentUser()
                carViewModel.fetchCarsOfCurrentUser()
            }
            else -> Unit
        }
    }

    // List of destinations where the Bottom Navigation Bar is visible
    val bottomNavDestinations = listOf(
        BottomNavDestination.MyCar,
        BottomNavDestination.ParkingHistory,
        BottomNavDestination.Account
    )

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    // Determine if the bottom navigation bar should be shown
    val shouldShowBottomNav = bottomNavDestinations.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomNav) {
                BottomNavigationBar(
                    currentDestination = bottomNavDestinations.find { it.route == currentRoute } ?: BottomNavDestination.MyCar,
                    onDestinationClicked = { destination ->
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("welcome") { WelcomeScreen(
                onContinueClicked = { navController.navigate("signIn") }
            ) }

            composable("signIn") { SignInScreen(
                authViewModel = authViewModel,
                onSignIn = { navController.navigate(BottomNavDestination.MyCar.route) },
                onSignUp = { navController.navigate("userInfo") }
            ) }

            composable("userInfo") { UserInfoScreen(
                userViewModel = userViewModel,
                authViewModel = authViewModel,
                onSaveUser = { navController.navigate("carInfo") },
                onBack = { navController.popBackStack() }
            ) }

            composable("carInfo") { CarInfoScreen(
                userViewModel = userViewModel,
                carViewModel = carViewModel,
                onCarSaved = { navController.navigate(BottomNavDestination.MyCar.route) }
            ) }

            composable(BottomNavDestination.MyCar.route) { HomeScreen(
                carViewModel = carViewModel,
                parkingHistoryViewModel = parkingHistoryViewModel
            ) }

            composable(BottomNavDestination.ParkingHistory.route) { ParkingHistoryScreen(
                userId = userViewModel.currentUser.value?.userId ?: 0L,
                carViewModel = carViewModel,
                parkingHistoryViewModel = parkingHistoryViewModel
            ) }

            composable(BottomNavDestination.Account.route) { AccountScreen(
                userName = userViewModel.currentUser.value?.name ?: "John Doe",
                onPersonalInfoClick = { navController.navigate("personalInfo") },
                onMyVehicleClick = { navController.navigate("vehicleInfo") },
                onSecurityClick = { navController.navigate("settingsScreen") },
                onLanguageClick = {  },
                onSignOutClick = {
                    authViewModel.signOut(context)
                    navController.navigate("signIn")
                },
            ) }

            composable("personalInfo") { PersonalInfoScreen(
                userViewModel = userViewModel,
                onBack = { navController.popBackStack() }
            ) }

            composable("vehicleInfo") { MyVehicleScreen(
                userId = userViewModel.currentUser.value?.userId ?: 0L,
                carViewModel = carViewModel,
                onBack = { navController.popBackStack() }
            ) }

            composable("settingsScreen") { SettingsScreen(
                authViewModel = authViewModel,
                userViewModel = userViewModel,
                onBack = { navController.popBackStack() }
            ) }
        }
    }
}