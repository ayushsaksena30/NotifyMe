package com.example.notifyme

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notifyme.ui.theme.NotifyMeTheme
import com.example.notifyme.ui.theme.clashDisplay
import com.example.notifyme.ui.theme.latoFontFamily

class EnablePermissionsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotifyMeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    EnableNotificationAccess()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (isNotificationAccessEnabled(this)) {
            Toast.makeText(this, "Notification access granted!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, DashboardActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}

@Composable
fun EnableNotificationAccess() {
    val context= LocalContext.current
    var isPermissionGranted by remember {
        mutableStateOf(isNotificationAccessEnabled(context))
    }

    LaunchedEffect(Unit) {
        isPermissionGranted = isNotificationAccessEnabled(context)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Dashboard",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 40.sp,
                    fontFamily = clashDisplay,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                "Please Enable Notification \nAccess",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 32.sp,
                    fontFamily = latoFontFamily,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        Button(
            onClick = {
                if (!isNotificationAccessEnabled(context)) {
                    context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
            border = BorderStroke(2.dp, Color.Black),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Text(
                text = "Enable Notification Access",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 20.sp,
                    fontFamily = latoFontFamily,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PermissionsPreview() {
    NotifyMeTheme {
        EnableNotificationAccess()
    }
}

fun isNotificationAccessEnabled(context: Context): Boolean {
    val pkgName = context.packageName
    val flat = Settings.Secure.getString(
        context.contentResolver,
        "enabled_notification_listeners"
    )
    return flat?.contains(pkgName) == true
}