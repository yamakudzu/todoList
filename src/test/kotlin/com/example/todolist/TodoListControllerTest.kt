package com.example.todolist

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class TodoListControllerTest {
    private lateinit var spyStubService: SpyStubService
    private lateinit var mockMVC: MockMvc


    @BeforeEach
    fun setup() {
        spyStubService = SpyStubService()
        mockMVC = MockMvcBuilders.standaloneSetup(TodoListController(spyStubService)).build()
    }

    @Test
    fun `getTodoList() returns status 200`() {
        mockMVC.perform(get("/tasks"))
                .andExpect(status().isOk)
    }

    @Test
    fun `getTodoList() returns todo list`() {
        spyStubService.getTodoList_return_value = listOf(
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

        assertThat(spyStubService.addTodo_isCalled, equalTo(true))
        assertThat(spyStubService.addTodo_todoArgument, equalTo(newTodo))
    }

    @Test
    fun `delete todo if successful returns deleted todo`() {
        // arrange
        spyStubService.deleteTodo_return_value = Todo(1, "Cooking Class")
        // action + assertion
        mockMVC.perform(delete("/tasks/1"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("Cooking Class")))

        assertThat(spyStubService.deleteTodo_arg_id, equalTo("1"))
    }

    @Test
    fun `delete todo if failed returns null`() {
        spyStubService.deleteTodo_return_value = null
        mockMVC.perform(delete("/tasks/1"))
                .andExpect(jsonPath("$").doesNotExist())
    }
}

class SpyStubService: TodoListService {
    var getTodoList_return_value: List<Todo> = emptyList()
    var addTodo_isCalled: Boolean = false
    var addTodo_todoArgument: NewTodo? = null
    var deleteTodo_arg_id: String = ""
    var deleteTodo_return_value: Todo? = null

    override fun getTodoList(): List<Todo> {
        println("TodoList Service Stub")
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