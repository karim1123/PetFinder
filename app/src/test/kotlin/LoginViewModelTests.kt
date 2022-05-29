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
            coEvery { currentUser?.uid } returns "test"
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

        loginViewModel.login("", "password")

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

        loginViewModel.login("email", "")

        loginViewModel.userSignInStatus.test {
            assertEquals(LoginStatus.IsNotAuthorized, awaitItem())
            assertEquals(LoginStatus.EmptyPassword, awaitItem())
        }
    }

    @Test
    fun `login failure produces LoginStatus#Error`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val loginUseCase = mockk<LoginUseCaseImpl> {
            coEvery { execute(any(), any()) } returns Resource.Error("error")
        }
        val auth = mockk<FirebaseAuth> {
            coEvery { currentUser?.uid } returns null
        }

        val loginViewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            dispatcher = dispatcher,
            firebaseAuth = auth
        )

        loginViewModel.login("email", "pass")

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
            coEvery { execute(any(), any()) } returns Resource.Success("test")
        }
        val auth = mockk<FirebaseAuth> {
            coEvery { currentUser?.uid } returns null
        }

        val loginViewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            dispatcher = dispatcher,
            firebaseAuth = auth
        )

        loginViewModel.login("email", "pass")

        loginViewModel.userSignInStatus.test {
            assertEquals(LoginStatus.IsNotAuthorized, awaitItem())
            assertEquals(LoginStatus.Loading, awaitItem())
            assertEquals(LoginStatus.Success, awaitItem())
        }
    }
}
