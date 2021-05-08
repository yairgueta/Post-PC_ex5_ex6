package exercise.android.reemh.todo_items

import java.io.Serializable
import java.util.*
import kotlin.math.sign

class TodoItem (_isDone: Boolean, _description: String) : Serializable, Comparable<TodoItem> {
    val creationTime: Calendar = Calendar.getInstance()
    var lastModified: Calendar = Calendar.getInstance()

    var isDone = _isDone
        set(value) {
            lastModified = Calendar.getInstance()
            field = value
        }

    var description = _description
        set(value) {
            lastModified = Calendar.getInstance()
            field = value
        }

    constructor(description: String) : this(false, description)

    constructor() : this(false, "")

    override fun compareTo(other: TodoItem): Int {
        val statusPriority = (if (this.isDone) 100 else 0) - (if (other.isDone) 100 else 0)
        val creationTimePriority = (other.creationTime.timeInMillis - this.creationTime.timeInMillis).sign

        return  statusPriority + creationTimePriority
    }

    override fun toString(): String {
//        return "(desc=$description, created=${creationTime.timeInMillis})"
        return "$description"
    }

    override fun equals(other: Any?): Boolean {
        val otherTodo = other as? TodoItem
        if (otherTodo == null) return false
        else return this.isDone == otherTodo.isDone && this.creationTime.timeInMillis == otherTodo.creationTime.timeInMillis
                && this.lastModified.timeInMillis == otherTodo.lastModified.timeInMillis && this.description == otherTodo.description
    }

    override fun hashCode(): Int {
        var result = creationTime.hashCode()
        result = 31 * result + lastModified.hashCode()
        result = 31 * result + isDone.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }
}