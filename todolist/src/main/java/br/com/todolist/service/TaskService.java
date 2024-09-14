package br.com.todolist.service;

import br.com.todolist.DTO.TaskDTO;
import br.com.todolist.model.TaskModel;
import br.com.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    private TaskDTO convertToDTO(TaskModel taskModel) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(taskModel.getId());
        taskDTO.setDescription(taskModel.getDescription());
        taskDTO.setPriority(taskModel.getPriority());
        taskDTO.setCreationDate(taskModel.getCreationDate());
        taskDTO.setStatus(taskModel.getStatus());
        return taskDTO;
    }

    public List<TaskDTO> createTask(TaskModel taskModel){
        taskRepository.save(taskModel);
        return findAll();
    }

    public List<TaskDTO> findAll() {
        return taskRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> findAllWithOrder(String order){
        return taskRepository.findAll(Sort.by(Sort.Order.asc(order)))
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> deleteTask(UUID id){
        taskRepository.deleteById(id);
        return findAll();
    }

    public List<TaskDTO> updateTask(TaskModel taskModel){
        taskRepository.save(taskModel);
        return findAll();
    }

}
