package com.stark.sqldelightapp.data

//import com.stark.sqldelightapp.personDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.stark.sqldelightapp.PersonDatabase
import db.persondb.Person
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PersonDataSourceImpl @Inject constructor(
    private val db: PersonDatabase
) : PersonDataSource {

    private val TAG = this::class.java.simpleName

    private val queries = db.personQueries

    suspend fun <T, i> runOnBackground(block: () -> i): T {
        return withContext(Dispatchers.IO) {
            block.invoke()
        } as T
    }

    override fun getAllPersons(): kotlinx.coroutines.flow.Flow<List<Person>> {
        return queries.getAllPersons().asFlow().mapToList()
    }

    override suspend fun insertPerson(firstName: String, lastName: String, id: Long?) {
        withContext(Dispatchers.IO) {
            queries.insertPerson(id, firstName, lastName)
        }
    }

    override suspend fun getPersonByFirstName(name: String): Person? {
        return withContext(Dispatchers.IO) {
            queries.getPersonByName(name).executeAsOneOrNull()
        }
    }

    override suspend fun deletePersonById(id: Long) {
        withContext(Dispatchers.IO) {
            queries.deletePersonById(id)
        }
    }
}