package com.example.flowplan.database

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

class DatabaseManager {
    val mapper = ObjectMapper().registerKotlinModule()
}