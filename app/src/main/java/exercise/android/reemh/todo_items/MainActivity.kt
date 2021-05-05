package exercise.android.reemh.todo_items

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    val ITEM_HOLDER_BUNDLE_KEY = "todo_item_holder"

    var holder: TodoItemsHolder? = null
    var recyclerTodoItems: RecyclerView? = null
    var addTaskField: EditText? = null
    var createItemButton: FloatingActionButton? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (holder == null) {
            holder = TodoItemsHolderImpl()
        }
        recyclerTodoItems = findViewById(R.id.recyclerTodoItemsList)
        addTaskField = findViewById(R.id.editTextInsertTask)
        createItemButton = findViewById(R.id.buttonCreateTodoItem)

        setCreateItemButtonListener()

        recyclerTodoItems!!.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = TodoAdapter(holder!!)
        recyclerTodoItems!!.adapter = adapter
        adapter.onStatusImageClickListener = {
            if (it.isDone) holder!!.markItemInProgress(it)
            else holder!!.markItemDone(it)
        }
        adapter.onClearBtnClickListener = {
            holder!!.deleteItem(it)
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
                notifyDataSetChanged()
            }

            holder.setClearButtonOnClickListener{
                onClearBtnClickListener?.invoke(todoItem)
                notifyDataSetChanged()
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

    private fun setCreateItemButtonListener() {
        createItemButton!!.setOnClickListener { v ->
            val input = addTaskField!!
            if (input.text.isEmpty()) {
                println(holder!!.currentItems.sorted())
                return@setOnClickListener
            }
            holder!!.addNewInProgressItem(input.text.toString())
            recyclerTodoItems!!.adapter!!.notifyDataSetChanged()
            input.setText("")
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