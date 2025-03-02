package com.nullpointer.devs.drivers.domain.useCase.auth.checkVerifyEmail

import com.nullpointer.devs.drivers.data.exceptions.auth.AuthException
import com.nullpointer.devs.drivers.domain.repository.AuthRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import timber.log.Timber
import java.util.concurrent.CancellationException

class CheckVerifyEmailUseCaseImplTest {

    private val authRepository: AuthRepository = mockk()
    private val useCase = CheckVerifyEmailUseCaseImpl(authRepository)

    private val mockTree = mockk<Timber.Tree>(relaxed = true)

    @Before
    fun setup() {
        // Clear all existing Timber trees
        Timber.uprootAll()
        // Plant the mocked tree
        Timber.plant(mockTree)
    }

    @After
    fun tearDown() {
        // Uproot all trees after tests to clean up
        Timber.uprootAll()
    }

    @Test
    fun `test checkVerifyEmail successfully calls authRepository`() = runBlocking {
        // Arrange
        coEvery { authRepository.checkVerifyEmail() } just Runs

        // Act
        useCase.checkVerifyEmail(CoroutineScope(Dispatchers.Default))

        // Assert
        coVerify { authRepository.checkVerifyEmail() }
    }

    @Test
    fun `test checkVerifyEmail handles UserNotFoundException`() = runBlocking {
        // Arrange
        val exception =
            AuthException.CheckVerifyEmailException.UserNotFoundException("User not found")
        coEvery { authRepository.checkVerifyEmail() } throws exception
        // Act
        useCase.checkVerifyEmail(CoroutineScope(Dispatchers.Default))

        // Assert
        verify { Timber.e("User not found while verifying email: $exception") }
    }

    @Test
    fun `test checkVerifyEmail handles UnauthorizedException`() = runBlocking {
        // Arrange
        val exception =
            AuthException.CheckVerifyEmailException.UnauthorizedException("Unauthorized")
        coEvery { authRepository.checkVerifyEmail() } throws exception

        // Act
        useCase.checkVerifyEmail(CoroutineScope(Dispatchers.Default))

        // Assert
        verify { Timber.e("Unauthorized while verifying email: $exception") }


    }

    @Test
    fun `test checkVerifyEmail handles generic exception`() = runBlocking {
        // Arrange
        val exception = Exception("Generic error")
        coEvery { authRepository.checkVerifyEmail() } throws exception

        // Act
        useCase.checkVerifyEmail(CoroutineScope(Dispatchers.Default))

        // Assert
        verify { Timber.e("Error while verifying email: $exception") }
    }

    @Test
    fun `test checkVerifyEmail handles CancellationException`() = runBlocking {
        // Arrange
        val exception = CancellationException("Operation cancelled")
        coEvery { authRepository.checkVerifyEmail() } throws exception

        try {
            useCase.checkVerifyEmail(CoroutineScope(Dispatchers.Default))
        } catch (e: CancellationException) {
            assertTrue(e.message == "Operation cancelled")
        }
    }
}