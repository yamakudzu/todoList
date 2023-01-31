package com.example.todolist

import org.springframework.stereotype.Repository

data class Todo (val id: Int, val name: String)
data class NewTodo(val name: String)

@Repository
class TodoListRepositoryImpl: TodoListRepository {
    private val todolist: MutableList<Todo> = mutableListOf(
            Todo(1, "running"),
            Todo(2, "cooking class"),
            Todo(3, "sleeping"),
            )

    override fun getTodoList(): List<Todo> {
        return todolist
    }

    override fun addTodo(todo: NewTodo) {
        val newId = todolist.last().id + 1
        todolist.add(Todo(newId,todo.name))
    }

    override fun deleteTodo(id: String): Todo? {
        val todoToBeDeleted = findById(id.toInt())
        todolist.remove(todoToBeDeleted)
        return todoToBeDeleted
    }

    private fun findById(id: Int): Todo? {
        for(todo in todolist) {
            if(todo.id == id) {
                return todo
            }
        }
        return null
    }
}

interface TodoListRepository {
    fun getTodoList(): List<Todo>
    fun addTodo(todo: NewTodo)
    fun deleteTodo(id: String): Todo?
}