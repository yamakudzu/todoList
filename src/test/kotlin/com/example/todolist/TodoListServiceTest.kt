package com.example.todolist

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.*
import org.junit.jupiter.api.Test

class TodoListServiceTest {
    @Test
    fun `getTodoList returns todo list from repository`() {
        // arrange
        val stubRepo = SpyStubTodoListRepo()
        val service = TodoListServiceImpl(stubRepo)
        stubRepo.getTodoList_return_value = listOf(Todo(1, "playing board game"), Todo(2, "shopping"))

        // action
        val result = service.getTodoList()

        // assertion
        assertThat(result, equalTo(listOf(Todo(1, "playing board game"), Todo(2,"shopping"))))
        assertThat(result.size, equalTo(2))
    }

    @Test
    fun `addTodoは Todo Objectをリポジトリに追加する`() {
        // arrange
        val spyRepo = SpyStubTodoListRepo()
        val service = TodoListServiceImpl(spyRepo)

        // action
        val newTodo = NewTodo("yay")
        service.addTodo(newTodo)

        // assertion
        // XXX リポジトリに追加されること
        // OOO StubリポジトリのaddTodoメソッドが正しい引数で呼ばれること
        assertThat(spyRepo.addTodo_isCalled, equalTo(true))
        assertThat(spyRepo.addTodo_todoArgument, equalTo(newTodo))
    }

    @Test
    fun `deleteTodoは指定されたidのTodoをdeleteする`() {
        //arrange
        val spyStubRepo = SpyStubTodoListRepo()
        val service = TodoListServiceImpl(spyStubRepo)
        //action
        val delTodo = Todo(100000, "sleep long")
        spyStubRepo.deleteTodo_return_value = delTodo

        val result = service.deleteTodo("100000")

        //assertion
        assertThat(spyStubRepo.deleteTodo_arg_id, equalTo("100000"))
        assertThat(result, equalTo(delTodo))
    }

    @Test
    fun `deleteTodoは指定されたidがない時にはnullをreturnする`() {
        //arrange
        val spyStubRepo = SpyStubTodoListRepo()
        val service = TodoListServiceImpl(spyStubRepo)
        spyStubRepo.deleteTodo_return_value = null

        //action
        val result = service.deleteTodo("100000")

        //assertion
        assertThat(spyStubRepo.deleteTodo_arg_id, equalTo("100000"))
        assertThat(result, equalTo(null))
    }
}

class SpyStubTodoListRepo: TodoListRepository {
    var getTodoList_return_value: List<Todo> = emptyList()

    var addTodo_isCalled: Boolean = false
    var addTodo_todoArgument: NewTodo? = null
    var deleteTodo_return_value: Todo? = null
    var deleteTodo_arg_id: String = ""


    override fun getTodoList(): List<Todo> {
        return getTodoList_return_value
    }

    override fun addTodo(todo: NewTodo) {
        addTodo_isCalled = true
        addTodo_todoArgument = todo
    }

    override fun deleteTodo(id: String): Todo? {
        deleteTodo_arg_id = id
        return deleteTodo_return_value
    }

}
