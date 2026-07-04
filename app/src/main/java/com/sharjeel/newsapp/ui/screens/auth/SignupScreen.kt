package com.sharjeel.newsapp.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
fun SignupScreenPreview() {
    NewsAppTheme {
        SignupScreen(
            onSignupClick = { _, _ -> },
            onLoginClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SignupScreenDarkPreview() {
    NewsAppTheme {
        SignupScreen(
            onSignupClick = { _, _ -> },
            onLoginClick = {}
        )
    }
}

@Composable
fun SignupScreen(
    onSignupClick: (String, String) -> Unit,
    onLoginClick: () -> Unit
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
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "Hello!",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 48.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Signup to get Started",
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
                verticalAlignment = Alignment.CenterVertically
            ) {
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

            Spacer(modifier = Modifier.height(13.dp))

            AkhbarButton(
                text = "Signup",
                onClick = { onSignupClick(username, password) }
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
                    text = "Already have an account ? ",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Text(
                    text = "Login",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }
    }
}
