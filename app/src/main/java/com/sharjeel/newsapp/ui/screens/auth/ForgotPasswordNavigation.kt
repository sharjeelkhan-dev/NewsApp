package com.sharjeel.newsapp.ui.screens.auth

import androidx.compose.runtime.*

sealed class ForgotPasswordStep {
    object MethodSelection : ForgotPasswordStep()
    object OtpVerification : ForgotPasswordStep()
    object ResetPassword : ForgotPasswordStep()
    object Congratulations : ForgotPasswordStep()
}

@Composable
fun ForgotPasswordNavigation(
    onBackToLogin: () -> Unit,
    onFinish: () -> Unit
) {
    var currentStep by remember { mutableStateOf<ForgotPasswordStep>(ForgotPasswordStep.MethodSelection) }

    when (currentStep) {
        is ForgotPasswordStep.MethodSelection -> {
            ForgotPasswordScreen(
                onBackClick = onBackToLogin,
                onSubmitClick = { 
                    // In a real app, you'd trigger the OTP send here
                    currentStep = ForgotPasswordStep.OtpVerification 
                }
            )
        }
        is ForgotPasswordStep.OtpVerification -> {
            OtpVerificationScreen(
                onBackClick = { currentStep = ForgotPasswordStep.MethodSelection },
                onVerifyClick = { 
                    // In a real app, you'd verify the OTP here
                    currentStep = ForgotPasswordStep.ResetPassword 
                }
            )
        }
        is ForgotPasswordStep.ResetPassword -> {
            ResetPasswordScreen(
                onBackClick = { currentStep = ForgotPasswordStep.OtpVerification },
                onSubmitClick = { 
                    // In a real app, you'd update the password here
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
