package com.rit.tunely

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.rit.tunely.navigation.TunelyNavGraph
import com.rit.tunely.ui.theme.TunelyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TunelyTheme {
                Surface { TunelyNavGraph() }
            }
        }
    }
}