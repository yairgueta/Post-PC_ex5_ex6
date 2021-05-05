package exercise.android.reemh.todo_items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import java.io.Serializable
import java.util.*

class TodoItemsHolderImpl : TodoItemsHolder {
    private val currentItems: MutableList<TodoItem> = mutableListOf()

    override fun getCurrentItems(): MutableList<TodoItem> {
        return currentItems.toMutableList()
    }

    private fun findInList(item: TodoItem): TodoItem? = currentItems.find{ other -> item == other}


    override fun addNewInProgressItem(description: String) {
        currentItems.add(TodoItem(description))
        currentItems.sort()
    }

    override fun markItemDone(item: TodoItem) {
        findInList(item)?.isDone = true
        currentItems.sort()
    }

    override fun markItemInProgress(item: TodoItem) {
        findInList(item)?.isDone = false
        currentItems.sort()
    }

    override fun deleteItem(item: TodoItem) {
        currentItems.remove(item)
        currentItems.sort()
    }

    override fun getSize(): Int {
        return currentItems.size
    }

    override fun get(i: Int): TodoItem {
        return currentItems.elementAt(i)
    }

    override fun saveInstance(): Serializable {
        return currentItems.toMutableList() as Serializable
    }

    override fun loadInstance(old: Serializable) {
        currentItems.clear()
        currentItems.addAll(old as MutableList<TodoItem>)
    }


}