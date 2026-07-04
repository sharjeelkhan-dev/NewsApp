package com.sharjeel.newsapp.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.components.AkhbarButton
import com.sharjeel.newsapp.ui.components.AkhbarTextField
import com.sharjeel.newsapp.ui.theme.NewsAppTheme

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    onSubmitClick: (String) -> Unit
) {
    var step by remember { mutableIntStateOf(1) }
    var selectedMethod by remember { mutableStateOf("Email") }
    var inputValue by remember { mutableStateOf("") }

    AppScaffold(
        topBar = {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(8.dp).offset(y = 24.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack, 
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        bottomBar = {
            AkhbarButton(
                text = "Submit",
                onClick = {
                    if (step == 1) step = 2 else onSubmitClick(inputValue)
                },
                modifier = Modifier.padding(24.dp)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
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
                    value = "+62-8421-4512-2531",
                    isSelected = selectedMethod == "SMS",
                    onClick = { selectedMethod = "SMS" }
                )
            } else {
                AkhbarTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = if (selectedMethod == "Email") "Email ID / Mobile number" else "Mobile Number*",
                    placeholder = ""
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
        ForgotPasswordScreen(onBackClick = {}, onSubmitClick = {})
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ForgotPasswordScreenDarkPreview() {
    NewsAppTheme {
        ForgotPasswordScreen(onBackClick = {}, onSubmitClick = {})
    }
}
