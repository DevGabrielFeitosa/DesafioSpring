package br.com.todolist.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "task_list")
public class TaskListModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Título não deve ser vazio.")
    @Size(min = 10, message = "Título não deve possuir menos que 10 caracteres.")
    private String title;

    @Size(max = 60, message = "Descrição não deve passar de 60 caracteres.")
    private String description;

    @NotBlank(message = "Prioridade não deve ser vazia.")
    private String priority;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @OneToMany(mappedBy = "taskListModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskModel> taskModels;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public Set<TaskModel> getTasks() {
        return taskModels;
    }

    public void setTasks(Set<TaskModel> taskModels) {
        this.taskModels = taskModels;
    }
}
