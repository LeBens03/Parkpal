# ParkPal

ParkPal is a mobile Android application that helps users save the location of their parked vehicles and navigate back to them. The app supports multiple vehicle registration, parking history tracking, and location sharing. It is built using modern Android technologies following clean architecture and MVVM principles.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Setup and Installation](#setup-and-installation)
- [Testing](#testing)
- [Future Work](#future-work)
- [Author](#author)
- [License](#license)

## Features

- User authentication using email/password or Google Sign-In
- Register and manage multiple vehicles
- Save and visualize current parking location on an embedded map
- Navigate back to the saved parking location via Google Maps
- Share parking location with other apps (email, WhatsApp, etc.)
- View history of past parking locations
- Update account information and manage vehicles
- Supports dark mode based on system settings
- Intuitive UI with swipe-to-delete and bottom navigation

## Technology Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose, Material Design
- **Architecture**: MVVM + Clean Architecture
- **Database**: Room
- **Authentication**: Firebase Authentication
- **Navigation**: Jetpack Navigation Compose
- **Location**: Google Maps SDK, FusedLocationProviderClient
- **Dependency Injection**: Hilt
- **Asynchronous Programming**: Kotlin Coroutines

## Architecture

ParkPal follows a modular and layered MVVM architecture with a clear separation of concerns:

- **Presentation Layer**: UI screens built with Jetpack Compose and backed by ViewModels
- **Domain Layer**: Business models and repositories that encapsulate core logic
- **Data Layer**: Room entities, DAOs, and mappers for local persistence

The ViewModels expose screen state using LiveData, StateFlow, or MutableState, and communicate with the domain layer via injected repositories.

## Project Structure

```
app/
│
├── presentation/ # UI screens and ViewModels
│ ├── viewmodel/
│ ├── screens/
│   ├── onboarding/
│   ├── main/
│ └── components/
│
├── domain/ # Business logic and repositories
| ├── model/
│ ├── repositories/
│
├── data/ # Room entities, DAOs, and mappers
│ ├── dao/
│ ├── entity/
│ ├── mapper/
│ └── AppDatabase.kt
│
├── di/ # Hilt modules for dependency injection
│
├── utils/ # Utility classes (e.g., location, event/state)
│
├── theme/ # Application-wide themes and styles
│
├── MainActivity.kt
├── ParkpalApplication.kt # Custom class for hilt di
└── AppNavigation.kt
```

## Setup and Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/LeBens03/Parkpal2.git
   
2. Open the project in Android Studio (version Arctic Fox or later).
3. Add your Google Maps API key in local.properties: MAPS_API_KEY=your_api_key
4. Connect your Firebase project and configure authentication providers.
5. Build and run the application on an emulator or Android device running Android 9.0 (API 28) or higher.

## Testing

### Unit Tests
Core business logic in ViewModels is tested using JUnit, Mockito, and coroutine testing utilities.
### Instrumented Tests
Authentication flow is validated using a fake ViewModel and UI automation.

## Future Work

### The following features are planned but not yet implemented:

In-app navigation without redirecting to Google Maps
Save favorite parking locations with photos and notes
Automatic parking location saving via Bluetooth when user moves away from car

## Author

Taha Benslimane
University of Turin
GitHub: LeBens03

## License

This project is provided for educational purposes.
You are free to use, modify, and distribute it under the terms of the MIT License.
