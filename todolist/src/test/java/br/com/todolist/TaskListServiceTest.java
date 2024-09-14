package br.com.todolist;

import br.com.todolist.DTO.TaskDTO;
import br.com.todolist.controller.TaskController;
import br.com.todolist.model.TaskListModel;
import br.com.todolist.model.TaskModel;
import br.com.todolist.repository.TaskRepository;
import br.com.todolist.service.TaskListService;
import br.com.todolist.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TaskController.class)
public class TaskListServiceTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private TaskListService taskListService;

    private UUID validTaskListId;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws ParseException {
        objectMapper = new ObjectMapper();

        validTaskListId = UUID.randomUUID();
        TaskListModel taskListModel = new TaskListModel();

        when(taskListService.findById(validTaskListId)).thenReturn(taskListModel);
    }

    @Test
    public void shouldCreateNewTaskList() throws Exception {
        String taskJson = "{\"description\": \"Teste para verificar se está tudo ok\", \"title\": \"Teste do título\" \"priority\": \"High\", \"creationDate\": \"2024-09-14 15:30:03\", \"status\": \"Open\"}";

        mockMvc.perform(post("/taskList")
                        .contentType(APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk());
    }

}



