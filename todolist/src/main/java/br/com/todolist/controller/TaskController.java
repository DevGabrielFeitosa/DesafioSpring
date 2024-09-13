package br.com.todolist.controller;

import br.com.todolist.model.Task;
import br.com.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping
    List<Task> create(@RequestBody Task task){
        return taskService.createTask(task);
    }

    @GetMapping("/list")
    List<Task> list(){
        return taskService.findAll();
    }

    @GetMapping("/list/orderedList")
    List<Task> listWithOrder(@RequestParam("order") String order){
        return taskService.findAllWithOrder(order);
    }

    @PutMapping
    List<Task> update(@RequestBody Task task){
        return taskService.updateTask(task);
    }

    @DeleteMapping("{id}")
    List<Task> create(@PathVariable("id") UUID id){
        return taskService.deleteTask(id);
    }
}
