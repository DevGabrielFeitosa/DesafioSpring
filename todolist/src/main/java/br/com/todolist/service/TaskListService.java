package br.com.todolist.service;

import br.com.todolist.DTO.TaskListDTO;
import br.com.todolist.exceptions.ResourceNotFoundException;
import br.com.todolist.model.TaskListModel;
import br.com.todolist.repository.TaskListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskListService {
    @Autowired
    private TaskListRepository taskListRepository;

    private TaskListDTO convertToDTO(TaskListModel taskListModel) {
        TaskListDTO taskListDTO = new TaskListDTO();
        taskListDTO.setId(taskListModel.getId());
        taskListDTO.setDescription(taskListModel.getDescription());
        taskListDTO.setPriority(taskListModel.getPriority());
        taskListDTO.setCreationDate(taskListModel.getCreationDate());
        taskListDTO.setTitle(taskListModel.getTitle());
        return taskListDTO;
    }

    public List<TaskListDTO> createList(TaskListModel taskListModel) {
        taskListRepository.save(taskListModel);
        return findAll();
    }

    public List<TaskListDTO> findAll() {
        return taskListRepository.findAllByOrderByPriorityAsc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TaskListDTO> deleteList(UUID id) {
        taskListRepository.deleteById(id);
        return findAll();
    }

    public List<TaskListDTO> updateList(TaskListModel taskListModel) {
        Optional<TaskListModel> existingTask = taskListRepository.findById(taskListModel.getId());

        if (existingTask.isPresent()) {
            TaskListModel taskToUpdate = existingTask.get();

            taskToUpdate.setTitle(taskListModel.getTitle());
            taskToUpdate.setDescription(taskListModel.getDescription());
            taskToUpdate.setPriority(taskListModel.getPriority());

            taskListRepository.save(taskToUpdate);

        } else {
            throw new ResourceNotFoundException("Não encontramos a Lista de Tarefas informada.");
        }

        return findAll();
    }

    public TaskListModel findById(UUID taskListId) {
        return taskListRepository.findById(taskListId)
                .orElseThrow(() -> new ResourceNotFoundException("Não encontramos a Lista de Tarefas informada."));
    }

}
