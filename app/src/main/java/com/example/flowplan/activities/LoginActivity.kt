package com.example.flowplan.activities

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.compose.FlowPlanTheme
import com.example.flowplan.api.ApiAdapter
import com.example.flowplan.api.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class LoginData(val email: String, val password: String)
class LoginActivity(private val httpClient: ApiAdapter) {
    private suspend fun makeLoginRequest(email: String, password: String): Result<String?> {
        val data = LoginData(email, password)
        val str = Json.encodeToString(value = data)

        return withContext(Dispatchers.IO) {
            httpClient.post("user/login", str)
        }
    }

    @Composable
    fun Login(navController: NavHostController, cacheFile: File?) {
        if (cacheFile?.readBytes()?.toString()?.isEmpty() == true) {
            navController.navigate("dashboard")
        }
        var login by remember {
            mutableStateOf("")
        }
        var pw by remember {
            mutableStateOf("")
        }

        var failed by remember {
            mutableStateOf(false)
        }

        val coroutineScope = rememberCoroutineScope()

        var token by remember {
            mutableStateOf("")
        }

        val clickHandle: () -> Unit = {
            coroutineScope.launch {
                var response: Result<String?> = Result.success(null)
                try {
                    response = makeLoginRequest(login, pw)
                } catch (e: Throwable) {
                    failed = true
                    return@launch
                }
                response.onFailure {
                    login = ""
                    pw = ""
                    failed = true
                    Log.d("INFO", it.toString())
                    return@launch
                }
                response.onSuccess { res ->
                    res?.toByteArray()?.let {
                        cacheFile?.writeBytes(it)
                        token = it.toString()
                    }
                    navController.navigate("dashboard")
                }
            }
        }
        val toast: @Composable () -> Unit = {
            Toast.makeText(
                LocalContext.current, "Couldn't Login", Toast.LENGTH_SHORT
            )
            failed = false
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                10.dp, alignment = Alignment.CenterVertically
            ),

            modifier = Modifier.padding(15.dp, 0.dp)
        ) {


            Row {
                Text(
                    text = "FlowPlan",
                    fontSize = 65.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.absoluteOffset(0.dp, (-100).dp)
                )
            }
            Row {
                OutlinedTextField(value = login,
                    onValueChange = { login = it },
                    placeholder = { Text(text = "Email") })
            }
            Row {
                OutlinedTextField(value = pw, onValueChange = { pw = it }, placeholder = {
                    Text(text = "Password")
                }, visualTransformation = PasswordVisualTransformation())
            }
            Row {
                TextButton(onClick = { navController.navigate("register") }) {
                    Text(text = "Register")
                }
                Button(onClick = { clickHandle() }) {
                    Text(text = "Login")
                }
            }
            if(failed) {
                toast()
            }
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    FlowPlanTheme {
        Surface(modifier = Modifier.fillMaxHeight()) {
            LoginActivity(HttpClient(appCache = File(""))).Login(
                NavHostController(LocalContext.current), null
            )
        }
    }
}