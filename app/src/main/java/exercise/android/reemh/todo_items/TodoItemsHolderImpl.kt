package exercise.android.reemh.todo_items

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

class TodoItemsHolderImpl(val sp : SharedPreferences?) : TodoItemsHolder {
    private val KEY_CURRENT_ITEMS_GSON = "todoitems.key.todoitemsholder.impl.currentitems.list"
    private val currentItems: MutableList<TodoItem> = mutableListOf()
    private val gson : Gson = Gson()

    constructor() : this(null)

    override fun getCurrentItems(): MutableList<TodoItem> {
        return currentItems.toMutableList()
    }

    private fun findItemAndIndex(item: TodoItem): Pair<TodoItem?, Int> {
        val i = currentItems.indexOf(item)
        return currentItems.getOrNull(i) to i
    }

    private fun writeChangesToSP(){
        val spe = sp?.edit()
        val jsonCurrentItems = gson.toJson(currentItems)
        spe?.putString(KEY_CURRENT_ITEMS_GSON, jsonCurrentItems)
        spe?.apply()
    }

    fun loadChangesFromSP(){
        if (sp == null) return

        val jsonItems = sp.getString(KEY_CURRENT_ITEMS_GSON, null) ?: return
        val loadedItems = gson.fromJson<MutableList<TodoItem>>(jsonItems, object : TypeToken<MutableList<TodoItem>>(){}.type)
        currentItems.clear()
        currentItems.addAll(loadedItems)
    }

    override fun addNewInProgressItem(description: String) {
        currentItems.add(0, TodoItem(description))
        writeChangesToSP()
    }

    private fun setStatus(item: TodoItem, _isDone: Boolean): Pair<Int, Int> {
        val (todoItem, i) = findItemAndIndex(item)
        todoItem!!.isDone = _isDone
        currentItems.sort()
        val (_, newIndex) = findItemAndIndex(todoItem)
        writeChangesToSP()
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
        writeChangesToSP()
        return index
    }

    override fun modifyItemDescription(item: TodoItem, description: String) {
        val (todoItem, _) = findItemAndIndex(item)
        todoItem!!.description = description
        writeChangesToSP()
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
        writeChangesToSP()
    }


}