package com.stark.sqldelightapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stark.sqldelightapp.data.PersonDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import db.persondb.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataSource: PersonDataSource
): ViewModel() {

    private val TAG = this::class.java.simpleName

    val persons: Flow<List<Person>> = dataSource.getAllPersons()

    // don't do it in main project, exposing mutable property is bad.
    lateinit var personDetail: MutableLiveData<Person>

    fun insertPerson(
        firstName: String,
        secondName: String,
        id: Long? = null,
    ) {
        if (firstName.isBlank() && secondName.isBlank()) {
            return
        }
        viewModelScope.launch {
            dataSource.insertPerson(firstName, secondName, id)
        }
    }

    fun getPersonByName(name: String) {
        viewModelScope.launch {
            personDetail.value = dataSource.getPersonByFirstName(name)
        }
    }

    fun deletePersonById(id: Long) {
            viewModelScope.launch {
                dataSource.deletePersonById(id)
            }
    }
}