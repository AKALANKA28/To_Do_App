package com.example.todo

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.adapter.ToDoAdapter
import com.example.todo.model.ToDoModel
import com.example.todo.utils.DatabaseHelper
import com.example.todo.utils.ToDoDao
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity(), OnDialogCloseListener {

    private lateinit var mRecyclerview: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var myDao: ToDoDao
    private var mList: MutableList<ToDoModel> = mutableListOf()
    private lateinit var adapter: ToDoAdapter
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerview = findViewById(R.id.recyclerview)
        fab = findViewById(R.id.fab)
        searchEditText = findViewById(R.id.editTextText2)
        myDao = ToDoDao(this)
        adapter = ToDoAdapter(myDao, this)

        mRecyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

        mList = myDao.getAllTasks().toMutableList()
        mList.reverse()
        adapter.setTasks(mList)

        fab.setOnClickListener {
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
        }

        val itemTouchHelper = ItemTouchHelper(RecyclerViewTouchHelper(adapter))
        itemTouchHelper.attachToRecyclerView(mRecyclerview)


        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                filterTasks(query)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        updateDate()

        // Call the function to set up the RadioGroup listener
//        setupRadioGroupListener()
    }
    private fun filterTasks(query: String) {
        val filteredList = mList.filter { task ->
            task.task.toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))
        }
        adapter.setTasks(filteredList)
    }

    private fun updateDate() {
        val dateTextView: TextView = findViewById(R.id.dateTextView)
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd MMM yyyy, EEEE", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        dateTextView.text = formattedDate
    }

    override fun onDialogClose(dialogInterface: DialogInterface?) {
        // Refresh the list of tasks from the database
        val updatedList = myDao.getAllTasks().toMutableList()
        updatedList.reverse()

        // Clear the existing list and add the updated list
        mList.clear()
        mList.addAll(updatedList)

        // Set the updated list to the adapter and notify the change
        adapter.setTasks(mList)
        adapter.notifyDataSetChanged()
    }



//    private fun filterTasksByPriority() {
//        val sortedList = mList.sortedByDescending { it.priority }
//        adapter.setTasks(sortedList)
//    }

//    private fun setupRadioGroupListener() {
//        val radioGroupPriority = findViewById<RadioGroup>(R.id.radioGroupPriority)
//
//        radioGroupPriority.setOnCheckedChangeListener { _, checkedId ->
//            when (checkedId) {
//                R.id.radioButton_priority_high,
//                R.id.radioButton_priority_medium,
//                R.id.radioButton_priority_low -> filterTasksByPriority()
//            }
//        }
//    }


}
