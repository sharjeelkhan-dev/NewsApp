package com.sharjeel.newsapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.sharjeel.newsapp.ui.theme.NewsAppTheme
import com.sharjeel.newsapp.ui.theme.White

import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.painter.Painter
import com.sharjeel.newsapp.ui.theme.BluePrimary

@Composable
fun AkhbarButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        )
    }
}

@Composable
fun SocialButton(
    icon: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(54.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = null
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            )
        }
    }
}

@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
                    )
                )
            )
            .padding(1.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun ShimmerPlaceholder(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
    )
}

@Composable
fun ToolbarIcon(
    painter: Painter,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Icon(
        painter = painter,
        contentDescription = null,
        tint = if (isSelected) BluePrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .size(20.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun AkhbarButtonPreview() {
    NewsAppTheme {
        AkhbarButton(
            text = "Button",
            onClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AkhbarButtonDarkPreview() {
    NewsAppTheme {
        AkhbarButton(
            text = "Button",
            onClick = {}
        )
    }
}
