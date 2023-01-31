package com.example.todolist

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class TodoListRepositoryTest {
    private lateinit var repository: TodoListRepository

    @Test
    fun `getTodoList returns todo list`() {
        // arrange
        repository = TodoListRepositoryImpl()
        // action
        val result = repository.getTodoList()

        // assertion
        assertThat(result[0].name, equalTo("running"))
        assertThat(result[0].id, equalTo(1))
        assertThat(result[1].name, equalTo("cooking class"))
        assertThat(result[1].id, equalTo(2))
        assertThat(result[2].name, equalTo("sleeping"))
        assertThat(result[2].id, equalTo(3))
    }

    @Test
    fun `addTodoを実行するとリストにTodoObjectが追加される`() {
        // arrange
        repository = TodoListRepositoryImpl()
        // action
        repository.addTodo(NewTodo("yay"))
        val result = repository.getTodoList()
        // assertion
        assertThat(result.size, equalTo(4))
        assertThat(result[3].name, equalTo("yay"))
        assertThat(result[3].id, equalTo(4))
    }

    @Test
    fun `deleteTodoをidで指定するとTodoが削除される`() {
        //arrange
        repository = TodoListRepositoryImpl()
        val previousList = repository.getTodoList()
        assertThat(previousList.size, equalTo(3))

        // action
        val deletedTodo = repository.deleteTodo("1")

        val afterDeleteList = repository.getTodoList()
        assertThat(deletedTodo, equalTo(Todo(1, "running")))
        assertThat(afterDeleteList.size, equalTo(2))
        assertThat(afterDeleteList[0].id , equalTo(2))
        assertThat(afterDeleteList[1].id , equalTo(3))
    }

    @Test
    fun `deleteTodoで指定したidが無ければnullをreturnする`() {
        //arrange
        repository = TodoListRepositoryImpl()
        val allList = repository.getTodoList()
        assertThat(allList.size, equalTo(3))
        //action
        val deletedTodo = repository.deleteTodo("99")
        //assertion
        assertThat(deletedTodo, equalTo(null))
        assertThat(allList.size, equalTo(3))

    }
}