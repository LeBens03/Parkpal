package com.example.parkpal.presentation.screens.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.parkpal.presentation.viewmodel.MapViewModel
import com.google.maps.android.compose.GoogleMap
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.parkpal.domain.model.ParkingLocation
import com.example.parkpal.presentation.MapEvent
import com.example.parkpal.presentation.viewmodel.CarViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.parkpal.R
import com.example.parkpal.domain.model.Car
import com.example.parkpal.presentation.modals.HomeBottomSheetContent
import com.example.parkpal.presentation.modals.ShowCarsBottomSheetContent
import com.example.parkpal.presentation.viewmodel.ParkingHistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    mapViewModel: MapViewModel,
    carViewModel: CarViewModel,
    parkingHistoryViewModel: ParkingHistoryViewModel
) {
    val state = mapViewModel.state
    val context = LocalContext.current

    val defaultCityLatLng = LatLng(45.0703, 7.6869)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultCityLatLng, 12f) //
    }

    val uiSettings = remember { MapUiSettings(
        zoomControlsEnabled = false,
        zoomGesturesEnabled = true,
        compassEnabled = true,
        myLocationButtonEnabled = true
    ) }

    val currentUserCars by carViewModel.currentUserCars.collectAsState()
    val address by mapViewModel.address.collectAsState()
    val distance by mapViewModel.distance.collectAsState()
    val parkingMarkerState = state.parkingLocation?.let { rememberMarkerState(position = LatLng(it.latitude, it.longitude)) }
    val sheetState = rememberModalBottomSheetState()

    var showBottomSheet by remember { mutableStateOf(false) }
    var permissionsGranted by remember { mutableStateOf(false) }
    var showCarSelectorSheet by remember { mutableStateOf(false) }
    var selectedCar by remember { mutableStateOf<Car?>(null) }

    val requestPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissionsGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        }
    )

    val parkingLocationLabel = stringResource(id = R.string.parking_location_label)
    val addressLabel = stringResource(id = R.string.address_label)
    val googleMaps = stringResource(id = R.string.google_maps)
    val shareParkingLocation = stringResource(id = R.string.share_parking_location)

    Log.d("HomeScreen", "Current User cars: $currentUserCars")
    Log.d("HomeScreen", "Selected Car: $selectedCar")
    Log.d("HomeScreen", "User location: ${state.userLocation}")
    Log.d("HomeScreen", "Parking location: ${state.parkingLocation}")
    Log.d("HomeScreen", "Address: $address")
    Log.d("HomeScreen", "Distance: $distance")

    LaunchedEffect(Unit) {
        val fineLocationStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocationStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (fineLocationStatus == PackageManager.PERMISSION_GRANTED ||
            coarseLocationStatus == PackageManager.PERMISSION_GRANTED
        ) {
            permissionsGranted = true
        } else {
            requestPermissionsLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(permissionsGranted) {
        if (permissionsGranted) {
            mapViewModel.startLocationUpdates()
            Log.d("HomeScreen", "Permissions granted")
        } else {
            Log.e("HomeScreen", "Permissions not granted")
        }
    }

    LaunchedEffect(state.userLocation) {
        state.userLocation?.let { userLoc ->
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLoc, 15f)
            cameraPositionState.animate(cameraUpdate, 1000)
        }
    }

    LaunchedEffect(state.parkingLocation, state.userLocation) {
        mapViewModel.fetchAddress(context, state.parkingLocation)
        mapViewModel.calculateDistance(state.userLocation, state.parkingLocation)
    }

    Scaffold (
        floatingActionButton = {
            if (state.parkingLocation == null && state.userLocation != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp, end = 16.dp), // adjust based on nav bar height
                    contentAlignment = Alignment.BottomEnd
                ) {
                    FloatingActionButton(
                        onClick = { showCarSelectorSheet = true },
                        modifier = Modifier
                            .size(56.dp)
                            .navigationBarsPadding(), // ensures it floats above nav bar
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        elevation = FloatingActionButtonDefaults.elevation(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_directions_car_24), // your parking icon
                            contentDescription = stringResource(id = R.string.park_my_car)
                        )
                    }
                }
            }
        }
    ){ paddingValues ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            properties = mapViewModel.state.properties,
            cameraPositionState = cameraPositionState,
            uiSettings = uiSettings,
        ) {
            // Parking location marker
            parkingMarkerState?.let {
                Marker(
                    state = parkingMarkerState,
                    title = stringResource(id = R.string.parked_here),
                    snippet = stringResource(id = R.string.car_location),
                    onClick = {
                        showBottomSheet = true
                        true
                    }
                )
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            HomeBottomSheetContent(
                distance = distance,
                address = address,
                onNavigateClick = {
                    val uri =
                        "google.navigation:q=${state.parkingLocation?.latitude},${state.parkingLocation?.longitude}".toUri()
                    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                        setPackage("com.google.android.apps.maps")
                    }
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Log.e("HomeScreen", "Error launching Google Maps app", e)
                    }
                },
                onArrivedClick = {
                    state.parkingLocation?.let { location ->
                        val updatedLocation = location.copy(
                            duration = System.currentTimeMillis() - location.timestamp
                        )
                        mapViewModel.onEvent(MapEvent.OnParkingLocationClicked(updatedLocation))
                        selectedCar?.let { car ->
                            parkingHistoryViewModel.addParkingLocation(updatedLocation, car.userId)
                        }
                        showBottomSheet = false
                    }
                },
                onShareClick = {
                    state.parkingLocation?.let { location ->
                        val googleMapsLink = "https://www.google.com/maps/search/?api=1&query=${location.latitude},${location.longitude}"
                        val shareText = buildString {
                            append(parkingLocationLabel)
                            append("\n")
                            append(addressLabel)
                            append(" $address\n")
                            append(googleMaps)
                            append(": $googleMapsLink")
                        }
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, shareParkingLocation))
                    }
                }
            )
        }
    }

    if (showCarSelectorSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showCarSelectorSheet = false
                selectedCar = null
            },
        ) {
            ShowCarsBottomSheetContent(
                cars = currentUserCars,
                onCarSelected = { car ->
                    selectedCar = car
                    if (state.parkingLocation == null && state.userLocation != null) {
                        state.userLocation.let { location ->
                            selectedCar?.let { car ->
                                val parkingLocation = ParkingLocation(
                                    latitude = location.latitude,
                                    longitude = location.longitude,
                                    address = address,
                                    carId = car.carId,
                                    userId = car.userId,
                                    timestamp = System.currentTimeMillis(),
                                    duration = 0
                                )
                                mapViewModel.onEvent(
                                    MapEvent.OnParkMyCarClicked(parkingLocation)
                                )
                            }
                        }
                    }
                    showCarSelectorSheet = false
                }
            )
        }
    }
}