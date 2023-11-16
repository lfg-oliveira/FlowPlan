package com.example.flowplan.model

class Workspace(id: String) {
    val workspaceId = id
    var wsTags = mutableListOf<Tag>()
}