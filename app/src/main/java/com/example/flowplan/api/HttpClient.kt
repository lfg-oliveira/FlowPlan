package com.example.flowplan.api


import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class HttpClient(
    private val client: OkHttpClient = OkHttpClient(),
    private val baseUrl: String = "http://192.168.0.83:3000/api/v1",
    private val mediaType: MediaType = "application/json".toMediaType(),
    private val appCache: File
) : ApiAdapter {
    private fun getToken(): String {
        return try {
            appCache.readText()
        } catch (e: Throwable) {
            ""
        }
    }
    override fun get(url: String, id: Int?): Result<String?> {
        val reqUrl = if (id == null) "${baseUrl}${url}" else "${baseUrl}${url}/${id}"
        val req = Request.Builder()
            .addHeader("session_token", getToken())
            .url(reqUrl)
            .get()
            .build()

        val response = client.newCall(req).execute()
        return if (response.code in listOf(201, 202)) {
            Result.success(response.body?.string())
        } else {
            Result.failure(Error(response.body?.string()))
        }
    }

    override fun post(url: String, data: String): Result<String?> {
        val reqUrl = "$baseUrl/$url"
        val req = Request.Builder()
            .addHeader("session_token", getToken())
            .url(reqUrl)
            .post(data.toRequestBody(mediaType))
            .build()

        val response = client.newCall(req).execute()
        return if (response.code in listOf(201, 202)) {
            Result.success(response.body?.string())
        } else {
            Result.failure(Error(response.body?.string()))
        }
    }

    override fun delete(url: String, id: Int?): Result<String?> {
        val reqUrl = if (id == null) "${baseUrl}/${url}" else "${baseUrl}/${url}/${id}"
        val req = Request.Builder()
            .addHeader("session_token", getToken())
            .url(reqUrl)
            .delete()
            .build()

        val response = client.newCall(req).execute()
        return if (response.code in listOf(201, 202)) {
            Result.success(response.body?.string())
        } else {
            Result.failure(Error(response.body?.string()))
        }
    }

    override fun put(url: String, data: String): Result<String?> {
        val reqUrl = "$baseUrl/$url"
        val req = Request.Builder()
            .addHeader("session_token", getToken())
            .url(reqUrl)
            .put(data.toRequestBody(mediaType))
            .build()

        val response = client.newCall(req).execute()
        return if (response.code in listOf(201, 202)) {
            Result.success(response.body?.string())
        } else {
            Result.failure(Error(response.body?.string()))
        }
    }
}