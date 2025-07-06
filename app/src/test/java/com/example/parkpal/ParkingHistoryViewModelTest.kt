package com.example.parkpal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.parkpal.domain.model.ParkingHistory
import com.example.parkpal.domain.model.ParkingLocation
import com.example.parkpal.domain.repository.ParkingHistoryRepository
import com.example.parkpal.presentation.viewmodel.ParkingHistoryViewModel
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class ParkingHistoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: ParkingHistoryRepository
    private lateinit var viewModel: ParkingHistoryViewModel

    private val sampleUserId = 123L
    private val sampleLocation = ParkingLocation(
        parkingLocationId = 1,
        userId = sampleUserId,
        carId = 10,
        latitude = 45.0,
        longitude = 7.0,
        address = "Sample Address",
        timestamp = 1000L,
        duration = 3600L
    )
    private val sampleHistory = ParkingHistory(
        parkingHistoryId = 1,
        userId = sampleUserId,
        parkingLocations = listOf(sampleLocation)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = Mockito.mock()
        viewModel = ParkingHistoryViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchParkingHistory success updates state`() = runTest(testDispatcher) {
        whenever(repository.getParkingHistoryByUserId(sampleUserId)).thenReturn(sampleHistory)

        viewModel.fetchParkingHistory(sampleUserId)

        advanceUntilIdle()

        val result = viewModel.currentParkingHistory.value
        Assert.assertEquals(sampleHistory, result)

        verify(repository, Mockito.times(1)).getParkingHistoryByUserId(sampleUserId)
    }

    @Test
    fun `fetchParkingHistory exception does not update state`() = runTest(testDispatcher) {
        whenever(repository.getParkingHistoryByUserId(sampleUserId)).thenThrow(RuntimeException("fail"))

        viewModel.fetchParkingHistory(sampleUserId)

        advanceUntilIdle()

        val result = viewModel.currentParkingHistory.value
        Assert.assertNull(result)

        verify(repository, Mockito.times(1)).getParkingHistoryByUserId(sampleUserId)
    }

    @Test
    fun `addParkingLocation adds to existing history`() = runTest(testDispatcher) {
        whenever(repository.insertParkingHistory(any())).thenReturn(Unit)
        whenever(repository.getParkingHistoryByUserId(sampleUserId)).thenReturn(sampleHistory)

        viewModel.fetchParkingHistory(sampleUserId)
        advanceUntilIdle()

        val newLocation = sampleLocation.copy(parkingLocationId = 2, address = "New Address")

        viewModel.addParkingLocation(newLocation, sampleUserId)
        advanceUntilIdle()

        val updatedHistory = viewModel.currentParkingHistory.value
        Assert.assertEquals(sampleUserId, updatedHistory?.userId)
        Assert.assertEquals(2, updatedHistory?.parkingLocations?.size)
        Assert.assertTrue(updatedHistory?.parkingLocations?.contains(newLocation) == true)

        verify(repository, Mockito.times(1)).insertParkingHistory(any())
    }

    @Test
    fun `addParkingLocation creates new history when none exists`() = runTest(testDispatcher) {
        whenever(repository.insertParkingHistory(any())).thenReturn(Unit)
        whenever(repository.getParkingHistoryByUserId(sampleUserId)).thenReturn(null)

        val before = viewModel.currentParkingHistory.value
        Assert.assertNull(before)

        val newLocation = sampleLocation.copy(parkingLocationId = 2)

        viewModel.addParkingLocation(newLocation, sampleUserId)
        advanceUntilIdle()

        val after = viewModel.currentParkingHistory.value
        Assert.assertNotNull(after)
        Assert.assertEquals(sampleUserId, after?.userId)
        Assert.assertEquals(1, after?.parkingLocations?.size)
        Assert.assertEquals(newLocation, after?.parkingLocations?.first())

        verify(repository, Mockito.times(1)).insertParkingHistory(any())
    }

    @Test
    fun `deleteParkingLocation removes location from existing history`() = runTest(testDispatcher) {
        val initialHistory = sampleHistory.copy(parkingLocations = listOf(sampleLocation))
        whenever(repository.insertParkingHistory(any())).thenReturn(Unit)
        whenever(repository.getParkingHistoryByUserId(sampleUserId)).thenReturn(initialHistory)

        viewModel.fetchParkingHistory(sampleUserId)
        advanceUntilIdle()

        viewModel.deleteParkingLocation(sampleLocation)
        advanceUntilIdle()

        val updatedHistory = viewModel.currentParkingHistory.value
        Assert.assertNotNull(updatedHistory)
        Assert.assertTrue(updatedHistory?.parkingLocations?.isEmpty() == true)

        verify(repository, Mockito.times(1)).insertParkingHistory(any())
    }

    @Test
    fun `deleteParkingLocation does nothing if no current history`() = runTest(testDispatcher) {
        whenever(repository.insertParkingHistory(any())).thenReturn(Unit)

        val before = viewModel.currentParkingHistory.value
        Assert.assertNull(before)

        viewModel.deleteParkingLocation(sampleLocation)
        advanceUntilIdle()

        val after = viewModel.currentParkingHistory.value
        Assert.assertNull(after)

        verify(repository, Mockito.never()).insertParkingHistory(any())
    }

    @Test
    fun `clearParkingHistory clears current history and calls repository delete`() =
        runTest(testDispatcher) {
            whenever(repository.deleteParkingHistoryById(sampleUserId)).thenReturn(Unit)
            whenever(repository.getParkingHistoryByUserId(sampleUserId)).thenReturn(sampleHistory)

            viewModel.fetchParkingHistory(sampleUserId)
            advanceUntilIdle()

            viewModel.clearParkingHistory(sampleUserId)
            advanceUntilIdle()

            val after = viewModel.currentParkingHistory.value
            Assert.assertNull(after)

            verify(repository, Mockito.times(1)).deleteParkingHistoryById(sampleUserId)
        }

    @Test
    fun `clearParkingHistory does nothing if no current history`() = runTest(testDispatcher) {
        whenever(repository.deleteParkingHistoryById(sampleUserId)).thenReturn(Unit)

        val before = viewModel.currentParkingHistory.value
        Assert.assertNull(before)

        viewModel.clearParkingHistory(sampleUserId)
        advanceUntilIdle()

        val after = viewModel.currentParkingHistory.value
        Assert.assertNull(after)

        verify(repository, Mockito.never()).deleteParkingHistoryById(any())
    }

}