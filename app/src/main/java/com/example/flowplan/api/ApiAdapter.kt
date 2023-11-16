package com.example.flowplan.api

interface ApiAdapter {
    fun get(url: String, id: Int?): Result<String?>
    fun post(url: String, data: String): Result<String?>
    fun delete(url: String, id: Int?): Result<String?>
    fun put(url: String, data: String): Result<String?>
}