package com.example.notifyme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notifyme.ui.theme.NotifyMeTheme
import com.example.notifyme.ui.theme.clashDisplay
import com.example.notifyme.ui.theme.latoFontFamily
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddAppActivity : ComponentActivity() {
    private val viewModel by viewModels<AddAppViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotifyMeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AddAppScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun AddAppScreen(viewModel: AddAppViewModel) {
    val context = LocalContext.current
    val apps by viewModel.apps.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadInstalledApps(context)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(WindowInsets.systemBars.asPaddingValues()),
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row() {
                Text(
                    text = "Select Apps",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 36.sp,
                        fontFamily = clashDisplay,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { (context as? ComponentActivity)?.finish()
                        CoroutineScope(Dispatchers.Default).launch {
                            val latestExcluded = NotificationPrefs.getExcludedPackages(context).first()
                            MyNotificationListener.excludedPackages = latestExcluded
                            withContext(Dispatchers.Main) {
                                MyNotificationListener.instance?.pushActiveNotificationsToViewModel()
                                DashboardViewModelSingleton.viewModel?.removeExcludedNotifications()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Text(
                        text = "Done",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 16.sp,
                            fontFamily = latoFontFamily,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp))
            {
                items(apps) { appInfo ->
                    AppRow(appInfo=appInfo, onCheckedChange = {
                        isChecked ->
                        viewModel.updateAppChecked(appInfo.packageName, isChecked, context)
                    })
                }
            }
        }
    }
}

@Composable
fun AppRow(appInfo: AppInfo, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = rememberDrawablePainter(drawable = appInfo.icon),
                contentDescription = appInfo.appName,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 12.dp),
                tint = Color.Unspecified
            )
            Column {
                Text(appInfo.appName, fontWeight = FontWeight.SemiBold)
                Text(appInfo.packageName, fontSize = 12.sp, color = Color.Gray)
            }
        }

        Checkbox(
            checked = appInfo.isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun GreetingPreview2() {
//    AddAppScreen(viewModel = viewModel())
//}