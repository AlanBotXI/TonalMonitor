package com.example.tonalmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.tonalmonitor.domain.model.AudioAnalyzer
import com.example.tonalmonitor.domain.viewModel.VMAudioMonitor
import com.example.tonalmonitor.ui.TonalMonitorScreen
import com.example.tonalmonitor.ui.theme.TonalMonitorTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: VMAudioMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[VMAudioMonitor::class.java]
        val analyzer = AudioAnalyzer()

        setContent {
            TonalMonitorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TonalMonitorScreen(viewModel = viewModel, analyzer)
                }

            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TonalMonitorTheme {
        Greeting("Android")
    }
}

