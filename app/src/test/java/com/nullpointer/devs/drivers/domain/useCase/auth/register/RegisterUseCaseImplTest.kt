package com.nullpointer.devs.drivers.domain.useCase.auth.register

import com.nullpointer.devs.drivers.R
import com.nullpointer.devs.drivers.data.exceptions.auth.AuthException
import com.nullpointer.devs.drivers.domain.model.RegisterData
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
import kotlin.coroutines.cancellation.CancellationException

class RegisterUseCaseImplTest {

    private val authRepository = mockk<AuthRepository>()
    private val testDispatcher = StandardTestDispatcher()
    private val registerUseCase = RegisterUseCaseImpl(authRepository)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `register should call onStarted and onFinished when registration is successful`() =
        runTest {
            // Given
            val registerData = mockk<RegisterData>()
            val onError: suspend (Int) -> Unit = mockk(relaxed = true)
            val onStarted: suspend () -> Unit = mockk(relaxed = true)
            val onFinished: suspend () -> Unit = mockk(relaxed = true)

            coEvery { authRepository.registerCredentials(registerData) } returns Unit

            // When
            val job = launch {

                registerUseCase.register(
                    scope = this,
                    onError = onError,
                    onStarted = onStarted,
                    onFinished = onFinished,
                    registerData = registerData
                )
            }

            // Then
            job.join()

            coVerify { onStarted() }
            coVerify { onFinished() }

        }

    @Test
    fun `register should call onError when registration fails with UserAlreadyExistsException`() =
        runTest {
            // Given
            val registerData = mockk<RegisterData>()
            val onError: suspend (Int) -> Unit = mockk(relaxed = true)
            val onStarted: suspend () -> Unit = mockk(relaxed = true)
            val onFinished: suspend () -> Unit = mockk(relaxed = true)

            coEvery { authRepository.registerCredentials(registerData) } throws AuthException.RegisterException.UserAlreadyExistsException(
                "User already exists"
            )

            // When
            val job = launch {

                registerUseCase.register(
                    scope = this,
                    onError = onError,
                    onStarted = onStarted,
                    onFinished = onFinished,
                    registerData = registerData
                )
            }

            // Then
            job.join()

            coVerify { onStarted() }
            coVerify { onError(any()) }

            val slot = slot<Int>()
            coVerify { onError(capture(slot)) }

            assertEquals(R.string.error_user_already_exists, slot.captured)
        }

    @Test
    fun `register should call onError when registration fails with TooManyRequestsException`() =
        runTest {
            // Given
            val registerData = mockk<RegisterData>()
            val onError: suspend (Int) -> Unit = mockk(relaxed = true)
            val onStarted: suspend () -> Unit = mockk(relaxed = true)
            val onFinished: suspend () -> Unit = mockk(relaxed = true)

            coEvery { authRepository.registerCredentials(registerData) } throws AuthException.RegisterException.TooManyRequestsException(
                "Too many requests"
            )

            // When
            val job = launch {

                registerUseCase.register(
                    scope = this,
                    onError = onError,
                    onStarted = onStarted,
                    onFinished = onFinished,
                    registerData = registerData
                )
            }

            // Then
            job.join()

            coVerify { onStarted() }
            coVerify { onError(any()) }

            val slot = slot<Int>()
            coVerify { onError(capture(slot)) }

            assertEquals(R.string.error_too_many_requests, slot.captured)
        }


    @Test
    fun `register should call onError when registration fails with ServerException`() =
        runTest {
            // Given
            val registerData = mockk<RegisterData>()
            val onError: suspend (Int) -> Unit = mockk(relaxed = true)
            val onStarted: suspend () -> Unit = mockk(relaxed = true)
            val onFinished: suspend () -> Unit = mockk(relaxed = true)

            coEvery { authRepository.registerCredentials(registerData) } throws AuthException.RegisterException.ServerException(
                "Server error"
            )

            // When
            val job = launch {

                registerUseCase.register(
                    scope = this,
                    onError = onError,
                    onStarted = onStarted,
                    onFinished = onFinished,
                    registerData = registerData
                )
            }

            // Then
            job.join()

            coVerify { onStarted() }
            coVerify { onError(any()) }

            val slot = slot<Int>()
            coVerify { onError(capture(slot)) }

            assertEquals(R.string.error_server, slot.captured)
        }

    @Test
    fun `register should call onError when registration fails with an unknown error`() =
        runTest {
            // Given
            val registerData = mockk<RegisterData>()
            val onError: suspend (Int) -> Unit = mockk(relaxed = true)
            val onStarted: suspend () -> Unit = mockk(relaxed = true)
            val onFinished: suspend () -> Unit = mockk(relaxed = true)

            coEvery { authRepository.registerCredentials(registerData) } throws Exception("Unknown error")

            // When
            val job = launch {

                registerUseCase.register(
                    scope = this,
                    onError = onError,
                    onStarted = onStarted,
                    onFinished = onFinished,
                    registerData = registerData
                )
            }

            // Then
            job.join()

            coVerify { onStarted() }
            coVerify { onError(any()) }

            val slot = slot<Int>()
            coVerify { onError(capture(slot)) }

            assertEquals(R.string.error_server, slot.captured)
        }

    @Test
    fun `register should call onError when registration fails with a CancellationException`() =
        runTest {
            // Given
            val registerData = mockk<RegisterData>()
            val onError: suspend (Int) -> Unit = mockk(relaxed = true)
            val onStarted: suspend () -> Unit = mockk(relaxed = true)
            val onFinished: suspend () -> Unit = mockk(relaxed = true)

            coEvery { authRepository.registerCredentials(registerData) } throws CancellationException()

            // When
            val job = launch {

                registerUseCase.register(
                    scope = this,
                    onError = onError,
                    onStarted = onStarted,
                    onFinished = onFinished,
                    registerData = registerData
                )
            }

            // Then
            job.join()

            coVerify { onStarted() }
        }

}