package br.com.todolist.repository;

import br.com.todolist.model.TaskListModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskListRepository extends JpaRepository<TaskListModel, UUID> {

    List<TaskListModel> findAllByOrderByCreationDateDesc();
}
