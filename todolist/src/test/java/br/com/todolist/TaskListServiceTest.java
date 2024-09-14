package br.com.todolist;

import br.com.todolist.controller.TaskController;
import br.com.todolist.controller.TaskListController;
import br.com.todolist.repository.TaskListRepository;
import br.com.todolist.service.TaskListService;
import br.com.todolist.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TaskListController.class)
public class TaskListServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskListService taskListService;

    @MockBean
    private TaskListRepository taskListRepository;

    @MockBean
    private TaskService taskService;

    @BeforeEach
    public void setUp() throws ParseException {
    }

    @Test
    public void shouldCreateNewTaskList() throws Exception {
        String taskJson = "{\"description\": \"Teste para verificar se está tudo ok\", \"title\": \"Teste do título\", \"priority\": \"High\", \"creationDate\": \"2024-09-14 15:30:03\"}";

        mockMvc.perform(post("/taskList")
                        .contentType(APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk());
    }

}



