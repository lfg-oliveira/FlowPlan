package com.example.flowplan

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

class FlowplanNavbar () {
    @Composable
    fun NavBar(navController: NavHostController, selected: Int) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            BottomAppBar {
                NavigationBarItem(selected = selected == 1, label = {
                    Text(text = "Dashboard")
                }, onClick = {
                    navController.popBackStack()
                    navController.navigate("dashboard")
                }, icon = {
                    Icon(
                        imageVector = Icons.Rounded.List,
                        contentDescription = "Go to Dashboard"
                    )
                })
                NavigationBarItem(selected = selected == 2,
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