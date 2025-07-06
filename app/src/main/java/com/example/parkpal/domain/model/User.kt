package com.example.parkpal.domain.model

/**
 * Represents a user in the domain/business layer of the application.
 *
 * This model is used throughout the app's business logic,
 * independent from the persistence layer.
 *
 * @property userId Unique identifier for the user, default is 0 for new users.
 * @property name Full name of the user.
 * @property email User's email address.
 * @property password Optional password for the user (nullable for external auth or security reasons).
 * @property phoneNumber Optional phone number of the user.
 * @property gender Optional gender information.
 * @property address Optional street address.
 * @property city City where the user resides.
 * @property country Optional country of residence.
 * @property zipCode Optional postal/zip code.
 * @property birthDate User's birth date as a string (e.g., "YYYY-MM-DD").
 */
data class User(
    val userId: Long = 0,
    val name: String,
    val email: String,
    val password: String?,
    val phoneNumber: String?,
    val gender: String?,
    val address: String?,
    val city: String,
    val country: String?,
    val zipCode: String?,
    val birthDate: String
)