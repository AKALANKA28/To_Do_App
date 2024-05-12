package com.example.todo


import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.todo.OnDialogCloseListener



import com.example.todo.model.ToDoModel
import com.example.todo.utils.DataBaseHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class AddNewTask : BottomSheetDialogFragment() {

    private var mTaskEditText: EditText? = null
    private var mDescriptionEditText: EditText? = null
    private var mTimeEditText: EditText? = null
    private var mSaveButton: Button? = null
    private var myDb: DataBaseHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_newtask, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTaskEditText = view.findViewById(R.id.edittext)
        mDescriptionEditText = view.findViewById(R.id.description_text)
        mTimeEditText = view.findViewById(R.id.time_text)
        mSaveButton = view.findViewById(R.id.button_save)
        myDb = DataBaseHelper(requireActivity())

        val bundle = arguments
        val isUpdate = bundle != null

        if (isUpdate) {
            mTaskEditText?.setText(bundle?.getString("task"))
            mDescriptionEditText?.setText(bundle?.getString("description"))
            mTimeEditText?.setText(bundle?.getString("time"))
        }

        mSaveButton?.isEnabled = !isUpdate

        mTaskEditText?.addTextChangedListener(textChangeListener)
        mDescriptionEditText?.addTextChangedListener(textChangeListener)
        mTimeEditText?.addTextChangedListener(textChangeListener)

        mSaveButton?.setOnClickListener {
            val task = mTaskEditText?.text.toString()
            val description = mDescriptionEditText?.text.toString()
            val time = mTimeEditText?.text.toString()

            if (isUpdate) {
                myDb?.updateTask(bundle!!.getInt("id"), task, description, time) // Ensure correct parameters are passed
            } else {
                val item = ToDoModel(task = task, description = description, time = time)
                item.status = 0
                myDb?.insertTask(item)
            }

            view.findViewById<TextView>(R.id.descriptionView)?.text = description
            view.findViewById<TextView>(R.id.timeView)?.text = time

            dismiss()
        }

    }

    private val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val task = mTaskEditText?.text.toString()
            val description = mDescriptionEditText?.text.toString()
            val time = mTimeEditText?.text.toString()

            mSaveButton?.isEnabled = task.isNotEmpty() && description.isNotEmpty() && time.isNotEmpty()
        }

        override fun afterTextChanged(s: Editable?) {}
    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity: Activity? = activity
        if (activity is OnDialogCloseListener) {
            (activity as OnDialogCloseListener).onDialogClose(dialog)
        }
    }

    companion object {
        const val TAG = "AddNewTask"
        fun newInstance(): AddNewTask {
            return AddNewTask()
        }
    }
}