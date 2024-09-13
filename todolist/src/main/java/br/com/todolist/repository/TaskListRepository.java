package br.com.todolist.repository;

import br.com.todolist.model.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskListRepository extends JpaRepository<TaskList, UUID> {

    List<TaskList> findAllByOrderByCreationDateDesc();
}
