package com.sharjeel.newsapp.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharjeel.newsapp.ui.components.AkhbarButton
import com.sharjeel.newsapp.ui.components.AkhbarTextField
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.theme.NewsAppTheme

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    onSubmitClick: (String, String) -> Unit, // value, method
    isLoading: Boolean = false
) {
    var step by remember { mutableIntStateOf(1) }
    var selectedMethod by remember { mutableStateOf("Email") }
    var inputValue by remember { mutableStateOf("") }
    var inputError by remember { mutableStateOf<String?>(null) }

    AppScaffold(
        topBar = {
            IconButton(
                onClick = {
                    if (step == 2) step = 1 else onBackClick()
                },
                modifier = Modifier
                    .padding(8.dp)
                    .offset(y = 24.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        bottomBar = {
            Box(modifier = Modifier.padding(24.dp)) {
                AkhbarButton(
                    text = if (step == 1) "Next" else "Submit",
                    isLoading = isLoading,
                    onClick = {
                        if (step == 1) {
                            step = 2
                        } else {
                            if (inputValue.isEmpty()) {
                                inputError = if (selectedMethod == "Email")
                                    "Please enter your email first"
                                else
                                    "Please enter your phone number first"
                            } else {
                                onSubmitClick(inputValue, selectedMethod)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Forgot\nPassword ?",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    lineHeight = 40.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (step == 1)
                    "Don’t worry! It happens. Please select the email or number associated with your account."
                else
                    "Don’t worry! It happens. Please enter the address associated with your account.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (step == 1) {
                ForgotPasswordMethodItem(
                    icon = Icons.Default.Email,
                    title = "via Email:",
                    value = "example@yourmail.com",
                    isSelected = selectedMethod == "Email",
                    onClick = { selectedMethod = "Email" }
                )

                Spacer(modifier = Modifier.height(16.dp))

                ForgotPasswordMethodItem(
                    icon = Icons.Default.Message,
                    title = "via SMS:",
                    value = "+67-1234-5678-9",
                    isSelected = selectedMethod == "SMS",
                    onClick = { selectedMethod = "SMS" }
                )
            } else {
                AkhbarTextField(
                    value = inputValue,
                    onValueChange = {
                        inputValue = it
                        if (inputError != null) inputError = null
                    },
                    label = if (selectedMethod == "Email") "Email Address*" else "Phone Number*",
                    placeholder = if (selectedMethod == "Email") "hello@gmail.com" else "+92 312 3456789",
                    isError = inputError != null,
                    errorMessage = inputError
                )
            }
        }
    }
}

@Composable
fun ForgotPasswordMethodItem(
    icon: ImageVector,
    title: String,
    value: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.padding(20.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.outline
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    NewsAppTheme {
        ForgotPasswordScreen(onBackClick = {}, onSubmitClick = { _, _ -> })
    }
}
