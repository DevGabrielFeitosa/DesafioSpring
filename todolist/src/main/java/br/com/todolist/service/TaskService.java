package br.com.todolist.service;

import br.com.todolist.DTO.TaskDTO;
import br.com.todolist.exceptions.ResourceNotFoundException;
import br.com.todolist.model.TaskListModel;
import br.com.todolist.model.TaskModel;
import br.com.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        return findAllWithOrder("creationDate");
    }

    public List<TaskDTO> findAllByTaskList(UUID taskListId) {
        return taskRepository.findByTaskListModelIdOrderByPriority(taskListId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> findAllWithOrder(String order) {
        Sort sort = getSort(order);
        List<TaskModel> tasks = taskRepository.findAll(sort);
        return tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private Sort getSort(String order) {
        switch (order) {
            case "priorityAsc":
                return Sort.by(Sort.Order.asc("priority"));
            case "priorityDesc":
                return Sort.by(Sort.Order.desc("priority"));
            case "dateAsc":
                return Sort.by(Sort.Order.asc("creationDate"));
            case "dateDesc":
                return Sort.by(Sort.Order.desc("creationDate"));
            default:
                return Sort.unsorted();
        }
    }

    public List<TaskDTO> deleteTask(UUID id){
        taskRepository.deleteById(id);
        return findAllWithOrder("creationDate");
    }

    public List<TaskDTO> updateTask(TaskModel taskModel){
        Optional<TaskModel> existingTask = taskRepository.findById(taskModel.getId());

        if (existingTask.isPresent()) {
            TaskModel taskToUpdate = existingTask.get();

            taskToUpdate.setDescription(taskModel.getDescription());
            taskToUpdate.setPriority(taskModel.getPriority());
            taskToUpdate.setStatus(taskModel.getStatus());

            taskRepository.save(taskToUpdate);

        }else {
            throw new ResourceNotFoundException("Não encontramos a Tarefa informada.");
        }

        return findAllWithOrder("creationDate");
    }

}
