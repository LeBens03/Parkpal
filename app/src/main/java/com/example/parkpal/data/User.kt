package com.example.parkpal.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val city: String,
    val birthDate: Date
)
