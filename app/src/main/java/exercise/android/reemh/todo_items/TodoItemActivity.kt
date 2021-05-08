package exercise.android.reemh.todo_items

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import java.util.*
import java.util.concurrent.TimeUnit

class TodoItemActivity : AppCompatActivity() {

    lateinit var todoItem: TodoItem
    lateinit var checkBox: CheckBox
    lateinit var createdText : TextView
    lateinit var lastModifiedText : TextView
    lateinit var descriptionText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_item)

        val app = application as CustomApplication

        val index = intent?.getIntExtra(CustomApplication.KEY_TODO_ITEM, -1)
        if (index == -1 || index == null){
            finish()
            return
        }else{
            todoItem = app.itemsHolder.get(index)
        }

        createdText = findViewById(R.id.creationTimeText)
        createdText.text = String.format(resources.getString(R.string.todo_item_info_page_creation_time), todoItem.creationTime)


        lastModifiedText = findViewById(R.id.lastMoidifiedText)
        refreshLastMod()

        checkBox = findViewById(R.id.todoActivity_checkBox)
        checkBox.isChecked = todoItem.isDone
        checkBox.setOnClickListener{
            when(todoItem.isDone){
                true -> app.itemsHolder.markItemInProgress(todoItem)
                false -> app.itemsHolder.markItemDone(todoItem)
            }
            notifyHolderChanged()
            refreshLastMod()
        }

        descriptionText = findViewById(R.id.todoItemDescription)
        descriptionText.setText(todoItem.description)
        descriptionText.addTextChangedListener(afterTextChanged = {
            app.itemsHolder.modifyItemDescription(todoItem, it.toString())
            notifyHolderChanged()
            refreshLastMod()
        })
    }

    private fun refreshLastMod(){
        val timePassedSinceLastModification = TimeUnit.MILLISECONDS.toMinutes(Calendar.getInstance().timeInMillis - todoItem.lastModified.timeInMillis)
        lastModifiedText.text = when (timePassedSinceLastModification){
            0L -> resources.getString(R.string.todo_item_info_page_last_modified_zero_min)
            1L -> resources.getString(R.string.todo_item_info_page_last_modified_one_min)
            in 2..59 -> resources.getString(R.string.todo_item_info_page_last_modified_this_hour, timePassedSinceLastModification)
            in 60 until 60*24 -> resources.getString(R.string.todo_item_info_page_last_modified_today, todoItem.lastModified)
            else -> resources.getString(R.string.todo_item_info_page_last_modified_day_or_more, todoItem.lastModified)
        }
    }

    private fun notifyHolderChanged(){
        Intent().also {
            it.action = CustomApplication.ACTION_BROADCAST_REFRESH_HOLDER
            sendBroadcast(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}