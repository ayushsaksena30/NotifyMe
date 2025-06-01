package com.example.notifyme

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notifyme.ui.theme.NotifyMeTheme
import com.example.notifyme.ui.theme.clashDisplay
import com.example.notifyme.ui.theme.latoFontFamily


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotifyMeTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                         color = MaterialTheme.colorScheme.background
                    ) {
                    LandingScreen(onGetStartedClick={
                        startActivity(Intent(this, EnablePermissionsActivity::class.java))
                    })
                }
            }
        }
    }
}

@Composable
fun LandingScreen(onGetStartedClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .padding(top=220.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "NotifyMe",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 68.sp,
                    fontFamily = clashDisplay,
                    fontWeight = FontWeight.Medium)
            )
        }

        Button(onClick = onGetStartedClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 148.dp)
                .width(205.dp)
                .height(63.dp)
        ) {
            Text(text = "Get Started",
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp,
                    fontFamily = latoFontFamily,
                    fontWeight = FontWeight.Normal))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LandingPreview() {
    NotifyMeTheme {
        LandingScreen(onGetStartedClick = {})
    }
}