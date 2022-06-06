import app.cash.turbine.test
import com.example.android.domain.common.Resource
import com.example.android.domain.usecases.login.LoginUseCaseImpl
import com.example.android.pets_finder.login.LoginStatus
import com.example.android.pets_finder.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class LoginViewModelTests {
    @Test
    fun `empty userLoggedIn produces LoginStatus#IsNotAuthorized`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val loginUseCase = mockk<LoginUseCaseImpl>()
        val auth = mockk<FirebaseAuth> {
            coEvery { currentUser?.uid } returns null
        }

        val loginViewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            dispatcher = dispatcher,
            firebaseAuth = auth
        )

        assertEquals(LoginStatus.IsNotAuthorized, loginViewModel.userSignInStatus.value)
    }

    @Test
    fun `not empty userLoggedIn produces LoginStatus#Success`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val loginUseCase = mockk<LoginUseCaseImpl>()
        val auth = mockk<FirebaseAuth> {
            coEvery { currentUser?.uid } returns ID
        }

        val loginViewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            dispatcher = dispatcher,
            firebaseAuth = auth
        )

        assertEquals(LoginStatus.Success, loginViewModel.userSignInStatus.value)

    }

    @Test
    fun `empty email produces LoginStatus#EmptyEmail`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val loginUseCase = mockk<LoginUseCaseImpl>()
        val auth = mockk<FirebaseAuth> {
            coEvery { currentUser?.uid } returns null
        }

        val loginViewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            dispatcher = dispatcher,
            firebaseAuth = auth
        )

        loginViewModel.login(EMPTY_STRING, PASSWORD)

        loginViewModel.userSignInStatus.test {
            assertEquals(LoginStatus.IsNotAuthorized, awaitItem())
            assertEquals(LoginStatus.EmptyEmail, awaitItem())
        }
    }

    @Test
    fun `empty password produces LoginStatus#EmptyEmail`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val loginUseCase = mockk<LoginUseCaseImpl>()
        val auth = mockk<FirebaseAuth> {
            coEvery { currentUser?.uid } returns null
        }

        val loginViewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            dispatcher = dispatcher,
            firebaseAuth = auth
        )

        loginViewModel.login(EMAIL, EMPTY_STRING)

        loginViewModel.userSignInStatus.test {
            assertEquals(LoginStatus.IsNotAuthorized, awaitItem())
            assertEquals(LoginStatus.EmptyPassword, awaitItem())
        }
    }

    @Test
    fun `login failure produces LoginStatus#Error`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val loginUseCase = mockk<LoginUseCaseImpl> {
            coEvery { execute(any(), any()) } returns Resource.Error(ERROR)
        }
        val auth = mockk<FirebaseAuth> {
            coEvery { currentUser?.uid } returns null
        }

        val loginViewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            dispatcher = dispatcher,
            firebaseAuth = auth
        )

        loginViewModel.login(EMAIL, PASSWORD)

        loginViewModel.userSignInStatus.test {
            assertEquals(LoginStatus.IsNotAuthorized, awaitItem())
            assertEquals(LoginStatus.Loading, awaitItem())
            assertEquals(LoginStatus.Error, awaitItem())
        }
    }

    @Test
    fun `login success produces LoginStatus#Success`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val loginUseCase = mockk<LoginUseCaseImpl> {
            coEvery { execute(any(), any()) } returns Resource.Success(ID)
        }
        val auth = mockk<FirebaseAuth> {
            coEvery { currentUser?.uid } returns null
        }

        val loginViewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            dispatcher = dispatcher,
            firebaseAuth = auth
        )

        loginViewModel.login(EMAIL, PASSWORD)

        loginViewModel.userSignInStatus.test {
            assertEquals(LoginStatus.IsNotAuthorized, awaitItem())
            assertEquals(LoginStatus.Loading, awaitItem())
            assertEquals(LoginStatus.Success, awaitItem())
        }
    }

    companion object{
        private const val EMPTY_STRING = ""
        private const val ID = "test id"
        private const val ERROR = "error"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }
}
