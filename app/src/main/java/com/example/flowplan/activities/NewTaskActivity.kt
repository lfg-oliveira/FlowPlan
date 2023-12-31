package com.example.flowplan.activities

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.FlowPlanTheme
import com.example.flowplan.api.ApiAdapter
import com.example.flowplan.api.HttpClient
import com.example.flowplan.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class AddTasks(val uid: Int, val client: ApiAdapter) {
    suspend fun createTask(title: String, description: String) {
        val task = Task(null, title, description)
        val json = Json.encodeToString(task)
        return withContext(Dispatchers.IO) {
            client.post("/task", json)
        }
    }
    @Composable
    fun AddTaskScreen() {
        var title by remember {
            mutableStateOf("")
        };
        var description by remember {
            mutableStateOf("")
        }
        val scope = rememberCoroutineScope()
        val clickHandle = {
            scope.launch {
                createTask(title, description)
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Nova Task",
                fontWeight = FontWeight.Bold,
                fontSize = 50.sp
            )
            OutlinedTextField(
                value = title,
                modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 10.dp),
                onValueChange = { title = it },
                label = { Text(text = "Título", fontSize = 13.sp) })


            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                },
                label = {
                    Text(text = "Descrição", fontSize = 13.sp)
                },
                modifier = Modifier
                    .height(120.dp)
                    .padding(start = 15.dp, end = 15.dp, top = 10.dp)
            )
            Row {
                Button(onClick = { clickHandle() }, modifier = Modifier.padding(vertical = 10.dp))
                {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = "Adicionando task")
                }
            }
        }
    }
}
@Preview
@Composable
fun PreviewAddTask() {
    FlowPlanTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
           AddTasks(0, HttpClient(appCache = File(""))).AddTaskScreen()
        }
    }
}
