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
public class TaskServiceTest {

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
    public void shouldCreateNewTask() throws Exception {
        String taskJson = "{\"description\": \"Teste para verificar se está tudo ok\", \"priority\": \"High\", \"creationDate\": \"2024-09-14 15:30:03\", \"status\": \"Open\"}";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date creationDate = sdf.parse("2024-09-14 15:30:03");

        TaskDTO createdTask = new TaskDTO();
        createdTask.setDescription("Teste para verificar se está tudo ok");
        createdTask.setPriority("High");
        createdTask.setCreationDate(creationDate);
        createdTask.setStatus("Open");

        mockMvc.perform(post("/task")
                        .param("taskListId", validTaskListId.toString())
                        .contentType(APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnBadRequestWhenTaskListIdIsMissing() throws Exception {
        String taskJson = "{\"description\": \"Teste para verificar se está tudo ok\", \"priority\": \"High\", \"creationDate\": \"2024-09-14 15:30:03\", \"status\": \"Open\"}";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date creationDate = sdf.parse("2024-09-14 15:30:03");

        TaskDTO createdTask = new TaskDTO();
        createdTask.setDescription("Teste para verificar se está tudo ok");
        createdTask.setPriority("High");
        createdTask.setCreationDate(creationDate);
        createdTask.setStatus("Open");

        mockMvc.perform(post("/task")
                        .contentType(APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.taskListId").value("Parâmetro ausente."));
    }

    @Test
    public void shouldReturnBadRequestWhenDescriptionIsBlank() throws Exception {
        String taskJson = "{\"description\": \"\", \"priority\": \"High\", \"creationDate\": \"2024-09-14 15:30:03\", \"status\": \"Open\"}";

        mockMvc.perform(post("/task")
                        .contentType(APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description").value("Descrição não deve ser vazia."));
    }

    @Test
    public void shouldReturnBadRequestWhenDescriptionIsTooLong() throws Exception {
        String taskJson = "{\"description\": \"" + "A".repeat(201) + "\", \"priority\": \"High\", \"creationDate\": \"2024-09-14 15:30:03\", \"status\": \"Open\"}";

        mockMvc.perform(post("/task")
                        .contentType("application/json")
                        .content(taskJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description").value("Descrição não deve possuir mais que 200 caracteres."));
    }

    @Test
    public void shouldReturnListOfTaks() throws Exception {
        TaskDTO task1 = new TaskDTO();
        task1.setDescription("Task 1");
        task1.setPriority("High");
        task1.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-09-14 15:30:03"));
        task1.setStatus("Open");

        TaskDTO task2 = new TaskDTO();
        task2.setDescription("Task 2");
        task2.setPriority("Low");
        task2.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-09-14 15:30:03"));
        task2.setStatus("Closed");

        when(taskService.findAll()).thenReturn(Arrays.asList(task1, task2));

        mockMvc.perform(get("/task/list")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Task 1"))
                .andExpect(jsonPath("$[1].description").value("Task 2"));


        mockMvc.perform(get("/task/list")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Task 1"))
                .andExpect(jsonPath("$[1].description").value("Task 2"));
    }

    @Test
    public void shouldReturnOrderedListOfTasks() throws Exception {
        TaskDTO task1 = new TaskDTO();
        task1.setDescription("Task 3");
        task1.setPriority("1");
        task1.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-09-14 15:30:03"));
        task1.setStatus("Open");

        TaskDTO task2 = new TaskDTO();
        task2.setDescription("Task 1");
        task2.setPriority("0");
        task2.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-09-14 15:30:03"));
        task2.setStatus("Closed");

        TaskDTO task3 = new TaskDTO();
        task3.setDescription("Task 2");
        task3.setPriority("2");
        task3.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-09-14 15:30:03"));
        task3.setStatus("Closed");

        when(taskService.findAllWithOrder("priority")).thenReturn(Arrays.asList(task2, task1, task3));

        mockMvc.perform(get("/task/list/orderedList")
                        .param("order", "priority")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Task 1"))
                .andExpect(jsonPath("$[1].description").value("Task 3"))
                .andExpect(jsonPath("$[2].description").value("Task 2"));
    }

    @Test
    public void shouldDeleteWithSuccess() throws Exception {
        UUID idTask1 = UUID.randomUUID();
        UUID idTask2 = UUID.randomUUID();

        TaskDTO task1 = new TaskDTO();
        task1.setId(idTask1);
        task1.setDescription("Task 1");
        task1.setPriority("High");
        task1.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-09-14 15:30:03"));
        task1.setStatus("Open");

        TaskDTO task2 = new TaskDTO();
        task2.setId(idTask2);
        task2.setDescription("Task 2");
        task2.setPriority("Low");
        task2.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-09-14 15:30:03"));
        task2.setStatus("Closed");

        when(taskService.findAll()).thenReturn(Arrays.asList(task1, task2));

        when(taskService.deleteTask(idTask2)).thenReturn(Arrays.asList(task1));

        mockMvc.perform(delete("/task/{id}", idTask2)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Task 1"));
    }

    @Test
    public void testUpdateTask() throws Exception {
        UUID taskId = UUID.randomUUID();
        UUID taskListId = UUID.randomUUID();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date creationDate = sdf.parse("2024-09-14 15:30:03");

        TaskModel initialTaskModel = new TaskModel();
        initialTaskModel.setId(taskId);
        initialTaskModel.setDescription("Initial description");
        initialTaskModel.setPriority("Medium");
        initialTaskModel.setCreationDate(creationDate);
        initialTaskModel.setStatus("Pending");

        TaskListModel taskListModel = new TaskListModel();
        taskListModel.setId(taskListId);

        when(taskListService.findById(taskListId)).thenReturn(taskListModel);

        TaskModel updatedTaskModel = new TaskModel();
        updatedTaskModel.setId(taskId);
        updatedTaskModel.setDescription("Updated description");
        updatedTaskModel.setPriority("High");
        updatedTaskModel.setCreationDate(creationDate);
        updatedTaskModel.setStatus("In Progress");
        updatedTaskModel.setTaskList(taskListModel);

        TaskDTO updatedTaskDTO = new TaskDTO();
        updatedTaskDTO.setId(updatedTaskModel.getId());
        updatedTaskDTO.setDescription(updatedTaskModel.getDescription());
        updatedTaskDTO.setPriority(updatedTaskModel.getPriority());
        updatedTaskDTO.setCreationDate(updatedTaskModel.getCreationDate());
        updatedTaskDTO.setStatus(updatedTaskModel.getStatus());

        when(taskService.updateTask(any(TaskModel.class))).thenReturn(Collections.singletonList(updatedTaskDTO));

        mockMvc.perform(put("/task")
                        .param("taskListId", taskListId.toString())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTaskModel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(updatedTaskDTO.getId().toString()))
                .andExpect(jsonPath("$[0].description").value(updatedTaskDTO.getDescription()))
                .andExpect(jsonPath("$[0].priority").value(updatedTaskDTO.getPriority()))
                .andExpect(jsonPath("$[0].creationDate").value(sdf.format(updatedTaskDTO.getCreationDate())))
                .andExpect(jsonPath("$[0].status").value(updatedTaskDTO.getStatus()))
                .andDo(print());
    }
}

