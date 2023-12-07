package com.hoon.tourinkorea.ui.sign

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hoon.tourinkorea.BuildConfig
import com.hoon.tourinkorea.MainActivity
import com.hoon.tourinkorea.R
import com.hoon.tourinkorea.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        checkUserLoginState()
    }

    private fun checkUserLoginState() {
        val user = auth.currentUser
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            binding.btnSignIn.setOnClickListener {
                setSignInRequest()
            }
        }
    }

    private fun setSignInRequest() {
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            ).build()
        startGoogleSignIn()
    }

    private fun startGoogleSignIn() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    getResultLauncher.launch(
                        IntentSenderRequest
                            .Builder(result.pendingIntent.intentSender)
                            .build()
                    )
                } catch (e: IntentSender.SendIntentException) {
                    showSnackBar(getString(R.string.error_login))
                }
            }
            .addOnFailureListener(this) {
                startGoogleSignUp()
            }
    }

    private fun startGoogleSignUp() {
        val request = GetSignInIntentRequest.builder()
            .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
            .build()

        Identity.getSignInClient(this)
            .getSignInIntent(request)
            .addOnSuccessListener { result ->
                getResultLauncher.launch(
                    IntentSenderRequest
                        .Builder(result.intentSender)
                        .build()
                )
            }
            .addOnFailureListener { e ->
                showSnackBar(getString(R.string.error_login_failed))
            }
    }

    private val getResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val idToken = oneTapClient.getSignInCredentialFromIntent(result.data).googleIdToken
                if (idToken != null) {
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Log.d("SignInActivity", "signInWithCredential:success")
                            } else {
                                Log.w("SignInActivity", "signInWithCredential:failure", task.exception)
                            }
                        }
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    showSnackBar(getString(R.string.error_token))
                }
            } catch (e: ApiException) {
                when (e.statusCode) {
                    CommonStatusCodes.CANCELED -> {
                        showSnackBar(getString(R.string.error_cancel))
                    }

                    CommonStatusCodes.NETWORK_ERROR -> {
                        showSnackBar(getString(R.string.error_network))
                    }

                    else -> {
                        showSnackBar(getString(R.string.error_unknown))
                    }
                }
            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar
            .make(binding.root, message, Snackbar.LENGTH_LONG)
            .show()
    }
}