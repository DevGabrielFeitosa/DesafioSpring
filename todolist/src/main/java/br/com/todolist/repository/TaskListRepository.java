package br.com.todolist.repository;

import br.com.todolist.model.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {
}
