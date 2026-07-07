package com.sharjeel.newsapp.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AkhbarButton
import com.sharjeel.newsapp.ui.components.AkhbarTextField
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.components.SocialButton
import com.sharjeel.newsapp.ui.theme.NewsAppTheme
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    NewsAppTheme {
        LoginScreen(
            onLoginClick = { _, _, _ -> },
            onSignupClick = {},
            onForgotPasswordClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenDarkPreview() {
    NewsAppTheme {
        LoginScreen(
            onLoginClick = { _, _, _ -> },
            onSignupClick = {},
            onForgotPasswordClick = {}
        )
    }
}

@Composable
fun LoginScreen(
    onLoginClick: (String, String, Boolean) -> Unit,
    onSignupClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onGoogleSignInClick: (String) -> Unit = {},
    onFacebookSignInClick: (String) -> Unit = {},
    isLoading: Boolean = false,
    initialEmail: String = "",
    initialRememberMe: Boolean = false
) {
    var email by remember { mutableStateOf(initialEmail) }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(initialRememberMe) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    // Pre-create CredentialManager
    val credentialManager = remember { CredentialManager.create(context) }
    val callbackManager = remember { CallbackManager.Factory.create() }

    // Re-add Facebook callback cleanup (accidentally removed)
    DisposableEffect(Unit) {
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                onFacebookSignInClick(result.accessToken.token)
            }
            override fun onCancel() {}
            override fun onError(error: FacebookException) {}
        })
        onDispose {
            LoginManager.getInstance().unregisterCallback(callbackManager)
        }
    }

    // Prepare Google ID Option ahead of time
    val serverclientid = "653227771496-ina1rq6rlaq1nip8nm0951sonta2jc53.apps.googleusercontent.com"
    val googleIdOption = remember {
        GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(serverclientid)
            .setAutoSelectEnabled(true)
            .build()
    }

    val handleGoogleSignIn = {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )
                val credential = result.credential
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    onGoogleSignInClick(googleIdTokenCredential.idToken)
                }
            } catch (_: GetCredentialException) {
                // Log error
            }
        }
    }

    val handleFacebookSignIn = {
        LoginManager.getInstance().logInWithReadPermissions(
            context as androidx.activity.ComponentActivity,
            callbackManager,
            listOf("email", "public_profile")
        )
    }

    AppScaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Hello",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 48.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = "Again!",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 48.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Welcome back you've\nbeen missed",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 30.sp
                )
            )
            Spacer(modifier = Modifier.height(48.dp))
            AkhbarTextField(
                value = email,
                onValueChange = { 
                    email = it
                    if (emailError != null) emailError = null
                },
                label = "Email or Phone Number*",
                isError = emailError != null,
                errorMessage = emailError
            )
            Spacer(modifier = Modifier.height(16.dp))
            AkhbarTextField(
                value = password,
                onValueChange = { 
                    password = it
                    if (passwordError != null) passwordError = null
                },
                label = "Password*",
                isPassword = true,
                passwordVisible = passwordVisible,
                onPasswordToggle = { passwordVisible = !passwordVisible },
                isError = passwordError != null,
                errorMessage = passwordError
            )
            Row(
                modifier = Modifier.fillMaxWidth().height(35.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.offset(x = (-12).dp)
                    )
                    Text(
                        text = "Remember me",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        modifier = Modifier.offset(x = (-19).dp)
                    )
                }
                Text(
                    text = "Forgot the password?",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.clickable { onForgotPasswordClick() }
                )
            }
            Spacer(modifier = Modifier.height(13.dp))
            AkhbarButton(
                text = "Login",
                isLoading = isLoading,
                onClick = { 
                    emailError = if (email.isEmpty()) "Please enter your email first" else null
                    passwordError = if (password.isEmpty()) "Please enter your password" else null
                    
                    if (emailError == null && passwordError == null) {
                        onLoginClick(email, password, rememberMe)
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "or continue with",
                modifier = Modifier.align(Alignment.CenterHorizontally).width(114.dp).height(21.dp),
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SocialButton(
                    icon = R.drawable.facebook,
                    text = "Facebook",
                    onClick = { handleFacebookSignIn() },
                    modifier = Modifier.weight(1f)
                )
                SocialButton(
                    icon = R.drawable.google,
                    text = "Google",
                    onClick = { handleGoogleSignIn() },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "don't have an account ? ",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Text(
                    text = "Sign Up",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.clickable { onSignupClick() }
                )
            }
        }
    }
}