package br.com.todolist;

import br.com.todolist.controller.TaskListController;
import br.com.todolist.exceptions.ResourceNotFoundException;
import br.com.todolist.model.TaskListModel;
import br.com.todolist.repository.TaskListRepository;
import br.com.todolist.service.TaskListService;
import br.com.todolist.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @MockBean
    private TaskListModel taskListModel;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws ParseException {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldCreateNewTaskList() throws Exception {
        String taskJson = "{\"description\": \"Teste para verificar se está tudo ok\", \"title\": \"Teste do título\", \"priority\": \"High\", \"creationDate\": \"2024-09-14 15:30:03\"}";

        mockMvc.perform(post("/taskList")
                        .contentType(APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnBadRequestWhenTitleIsMissing() throws Exception {
        String taskJson = "{\"description\": \"Descricao valida\", \"priority\": \"High\", \"creationDate\": \"2024-09-14 15:30:03\"}";

        mockMvc.perform(post("/taskList")
                        .contentType(APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Título não deve ser vazio."));
    }

    @Test
    public void shouldReturnBadRequestWhenTitleIsTooShort() throws Exception {
        String taskJson = "{\"title\": \"Short\", \"description\": \"Descricao valida\", \"priority\": \"High\", \"creationDate\": \"2024-09-14 15:30:03\"}";

        mockMvc.perform(post("/taskList")
                        .contentType(APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Título não deve possuir menos que 10 caracteres."));
    }

    @Test
    public void shouldReturnBadRequestWhenDescriptionIsTooLong() throws Exception {
        String longDescription = "A".repeat(61);
        String taskJson = String.format("{\"title\": \"Valid Title\", \"description\": \"%s\", \"priority\": \"High\", \"creationDate\": \"2024-09-14 15:30:03\"}", longDescription);

        mockMvc.perform(post("/taskList")
                        .contentType(APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description").value("Descrição não deve passar de 60 caracteres."));
    }

    @Test
    public void shouldReturnBadRequestWhenCreationDateIsInvalid() throws Exception {
        String taskJson = "{\"title\": \"Valid Title\", \"description\": \"Descricao valida\", \"priority\": \"High\", \"creationDate\": \"invalid-date-format\"}";

        mockMvc.perform(post("/taskList")
                        .contentType(APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenRequiredFieldsAreMissing() throws Exception {
        String taskJson = "{}";

        mockMvc.perform(post("/taskList")
                        .contentType(APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Título não deve ser vazio."));
    }

    @Test
    public void shouldReturnAllTasks() throws Exception {
        TaskListModel task1 = new TaskListModel();
        task1.setId(UUID.randomUUID());
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setPriority("Medium");
        task1.setCreationDate(new Date());

        TaskListModel task2 = new TaskListModel();
        task2.setId(UUID.randomUUID());
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setPriority("High");
        task2.setCreationDate(new Date());

        when(taskListService.findAll()).thenReturn(Arrays.asList(task1, task2));

        mockMvc.perform(get("/taskList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    public void testUpdateSuccess() throws Exception {
        UUID existingId = UUID.randomUUID();
        TaskListModel existingTask = new TaskListModel();
        existingTask.setId(existingId);
        existingTask.setTitle("Original Title");
        existingTask.setDescription("Original Description");
        existingTask.setPriority("Low");
        existingTask.setCreationDate(new Date());

        TaskListModel updateTask = new TaskListModel();
        updateTask.setId(existingId);
        updateTask.setTitle("Updated Title");
        updateTask.setDescription("Updated Description");
        updateTask.setPriority("High");
        updateTask.setCreationDate(new Date());

        when(taskListService.updateList(any(TaskListModel.class)))
                .thenReturn(Collections.singletonList(updateTask));

        mockMvc.perform(put("/taskList")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Updated Title"))
                .andExpect(jsonPath("$[0].description").value("Updated Description"))
                .andExpect(jsonPath("$[0].priority").value("High"));
    }

    @Test
    public void testUpdateFailure() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        TaskListModel updateTask = new TaskListModel();
        updateTask.setId(nonExistentId);
        updateTask.setTitle("Updated Title");

        when(taskListService.updateList(any(TaskListModel.class)))
                .thenThrow(new ResourceNotFoundException("Não encontramos a Lista de Tarefas informada."));

        mockMvc.perform(put("/taskList")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateTask)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Não encontramos a Lista de Tarefas informada."));
    }

    @Test
    public void shouldDeleteTask() throws Exception {
        UUID taskId = UUID.randomUUID();
        TaskListModel task = new TaskListModel();
        task.setId(taskId);
        task.setTitle("Task to delete");
        task.setDescription("Description");
        task.setPriority("High");
        task.setCreationDate(new Date());

        when(taskListService.deleteList(taskId)).thenReturn(Collections.emptyList());

        mockMvc.perform(delete("/taskList/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}



