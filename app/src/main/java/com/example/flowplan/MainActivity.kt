package com.example.flowplan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.FlowPlanTheme
import com.example.flowplan.activities.AddTasks
import com.example.flowplan.activities.LoginActivity
import com.example.flowplan.activities.RegisterActivity
import com.example.flowplan.api.HttpClient
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json.Default.decodeFromString
import java.lang.Exception

@Serializable
data class JWTBody(val exp: Long, val uid: Int, val iat: Int)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val cacheFile = cacheDir
            var loggedIn by remember {
                mutableStateOf(false)
            }
            var uid = 0
            try {
                val jwt = cacheFile.readBytes().toString()

                if (jwt.isNotEmpty()) {
                    val body = decodeFromString<JWTBody>(jwt.split(".")[1])
                    loggedIn = System.currentTimeMillis() / 1000 > body.exp
                    uid = body.uid
                }
            } catch (e: Exception) {
                loggedIn = false
            } finally {
                var initial = if (loggedIn) "dashboard" else "login"

                FlowPlanTheme {
                    val navController = rememberNavController()
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(navController = navController, startDestination = initial) {
                            composable("dashboard") {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxWidth(.7f)
                                        .verticalScroll(
                                            state = ScrollState(0),
                                            enabled = true
                                        )
                                ) {
                                    Text(
                                        text = "Dashboard Title",
                                        textAlign = TextAlign.Center,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 44.sp,
                                    )

                                    ElevatedCard(
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = 10.dp,
                                            pressedElevation = 1.dp
                                        ),
                                        modifier = Modifier
                                            .size(width = 300.dp, height = 150.dp)
                                    ) {
                                        TextButton(
                                            onClick = { navController.navigate("details/1") },
                                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                        )
                                        {
                                            Text(text = "Hello Card")
                                        }

                                        SuggestionChip(
                                            onClick = {},
                                            label = { Text("Common activity") },
                                        )
                                    }
                                }
                            }
                            composable("new") {
                                AddTasks(uid, HttpClient(appCache = cacheFile)).AddTaskScreen()
                            }
                            composable("login") {
                                LoginActivity(HttpClient(appCache = cacheFile)).Login(
                                    navController,
                                    cacheFile = cacheFile
                                )
                            }
                            composable("register") {
                                RegisterActivity(HttpClient(appCache = cacheFile))
                                    .Register(navController, cacheFile)
                            }
                        }
                        if (loggedIn) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Bottom

                            ) {
                                BottomAppBar {
                                    NavigationBarItem(
                                        selected = true,
                                        label = {
                                            Text(text = "Dashboard")
                                        },
                                        onClick = {
                                            navController.popBackStack()
                                            navController.navigate("dashboard")
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Rounded.List,
                                                contentDescription = "Go to Dashboard"
                                            )
                                        })
                                    NavigationBarItem(
                                        selected = false,
                                        onClick = { navController.navigate("new") },
                                        label = {
                                            Text(text = "New")
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Rounded.Add,
                                                contentDescription = "Create new Task"
                                            )
                                        })

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
