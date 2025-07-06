package com.example.parkpal.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.parkpal.domain.model.User
import com.example.parkpal.domain.repository.UserRepository
import com.example.parkpal.getOrAwaitValue
import com.example.parkpal.presentation.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.time.delay
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.times
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class UserViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // For LiveData

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var userRepository: UserRepository
    private lateinit var userViewModel: UserViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        userRepository = mock() // Mockito
        userViewModel = UserViewModel(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCurrentUser initial state`() {
        val value = userViewModel.currentUser.getOrAwaitValue()
        assertNull(value)
    }

    @Test
    fun `fetchCurrentUser   successful fetch`() = runTest{
        val mockEmail = "test@gmail.com"
        val expectedUser = User(
            userId = 1L,
            name = "Test User",
            email = mockEmail,
            password = "securePassword123",
            phoneNumber = "+1234567890",
            gender = "Male",
            address = "123 Test Street",
            city = "Testville",
            country = "Testland",
            zipCode = "12345",
            birthDate = "1990-01-01"
        )

        mockStatic(FirebaseAuth::class.java).use { firebaseAuthMock ->
            val firebaseUser = mock(FirebaseUser::class.java)
            whenever(firebaseUser.email).thenReturn(mockEmail)

            val auth = mock(FirebaseAuth::class.java)
            whenever(auth.currentUser).thenReturn(firebaseUser)
            firebaseAuthMock.`when`<Any> { FirebaseAuth.getInstance() }.thenReturn(auth)

            // Mock repository to return expected user
            whenever(userRepository.getUserByEmail(mockEmail)).thenReturn(expectedUser)

            // Act
            userViewModel.fetchCurrentUser()

            // Wait for coroutines to complete
            advanceUntilIdle()

            // Assert
            val result = userViewModel.currentUser.getOrAwaitValue()
            assertEquals(expectedUser, result)
        }
    }

    @Test
    fun `fetchCurrentUser   user not authenticated`() = runTest {
        mockStatic(FirebaseAuth::class.java).use { firebaseAuthMock ->
            // Mock FirebaseAuth and its currentUser to null (user not authenticated)
            val auth = mock(FirebaseAuth::class.java)
            whenever(auth.currentUser).thenReturn(null)
            firebaseAuthMock.`when`<Any> { FirebaseAuth.getInstance() }.thenReturn(auth)

            // Optional: observe currentUser before calling fetchCurrentUser
            val beforeValue = userViewModel.currentUser.value

            // Call fetchCurrentUser
            userViewModel.fetchCurrentUser()

            // Wait for coroutines to complete
            advanceUntilIdle()

            // Verify LiveData remains null (or unchanged)
            val afterValue = userViewModel.currentUser.value
            assertEquals(beforeValue, afterValue)
            assertNull(afterValue)
        }
    }

    @Test
    fun `fetchCurrentUser   repository throws exception`() = runTest {
        val mockEmail = "test@gmail.com"

        mockStatic(FirebaseAuth::class.java).use { firebaseAuthMock ->
            val firebaseUser = mock(FirebaseUser::class.java)
            whenever(firebaseUser.email).thenReturn(mockEmail)

            val auth = mock(FirebaseAuth::class.java)
            whenever(auth.currentUser).thenReturn(firebaseUser)
            firebaseAuthMock.`when`<Any> { FirebaseAuth.getInstance() }.thenReturn(auth)

            // Make repository throw an exception
            whenever(userRepository.getUserByEmail(mockEmail)).thenThrow(RuntimeException("Repository error"))

            // Act
            userViewModel.fetchCurrentUser()

            // Assert
            val result = userViewModel.currentUser.getOrAwaitValue()
            assertNull(result)
        }
    }

    @Test
    fun `fetchCurrentUser   user not found in repository`() = runTest {
        val mockEmail = "test@gmail.com"

        mockStatic(FirebaseAuth::class.java).use { firebaseAuthMock ->
            val firebaseUser = mock(FirebaseUser::class.java)
            whenever(firebaseUser.email).thenReturn(mockEmail)

            val auth = mock(FirebaseAuth::class.java)
            whenever(auth.currentUser).thenReturn(firebaseUser)
            firebaseAuthMock.`when`<Any> { FirebaseAuth.getInstance() }.thenReturn(auth)

            // Simulate user not found (repository returns null)
            whenever(userRepository.getUserByEmail(mockEmail)).thenReturn(null)

            // Act
            userViewModel.fetchCurrentUser()

            // Assert
            val result = userViewModel.currentUser.getOrAwaitValue()
            assertNull(result)
        }
    }

    @Test
    fun `insertUser   successful insertion`() = runTest {
        val userToInsert = User(
            userId = 0L,
            name = "New User",
            email = "newuser@example.com",
            password = "securePass123",
            phoneNumber = "+123456789",
            gender = "Other",
            address = "Test Address",
            city = "Test City",
            country = "Test Country",
            zipCode = "00000",
            birthDate = "2000-01-01"
        )

        val insertedUserId = 42L
        val expectedUser = userToInsert.copy(userId = insertedUserId)

        // Mock insertUser to return the new userId
        whenever(userRepository.insertUser(userToInsert)).thenReturn(insertedUserId)

        // Act
        userViewModel.insertUser(userToInsert)

        // Assert
        val result = userViewModel.currentUser.getOrAwaitValue()
        assertEquals(expectedUser, result)
    }

    @Test
    fun `insertUser   repository throws exception during insertion`() = runTest {
        val userToInsert = User(
            userId = 0L,
            name = "Error User",
            email = "error@example.com",
            password = "password",
            phoneNumber = "+999999999",
            gender = "Unknown",
            address = "123 Error St",
            city = "Errortown",
            country = "Errorland",
            zipCode = "99999",
            birthDate = "2000-01-01"
        )

        val previousUser = User(
            userId = 5L,
            name = "Previous User",
            email = "prev@example.com",
            password = "prevPass",
            phoneNumber = "+111111111",
            gender = "M",
            address = "456 Old St",
            city = "Oldville",
            country = "Oldland",
            zipCode = "11111",
            birthDate = "1999-09-09"
        )
        userViewModel.currentUser.value = previousUser

        // Mock the repository to throw an exception
        whenever(userRepository.insertUser(userToInsert)).thenThrow(RuntimeException("DB error"))

        // Act
        userViewModel.insertUser(userToInsert)

        // Assert: should still be the previous user
        val result = userViewModel.currentUser.getOrAwaitValue()
        assertEquals(previousUser, result)
    }

    @Test
    fun `insertUser   inserting user with pre existing userId`() = runTest {
        val oldUserId = 42L
        val newUserId = 99L

        val userWithOldId = User(
            userId = oldUserId,
            name = "John Doe",
            email = "john@example.com",
            password = "pass",
            phoneNumber = "+123456789",
            gender = "Male",
            address = "123 Street",
            city = "City",
            country = "Country",
            zipCode = "12345",
            birthDate = "1990-01-01"
        )

        // Mock the repository to return a new userId
        whenever(userRepository.insertUser(userWithOldId)).thenReturn(newUserId)

        // Act
        userViewModel.insertUser(userWithOldId)

        // Assert
        val result = userViewModel.currentUser.getOrAwaitValue()
        assertEquals(newUserId, result?.userId)
    }

    @Test
    fun `updateUser   successful update`() = runTest {
        val userToUpdate = User(
            userId = 42L,
            name = "Updated Name",
            email = "updated@example.com",
            password = "newPass",
            phoneNumber = "+987654321",
            gender = "Other",
            address = "456 Updated St",
            city = "NewCity",
            country = "NewCountry",
            zipCode = "67890",
            birthDate = "1995-05-05"
        )

        // Mock the suspend function to return Unit
        whenever(userRepository.updateUser(userToUpdate)).thenReturn(Unit)

        // Act
        userViewModel.updateUser(userToUpdate)

        // Assert
        val result = userViewModel.currentUser.getOrAwaitValue()
        assertEquals(userToUpdate, result)
    }

    @Test
    fun `updateUser   repository throws exception during update`() = runTest {
        val initialUser = User(
            userId = 1L,
            name = "Initial User",
            email = "initial@example.com",
            password = "pass",
            phoneNumber = "000",
            gender = "Male",
            address = "Address",
            city = "City",
            country = "Country",
            zipCode = "00000",
            birthDate = "1990-01-01"
        )

        // Set the initial value of currentUser LiveData
        userViewModel.currentUser.value = initialUser

        val userToUpdate = initialUser.copy(name = "Updated Name")

        // Mock repository to throw exception on updateUser
        whenever(userRepository.updateUser(userToUpdate)).thenThrow(RuntimeException("Update failed"))

        // Act: Call updateUser, which should catch the exception internally
        userViewModel.updateUser(userToUpdate)

        // Assert: currentUser LiveData should remain unchanged (still initialUser)
        val result = userViewModel.currentUser.getOrAwaitValue()
        assertEquals(initialUser, result)
    }

    @Test
    fun `updateUser   updating a non existent user repository throws exception`() = runTest {
        val nonExistentUser = User(
            userId = 9999L,
            name = "Non Existent",
            email = "nonexistent@example.com",
            password = "pass",
            phoneNumber = "000",
            gender = "Other",
            address = "Nowhere",
            city = "Ghost Town",
            country = "Nowhere Land",
            zipCode = "00000",
            birthDate = "2000-01-01"
        )

        // Assume currentUser initially null or some user
        val initialUser = userViewModel.currentUser.getOrAwaitValue()

        // Mock updateUser to throw exception for non-existent user
        whenever(userRepository.updateUser(nonExistentUser)).thenThrow(RuntimeException("User not found"))

        userViewModel.updateUser(nonExistentUser)

        // currentUser should remain unchanged (initial value)
        val result = userViewModel.currentUser.getOrAwaitValue()
        assertEquals(initialUser, result)
    }

    @Test
    fun `clearCurrentUser   when current user is not null`() = runTest {
        // Arrange: set currentUser to a non-null User
        val existingUser = User(
            userId = 1L,
            name = "Test User",
            email = "test@gmail.com",
            password = "securePassword123",
            phoneNumber = "+1234567890",
            gender = "Male",
            address = "123 Test Street",
            city = "Testville",
            country = "Testland",
            zipCode = "12345",
            birthDate = "1990-01-01"
        )
        userViewModel.setCurrentUser(existingUser)

        // Act: clear current user
        userViewModel.clearCurrentUser()

        // Assert: currentUser is null
        val clearedUser = userViewModel.currentUser.getOrAwaitValue()
        assertNull(clearedUser)
    }

    @Test
    fun `clearCurrentUser   when current user is already null`() = runTest {
        // Arrange: currentUser is initially null (default state)
        val initial = userViewModel.currentUser.getOrAwaitValue()
        assertNull(initial)

        // Act: call clearCurrentUser()
        userViewModel.clearCurrentUser()

        // Assert: currentUser remains null
        val afterClear = userViewModel.currentUser.getOrAwaitValue()
        assertNull(afterClear)
    }


    @Test
    fun `ViewModel coroutine scope cancellation`() = runTest {
        val mockEmail = "test@gmail.com"

        // Mock FirebaseAuth to return a valid user email
        mockStatic(FirebaseAuth::class.java).use { firebaseAuthMock ->
            val firebaseUser = mock(FirebaseUser::class.java)
            whenever(firebaseUser.email).thenReturn(mockEmail)

            val auth = mock(FirebaseAuth::class.java)
            whenever(auth.currentUser).thenReturn(firebaseUser)
            firebaseAuthMock.`when`<Any> { FirebaseAuth.getInstance() }.thenReturn(auth)

            // Make repository getUserByEmail suspend for a long time (simulate delay)
            whenever(userRepository.getUserByEmail(mockEmail)).thenAnswer {
                runBlocking {
                    delay(5000)
                    User(1L, "Test User", mockEmail, "pass", "", "", "", "", "", "", birthDate = "")
                }
            }

            // Launch fetchCurrentUser in a separate coroutine
            val job = launch {
                userViewModel.fetchCurrentUser()
            }

            // Immediately cancel ViewModel's scope to simulate ViewModel destroyed
            userViewModel.viewModelScope.cancel()

            // Wait for job to complete (it should cancel quickly)
            job.join()

            // The LiveData value should remain null because the coroutine was cancelled before it could set a value
            val result = userViewModel.currentUser.value
            assertNull(result)
        }
    }

    @Test
    fun `LiveData observation`() = runTest {
        val mockEmail = "test@gmail.com"
        val user1 = User(
            userId = 1L, name = "User One", email = mockEmail,
            password = "pass1", phoneNumber = "123", gender = "M",
            address = "Addr1", city = "City1", country = "Country1", zipCode = "111", birthDate = "2000-01-01"
        )
        val user2 = User(
            userId = 2L, name = "User Two", email = mockEmail,
            password = "pass2", phoneNumber = "456", gender = "F",
            address = "Addr2", city = "City2", country = "Country2", zipCode = "222", birthDate = "2001-02-02"
        )

        val observer = mock<Observer<User?>>()
        userViewModel.currentUser.observeForever(observer)

        mockStatic(FirebaseAuth::class.java).use { firebaseAuthMock ->
            val firebaseUser = mock<FirebaseUser>()
            whenever(firebaseUser.email).thenReturn(mockEmail)
            val auth = mock<FirebaseAuth>()
            whenever(auth.currentUser).thenReturn(firebaseUser)
            firebaseAuthMock.`when`<Any> { FirebaseAuth.getInstance() }.thenReturn(auth)

            whenever(userRepository.getUserByEmail(mockEmail)).thenReturn(user1)

            userViewModel.fetchCurrentUser()
            advanceUntilIdle()

            whenever(userRepository.insertUser(user2)).thenReturn(user2.userId)
            userViewModel.insertUser(user2)
            advanceUntilIdle()

            whenever(userRepository.updateUser(user2)).thenReturn(Unit)
            userViewModel.updateUser(user2)
            advanceUntilIdle()

            userViewModel.clearCurrentUser()
            advanceUntilIdle()
        }

        val captor = argumentCaptor<User>()
        verify(observer, atLeastOnce()).onChanged(captor.capture())
        val emittedValues = captor.allValues

        // Expected sequence (may start with initial null emitted by LiveData)
        // null (initial) -> user1 (fetch) -> user2 (insert) -> user2 (update) -> null (clear)
        // Check emitted sequence contains these values in order:
        val expectedSequence = listOf(null, user1, user2, user2, null)

        // You can check that emittedValues contains at least all expected states in order
        // Or exactly equals expected sequence if your LiveData starts with null:
        assertEquals(expectedSequence, emittedValues)

        userViewModel.currentUser.removeObserver(observer)
    }

    @Test
    fun `insertUser   user object with null or empty fields if permissible`() = runTest {
        val incompleteUser = User(
            userId = 0L,
            name = "Partial User",              // non-nullable
            email = "partial@example.com",      // non-nullable
            password = null,
            phoneNumber = "",
            gender = null,
            address = "",
            city = "Test City",                 // non-nullable
            country = null,
            zipCode = "",
            birthDate = "2000-01-01"            // non-nullable
        )

        // Mock repository to assign userId 10 on insert
        whenever(userRepository.insertUser(incompleteUser)).thenReturn(10L)

        val observer = mock<Observer<User?>>()
        userViewModel.currentUser.observeForever(observer)

        userViewModel.insertUser(incompleteUser)
        advanceUntilIdle()

        val expectedUser = incompleteUser.copy(userId = 10L)

        verify(observer).onChanged(expectedUser)

        userViewModel.currentUser.removeObserver(observer)
    }

    @Test
    fun `updateUser   user object with null or empty fields if permissible`() = runTest {
        val updatedUser = User(
            userId = 5L,
            name = "Existing User",           // non-nullable, unchanged
            email = "existing@example.com",   // non-nullable, unchanged
            password = null,
            phoneNumber = "",
            gender = null,
            address = "",
            city = "CityX",                  // non-nullable, unchanged
            country = null,
            zipCode = "",
            birthDate = "1995-05-05"          // non-nullable, unchanged
        )

        whenever(userRepository.updateUser(updatedUser)).thenReturn(Unit)

        val observer = mock<Observer<User?>>()
        userViewModel.currentUser.observeForever(observer)

        userViewModel.updateUser(updatedUser)
        advanceUntilIdle()

        verify(observer).onChanged(updatedUser)

        userViewModel.currentUser.removeObserver(observer)
    }

}