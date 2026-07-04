package com.sharjeel.newsapp.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.components.AkhbarButton
import com.sharjeel.newsapp.ui.components.AkhbarLogo
import com.sharjeel.newsapp.ui.theme.NewsAppTheme

@Composable
fun CongratulationsScreen(
    onGoToHomeClick: () -> Unit
) {
    AppScaffold(
        bottomBar = {
            AkhbarButton(
                text = "Go to homepage",
                onClick = onGoToHomeClick,
                modifier = Modifier.padding(24.dp)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AkhbarLogo()

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Congratulations!",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Your account is ready to use",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 18.sp
                ),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CongratulationsScreenPreview() {
    NewsAppTheme {
        CongratulationsScreen(onGoToHomeClick = {})
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CongratulationsScreenDarkPreview() {
    NewsAppTheme {
        CongratulationsScreen(onGoToHomeClick = {})
    }
}
