import app.cash.turbine.test
import com.example.android.domain.common.Resource
import com.example.android.domain.usecases.registration.RegisterUseCase
import com.example.android.domain.usecases.user.AddUserToDBUseCase
import com.example.android.pets_finder.registration.RegistrationStatus
import com.example.android.pets_finder.registration.RegistrationViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class RegistrationViewModelTests {
    @Test
    fun `empty email produces RegistrationStatus#EmptyEmail`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val registerUseCase = mockk<RegisterUseCase>()
        val addUserToDBUseCase = mockk<AddUserToDBUseCase>()

        val registrationViewModel = RegistrationViewModel(
            registerUseCase = registerUseCase,
            addUserToDBUseCase = addUserToDBUseCase,
            dispatcher = dispatcher
        )

        registrationViewModel.register(
            EMPTY_STRING,
            USERNAME,
            PHONE_NUMBER,
            PASSWORD,
            PASSWORD
        )

        registrationViewModel.userSignUpStatus.test {
            assertEquals(RegistrationStatus.Initialization, awaitItem())
            assertEquals(RegistrationStatus.EmptyEmail, awaitItem())
        }
    }

    @Test
    fun `empty userName produces RegistrationStatus#EmptyUserName`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val registerUseCase = mockk<RegisterUseCase>()
        val addUserToDBUseCase = mockk<AddUserToDBUseCase>()

        val registrationViewModel = RegistrationViewModel(
            registerUseCase = registerUseCase,
            addUserToDBUseCase = addUserToDBUseCase,
            dispatcher = dispatcher
        )

        registrationViewModel.register(
            EMAIL,
            EMPTY_STRING,
            PHONE_NUMBER,
            PASSWORD,
            PASSWORD
        )

        registrationViewModel.userSignUpStatus.test {
            assertEquals(RegistrationStatus.Initialization, awaitItem())
            assertEquals(RegistrationStatus.EmptyUserName, awaitItem())
        }
    }

    @Test
    fun `empty phoneNumber produces RegistrationStatus#EmptyPhoneNumber`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val registerUseCase = mockk<RegisterUseCase>()
        val addUserToDBUseCase = mockk<AddUserToDBUseCase>()

        val registrationViewModel = RegistrationViewModel(
            registerUseCase = registerUseCase,
            addUserToDBUseCase = addUserToDBUseCase,
            dispatcher = dispatcher
        )

        registrationViewModel.register(
            EMAIL,
            USERNAME,
            EMPTY_STRING,
            PASSWORD,
            PASSWORD
        )

        registrationViewModel.userSignUpStatus.test {
            assertEquals(RegistrationStatus.Initialization, awaitItem())
            assertEquals(RegistrationStatus.EmptyPhoneNumber, awaitItem())
        }
    }

    @Test
    fun `empty password produces RegistrationStatus#EmptyPassword`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val registerUseCase = mockk<RegisterUseCase>()
        val addUserToDBUseCase = mockk<AddUserToDBUseCase>()

        val registrationViewModel = RegistrationViewModel(
            registerUseCase = registerUseCase,
            addUserToDBUseCase = addUserToDBUseCase,
            dispatcher = dispatcher
        )

        registrationViewModel.register(
            EMAIL,
            USERNAME,
            PHONE_NUMBER,
            EMPTY_STRING,
            PASSWORD
        )

        registrationViewModel.userSignUpStatus.test {
            assertEquals(RegistrationStatus.Initialization, awaitItem())
            assertEquals(RegistrationStatus.EmptyPassword, awaitItem())
        }
    }

    @Test
    fun `empty password produces RegistrationStatus#EmptyConfPassword`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val registerUseCase = mockk<RegisterUseCase>()
        val addUserToDBUseCase = mockk<AddUserToDBUseCase>()

        val registrationViewModel = RegistrationViewModel(
            registerUseCase = registerUseCase,
            addUserToDBUseCase = addUserToDBUseCase,
            dispatcher = dispatcher
        )

        registrationViewModel.register(
            EMAIL,
            USERNAME,
            PHONE_NUMBER,
            PASSWORD,
            EMPTY_STRING
        )

        registrationViewModel.userSignUpStatus.test {
            assertEquals(RegistrationStatus.Initialization, awaitItem())
            assertEquals(RegistrationStatus.EmptyConfirmPassword, awaitItem())
        }
    }

    @Test
    fun `mismatched passwords produces RegistrationStatus#PassAndConfDoNotMatch`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val registerUseCase = mockk<RegisterUseCase>()
        val addUserToDBUseCase = mockk<AddUserToDBUseCase>()

        val registrationViewModel = RegistrationViewModel(
            registerUseCase = registerUseCase,
            addUserToDBUseCase = addUserToDBUseCase,
            dispatcher = dispatcher
        )

        registrationViewModel.register(
            EMAIL,
            USERNAME,
            PHONE_NUMBER,
            PASSWORD,
            WRONG_CONF_PASS
        )

        registrationViewModel.userSignUpStatus.test {
            assertEquals(RegistrationStatus.Initialization, awaitItem())
            assertEquals(RegistrationStatus.PassAndConfDoNotMatch, awaitItem())
        }
    }

    @Test
    fun `register failure produces RegistrationStatus#Error`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val registerUseCase = mockk<RegisterUseCase>{
            coEvery { execute(any(), any()) } returns Resource.Error(ERROR)
        }
        val addUserToDBUseCase = mockk<AddUserToDBUseCase> {
            coEvery { execute(any()) } returns Resource.Success(SUCCESS)
        }

        val registrationViewModel = RegistrationViewModel(
            registerUseCase = registerUseCase,
            addUserToDBUseCase = addUserToDBUseCase,
            dispatcher = dispatcher
        )

        registrationViewModel.register(
            EMAIL,
            USERNAME,
            PHONE_NUMBER,
            PASSWORD,
            PASSWORD
        )

        registrationViewModel.userSignUpStatus.test {
            assertEquals(RegistrationStatus.Initialization, awaitItem())
            assertEquals(RegistrationStatus.Loading, awaitItem())
            assertEquals(RegistrationStatus.Error, awaitItem())
        }
    }

    @Test
    fun `addUserToBD failure produces RegistrationStatus#Error`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val registerUseCase = mockk<RegisterUseCase>{
            coEvery { execute(any(), any()) } returns Resource.Success(SUCCESS)
        }
        val addUserToDBUseCase = mockk<AddUserToDBUseCase> {
            coEvery { execute(any()) } returns Resource.Error(ERROR)
        }

        val registrationViewModel = RegistrationViewModel(
            registerUseCase = registerUseCase,
            addUserToDBUseCase = addUserToDBUseCase,
            dispatcher = dispatcher
        )

        registrationViewModel.register(
            EMAIL,
            USERNAME,
            PHONE_NUMBER,
            PASSWORD,
            PASSWORD
        )

        registrationViewModel.userSignUpStatus.test {
            assertEquals(RegistrationStatus.Initialization, awaitItem())
            assertEquals(RegistrationStatus.Loading, awaitItem())
            assertEquals(RegistrationStatus.Error, awaitItem())
        }
    }

    @Test
    fun `addUserToBD success produces RegistrationStatus#Error`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val registerUseCase = mockk<RegisterUseCase>{
            coEvery { execute(any(), any()) } returns Resource.Success(SUCCESS)
        }
        val addUserToDBUseCase = mockk<AddUserToDBUseCase> {
            coEvery { execute(any()) } returns Resource.Success(SUCCESS)
        }

        val registrationViewModel = RegistrationViewModel(
            registerUseCase = registerUseCase,
            addUserToDBUseCase = addUserToDBUseCase,
            dispatcher = dispatcher
        )

        registrationViewModel.register(
            EMAIL,
            USERNAME,
            PHONE_NUMBER,
            PASSWORD,
            PASSWORD
        )

        registrationViewModel.userSignUpStatus.test {
            assertEquals(RegistrationStatus.Initialization, awaitItem())
            assertEquals(RegistrationStatus.Loading, awaitItem())
            assertEquals(RegistrationStatus.Success, awaitItem())
        }
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val ERROR = "error"
        private const val SUCCESS = "success"
        private const val EMAIL = "email"
        private const val USERNAME = "username"
        private const val PHONE_NUMBER = "89124445566"
        private const val PASSWORD = "password"
        private const val WRONG_CONF_PASS = "pass"
    }
}
