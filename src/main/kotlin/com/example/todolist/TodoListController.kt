package com.example.todolist

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TodoListController(val service: TodoListService) {
    // controller -> service -> repo

    @GetMapping("/tasks")
    fun getTodoList(): List<Todo> {
        return service.getTodoList()
    }

    @PostMapping("/tasks")
    fun addTodo(@RequestBody todo: NewTodo) {
        service.addTodo(todo)
    }

}