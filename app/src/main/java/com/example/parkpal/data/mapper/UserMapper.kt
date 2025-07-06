package com.example.parkpal.data.mapper

import com.example.parkpal.data.entity.UserEntity
import com.example.parkpal.domain.model.User

/**
 * Extension function to convert a UserEntity (database layer) to a User (domain layer).
 *
 * This mapping isolates the domain model from persistence concerns,
 * allowing the app to work with clean business logic objects.
 *
 * @receiver UserEntity The data entity fetched from the Room database.
 * @return User The corresponding domain model object.
 */
fun UserEntity.toUser(): User {
    return User(
        userId = userId,
        name = name,
        email = email,
        password = password,
        phoneNumber = phoneNumber,
        city = city,
        birthDate = birthDate,
        address = address,
        country = country,
        zipCode = zipCode,
        gender = gender
    )
}

/**
 * Extension function to convert a User (domain layer) to a UserEntity (database layer).
 *
 * This mapping prepares the domain model for storage in the Room database.
 *
 * @receiver User The domain model object representing user data.
 * @return UserEntity The corresponding database entity.
 */
fun User.toUserEntity(): UserEntity {
    return UserEntity(
        userId = userId,
        name = name,
        email = email,
        password = password,
        phoneNumber = phoneNumber,
        city = city,
        birthDate = birthDate,
        address = address,
        country = country,
        zipCode = zipCode,
        gender = gender
    )
}