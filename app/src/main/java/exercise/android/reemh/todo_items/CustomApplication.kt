package exercise.android.reemh.todo_items

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager

class CustomApplication : Application() {
    companion object {
        val KEY_TODO_ITEM = "todo.app.todo.item.key"
        val ACTION_BROADCAST_REFRESH_HOLDER = "todo.app.refresh.todo.items.holder"
    }

    lateinit var itemsHolder: TodoItemsHolder

    override fun onCreate() {
        super.onCreate()
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        itemsHolder = TodoItemsHolderImpl(sp)
        (itemsHolder as TodoItemsHolderImpl).loadChangesFromSP()
    }
}