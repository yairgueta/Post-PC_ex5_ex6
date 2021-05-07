package exercise.android.reemh.todo_items

import android.graphics.Paint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    val ITEM_HOLDER_BUNDLE_KEY = "todo_item_holder"

    @JvmField
    var holder: TodoItemsHolder? = null
    lateinit var recyclerTodoItems: RecyclerView
    lateinit var addTaskField: EditText
    lateinit var createItemButton: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (holder == null) holder = TodoItemsHolderImpl()
        recyclerTodoItems = findViewById(R.id.recyclerTodoItemsList)
        addTaskField = findViewById(R.id.editTextInsertTask)
        createItemButton = findViewById(R.id.buttonCreateTodoItem)

        makeTaskFieldResponseToDone()
        setCreateItemButtonListener()

        recyclerTodoItems.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = TodoAdapter(holder!!)
        recyclerTodoItems.adapter = adapter
        adapter.onStatusImageClickListener = {
            when (it.isDone){
                true -> {
                    val (s,t) = holder!!.markItemInProgress(it)
                    adapter.notifyItemMoved(s, t)
                    adapter.notifyItemChanged(t)
                }
                false -> {
                    val (s,t) = holder!!.markItemDone(it)
                    adapter.notifyItemMoved(s, t)
                    adapter.notifyItemChanged(t)
                }
            }
        }
        adapter.onClearBtnClickListener = {
            val i = holder!!.deleteItem(it)
            adapter.notifyItemRemoved(i)
        }
    }

    private fun setCreateItemButtonListener() {
        createItemButton.setOnClickListener {
            val input = addTaskField
            if (input.text.isEmpty()) return@setOnClickListener
            holder!!.addNewInProgressItem(input.text.toString())
            recyclerTodoItems.adapter!!.notifyItemInserted(0)
            recyclerTodoItems.scrollToPosition(0)
            input.setText("")
        }
    }

    private fun makeTaskFieldResponseToDone() {
        addTaskField.isSingleLine = true
        addTaskField.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_NEXT,
                EditorInfo.IME_ACTION_DONE -> {
                    createItemButton.callOnClick(); true
                }
                EditorInfo.IME_ACTION_UNSPECIFIED -> {
                    if (event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                        createItemButton.callOnClick()
                        true
                    } else false
                }
                else -> false
            }
        }
    }

    class TodoAdapter(private var todoItemsHolder: TodoItemsHolder) : RecyclerView.Adapter<TodoItemsViewHolder>() {
        var onStatusImageClickListener: ((TodoItem) -> Unit)? = null
        var onClearBtnClickListener: ((TodoItem) -> Unit)? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_todo_item, parent, false)
            val holder = TodoItemsViewHolder(view)
            return holder
        }

        override fun onBindViewHolder(holder: TodoItemsViewHolder, position: Int) {
            val todoItem: TodoItem = todoItemsHolder.get(position)
            holder.updateView(todoItem)

            holder.setStatusImageOnClickListener{
                onStatusImageClickListener?.invoke(todoItem)
            }

            holder.setClearButtonOnClickListener{
                onClearBtnClickListener?.invoke(todoItem)
            }
        }

        override fun getItemCount(): Int {
            return todoItemsHolder.getSize()
        }

    }

    class TodoItemsViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private var descriptionTextView : TextView = view.findViewById(R.id.todoDescription)
        private var statusImage : ImageButton = view.findViewById(R.id.statusImage)
        private var clearBtn : ImageButton = view.findViewById(R.id.todoClearBtn)

        fun setInProgress(){
            descriptionTextView.paintFlags = descriptionTextView.paintFlags and (Paint.STRIKE_THRU_TEXT_FLAG.inv())
        }

        fun setDone(){
            descriptionTextView.paintFlags = descriptionTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        fun updateView(item: TodoItem){
            descriptionTextView.text = item.description
            if (item.isDone) setDone()
            else setInProgress()
            statusImage.setImageResource(if (item.isDone) android.R.drawable.checkbox_on_background else android.R.drawable.checkbox_off_background)
        }

        fun setStatusImageOnClickListener(l: View.OnClickListener){
            statusImage.setOnClickListener(l)
        }

        fun setClearButtonOnClickListener(l: View.OnClickListener){
            clearBtn.setOnClickListener(l)
        }
    }




    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(ITEM_HOLDER_BUNDLE_KEY, holder!!.saveInstance())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        holder!!.loadInstance(savedInstanceState.getSerializable(ITEM_HOLDER_BUNDLE_KEY)!!)

    }
}