package br.com.todolist.controller;

import br.com.todolist.model.TaskListModel;
import br.com.todolist.service.TaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/taskList")
public class TaskListController {
    @Autowired
    private TaskListService taskListService;

    @PostMapping
    List<TaskListModel> create(@RequestBody TaskListModel taskListModel){
        return taskListService.createList(taskListModel);
    }

    @GetMapping
    List<TaskListModel> list(){
        return taskListService.findAll();
    }

    @PutMapping
    List<TaskListModel> update(@RequestBody TaskListModel taskListModel){
        return taskListService.updateList(taskListModel);
    }

    @DeleteMapping("{id}")
    List<TaskListModel> delete(@PathVariable("id") UUID id){
        return taskListService.deleteList(id);
    }
}
