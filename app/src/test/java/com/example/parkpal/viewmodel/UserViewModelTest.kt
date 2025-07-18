package com.example.parkpal.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.parkpal.domain.repository.UserRepository
import com.example.parkpal.getOrAwaitValue
import com.example.parkpal.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
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
    fun `fetchCurrentUser   successful fetch`() {
        // Mock `FirebaseAuth` to return a valid authenticated user with an email. 
        // Mock `userRepository.getUserByEmail` to return a valid User object. 
        // Verify that `currentUser` LiveData is updated with the fetched User.
        // TODO implement test
    }

    @Test
    fun `fetchCurrentUser   user not authenticated`() {
        // Mock `FirebaseAuth.getInstance().currentUser` to return null. 
        // Verify that an `IllegalStateException` is logged (or handled appropriately if error handling changes). 
        // Verify that `currentUser` LiveData remains null or unchanged.
        // TODO implement test
    }

    @Test
    fun `fetchCurrentUser   repository throws exception`() {
        // Mock `FirebaseAuth` to return a valid authenticated user. 
        // Mock `userRepository.getUserByEmail` to throw an exception (e.g., IOException, custom repository exception). 
        // Verify that the exception is logged. 
        // Verify that `currentUser` LiveData remains null or unchanged.
        // TODO implement test
    }

    @Test
    fun `fetchCurrentUser   user not found in repository`() {
        // Mock `FirebaseAuth` to return a valid authenticated user. 
        // Mock `userRepository.getUserByEmail` to return null (indicating user not found). 
        // Verify that `currentUser` LiveData is updated with null.
        // TODO implement test
    }

    @Test
    fun `fetchCurrentUser   concurrent calls`() {
        // Call `fetchCurrentUser` multiple times in quick succession. 
        // Ensure that the LiveData is updated correctly based on the last successful call or handles concurrent requests gracefully (e.g., cancels previous, only processes one).
        // TODO implement test
    }

    @Test
    fun `insertUser   successful insertion`() {
        // Provide a valid User object. 
        // Mock `userRepository.insertUser` to return a valid userId (e.g., a positive Long). 
        // Verify that `currentUser` LiveData is updated with the inserted User, including the `userId` returned by the repository.
        // TODO implement test
    }

    @Test
    fun `insertUser   repository throws exception during insertion`() {
        // Provide a valid User object. 
        // Mock `userRepository.insertUser` to throw an exception (e.g., SQLiteConstraintException, IOException). 
        // Verify that the exception is logged. 
        // Verify that `currentUser` LiveData remains unchanged or in its previous state.
        // TODO implement test
    }

    @Test
    fun `insertUser   inserting user with pre existing userId`() {
        // Provide a User object that already has a `userId` set. 
        // Mock `userRepository.insertUser` to return a new `userId`. 
        // Verify that `currentUser.value` is updated with the User object having the *new* `userId` from the repository, not the pre-existing one.
        // TODO implement test
    }

    @Test
    fun `updateUser   successful update`() {
        // Provide a valid User object with an existing `userId`. 
        // Mock `userRepository.updateUser` to complete successfully. 
        // Verify that `currentUser` LiveData is updated with the provided User object.
        // TODO implement test
    }

    @Test
    fun `updateUser   repository throws exception during update`() {
        // Provide a valid User object. 
        // Mock `userRepository.updateUser` to throw an exception (e.g., OptimisticLockException, IOException). 
        // Verify that the exception is logged. 
        // Verify that `currentUser` LiveData remains unchanged or in its previous state.
        // TODO implement test
    }

    @Test
    fun `updateUser   updating a non existent user  repository behavior dependent `() {
        // Provide a User object whose `userId` does not exist in the repository. 
        // Mock `userRepository.updateUser` to reflect this (e.g., return 0 rows affected, or throw specific exception). 
        // Verify appropriate logging and that `currentUser` LiveData behavior is as expected (e.g., remains unchanged or updated if the method optimistically assumes success).
        // TODO implement test
    }

    @Test
    fun `clearCurrentUser   when current user is not null`() {
        // First, ensure `currentUser` has a non-null User value (e.g., after a successful fetch or insert). 
        // Call `clearCurrentUser()`. 
        // Verify that `currentUser` LiveData is updated to null. 
        // Verify that the appropriate log message is generated.
        // TODO implement test
    }

    @Test
    fun `clearCurrentUser   when current user is already null`() {
        // Ensure `currentUser` is already null. 
        // Call `clearCurrentUser()`. 
        // Verify that `currentUser` LiveData remains null. 
        // Verify that the appropriate log message is generated.
        // TODO implement test
    }

    @Test
    fun `ViewModel coroutine scope cancellation`() {
        // Initiate a long-running operation in `fetchCurrentUser`, `insertUser`, or `updateUser` (e.g., by making the repository suspend for a while). 
        // Clear the `viewModelScope` (simulating ViewModel destruction). 
        // Verify that the coroutine is cancelled and does not attempt to update `currentUser` LiveData after cancellation.
        // TODO implement test
    }

    @Test
    fun `LiveData observation`() {
        // Set up an observer on `currentUser`. 
        // Perform actions like `fetchCurrentUser`, `insertUser`, `updateUser`, `clearCurrentUser`. 
        // Verify that the observer receives the correct sequence of User states or null values.
        // TODO implement test
    }

    @Test
    fun `fetchCurrentUser   email with special characters`() {
        // Mock `FirebaseAuth` to return a user with an email containing special characters (e.g., '+', '.', '%'). 
        // Mock `userRepository.getUserByEmail` to handle such emails correctly and return a valid User. 
        // Verify `currentUser` is updated.
        // TODO implement test
    }

    @Test
    fun `insertUser   user object with null or empty fields  if permissible `() {
        // Provide a User object where some non-essential fields are null or empty strings. 
        // Mock `userRepository.insertUser` to handle this. 
        // Verify `currentUser` is updated correctly.
        // TODO implement test
    }

    @Test
    fun `updateUser   user object with null or empty fields  if permissible `() {
        // Provide a User object for update where some fields are set to null or empty strings. 
        // Mock `userRepository.updateUser` to handle this. 
        // Verify `currentUser` is updated correctly.
        // TODO implement test
    }

}