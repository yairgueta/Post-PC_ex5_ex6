package exercise.android.reemh.todo_items

import android.content.SharedPreferences
import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito
import java.io.Serializable

class TodoItemsHolderImplTest : TestCase() {
    @Test
    fun test_when_addingTodoItem_then_callingListShouldHaveThisItem() {
        // setup
        val holderUnderTest = TodoItemsHolderImpl()
        assertEquals(0, holderUnderTest.getCurrentItems().size)

        // test
        holderUnderTest.addNewInProgressItem("do shopping")

        // verify
        assertEquals(1, holderUnderTest.getCurrentItems().size)
    }

    @Test
    fun test_when_addingNewItem_then_itemShouldBeInProgress(){
        val holder = TodoItemsHolderImpl()

        holder.addNewInProgressItem("write tests")

        assertFalse(holder.getCurrentItems()[0].isDone)
    }

    @Test
    fun test_when_markItemDone_then_itShouldBeDone(){
        val holder = TodoItemsHolderImpl()

        holder.addNewInProgressItem("eat something")
        val item = holder.getCurrentItems()[0]
        holder.markItemDone(item)

        assertTrue(item.isDone)

        holder.markItemInProgress(item)
        assertFalse(item.isDone)
    }

    @Test
    fun test_when_callingGetCurrentItems_then_shouldReturnCopyOfList(){
        val holder = TodoItemsHolderImpl()

        holder.addNewInProgressItem("drink coffee")
        holder.addNewInProgressItem("walk the dog")

        holder.getCurrentItems().clear()
        assertEquals(2, holder.getCurrentItems().size)
    }

    @Test
    fun test_when_deleteItem_then_itemShouldBeDeleted(){
        val holder = TodoItemsHolderImpl()

        holder.addNewInProgressItem("feed the baby")
        holder.addNewInProgressItem("charge the phone")
        holder.addNewInProgressItem("lock the door")
        val item = holder.getCurrentItems()[1]

        holder.deleteItem(item)

        assertEquals(2, holder.getCurrentItems().size)

        for (it in holder.getCurrentItems())
            assertNotSame(item, it)
    }

    @Test
    fun test_when_saveAndLoadState_then_shouldReturnToLastState(){
        val holder = TodoItemsHolderImpl()

        holder.addNewInProgressItem("buy milk")
        holder.addNewInProgressItem("finish homework")
        holder.addNewInProgressItem("bake a cake")

        val savedState: Serializable = holder.saveInstance()

        holder.addNewInProgressItem("call my sister")
        holder.addNewInProgressItem("paint the wall")

        assertEquals(5, holder.getSize())
        holder.loadInstance(savedState)
        assertEquals(3, holder.getSize())
    }

    @Test
    fun test_when_saveState_and_loadToOther_then_shouldLoadTheFirstOne(){
        val holder1 = TodoItemsHolderImpl()
        val holder2 = TodoItemsHolderImpl()

        holder1.addNewInProgressItem("pay bills")
        holder1.addNewInProgressItem("ask father money")

        holder2.addNewInProgressItem("play pokemon")
        holder2.addNewInProgressItem("get a shower")
        holder2.addNewInProgressItem("go to sleep")
        holder2.addNewInProgressItem("wash the dish")

        assertEquals(2, holder1.getSize())
        assertEquals(4, holder2.getSize())

        val savedState = holder1.saveInstance()
        holder1.addNewInProgressItem("clean the house")
        holder2.loadInstance(savedState)

        assertEquals(2, holder2.getSize())
    }

    @Test
    fun test_when_addingNewTasks_then_shouldSortThemByTime(){
        val tasks = arrayListOf("eat chicken", "make laundry", "clean history", "read a book",
                "clean the keyboard", "print documents")
        val holder = TodoItemsHolderImpl()

        for ((i, task) in tasks.withIndex()) {
            holder.addNewInProgressItem(task)
            assertEquals(task, holder.getCurrentItems()[0].description)
        }

        val item = holder.getCurrentItems()[3]
        assertEquals(tasks[2], item.description)
    }

    @Test
    fun test_when_setTaskAsDone_then_shouldSortDown(){
        val tasks = arrayListOf("eat chicken", "make laundry", "clean history", "read a book",
                "clean the keyboard", "print documents")
        val holder = TodoItemsHolderImpl()

        for (task in tasks) {
            holder.addNewInProgressItem(task)
        }

        val item = holder.getCurrentItems()[3]
        assertEquals(tasks[2], item.description)

        holder.markItemDone(item)

        val expectedList = tasks.toMutableList()
        expectedList.removeAt(2)
        expectedList.reverse()
        expectedList.add(tasks[2])

        expectedList.withIndex().forEach{ (i, it) -> assertEquals(it, holder.get(i).description)}
    }

    @Test
    fun test_listOrdering(){
        val holder = TodoItemsHolderImpl()

        for (i in 1..9){
            holder.addNewInProgressItem(i.toString())
        }

        for ((i,t) in listOf("9","8","7","6","5","4","3","2","1").withIndex()){
            assertEquals(t, holder.getCurrentItems().get(i).description)
        }

        var cur = holder.getCurrentItems()
        holder.markItemDone(cur[2])
        holder.markItemDone(cur[5])

        for ((i,t) in listOf("9","8","6","5","3","2","1","7","4").withIndex()){
            assertEquals(t, holder.getCurrentItems().get(i).description)
        }

    }
}