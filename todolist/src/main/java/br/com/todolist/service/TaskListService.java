package br.com.todolist.service;

import br.com.todolist.model.TaskListModel;
import br.com.todolist.repository.TaskListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskListService {
    @Autowired
    private TaskListRepository taskListRepository;

    public List<TaskListModel> createList(TaskListModel TaskListModel){
        taskListRepository.save(TaskListModel);
        return findAll();
    }

    public List<TaskListModel> findAll(){
        return taskListRepository.findAllByOrderByCreationDateDesc();
    }

    public List<TaskListModel> deleteList(UUID id){
        taskListRepository.deleteById(id);
        return findAll();
    }

    public List<TaskListModel> updateList(TaskListModel TaskListModel){
        taskListRepository.save(TaskListModel);
        return findAll();
    }

}
