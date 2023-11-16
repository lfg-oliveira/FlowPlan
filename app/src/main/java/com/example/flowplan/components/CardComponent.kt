package com.example.flowplan.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.flowplan.model.Task

class CardComponent(private val task: Task) {
    @Composable
    fun TaskCard(navController: NavHostController) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp,
                pressedElevation = 1.dp
            ),
            modifier = Modifier
                .size(width = 300.dp, height = 150.dp)
        ) {
            TextButton(
                onClick = { navController.navigate("details/${task.id}") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            {
                Text(text = task.title)
            }
            SuggestionChip(
                onClick = {},
                label = { Text("Common activity") },
            )
        }
    }
}