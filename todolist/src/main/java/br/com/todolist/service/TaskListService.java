package br.com.todolist.service;

import br.com.todolist.model.Task;
import br.com.todolist.model.TaskList;
import br.com.todolist.repository.TaskListRepository;
import br.com.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskListService {
    @Autowired
    private TaskListRepository taskListRepository;

    public List<TaskList> createList(TaskList TaskList){
        taskListRepository.save(TaskList);
        return findAll();
    }

    public List<TaskList> findAll(){
        return taskListRepository.findAllByOrderByCreationDateDesc();
    }

    public List<TaskList> deleteList(Long id){
        taskListRepository.deleteById(id);
        return findAll();
    }

    public List<TaskList> updateList(TaskList TaskList){
        taskListRepository.save(TaskList);
        return findAll();
    }

}
