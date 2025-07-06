package com.example.parkpal.presentation.utils

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import com.example.parkpal.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

/**
 * Represents a signed-in Google user with basic profile info.
 */
data class GoogleUser(
    val uid: String,
    val displayName: String?,
    val email: String?,
    val photoUrl: String?
)

/**
 * Wrapper class that handles Google Sign-In via the Android Credentials API
 * and Firebase Authentication.
 *
 * @param context The Android Context.
 */
class GoogleSignInClient(private val context: Context) {
    private val auth = FirebaseAuth.getInstance()
    private val credentialManager = CredentialManager.create(context)

    /**
     * Performs Google Sign-In flow.
     *
     * @return [Result] wrapping [GoogleUser] on success, or an Exception on failure.
     */
    suspend fun signIn(): Result<GoogleUser> {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(context.getString(R.string.web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(context, request)
            val credential = result.credential

            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val firebaseCredential =
                    GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                val firebaseUser = auth.signInWithCredential(firebaseCredential).await().user

                firebaseUser?.let {
                    Result.success(
                        GoogleUser(
                            uid = it.uid,
                            displayName = it.displayName,
                            email = it.email,
                            photoUrl = it.photoUrl?.toString()
                        )
                    )
                } ?: Result.failure(Exception("Firebase user is null"))
            } else {
                Result.failure(Exception("Invalid Google ID Credential"))
            }
        } catch (e: GetCredentialException) {
            Log.e("GoogleSignInClient", "Credential Error: ${e.localizedMessage}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("GoogleSignInClient", "Sign-in Error: ${e.localizedMessage}")
            Result.failure(e)
        }
    }

    /**
     * Signs out the currently authenticated user.
     *
     * This clears Firebase authentication and credential manager state.
     *
     * @return [Result] wrapping success or an Exception on failure.
     */
    suspend fun signOut(): Result<Unit> {
        return try {
            // Firebase sign out
            auth.signOut()

            // Clear credential state from Credential Manager
            val clearRequest = ClearCredentialStateRequest()
            credentialManager.clearCredentialState(clearRequest)

            Result.success(Unit)
        } catch (e: ClearCredentialException) {
            Log.e("GoogleSignInClient", "ClearCredentialException: ${e.localizedMessage}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("GoogleSignInClient", "Sign-out Error: ${e.localizedMessage}")
            Result.failure(e)
        }
    }
}