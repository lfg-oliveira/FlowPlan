package com.example.flowplan.model

import kotlinx.serialization.Serializable

@Serializable
data class Task( val taskId: Int?,
         var title: String,
         var description: String,
//         var tags: MutableList<Tag>,
//         var asignees: MutableList<User>
)

