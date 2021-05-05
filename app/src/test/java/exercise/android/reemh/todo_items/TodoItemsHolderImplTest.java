package exercise.android.reemh.todo_items;

import junit.framework.TestCase;

import org.junit.Test;

public class TodoItemsHolderImplTest extends TestCase {
  @Test
  public void when_addingTodoItem_then_callingListShouldHaveThisItem(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
    assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping");

    // verify
    assertEquals(1, holderUnderTest.getCurrentItems().size());
  }



  // TODO: add at least 10 more tests to verify correct behavior of your implementation of `TodoItemsHolderImpl` class
}