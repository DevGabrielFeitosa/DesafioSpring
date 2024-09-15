package br.com.todolist.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class TaskModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Descrição não deve ser vazia.")
    @Size(max = 200, message = "Descrição não deve possuir mais que 200 caracteres.")
    private String description;

    @NotBlank(message = "Prioridade não deve ser vazia.")
    private String priority;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    private String status;

    @ManyToOne
    @JoinColumn(name = "task_list_id")
    private TaskListModel taskListModel;

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

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public TaskListModel getTaskList() {
        return taskListModel;
    }

    public void setTaskList(TaskListModel taskListModel) {
        this.taskListModel = taskListModel;
    }
}
