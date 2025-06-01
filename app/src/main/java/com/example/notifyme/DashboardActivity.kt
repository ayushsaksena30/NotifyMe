package com.example.notifyme

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notifyme.ui.theme.NotifyMeTheme
import com.example.notifyme.ui.theme.clashDisplay
import com.example.notifyme.ui.theme.latoFontFamily
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import okhttp3.internal.http2.Header

class DashboardActivity : ComponentActivity() {
    private val viewModel = DashboardViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        DashboardViewModelSingleton.viewModel = viewModel

        setContent {
            NotifyMeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MyNotificationListener.instance?.pushActiveNotificationsToViewModel()
                    DashboardScreen(viewModel)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DashboardViewModelSingleton.viewModel = null
    }

    override fun onResume() {
        super.onResume()
        MyNotificationListener.instance?.pushActiveNotificationsToViewModel()
        viewModel.simulateConnection(true)
    }
}

@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val isConnected by viewModel.isConnected.collectAsState() //TODO: Handle this
    val logs by viewModel.notificationLog.collectAsState()
    val context = LocalContext.current
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(WindowInsets.systemBars.asPaddingValues())
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row {
                Text(
                    "Dashboard",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 36.sp,
                        fontFamily = clashDisplay,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {context.startActivity(Intent(context, SettingsActivity::class.java))},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(2.dp, Color.Black)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Add",
                        Modifier.size(26.dp)
                    )
                }
            }

            Row {
                Text(
                    text = "Connection:",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 26.sp,
                        fontFamily = latoFontFamily,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { /*TODO: Implement connection logic*/},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        Modifier.size(26.dp)
                    )
                }
            }

            Row {
                Text(
                    "Recent Notifications:",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 26.sp,
                        fontFamily = latoFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = Modifier.weight(1f))

                if (logs.isNotEmpty()) {
                    Button(
                        onClick = { viewModel.clearAllNotifications() },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)
                    ) {
                        Text("Clear All")
                    }
                }
            }

            LazyColumn(
            ){
                item { Header }
                items(logs.reversed()) { notification ->
                    NotificationCard(notification,onDismiss = {
                        viewModel.removeNotification(notification)
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun NotificationCard(data: NotificationData, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val appIcon = try {
        context.packageManager.getApplicationIcon(data.packageName)
    } catch (e: Exception) {
        null
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (appIcon != null) {
                        Icon(
                            painter = rememberDrawablePainter(drawable = appIcon),
                            contentDescription = "App Icon",
                            modifier = Modifier
                                .size(38.dp)
                                .padding(end = 8.dp),
                            tint = Color.Unspecified
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Default Icon",
                            modifier = Modifier.padding(end = 8.dp),
                            tint = Color.Gray
                        )
                    }

                    Text(
                        text = data.appName,
                        fontSize = 18.sp,
                        fontFamily = latoFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    data.title,
                    fontFamily = latoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Message: ${data.message}",
                    fontFamily = latoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formatTimestamp(data.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }

            IconButton(
                onClick = { onDismiss() },
                modifier = Modifier
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Dismiss"
                )
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val formatter = java.text.SimpleDateFormat("hh:mm a • dd MMM yyyy", java.util.Locale.getDefault())
    return formatter.format(java.util.Date(timestamp))
}

@Composable
@Preview(apiLevel = 33,showBackground = true, showSystemUi = true)
fun DashboardPreview() {
        DashboardScreen(viewModel = DashboardViewModel())
}