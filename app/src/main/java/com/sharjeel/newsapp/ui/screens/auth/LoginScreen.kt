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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.components.AkhbarButton
import com.sharjeel.newsapp.ui.components.AkhbarTextField
import com.sharjeel.newsapp.ui.components.SocialButton
import com.sharjeel.newsapp.ui.theme.NewsAppTheme

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    NewsAppTheme {
        LoginScreen(
            onLoginClick = { _, _ -> },
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
            onLoginClick = { _, _ -> },
            onSignupClick = {},
            onForgotPasswordClick = {}
        )
    }
}

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onSignupClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    AppScaffold { padding ->
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
                value = username,
                onValueChange = { username = it },
                label = "Username*"
            )
            Spacer(modifier = Modifier.height(16.dp))
            AkhbarTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password*",
                isPassword = true,
                passwordVisible = passwordVisible,
                onPasswordToggle = { passwordVisible = !passwordVisible }
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
                onClick = { onLoginClick(username, password) }
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
                    icon = R.drawable.icon__1_,
                    text = "Facebook",
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )
                SocialButton(
                    icon = R.drawable.icon,
                    text = "Google",
                    onClick = {},
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
