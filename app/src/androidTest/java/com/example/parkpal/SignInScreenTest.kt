package com.example.parkpal

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.parkpal.presentation.screens.main.SignInScreen
import com.example.parkpal.presentation.viewmodel.AuthState
import com.example.parkpal.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeViewModel: FakeAuthViewModel

    @Before
    fun setup() {
        fakeViewModel = FakeAuthViewModel()

        composeTestRule.setContent {
            SignInScreen(
                authViewModel = fakeViewModel,
                onSignIn = {},
                onSignUp = {}
            )
        }
    }

    @Test
    fun login_withValidEmailAndPassword_triggersLogin() {
        composeTestRule.onNodeWithTag("emailField")
            .performTextInput("test@example.com")

        composeTestRule.onNodeWithTag("passwordField")
            .performTextInput("123456")

        composeTestRule.onNodeWithTag("signInButton")
            .performClick()

        composeTestRule.waitForIdle()

        assert(fakeViewModel.lastEmail == "test@example.com")
        assert(fakeViewModel.lastPassword == "123456")
        assert(fakeViewModel.authState.value is AuthState.Authenticated)
    }

    @Test
    fun googleSignIn_buttonClick_triggersGoogleFlow() {
        composeTestRule.onNodeWithTag("googleSignInButton")
            .performClick()

        composeTestRule.waitForIdle()

        assert(fakeViewModel.googleSignInCalled)
        assert(fakeViewModel.authState.value is AuthState.Authenticated)
    }
}

class FakeAuthViewModel : AuthViewModel() {

    override val authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)

    var lastEmail: String? = null
    var lastPassword: String? = null
    var googleSignInCalled = false

    override fun signIn(email: String, password: String) {
        lastEmail = email
        lastPassword = password
        authState.value = if (email == "test@example.com" && password == "123456") {
            AuthState.Authenticated
        } else {
            AuthState.Error("Invalid credentials")
        }
    }

    override fun signInWithGoogle(context: android.content.Context) {
        googleSignInCalled = true
        authState.value = AuthState.Authenticated
    }
}
