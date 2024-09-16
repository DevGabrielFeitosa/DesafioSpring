package br.com.todolist.repository;

import br.com.todolist.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskModel, UUID> {

    List<TaskModel> findByTaskListModelIdOrderByPriority(UUID taskListId);
}
