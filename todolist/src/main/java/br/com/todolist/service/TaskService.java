package br.com.todolist.service;

import br.com.todolist.model.Task;
import br.com.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> createTask(Task task){
        taskRepository.save(task);
        return findAll();
    }

    public List<Task> findAll(){
        return taskRepository.findAll();
    }

    public List<Task> findAllWithOrder(String order){
        return taskRepository.findAll(Sort.by(Sort.Order.asc(order)));
    }

    public List<Task> deleteTask(UUID id){
        taskRepository.deleteById(id);
        return findAll();
    }

    public List<Task> updateTask(Task task){
        taskRepository.save(task);
        return findAll();
    }

}
