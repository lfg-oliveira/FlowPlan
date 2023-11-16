package com.example.flowplan.activities

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.flowplan.api.ApiAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.coroutines.coroutineContext

@Serializable
data class RegisterData(val email: String, val password: String)
class RegisterActivity(private val client: ApiAdapter) {
    @Composable
    fun Register(navController: NavHostController, cacheFile: File) {

        var login by remember {
            mutableStateOf("")
        }

        var pw by remember {
            mutableStateOf("")
        }

        val coroutineScope = rememberCoroutineScope()

        var token by remember {
            mutableStateOf("")
        }
        var failed by remember {
            mutableStateOf(false)
        }
        val clickHandle: () -> Unit = {
            coroutineScope.launch {
                var response: Result<String?> = Result.success(null)
                try {
                    response = makeRegisterRequest(login, pw)
                }
                catch (e: Throwable) {
                    failed = true
                }
                response.onFailure {
                    login = ""
                    pw = ""
                    failed = true
                    return@onFailure
                }
                response.onSuccess { res ->
                    res?.toByteArray()?.let {
                        cacheFile.writeBytes(it)
                        token = it.toString()
                    }
                    navController.navigate("dashboard")
                }
            }
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
//                TextButton(onClick = { navController.navigate("register") }) {
//                    Text(text = "Register")
//                }
                Button(onClick = { clickHandle() }) {
                    Text(text = "Register")
                }
            }
            if (failed) {
                Toast.makeText(
                    LocalContext.current, "Failed registering." +
                            "Is your internet active?", Toast.LENGTH_SHORT
                ).show()
                failed = false
            }
        }
    }

    private suspend fun makeRegisterRequest(login: String, pw: String): Result<String?> {
        val data = RegisterData(login, pw)
        val str = Json.encodeToString(value = data)
        Log.d("INFO", str)

        return withContext(Dispatchers.IO) {
            client.post("user/register", str)
        }
    }
}

