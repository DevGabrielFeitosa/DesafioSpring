package br.com.todolist.repository;

import br.com.todolist.model.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {

    List<TaskList> findAllByOrderByCreationDateDesc();
}
