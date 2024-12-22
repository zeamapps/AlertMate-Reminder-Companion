package com.zeamapps.snoozy.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.zeamapps.snoozy.presentation.viewmodel.ThemeMode
import com.zeamapps.snoozy.utill.SnoozyColors

@Composable
fun MyAppTheme(
    themeMode: ThemeMode ,
    content: @Composable () -> Unit
) {

   var darkTheme =  when(themeMode){
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM_DEFAULT -> isSystemInDarkTheme()
    }
    val colors = if (darkTheme) {
            darkColorScheme(
            //    Color(0xFF121212)
                primary = MaterialTheme.colorScheme.primary,
                secondary = Color(0xFF03DAC5),
                background = SnoozyColors.DarkGray,
                surface = CharcoalGrey,
                onPrimary = Color.White,
                onSecondary = Color.Black,
                onBackground = Color.White,
                onSurface = Color.White,
                onTertiary = Color.LightGray,
                onTertiaryContainer =  Color(0xFF333333)
            )
        } else {
            lightColorScheme(
                primary = MaterialTheme.colorScheme.primary,
                secondary = Color(0xFF03DAC6),
                background = Color(0xFFFAFAFA),
                surface = Color(0xFFF1F1F1),
                onPrimary = Color.White,
                onSecondary = Color.White,
                onBackground = Color.Black,
                onSurface = Color.Black,
                onTertiary = Color.DarkGray,
                onTertiaryContainer = Color(0xFFFAFAFA)
            )
        }


    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
