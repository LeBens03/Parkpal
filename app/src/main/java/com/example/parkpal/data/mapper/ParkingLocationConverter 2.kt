package com.example.parkpal.data.mapper

import androidx.room.TypeConverter
import com.example.parkpal.domain.model.ParkingLocation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * TypeConverter to help Room database persist complex types that are not natively supported,
 * specifically a list of ParkingLocation objects.
 *
 * Room only supports primitive types and simple collections directly,
 * so we convert the list to a JSON string for storage,
 * and back to a list when reading from the database.
 */
class ParkingLocationConverter {

    private val gson = Gson()

    /**
     * Converts a List of ParkingLocation objects into a JSON String.
     *
     * @param value List of ParkingLocation to convert.
     * @return JSON representation of the list as a String.
     */
    @TypeConverter
    fun fromParkingLocationList(value: List<ParkingLocation>): String {
        return gson.toJson(value)
    }

    /**
     * Converts a JSON String back into a List of ParkingLocation objects.
     *
     * @param value JSON string representing the list.
     * @return Deserialized List of ParkingLocation.
     */
    @TypeConverter
    fun toParkingLocationList(value: String): List<ParkingLocation> {
        val type = object : TypeToken<List<ParkingLocation>>() {}.type
        return gson.fromJson(value, type)
    }
}