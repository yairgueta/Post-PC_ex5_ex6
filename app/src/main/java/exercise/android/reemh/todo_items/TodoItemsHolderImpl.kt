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

    private fun findItemAndIndex(item: TodoItem): Pair<TodoItem?, Int> {
        val i = currentItems.indexOf(item)
        return currentItems.getOrNull(i) to i
    }


    override fun addNewInProgressItem(description: String) {
        currentItems.add(0, TodoItem(description))
//        currentItems.sort()
    }

    private fun setStatus(item: TodoItem, _isDone: Boolean): Pair<Int, Int> {
        val (todoItem, i) = findItemAndIndex(item)
        todoItem!!.isDone = _isDone
        currentItems.sort()
        val (_, newIndex) = findItemAndIndex(todoItem)
        return i to newIndex
    }

    override fun markItemDone(item: TodoItem): Pair<Int, Int> {
        return setStatus(item, true)
    }

    override fun markItemInProgress(item: TodoItem): Pair<Int, Int> {
        return setStatus(item, false)
    }

    override fun deleteItem(item: TodoItem): Int {
        val index = currentItems.indexOf(item)
        currentItems.removeAt(index)
        return index
//        currentItems.sort()
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