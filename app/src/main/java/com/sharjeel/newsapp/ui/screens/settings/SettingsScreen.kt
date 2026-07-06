package com.sharjeel.newsapp.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.theme.BluePrimary
import com.sharjeel.newsapp.ui.theme.NewsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    var isDarkMode by remember { mutableStateOf(false) }

    AppScaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingsItem(
                iconPainter = painterResource(id = R.drawable.bell_line_icon),
                title = "Notification",
                onClick = { /* TODO: Notification Settings */ }
            )
            SettingsItem(
                iconPainter = painterResource(id = R.drawable.lock_line_icon),
                title = "Security",
                onClick = { /* TODO: Security Settings */ }
            )

            SettingsItem(
                iconPainter = painterResource(id = R.drawable.information_mark_circle_outline_icon),
                title = "Help",
                onClick = { /* TODO: Help Settings */ }
            )

            SettingsItem(
                iconPainter = painterResource(id = R.drawable.moon_line_icon),
                title = "Dark Mode",
                onClick = { isDarkMode = !isDarkMode },
                trailing = {
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { isDarkMode = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = BluePrimary,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                            uncheckedBorderColor = Color.Transparent
                        ),
                        thumbContent = {
                            if (isDarkMode) {
                                Icon(
                                    imageVector = Icons.Filled.DarkMode,
                                    contentDescription = "Dark Mode",
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                    tint = BluePrimary
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.LightMode,
                                    contentDescription = "Light Mode",
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                    tint = Color.Gray
                                )
                            }
                        }
                    )
                }
            )

            SettingsItem(
                iconPainter = painterResource(id = R.drawable.logout_icon),
                title = "Logout",
                onClick = { /* TODO: Logout Logic */ }
            )
        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    onClick: () -> Unit,
    iconVector: ImageVector? = null,
    iconPainter: androidx.compose.ui.graphics.painter.Painter? = null,
    trailing: @Composable (() -> Unit)? = null
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                if (iconVector != null) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                } else if (iconPainter != null) {
                    Icon(
                        painter = iconPainter,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Normal
                )
            )
        }
        
        if (trailing != null) {
            trailing()
        } else {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    NewsAppTheme {
        SettingsScreen(onBackClick = {})
    }
}
