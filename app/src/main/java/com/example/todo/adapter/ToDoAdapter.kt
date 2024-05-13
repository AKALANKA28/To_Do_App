package com.example.todo.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.AddNewTask
import com.example.todo.MainActivity
import com.example.todo.model.ToDoModel
import com.example.todo.R
import com.example.todo.utils.ToDoDao

class ToDoAdapter(private val myDao: ToDoDao, private val activity: MainActivity) :
    RecyclerView.Adapter<ToDoAdapter.MyViewHolder>() {

    private var mList: List<ToDoModel> = listOf()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mCheckBox: CheckBox = itemView.findViewById(R.id.mcheckbox)
        var mDescriptionTextView: TextView = itemView.findViewById(R.id.descriptionView)
        var mTimeTextView: TextView = itemView.findViewById(R.id.timeView)
        var mPriorityView: TextView = itemView.findViewById(R.id.priority)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mList[position]
        holder.mCheckBox.text = item.task // Set task text
        holder.mCheckBox.isChecked = toBoolean(item.status)
        holder.mDescriptionTextView.text = item.description // Update description text
        holder.mTimeTextView.text = item.time // Update time text
        holder.mPriorityView.text = item.priority // Update priority text

        holder.mCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                myDao.updateStatus(item.id, 1)
            } else {
                myDao.updateStatus(item.id, 0)
            }
        }

        holder.itemView.setOnClickListener {
            editItem(position)
        }
    }

    fun filterTasksByPriority(priority: String) {
        val filteredList = if (priority.isEmpty()) {
            mList
        } else {
            mList.filter { task ->
                task.priority == priority
            }
        }
        setTasks(filteredList)
    }

    private fun toBoolean(num: Int): Boolean {
        return num != 0
    }

    fun getContext(): Context {
        return activity
    }

    fun setTasks(mList: List<ToDoModel>) {
        this.mList = mList
        notifyDataSetChanged()
    }

    fun deleteTask(position: Int) {
        val item = mList[position]
        myDao.deleteTask(item.id)
        this.mList = mList.filterIndexed { index, _ -> index != position }
        notifyItemRemoved(position)
    }

    fun editItem(position: Int) {
        val item = mList[position]
        val bundle = Bundle().apply {
            putInt("id", item.id)
            putString("task", item.task)
            putString("description", item.description)
            putString("time", item.time)
            putString("priority", item.priority) // Pass priority to the dialog
        }
        val task = AddNewTask()
        task.arguments = bundle
        task.show(activity.supportFragmentManager, task.tag)
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}
