package com.nullpointer.devs.drivers.domain.useCase.auth.login

import com.nullpointer.devs.drivers.R
import com.nullpointer.devs.drivers.data.exceptions.auth.AuthException
import com.nullpointer.devs.drivers.domain.model.CredentialsData
import com.nullpointer.devs.drivers.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CancellationException

class LoginUseCaseImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private val authRepository = mockk<AuthRepository>()
    private val loginUseCase = LoginUseCaseImpl(authRepository)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `login should call onStarted and onFinished when login is successful`() = runTest {
        // Given
        val credentialsData = mockk<CredentialsData>()
        val onError: suspend (Int) -> Unit = mockk(relaxed = true)
        val onStarted: suspend () -> Unit = mockk(relaxed = true)
        val onFinished: suspend () -> Unit = mockk(relaxed = true)

        coEvery { authRepository.loginCredentials(credentialsData) } returns Unit

        // When
        val job = launch {

            loginUseCase.login(
                scope = this,
                onError = onError,
                onStarted = onStarted,
                onFinished = onFinished,
                credentialsData = credentialsData
            )
        }

        // Then
        job.join()

        coVerify { onStarted() }
        coVerify { onFinished() }
        coVerify(exactly = 0) { onError(any()) }

    }

    @Test
    fun `login should call onError when login fails with UserNotFoundException`() = runTest {
        // Given
        val credentialsData = mockk<CredentialsData>()
        val onError: suspend (Int) -> Unit = mockk(relaxed = true)
        val onStarted: suspend () -> Unit = mockk(relaxed = true)
        val onFinished: suspend () -> Unit = mockk(relaxed = true)

        coEvery { authRepository.loginCredentials(credentialsData) } throws AuthException.LoginException.UserNotFoundException(
            "User not found"
        )

        // When
        val job = launch {

            loginUseCase.login(
                scope = this,
                onError = onError,
                onStarted = onStarted,
                onFinished = onFinished,
                credentialsData = credentialsData
            )
        }

        // Then
        job.join()

        coVerify { onError(any()) }
        coVerify { onStarted() }
        coVerify { onFinished() }

        val slot = slot<Int>()
        coVerify { onError(capture(slot)) }

        assertEquals(R.string.error_user_not_found, slot.captured)

    }

    @Test
    fun `login should call onError when login fails with InvalidCredentialsException`() = runTest {
        // Given
        val credentialsData = mockk<CredentialsData>()
        val onError: suspend (Int) -> Unit = mockk(relaxed = true)
        val onStarted: suspend () -> Unit = mockk(relaxed = true)
        val onFinished: suspend () -> Unit = mockk(relaxed = true)

        coEvery { authRepository.loginCredentials(credentialsData) } throws AuthException.LoginException.InvalidCredentialsException(
            "Invalid credentials"
        )

        // When
        val job = launch {

            loginUseCase.login(
                scope = this,
                onError = onError,
                onStarted = onStarted,
                onFinished = onFinished,
                credentialsData = credentialsData
            )
        }

        // Then
        job.join()

        coVerify { onError(any()) }
        coVerify { onStarted() }
        coVerify { onFinished() }

        val slot = slot<Int>()
        coVerify { onError(capture(slot)) }

        assertEquals(R.string.error_invalid_credentials, slot.captured)

    }

    @Test
    fun `login should call onError when login fails with ServerException`() = runTest {
        // Given
        val credentialsData = mockk<CredentialsData>()
        val onError: suspend (Int) -> Unit = mockk(relaxed = true)
        val onStarted: suspend () -> Unit = mockk(relaxed = true)
        val onFinished: suspend () -> Unit = mockk(relaxed = true)

        coEvery { authRepository.loginCredentials(credentialsData) } throws AuthException.LoginException.ServerException(
            "Server error"
        )

        // When
        val job = launch {

            loginUseCase.login(
                scope = this,
                onError = onError,
                onStarted = onStarted,
                onFinished = onFinished,
                credentialsData = credentialsData
            )
        }

        // Then
        job.join()

        coVerify { onError(any()) }
        coVerify { onStarted() }
        coVerify { onFinished() }

        val slot = slot<Int>()
        coVerify { onError(capture(slot)) }

        assertEquals(R.string.error_server, slot.captured)

    }

    @Test
    fun `login should call onError when login fails with TooManyRequestsException`() = runTest {
        // Given
        val credentialsData = mockk<CredentialsData>()
        val onError: suspend (Int) -> Unit = mockk(relaxed = true)
        val onStarted: suspend () -> Unit = mockk(relaxed = true)
        val onFinished: suspend () -> Unit = mockk(relaxed = true)

        coEvery { authRepository.loginCredentials(credentialsData) } throws AuthException.LoginException.TooManyRequestsException(
            "Too many requests"
        )

        // When
        val job = launch {

            loginUseCase.login(
                scope = this,
                onError = onError,
                onStarted = onStarted,
                onFinished = onFinished,
                credentialsData = credentialsData
            )
        }

        // Then
        job.join()

        coVerify { onError(any()) }
        coVerify { onStarted() }
        coVerify { onFinished() }

        val slot = slot<Int>()
        coVerify { onError(capture(slot)) }

        assertEquals(R.string.error_too_many_requests, slot.captured)

    }

    @Test
    fun `login should call onError when login fails with unknown exception`() = runTest {
        // Given
        val credentialsData = mockk<CredentialsData>()
        val onError: suspend (Int) -> Unit = mockk(relaxed = true)
        val onStarted: suspend () -> Unit = mockk(relaxed = true)
        val onFinished: suspend () -> Unit = mockk(relaxed = true)

        coEvery { authRepository.loginCredentials(credentialsData) } throws Exception("Unknown error")

        // When
        val job = launch {

            loginUseCase.login(
                scope = this,
                onError = onError,
                onStarted = onStarted,
                onFinished = onFinished,
                credentialsData = credentialsData
            )
        }

        // Then
        job.join()

        coVerify { onError(any()) }
        coVerify { onStarted() }
        coVerify { onFinished() }

        val slot = slot<Int>()
        coVerify { onError(capture(slot)) }

        assertEquals(R.string.error_server, slot.captured)

    }

    @Test
    fun `login should call onError when login fails with CancellationException`() = runTest {
        // Given
        val credentialsData = mockk<CredentialsData>()
        val onError: suspend (Int) -> Unit = mockk(relaxed = true)
        val onStarted: suspend () -> Unit = mockk(relaxed = true)
        val onFinished: suspend () -> Unit = mockk(relaxed = true)

        coEvery { authRepository.loginCredentials(credentialsData) } throws CancellationException("Cancelled")

        // When
        val job = launch {

            loginUseCase.login(
                scope = this,
                onError = onError,
                onStarted = onStarted,
                onFinished = onFinished,
                credentialsData = credentialsData
            )
        }

        // Then
        job.join()


        coVerify { onStarted() }
        coVerify { onFinished() }


    }
}