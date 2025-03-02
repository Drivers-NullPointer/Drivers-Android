import com.nullpointer.devs.drivers.R
import com.nullpointer.devs.drivers.data.exceptions.auth.AuthException
import com.nullpointer.devs.drivers.domain.model.ForgotPasswordData
import com.nullpointer.devs.drivers.domain.repository.AuthRepository
import com.nullpointer.devs.drivers.domain.useCase.auth.forgot.ForgotPasswordUseCaseImpl
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CancellationException


class ForgotPasswordUseCaseImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private val authRepository: AuthRepository = mockk()
    private val useCase = ForgotPasswordUseCaseImpl(authRepository)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test forgotPassword successfully calls callbacks`() = runTest {
        // Arrange
        val forgotData = ForgotPasswordData("user@example.com")
        val onError: suspend (Int) -> Unit = mockk(relaxed = true)
        val onStarted: suspend () -> Unit = mockk(relaxed = true)
        val onFinished: suspend () -> Unit = mockk(relaxed = true)
        val onSuccessful: suspend () -> Unit = mockk(relaxed = true)

        coEvery { authRepository.forgotPassword(forgotData) } just Runs

        // Act
        val job = launch {
            useCase.forgotPassword(
                scope = this, // Use the test scope here
                onError = onError,
                onStarted = onStarted,
                onFinished = onFinished,
                onSuccessful = onSuccessful,
                forgotData = forgotData
            )
        }

        job.join() // Wait for the job to complete

        // Assert
        coVerify { onStarted() }
        coVerify { onSuccessful() }
        coVerify { onFinished() }
        coVerify(exactly = 0) { onError(any()) }
    }

    @Test
    fun `test forgotPassword handles exception and calls UserNotFoundException `() = runTest {
        // Arrange
        val forgotData = ForgotPasswordData("user@example.com")

        val onError: suspend (Int) -> Unit = mockk(relaxed = true)
        val onStarted: suspend () -> Unit = mockk(relaxed = true)
        val onFinished: suspend () -> Unit = mockk(relaxed = true)
        val onSuccessful: suspend () -> Unit = mockk(relaxed = true)

        coEvery { authRepository.forgotPassword(forgotData) } throws AuthException.ForgotException.UserNotFoundException(
            "User not found"
        )

        // Act
        val job = launch {
            useCase.forgotPassword(
                scope = this, // Use the test scope here
                onError = onError,
                onStarted = onStarted,
                onFinished = onFinished,
                onSuccessful = onSuccessful,
                forgotData = forgotData
            )
        }

        job.join() // Wait for the job to complete

        // Assert
        coVerify { onStarted() }
        coVerify(exactly = 0) { onSuccessful() }
        coVerify { onFinished() }
        coVerify { onError(any()) }

        // Verify that the correct error message was passed to the onError callback
        val slot = slot<Int>()
        coVerify { onError(capture(slot)) }

        assertTrue(slot.captured == R.string.error_user_not_found)
    }


    @Test
    fun `test forgotPassword handles exception and calls EmailUserNotVerifiedException  `() =
        runTest {
            // Arrange
            val forgotData = ForgotPasswordData("user@example.com")

            val onError: suspend (Int) -> Unit = mockk(relaxed = true)
            val onStarted: suspend () -> Unit = mockk(relaxed = true)
            val onFinished: suspend () -> Unit = mockk(relaxed = true)
            val onSuccessful: suspend () -> Unit = mockk(relaxed = true)

            coEvery { authRepository.forgotPassword(forgotData) } throws AuthException.ForgotException.EmailUserNotVerifiedException(
                "Email not verified"
            )

            // Act
            val job = launch {
                useCase.forgotPassword(
                    scope = this, // Use the test scope here
                    onError = onError,
                    onStarted = onStarted,
                    onFinished = onFinished,
                    onSuccessful = onSuccessful,
                    forgotData = forgotData
                )
            }

            job.join() // Wait for the job to complete

            // Assert
            coVerify { onStarted() }
            coVerify(exactly = 0) { onSuccessful() }
            coVerify { onFinished() }
            coVerify { onError(any()) }

            // Verify that the correct error message was passed to the onError callback
            val slot = slot<Int>()
            coVerify { onError(capture(slot)) }

            assertTrue(slot.captured == R.string.error_email_not_verified)
        }

    @Test
    fun `test forgotPassword handles exception and calls TooManyRequestsException  `() = runTest {
        // Arrange
        val forgotData = ForgotPasswordData("user@example.com")

        val onError: suspend (Int) -> Unit = mockk(relaxed = true)
        val onStarted: suspend () -> Unit = mockk(relaxed = true)
        val onFinished: suspend () -> Unit = mockk(relaxed = true)
        val onSuccessful: suspend () -> Unit = mockk(relaxed = true)

        coEvery { authRepository.forgotPassword(forgotData) } throws AuthException.ForgotException.TooManyRequestsException(
            "Too many requests"
        )

        // Act
        val job = launch {
            useCase.forgotPassword(
                scope = this, // Use the test scope here
                onError = onError,
                onStarted = onStarted,
                onFinished = onFinished,
                onSuccessful = onSuccessful,
                forgotData = forgotData
            )
        }

        job.join() // Wait for the job to complete

        // Assert
        coVerify { onStarted() }
        coVerify(exactly = 0) { onSuccessful() }
        coVerify { onFinished() }
        coVerify { onError(any()) }

        // Verify that the correct error message was passed to the onError callback
        val slot = slot<Int>()
        coVerify { onError(capture(slot)) }

        assertTrue(slot.captured == R.string.error_too_many_requests)

    }

    @Test
    fun `test forgotPassword handles exception and calls ServerException`() = runTest {
        // Arrange
        val forgotData = ForgotPasswordData("user@example.com")

        val onError: suspend (Int) -> Unit = mockk(relaxed = true)
        val onStarted: suspend () -> Unit = mockk(relaxed = true)
        val onFinished: suspend () -> Unit = mockk(relaxed = true)
        val onSuccessful: suspend () -> Unit = mockk(relaxed = true)

        coEvery { authRepository.forgotPassword(forgotData) } throws AuthException.ForgotException.ServerException(
            "Server error"
        )

        // Act
        val job = launch {
            useCase.forgotPassword(
                scope = this, // Use the test scope here
                onError = onError,
                onStarted = onStarted,
                onFinished = onFinished,
                onSuccessful = onSuccessful,
                forgotData = forgotData
            )
        }

        job.join() // Wait for the job to complete

        // Assert
        coVerify { onStarted() }
        coVerify(exactly = 0) { onSuccessful() }
        coVerify { onFinished() }
        coVerify { onError(any()) }

        // Verify that the correct error message was passed to the onError callback
        val slot = slot<Int>()
        coVerify { onError(capture(slot)) }

        assertTrue(slot.captured == R.string.error_server)

    }


    @Test
    fun `test forgotPassword handles exception and calls Exception`() = runTest {
        // Arrange
        val forgotData = ForgotPasswordData("user@example.com")

        val onError: suspend (Int) -> Unit = mockk(relaxed = true)
        val onStarted: suspend () -> Unit = mockk(relaxed = true)
        val onFinished: suspend () -> Unit = mockk(relaxed = true)
        val onSuccessful: suspend () -> Unit = mockk(relaxed = true)

        coEvery { authRepository.forgotPassword(forgotData) } throws Exception("Generic error")

        // Act
        val job = launch {
            useCase.forgotPassword(
                scope = this, // Use the test scope here
                onError = onError,
                onStarted = onStarted,
                onFinished = onFinished,
                onSuccessful = onSuccessful,
                forgotData = forgotData
            )
        }

        job.join() // Wait for the job to complete

        // Assert
        coVerify { onStarted() }
        coVerify(exactly = 0) { onSuccessful() }
        coVerify { onFinished() }
        coVerify { onError(any()) }

        // Verify that the correct error message was passed to the onError callback
        val slot = slot<Int>()
        coVerify { onError(capture(slot)) }

        assertTrue(slot.captured == R.string.error_server)

    }


    @Test
    fun `test forgotPassword handles exception and calls CancellationException`() = runTest {
        // Arrange
        val forgotData = ForgotPasswordData("user@example.com")

        val onError: suspend (Int) -> Unit = mockk(relaxed = true)
        val onStarted: suspend () -> Unit = mockk(relaxed = true)
        val onFinished: suspend () -> Unit = mockk(relaxed = true)
        val onSuccessful: suspend () -> Unit = mockk(relaxed = true)

        coEvery { authRepository.forgotPassword(forgotData) } throws CancellationException("Generic error")

        // Act
        val job = launch {
            try {
                useCase.forgotPassword(
                    scope = this, // Use the test scope here
                    onError = onError,
                    onStarted = onStarted,
                    onFinished = onFinished,
                    onSuccessful = onSuccessful,
                    forgotData = forgotData
                )
            } catch (e: CancellationException) {
                assertTrue(e.message == "Generic error")
            }

        }

        job.join() // Wait for the job to complete


    }

}