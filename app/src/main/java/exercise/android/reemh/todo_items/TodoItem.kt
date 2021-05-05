package exercise.android.reemh.todo_items

import java.io.Serializable

class TodoItem (var isDone: Boolean, val description: String) : Serializable, Comparable<TodoItem> {
    private companion object {
        private var _nextID = 1
    }
    var id: Int = _nextID++

    constructor(description: String) : this(false, description)

    override fun compareTo(other: TodoItem): Int {
        return (if (this.isDone) 100 else 0) - (if (other.isDone) 100 else 0) + (if (other.id - this.id> 0) 1 else -1)
    }

    override fun toString(): String {
        return "(isDone=$isDone, desc=$description, id=$id)"
    }
}