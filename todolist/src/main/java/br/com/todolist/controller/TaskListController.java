package br.com.todolist.controller;

import br.com.todolist.model.TaskList;
import br.com.todolist.service.TaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taskList")
public class TaskListController {
    @Autowired
    private TaskListService taskListService;

    @PostMapping
    List<TaskList> create(@RequestBody TaskList taskList){
        return taskListService.createList(taskList);
    }

    @GetMapping
    List<TaskList> list(){
        return taskListService.findAll();
    }

    @PutMapping
    List<TaskList> update(@RequestBody TaskList taskList){
        return taskListService.updateList(taskList);
    }

    @DeleteMapping("{id}")
    List<TaskList> delete(@PathVariable("id") Long id){
        return taskListService.deleteList(id);
    }
}
