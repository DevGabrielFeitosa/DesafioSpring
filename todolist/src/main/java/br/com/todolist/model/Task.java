package br.com.todolist.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    private String description;

    private String priority;

    private Date creationDate;

    private String status;

    @ManyToOne
    @JoinColumn(name = "task_list_id")
    private TaskList taskList;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }
}
