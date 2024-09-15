package br.com.todolist.service;

import br.com.todolist.exceptions.ResourceNotFoundException;
import br.com.todolist.model.TaskListModel;
import br.com.todolist.repository.TaskListRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskListService {
    @Autowired
    private TaskListRepository taskListRepository;

    public List<TaskListModel> createList(TaskListModel taskListModel){
        taskListRepository.save(taskListModel);
        return findAll();
    }

    public List<TaskListModel> findAll(){
        return taskListRepository.findAllByOrderByCreationDateDesc();
    }

    public List<TaskListModel> deleteList(UUID id){
        taskListRepository.deleteById(id);
        return findAll();
    }

    public List<TaskListModel> updateList(TaskListModel taskListModel){
        Optional<TaskListModel> existingTask = taskListRepository.findById(taskListModel.getId());

        if (existingTask.isPresent()) {
            TaskListModel taskToUpdate = existingTask.get();

            taskToUpdate.setTitle(taskListModel.getTitle());
            taskToUpdate.setDescription(taskListModel.getDescription());
            taskToUpdate.setPriority(taskListModel.getPriority());

            taskListRepository.save(taskToUpdate);

        }else {
            throw new ResourceNotFoundException("Não encontramos a Lista de Tarefas informada.");
        }

        return findAll();
    }

    public TaskListModel findById(UUID taskListId){
        return taskListRepository.findById(taskListId)
                .orElseThrow(() -> new ResourceNotFoundException("Não encontramos a Lista de Tarefas informada."));
    }

}
