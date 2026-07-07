package com.sharjeel.newsapp.ui.screens.onboarding

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AkhbarButton
import com.sharjeel.newsapp.ui.components.AkhbarTextField
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.theme.BluePrimary
import com.sharjeel.newsapp.ui.theme.NewsAppTheme

@Preview(showBackground = true)
@Composable
fun FillProfileScreenPreview() {
    NewsAppTheme {
        FillProfileScreen(
            onBackClick = {},
            onNextClick = { _, _, _, _, _, _ -> },
            onImagePick = {},
            initialEmail = "hello@gmail.com",
            initialPhone = "+92 123 456789"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillProfileScreen(
    onBackClick: () -> Unit,
    onNextClick: (String, String, String, String, String, String) -> Unit,
    onImagePick: (android.net.Uri) -> Unit,
    initialEmail: String = "",
    initialPhone: String = "",
    profileImageUrl: String = "",
    isLoading: Boolean = false
) {
    var username by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(initialEmail) }
    var phoneNumber by remember { mutableStateOf(initialPhone) }
    var bio by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }
    
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        uri?.let { onImagePick(it) }
    }

    AppScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Fill your Profile",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            AkhbarButton(
                text = "Next",
                onClick = { 
                    emailError = if (email.isEmpty()) "Email Address is required" else null
                    phoneError = if (phoneNumber.isEmpty()) "Phone Number is required" else null
                    
                    if (emailError == null && phoneError == null) {
                        onNextClick(username, fullName, email, phoneNumber, bio, website) 
                    }
                },
                isLoading = isLoading,
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Profile Image Section
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEEF1F4))
                        .clickable { galleryLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (profileImageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = profileImageUrl,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(BluePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            val letter = (fullName.takeIf { it.isNotEmpty() } ?: username.takeIf { it.isNotEmpty() } ?: "U")
                                .take(1).uppercase()
                            Text(
                                text = letter,
                                style = MaterialTheme.typography.displayLarge.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 48.sp
                                )
                            )
                        }
                    }

                    // Agar upload ho raha ho toh yahan spinner dikhayein
                    if (isLoading) {
                        androidx.compose.material3.CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = BluePrimary
                        )
                    }
                }

                // Camera Icon Overlay
                Surface(
                    onClick = { galleryLauncher.launch("image/*") },
                    shape = CircleShape,
                    color = BluePrimary,
                    modifier = Modifier
                        .size(32.dp)
                        .offset(x = (-4).dp, y = (-4).dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Change Photo",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            AkhbarTextField(
                value = username,
                onValueChange = { username = it },
                label = "Username",
            )

            Spacer(modifier = Modifier.height(16.dp))

            AkhbarTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = "Full Name",
            )

            Spacer(modifier = Modifier.height(16.dp))

            AkhbarTextField(
                value = email,
                onValueChange = { 
                    email = it
                    if (emailError != null) emailError = null
                },
                label = "Email Address*",
                isError = emailError != null,
                errorMessage = emailError,
                enabled = initialEmail.isEmpty() // Disable if already provided during signup
            )

            Spacer(modifier = Modifier.height(16.dp))

            AkhbarTextField(
                value = phoneNumber,
                onValueChange = { 
                    phoneNumber = it
                    if (phoneError != null) phoneError = null
                },
                label = "Phone Number*",
                isError = phoneError != null,
                errorMessage = phoneError,
                enabled = initialPhone.isEmpty() // Disable if already provided during signup
            )

            Spacer(modifier = Modifier.height(16.dp))

            AkhbarTextField(
                value = bio,
                onValueChange = { bio = it },
                label = "Bio",
            )

            Spacer(modifier = Modifier.height(16.dp))

            AkhbarTextField(
                value = website,
                onValueChange = { website = it },
                label = "Website",
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
