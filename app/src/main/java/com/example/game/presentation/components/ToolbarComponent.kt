package com.example.game.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarComponent(
    title: String = "",
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    icon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    iconColor: Color = MaterialTheme.colorScheme.onSurface,
    showBackButton: Boolean = false,
    onIconClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = titleColor,
                modifier = Modifier.fillMaxWidth()
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = titleColor
        ),
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onIconClick) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ToolbarComponentPreview() {
    ToolbarComponent(title = "Level 1", showBackButton = true)
}