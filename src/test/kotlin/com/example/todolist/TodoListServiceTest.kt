package com.example.todolist

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.*
import org.junit.jupiter.api.Test

class TodoListServiceTest {
    @Test
    fun `getTodoList returns todo list from repository`() {
        // arrange
        val stubRepo = SpyTodoListRepo()
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
        val stubRepo = SpyTodoListRepo()
        val service = TodoListServiceImpl(stubRepo)

        // action
        val newTodo = NewTodo("yay")
        service.addTodo(newTodo)

        // assertion
        // XXX リポジトリに追加されること
        // OOO StubリポジトリのaddTodoメソッドが正しい引数で呼ばれること
        assertThat(stubRepo.addTodo_isCalled, equalTo(true))
        assertThat(stubRepo.addTodo_todoArgument, equalTo(newTodo))
    }
}

class SpyTodoListRepo: TodoListRepository {
    var getTodoList_return_value: List<Todo> = emptyList()

    var addTodo_isCalled: Boolean = false
    var addTodo_todoArgument: NewTodo? = null

    override fun getTodoList(): List<Todo> {
        return getTodoList_return_value
    }

    override fun addTodo(todo: NewTodo) {
        addTodo_isCalled = true
        addTodo_todoArgument = todo
    }
}
