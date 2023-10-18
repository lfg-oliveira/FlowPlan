package com.example.flowplan.model


class User(id: String) {
    private val userId = id
    var ownedWorkspaces = mutableListOf<Workspace>()
    var name = ""
    var email = ""
    var phones = mutableListOf<String>()
        private set

    override fun equals(other: Any?): Boolean {
        return userId == other
    }

    fun addPhone(phone: String) {
        phones.add(phone)
    }

}