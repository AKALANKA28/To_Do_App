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
import android.widget.RadioGroup
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
    private var mRadioGroupPriority: RadioGroup? = null // RadioGroup for priority selection

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_newtask, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        mRadioGroupPriority = view.findViewById(R.id.radioGroupPriority)
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
            // Set the selected radio button based on the priority
            val priority = bundle?.getString("priority")
            when (priority) {
                "High Priority" -> mRadioGroupPriority?.check(R.id.radioButton_priority_high)
                "Medium Priority" -> mRadioGroupPriority?.check(R.id.radioButton_priority_medium)
                "Low Priority" -> mRadioGroupPriority?.check(R.id.radioButton_priority_low)
            }
        }

        mSaveButton?.isEnabled = !isUpdate

        // Set the click listener for the save button
        mSaveButton?.setOnClickListener {
            val task = mTaskEditText?.text.toString()
            val description = mDescriptionEditText?.text.toString()
            val time = mTimeEditText?.text.toString()
            val priority = when (mRadioGroupPriority?.checkedRadioButtonId) {
                R.id.radioButton_priority_high -> "High Priority"
                R.id.radioButton_priority_medium -> "Medium Priority"
                R.id.radioButton_priority_low -> "Low Priority"
                else -> "" // Default value
            }

            if (isUpdate) {
                myDb?.updateTask(bundle!!.getInt("id"), task, description, time, priority)
            } else {
                val item = ToDoModel(task = task, description = description, time = time, priority = priority)
                item.status = 0
                myDb?.insertTask(item)
            }

            dismiss()
        }

        // Set text change listener for enabling/disabling save button
        mTaskEditText?.addTextChangedListener(textChangeListener)
        mDescriptionEditText?.addTextChangedListener(textChangeListener)
        mTimeEditText?.addTextChangedListener(textChangeListener)
    }

    // TextWatcher for enabling/disabling save button
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
