package br.com.todolist.service;

import br.com.todolist.model.TaskModel;
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

    public List<TaskModel> createTask(TaskModel taskModel){
        taskRepository.save(taskModel);
        return findAll();
    }

    public List<TaskModel> findAll(){
        return taskRepository.findAll();
    }

    public List<TaskModel> findAllWithOrder(String order){
        return taskRepository.findAll(Sort.by(Sort.Order.asc(order)));
    }

    public List<TaskModel> deleteTask(UUID id){
        taskRepository.deleteById(id);
        return findAll();
    }

    public List<TaskModel> updateTask(TaskModel taskModel){
        taskRepository.save(taskModel);
        return findAll();
    }

}
