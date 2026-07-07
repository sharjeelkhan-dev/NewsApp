package com.sharjeel.newsapp.ui.screens.auth

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

sealed class ForgotPasswordStep {
    object MethodSelection : ForgotPasswordStep()
    data class OtpVerification(val phoneNumber: String) : ForgotPasswordStep()
    object ResetPassword : ForgotPasswordStep()
    object Congratulations : ForgotPasswordStep()
}

@Composable
fun ForgotPasswordNavigation(
    onBackToLogin: () -> Unit,
    onFinish: () -> Unit,
    onSendResetEmail: (String) -> Unit,
    onSendOtp: (String, Activity) -> Unit,
    onVerifyOtp: (String) -> Unit,
    isLoading: Boolean = false,
    navigateToCongratulations: Boolean = false,
    navigateToOtp: String? = null,
    navigateToResetPassword: Boolean = false
) {
    var currentStep by remember { mutableStateOf<ForgotPasswordStep>(ForgotPasswordStep.MethodSelection) }
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }

    LaunchedEffect(navigateToCongratulations) {
        if (navigateToCongratulations) {
            currentStep = ForgotPasswordStep.Congratulations
        }
    }

    LaunchedEffect(navigateToOtp) {
        navigateToOtp?.let {
            currentStep = ForgotPasswordStep.OtpVerification(it)
        }
    }
    
    LaunchedEffect(navigateToResetPassword) {
        if (navigateToResetPassword) {
            currentStep = ForgotPasswordStep.ResetPassword
        }
    }

    when (val step = currentStep) {
        is ForgotPasswordStep.MethodSelection -> {
            ForgotPasswordScreen(
                onBackClick = onBackToLogin,
                onSubmitClick = { value, method ->
                    if (method == "Email") {
                        onSendResetEmail(value)
                    } else {
                        activity?.let { onSendOtp(value, it) }
                    }
                },
                isLoading = isLoading
            )
        }
        is ForgotPasswordStep.OtpVerification -> {
            OtpVerificationScreen(
                onBackClick = { currentStep = ForgotPasswordStep.MethodSelection },
                onVerifyClick = { otp ->
                    onVerifyOtp(otp)
                },
                isLoading = isLoading,
                phoneNumber = step.phoneNumber
            )
        }
        is ForgotPasswordStep.ResetPassword -> {
            ResetPasswordScreen(
                onBackClick = { currentStep = ForgotPasswordStep.MethodSelection },
                onSubmitClick = { 
                    currentStep = ForgotPasswordStep.Congratulations 
                }
            )
        }
        is ForgotPasswordStep.Congratulations -> {
            CongratulationsScreen(
                onGoToHomeClick = onFinish
            )
        }
    }
}

private fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}
