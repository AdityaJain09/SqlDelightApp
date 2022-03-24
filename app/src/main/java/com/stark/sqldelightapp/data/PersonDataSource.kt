package com.stark.sqldelightapp.data

import db.persondb.Person
import kotlinx.coroutines.flow.Flow

interface PersonDataSource {

    fun getAllPersons(): Flow<List<Person>>

    suspend fun insertPerson(
        firstName: String,
        lastName: String,
        id: Long? = null
    )

    suspend fun getPersonByFirstName(name: String): Person?

    suspend fun deletePersonById(id: Long)
}