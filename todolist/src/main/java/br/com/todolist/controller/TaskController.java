package br.com.todolist.controller;

import br.com.todolist.DTO.TaskDTO;
import br.com.todolist.model.TaskListModel;
import br.com.todolist.model.TaskModel;
import br.com.todolist.service.TaskListService;
import br.com.todolist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskListService taskListService;

    @PostMapping
    List<TaskDTO> create(@Valid @RequestBody TaskModel taskModel, @RequestParam UUID taskListId){
        TaskListModel taskListModel = taskListService.findById(taskListId);

        taskModel.setTaskList(taskListModel);

        return taskService.createTask(taskModel);
    }

    @GetMapping("/list")
    List<TaskDTO> list(@RequestParam("id") UUID id){
        return taskService.findAllByTaskList(id);
    }

    @GetMapping("/list/orderedList")
    List<TaskDTO> listWithOrder(@RequestParam("order") String order){
        return taskService.findAllWithOrder(order);
    }

    @PutMapping
    List<TaskDTO> update(@Valid @RequestBody TaskModel taskModel){
        return taskService.updateTask(taskModel);
    }

    @DeleteMapping()
    List<TaskDTO> delete(@RequestParam("id") UUID id){
        return taskService.deleteTask(id);
    }
}
