package com.example.flowplan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.FlowPlanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FlowPlanTheme (darkTheme = true){
                val navController = rememberNavController()


                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "Dashboard") {
                        composable("dashboard") {
                            Text(text = "You're in Dashboard")
                        }
                        composable("new") {
                            Text("You are in New")
                        }
                        composable("profile") {
                            Text(text = "You are in Profile.")
                        }
                    }
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom
                    ){
                        BottomAppBar {
                            NavigationBarItem(
                                selected = true,
                                label = {
                                        Text(text = "Dashboard")
                                },
                                onClick = { navController.navigate("dashboard") },
                                icon = { Icon(imageVector = Icons.Rounded.List,
                                    contentDescription = "Go to Dashboard") })
                            NavigationBarItem(
                                selected = false,
                                onClick = { navController.navigate("new") },
                                label = {
                                    Text(text = "New")
                                },
                                icon = { Icon(imageVector = Icons.Rounded.Add,
                                    contentDescription = "Create new Task") })

                        }
                    }
                }
            }
        }
    }
}

