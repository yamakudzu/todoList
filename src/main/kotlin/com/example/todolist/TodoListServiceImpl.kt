package com.example.todolist

import org.springframework.stereotype.Service

@Service
class TodoListServiceImpl(val todoListRepository: TodoListRepository): TodoListService {
    override fun getTodoList(): List<Todo> {
        println("TodoList Service Implementation")
        return todoListRepository.getTodoList()
    }

    override fun addTodo(todo: NewTodo) {
        todoListRepository.addTodo(todo)
    }
}

interface TodoListService {
    fun getTodoList(): List<Todo>
    fun addTodo(todo: NewTodo)
}