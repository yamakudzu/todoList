package com.example.todolist

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class TodoListControllerTest {
    private lateinit var spyService: SpyService
    private lateinit var mockMVC: MockMvc


    @BeforeEach
    fun setup() {
        spyService = SpyService()
        mockMVC = MockMvcBuilders.standaloneSetup(TodoListController(spyService)).build()
    }

    @Test
    fun `getTodoList() returns status 200`() {
        mockMVC.perform(get("/tasks"))
                .andExpect(status().isOk)
    }

    @Test
    fun `getTodoList() returns todo list`() {
        spyService.getTodoList_return_value = listOf(
                Todo(1, "karaoke 3 hours"),
                Todo(2, "Watch Avatar")
        )

        mockMVC.perform(get("/tasks"))
                .andExpect(jsonPath("$.length()", equalTo(2)))
                .andExpect(jsonPath("$[0].name", equalTo("karaoke 3 hours")))
                .andExpect(jsonPath("$[1].name", equalTo("Watch Avatar")))
    }

    @Test
    fun `addTodoは Todo Objectをリポジトリに追加する`() {
        val newTodo = NewTodo( "yay")

        val objectMapper = ObjectMapper()
        mockMVC.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTodo)))
                .andExpect(status().isOk)

        assertThat(spyService.addTodo_isCalled, equalTo(true))
        assertThat(spyService.addTodo_todoArgument, equalTo(newTodo))
    }
}

class SpyService: TodoListService {
    var getTodoList_return_value: List<Todo> = emptyList()

    var addTodo_isCalled: Boolean = false
    var addTodo_todoArgument: NewTodo? = null

    override fun getTodoList(): List<Todo> {
        println("TodoList Service Stub")
        return getTodoList_return_value
    }

    override fun addTodo(todo: NewTodo) {
        addTodo_isCalled = true
        addTodo_todoArgument = todo
    }
}