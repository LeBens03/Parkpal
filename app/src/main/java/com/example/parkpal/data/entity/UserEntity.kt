package com.example.parkpal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a user in the application.
 * This entity corresponds to the "user_table" in the Room database.
 *
 * @property userId The unique identifier for the user (auto-generated primary key).
 * @property name The full name of the user.
 * @property email The user's email address (used for login/identification).
 * @property password The user's password (nullable for security or external auth).
 * @property phoneNumber The user's phone number (optional).
 * @property gender The gender of the user (optional).
 * @property address The user's street address (optional).
 * @property city The city where the user resides.
 * @property country The country where the user resides (optional).
 * @property zipCode The postal code of the user's address (optional).
 * @property birthDate The user's birth date, stored as a string (e.g., "YYYY-MM-DD").
 */
@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Long,
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