package com.example.parkpal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.parkpal.domain.model.Car
import com.example.parkpal.domain.repository.CarRepository
import com.example.parkpal.presentation.viewmodel.CarViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class CarViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var carRepository: CarRepository
    private lateinit var carViewModel: CarViewModel


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        carRepository = Mockito.mock() // Mockito
        carViewModel = CarViewModel(carRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchCarsOfCurrentUser   successful fetch`() = runTest {
        val mockEmail = "test@gmail.com"
        val expectedCars = listOf(
            Car(1, 1, "Toyota", "Corolla", 2020, "ABC123"),
            Car(2, 1, "Honda", "Civic", 2019, "XYZ789")
        )

        Mockito.mockStatic(FirebaseAuth::class.java).use { firebaseAuthMock ->
            val firebaseUser = Mockito.mock<FirebaseUser>()
            whenever(firebaseUser.email).thenReturn(mockEmail)

            val auth = Mockito.mock<FirebaseAuth>()
            whenever(auth.currentUser).thenReturn(firebaseUser)

            firebaseAuthMock.`when`<Any> { FirebaseAuth.getInstance() }.thenReturn(auth)
            whenever(carRepository.getCarByUserEmail(mockEmail)).thenReturn(expectedCars)

            // When
            carViewModel.fetchCarsOfCurrentUser()
            advanceUntilIdle()

            // Then
            val actualCars = carViewModel.currentUserCars.value
            Assert.assertEquals(expectedCars, actualCars)
        }
    }

    @Test
    fun `fetchCarsOfCurrentUser   user not authenticated`() = runTest {
        Mockito.mockStatic(FirebaseAuth::class.java).use { firebaseAuthMock ->
            val auth = Mockito.mock<FirebaseAuth>()
            whenever(auth.currentUser).thenReturn(null)
            firebaseAuthMock.`when`<Any> { FirebaseAuth.getInstance() }.thenReturn(auth)

            // When
            carViewModel.fetchCarsOfCurrentUser()
            advanceUntilIdle()

            // Then
            val actualCars = carViewModel.currentUserCars.value
            Assert.assertTrue(actualCars.isEmpty())
        }
    }

    @Test
    fun `fetchCarsOfCurrentUser   repository throws exception`() = runTest {
        val mockEmail = "test@gmail.com"

        Mockito.mockStatic(FirebaseAuth::class.java).use { firebaseAuthMock ->
            val firebaseUser = Mockito.mock<FirebaseUser>()
            whenever(firebaseUser.email).thenReturn(mockEmail)

            val auth = Mockito.mock<FirebaseAuth>()
            whenever(auth.currentUser).thenReturn(firebaseUser)

            firebaseAuthMock.`when`<Any> { FirebaseAuth.getInstance() }.thenReturn(auth)

            // Simulate repository throwing exception
            whenever(carRepository.getCarByUserEmail(mockEmail)).thenThrow(RuntimeException("DB error"))

            // When
            carViewModel.fetchCarsOfCurrentUser()
            advanceUntilIdle()

            // Then: state should remain empty (default)
            Assert.assertTrue(carViewModel.currentUserCars.value.isEmpty())
        }
    }

    @Test
    fun `insertCar   successful insertion`() = runTest {
        val initialCars = listOf(
            Car(1, 1, "Toyota", "Corolla", 2020, "ABC123")
        )
        val newCar = Car(2, 1, "Honda", "Civic", 2019, "XYZ789")

        // Set initial cars in state
        carViewModel.setCarOfCurrentUser(initialCars)

        // Mock insertCar call to just complete normally
        whenever(carRepository.insertCar(newCar)).thenReturn(2L)

        // Act
        carViewModel.insertCar(newCar)
        advanceUntilIdle()

        // Assert: new car appended
        val expectedCars = initialCars + newCar
        Assert.assertEquals(expectedCars, carViewModel.currentUserCars.value)
    }

    @Test
    fun `insertCar   repository throws exception`() = runTest {
        val initialCars = listOf(
            Car(1, 1, "Toyota", "Corolla", 2020, "ABC123")
        )
        val newCar = Car(2, 1, "Honda", "Civic", 2019, "XYZ789")

        carViewModel.setCarOfCurrentUser(initialCars)

        whenever(carRepository.insertCar(newCar)).thenThrow(RuntimeException("DB insert error"))

        carViewModel.insertCar(newCar)
        advanceUntilIdle()

        // The list should remain unchanged because insertion failed
        Assert.assertEquals(initialCars, carViewModel.currentUserCars.value)
    }

    @Test
    fun `deleteCar   successful deletion`() = runTest {
        val initialCars = listOf(
            Car(1, 1, "Toyota", "Corolla", 2020, "ABC123"),
            Car(2, 1, "Honda", "Civic", 2019, "XYZ789")
        )
        val carToDelete = initialCars[0]

        carViewModel.setCarOfCurrentUser(initialCars)

        whenever(carRepository.deleteCar(carToDelete)).thenReturn(Unit)

        carViewModel.deleteCar(carToDelete)
        advanceUntilIdle()

        val expectedCars = listOf(initialCars[1])
        Assert.assertEquals(expectedCars, carViewModel.currentUserCars.value)
    }

    @Test
    fun `deleteCar   repository throws exception`() = runTest {
        val initialCars = listOf(
            Car(1, 1, "Toyota", "Corolla", 2020, "ABC123"),
            Car(2, 1, "Honda", "Civic", 2019, "XYZ789")
        )
        val carToDelete = initialCars[0]

        carViewModel.setCarOfCurrentUser(initialCars)

        whenever(carRepository.deleteCar(carToDelete)).thenThrow(RuntimeException("DB delete error"))

        carViewModel.deleteCar(carToDelete)
        advanceUntilIdle()

        // List should remain unchanged due to exception
        Assert.assertEquals(initialCars, carViewModel.currentUserCars.value)
    }

    @Test
    fun `getCarByUserId   fetches cars and updates state`() = runTest {
        val userId = 1L
        val fetchedCars = listOf(
            Car(1, userId, "Toyota", "Corolla", 2020, "ABC123"),
            Car(2, userId, "Honda", "Civic", 2019, "XYZ789")
        )
        whenever(carRepository.getCarsByUserId(userId)).thenReturn(fetchedCars)

        carViewModel.getCarByUserId(userId)
        advanceUntilIdle()

        Assert.assertEquals(fetchedCars, carViewModel.currentUserCars.value)
    }

    @Test
    fun `fetchLicensePlatesForCars   repository returns valid plates updates state`() = runTest {
        val carIds = listOf(1L, 2L)
        val car1 = Car(1, 1, "Toyota", "Corolla", 2020, "ABC123")
        val car2 = Car(2, 1, "Honda", "Civic", 2019, "XYZ789")

        whenever(carRepository.getCarById(1L)).thenReturn(car1)
        whenever(carRepository.getCarById(2L)).thenReturn(car2)

        carViewModel.fetchLicensePlatesForCars(carIds)
        advanceUntilIdle()

        val expected = mapOf(1L to "ABC123", 2L to "XYZ789")
        Assert.assertEquals(expected, carViewModel.licensePlates.value)
    }

    @Test
    fun `fetchLicensePlatesForCars   some IDs not found fallback to Unknown`() = runTest {
        val carIds = listOf(1L, 2L, 3L)
        val car1 = Car(1, 1, "Toyota", "Corolla", 2020, "ABC123")
        val car2 = Car(2, 1, "Honda", "Civic", 2019, "XYZ789")

        whenever(carRepository.getCarById(1L)).thenReturn(car1)
        whenever(carRepository.getCarById(2L)).thenReturn(car2)
        whenever(carRepository.getCarById(3L)).thenReturn(null)

        carViewModel.fetchLicensePlatesForCars(carIds)
        advanceUntilIdle()

        val expected = mapOf(1L to "ABC123", 2L to "XYZ789", 3L to "Unknown")
        Assert.assertEquals(expected, carViewModel.licensePlates.value)
    }

    @Test
    fun `fetchLicensePlatesForCars   repository throws exception plates not updated`() = runTest {
        val carIds = listOf(1L, 2L)
        whenever(carRepository.getCarById(any())).thenThrow(RuntimeException("DB error"))

        // Capture initial state
        val initialPlates = carViewModel.licensePlates.value

        carViewModel.fetchLicensePlatesForCars(carIds)
        advanceUntilIdle()

        // Should remain unchanged
        Assert.assertEquals(initialPlates, carViewModel.licensePlates.value)
    }

    @Test
    fun `clearCarOfCurrentUser   resets currentUserCars to empty list`() = runTest {
        val cars = listOf(Car(1, 1, "Toyota", "Corolla", 2020, "ABC123"))

        carViewModel.setCarOfCurrentUser(cars)

        carViewModel.clearCarOfCurrentUser()

        Assert.assertTrue(carViewModel.currentUserCars.value.isEmpty())
    }

}