package com.stark.sqldelightapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stark.sqldelightapp.R
import com.stark.sqldelightapp.SqlDelightAdapter
import dagger.hilt.android.AndroidEntryPoint
import db.persondb.Person
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/*
*  Crud Operations using Sql Delight
*  No Architecture is being used, purpose of this app to learn basic sql delight
*  there are other apps with proper architecture in my repos.
*/
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var person: Person?= null

    private lateinit var adapter: SqlDelightAdapter
    val vm: MainViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var firstNameTv: TextView
    private lateinit var lastNameTv: TextView
    private lateinit var saveBtn: Button
private val TAG = this::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initRecyclerView()
        observers()
        saveBtn.setOnClickListener {
            if (validateName()) {
                try {
                    if (person != null) {
                        val person = Person(
                            person!!.id,
                            firstNameTv.text.toString(),
                            lastNameTv.text.toString()
                        )
                        vm.insertPerson(person.firstName, person.secondName, person.id)
                        Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        val firstName = firstNameTv.text.toString()
                        val lastName = lastNameTv.text.toString()
                        vm.insertPerson(firstName, lastName)
                        Toast.makeText(this, "Inserted Successfully", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Fail to Perform Action", Toast.LENGTH_SHORT).show()
                }
                clearAfterSave()
            }
        }
    }

    private fun validateName(): Boolean {
        if (firstNameTv.text.isBlank() || lastNameTv.text.isBlank()){
            Toast.makeText(this, "Please Enter Full Name", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun clearAfterSave() {
        person = null
        firstNameTv.text = ""
        lastNameTv.text = ""
    }

    private fun observers() {
        lifecycleScope.launch {
            vm.persons.collect {
                adapter.submitList(it)
            }
        }
    }

    private fun initRecyclerView() {
        adapter = SqlDelightAdapter( {
            vm.deletePersonById(it)
            Toast.makeText(this, "Delete Successfully.", Toast.LENGTH_SHORT).show()
        },{
            person = it
            firstNameTv.text = it.firstName
            lastNameTv.text = it.secondName
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun initViews() {
        firstNameTv = findViewById(R.id.first_name)
        lastNameTv = findViewById(R.id.second_name)
        saveBtn = findViewById(R.id.save_button)
        recyclerView = findViewById(R.id.recycler_view)
    }
}