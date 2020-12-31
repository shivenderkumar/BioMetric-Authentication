package com.shivenderkumar.biometricsapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    var executor: Executor? = null
    var biometricManager: BiometricManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        executor = ContextCompat.getMainExecutor(this)
        biometricManager = BiometricManager.from(this)

        startAunthentication()
    }

    private fun startAunthentication() {

        when (this.biometricManager!!.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                authUser(this.executor)
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(
                    this,
                    "NO BIOMETRIC HARDWARE",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(
                    this,
                    "BIOMETRIC HARDWARE UNAVAILABLE",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(
                    this,
                    "BIOMETRIC NOT SETUP IN DEVICE",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }

    }

    private fun authUser(executor: Executor?) {

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authentication Required !!")
            .setSubtitle("Important for login")
            .setDescription("Place your finger on device senson to verify your identity!!")
            .setDeviceCredentialAllowed(true)
            .build()


        val biometricPrompt = BiometricPrompt(this, executor!!,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    tv.text = "Welcome"
                }

                override fun onAuthenticationError(
                    errorCode: Int, errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    finish()

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }

}