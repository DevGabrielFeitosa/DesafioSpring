package br.com.todolist.controller;

import br.com.todolist.DTO.TaskListDTO;
import br.com.todolist.model.TaskListModel;
import br.com.todolist.service.TaskListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/taskList")
public class TaskListController {
    @Autowired
    private TaskListService taskListService;

    @PostMapping
    List<TaskListDTO> create(@Valid @RequestBody TaskListModel taskListModel){
        return taskListService.createList(taskListModel);
    }

    @GetMapping
    List<TaskListDTO> list(){
        return taskListService.findAll();
    }

    @PutMapping
    ResponseEntity<List<TaskListDTO>> update(@Valid @RequestBody TaskListModel taskListModel){
        List<TaskListDTO> updatedList = taskListService.updateList(taskListModel);
        return new ResponseEntity<>(updatedList, HttpStatus.OK);
    }

    @DeleteMapping()
    List<TaskListDTO> delete(@RequestParam("id")  UUID id){
        return taskListService.deleteList(id);
    }
}
