package com.example.flowplan.model

import java.util.UUID

data class Task( val taskId: String,
         var title: String,
         var description: String,
         var tags: MutableList<Tag>,
         var asignees: MutableList<User>)

