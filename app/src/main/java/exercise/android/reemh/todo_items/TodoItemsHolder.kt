package exercise.android.reemh.todo_items

import java.io.Serializable
import java.util.*

interface TodoItemsHolder {
    /** Get a copy of the current items list  */
    val currentItems: MutableList<TodoItem>

    /**
     * Creates a new TodoItem and adds it to the list, with the @param description and status=IN-PROGRESS
     * Subsequent calls to [getCurrentItems()] should have this new TodoItem in the list
     */
    fun addNewInProgressItem(description: String)

    /** mark the @param item as DONE  */
    fun markItemDone(item: TodoItem)

    /** mark the @param item as IN-PROGRESS  */
    fun markItemInProgress(item: TodoItem)

    /** delete the @param item  */
    fun deleteItem(item: TodoItem)

    fun getSize() : Int

    fun get(i: Int) : TodoItem

    fun saveInstance() : Serializable

    fun loadInstance(old: Serializable)
}