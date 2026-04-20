package com.cop4331.cinematch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cop4331.cinematch.navigation.NavGraph
import com.cop4331.cinematch.ui.theme.CineMatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CineMatchTheme {
                NavGraph()
            }
        }
    }
}