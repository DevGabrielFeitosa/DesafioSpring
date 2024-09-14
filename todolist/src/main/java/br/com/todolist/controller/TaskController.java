package br.com.todolist.controller;

import br.com.todolist.model.TaskListModel;
import br.com.todolist.model.TaskModel;
import br.com.todolist.service.TaskListService;
import br.com.todolist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskListService taskListService;

    @PostMapping
    List<TaskModel> create(@Valid @RequestBody TaskModel taskModel, @RequestParam UUID taskListId){
        TaskListModel taskListModel = taskListService.findById(taskListId);

        taskModel.setTaskList(taskListModel);

        return taskService.createTask(taskModel);
    }

    @GetMapping("/list")
    List<TaskModel> list(){
        return taskService.findAll();
    }

    @GetMapping("/list/orderedList")
    List<TaskModel> listWithOrder(@RequestParam("order") String order){
        return taskService.findAllWithOrder(order);
    }

    @PutMapping
    List<TaskModel> update(@Valid @RequestBody TaskModel taskModel){
        return taskService.updateTask(taskModel);
    }

    @DeleteMapping("{id}")
    List<TaskModel> delete(@PathVariable("id") UUID id){
        return taskService.deleteTask(id);
    }
}
