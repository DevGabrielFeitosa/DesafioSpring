let listIdUniversal;

$(document).ready(function () {
    getTaskLists();
    ordenateTask();
});

function formatDateForBackend(date) {
    return moment(date).format('YYYY-MM-DD HH:mm:ss');
}

function formatDateForFrontend(date) {
    return moment(date).format('DD-MM-YYYY HH:mm');
}

function formatPriorityForFrontend(priority) {
    let formattedPriorirty

    switch (priority){
        case '0':
            formattedPriorirty = 'Alta';
            break;
        case '1':
            formattedPriorirty = 'Média';
            break
        case '2':
            formattedPriorirty = 'Baixa';
            break
    }

    return formattedPriorirty;
}

function getTaskLists() {
    $.ajax({
        type: "GET",
        url: 'http://localhost:8080/taskList',
        dataType: 'json',
        contentType: 'application/json',
        processData: false,
        cache: false,
        success: function (result) {
            const list = document.getElementById('lists');
            if (result.length > 0) {

                list.innerHTML = '';

                result.forEach(item => {
                    const listItem = document.createElement('li');
                    listItem.className = 'list-group-item m-2 d-flex align-items-center';

                    listItem.innerHTML = `
                                                    <div class="list-item-container">
                                                        <h5 class="title">${item.title}</h5>
                                                        <p>${item.description}</p>
                                                        <small>Prioridade: ${formatPriorityForFrontend(item.priority)} | Data de Criação: ${formatDateForFrontend(item.creationDate)}</small>
                                                    </div>
                                                    <div>
                                                        <button type="button" class="btn btn-warning btn-sm me-2" onclick="openModalEditList('${item.id}','${item.title}','${item.description}','${item.priority}')">Editar</button>
                                                        <button type="button" class="btn btn-danger btn-sm" onclick="deleteList('${item.id}')">Excluir</button>
                                                    </div>
                                                `;

                    listItem.addEventListener('click', () => {
                        event.stopPropagation();

                        document.querySelectorAll('.list-item-container').forEach(container => {
                            container.classList.remove('selected');
                        });

                        listItem.querySelector('.list-item-container').classList.add('selected');

                        loadTasksForList(`${item.id}`)
                    });

                    list.appendChild(listItem);
                });

            } else {
                list.innerHTML = '<li class="list-group-item">Nenhum item disponível.</li>';

            }
        },
        error: function (result) {
            alert("Ocorreu um erro ao tentar listar suas tarefas.")
        }
    });
}

function createList() {

    let title = $('#listTitle').val()
    let description = $('#listDescription').val()
    let priority = $('#listPriority').val()

    if (title.trim() == ""){
        alert("O título não pode ser vazio!")
        return
    } else if (title.length < 10){
        alert("O título não deve ter menos que 10 caracteres.")
        return
    }

    if (description.length > 60){
        alert("A descrição não deve ter mais que 60 caracteres.")
        return
    }

    if (priority == ""){
        alert("É necessário informar uma prioridade.")
        return
    }

    let creationDate = formatDateForBackend(new Date());

    let jsonData = {
        "title": title,
        "description": description,
        "priority": priority,
        "creationDate": creationDate
    }

    $.ajax({
        type: "POST",
        url: `http://localhost:8080/taskList`,
        dataType: 'json',
        data: JSON.stringify(jsonData),
        contentType: 'application/json',
        processData: false,
        cache: false,
        success: function (response) {
            Swal.fire(
                {
                    title: 'Cadastrado!',
                    text: "A lista foi cadastrada com sucesso.",
                    icon: 'success',
                    showCancelButton: false,
                    confirmButtonColor: '#3A6332',
                    confirmButtonText: 'Confirmar'
                })

            cleanModalCreateTaskList()
            $('#createListModal').modal('hide');
            getTaskLists()
        },
        error: function (xhr, status, error) {
            Swal.fire({
                    title: 'Erro',
                    text: "Ocorreu um erro ao tentar deletar.",
                    icon: 'error',
                    showCancelButton: false,
                    confirmButtonColor: '#3A6332',
                    confirmButtonText: 'Confirmar'
                }
            );
        }
    });
}

function deleteList(listId) {
    Swal.fire({
        title: 'Tem certeza?',
        text: "Você não poderá reverter isso!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sim, exclua!',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                type: "DELETE",
                url: `http://localhost:8080/taskList?id=${listId}`,
                dataType: 'json',
                contentType: 'application/json',
                processData: false,
                cache: false,
                success: function (response) {
                    Swal.fire(
                        {
                            title: 'Deletado!',
                            text: "A lista foi excluída com sucesso.",
                            icon: 'success',
                            showCancelButton: false,
                            confirmButtonColor: '#3A6332',
                            confirmButtonText: 'Confirmar'
                        })

                    getTaskLists()
                },
                error: function (xhr, status, error) {
                    Swal.fire({
                            title: 'Erro',
                            text: "Ocorreu um erro ao tentar deletar.",
                            icon: 'error',
                            showCancelButton: false,
                            confirmButtonColor: '#3A6332',
                            confirmButtonText: 'Confirmar'
                        }
                    );
                }
            });
        }
    });
}

function editList(){
    let id = $('#editListId').val();
    let title = $('#editListTitle').val();
    let description = $('#editListDescription').val();
    let priority = $('#editListPriority').val();
    let creationDate = $('#creationDate').val();

    if (title.trim() == "") {
        alert("O título não pode ser vazio!");
        return;
    } else if (title.length < 10) {
        alert("O título não deve ter menos que 10 caracteres.");
        return;
    }

    if (description.length > 60) {
        alert("A descrição não deve ter mais que 60 caracteres.");
        return;
    }

    if (priority == "") {
        alert("É necessário informar uma prioridade.");
        return;
    }

    let jsonEdit = {
        "id":id,
        "title": title,
        "description": description,
        "priority": priority,
        "creationDate": formatDateForBackend(creationDate)
    }

    $.ajax({
        type: "PUT",
        url: `http://localhost:8080/taskList`,
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(jsonEdit),
        success: function(response) {
            Swal.fire(
                {
                    title: 'Editado!',
                    text: "A lista foi editada com sucesso.",
                    icon: 'success',
                    showCancelButton: false,
                    confirmButtonColor: '#3A6332',
                    confirmButtonText: 'Confirmar'
                })

            $('#editListModal').modal('hide');
            getTaskLists()

        },
        error: function(xhr, status, error) {
            alert("Erro ao salvar as alterações.");
        }
    });
}

function openModalEditList(id,title,description,priority,creationDate){
    $('#editListId').val('')
    $('#editListTitle').val('')
    $('#editListDescription').val('')
    $('#editListPriority').val('')
    $('#editListCreationDate').val('')

    $('#editListId').val(id)
    $('#editListTitle').val(title)
    $('#editListDescription').val(description)
    $('#editListPriority').val(priority)
    $('#editListCreationDate').val(creationDate)

    let myModal = new bootstrap.Modal(document.getElementById('editListModal'));

    myModal.show()
}

function cleanModalCreateTaskList(){
    $('#listTitle').val('')
    $('#listDescription').val('')
    $('#listPriority').val('')
}

//TASKS

function ordenateTask(){
    const sortSelect = document.getElementById('sortTasks');

    sortSelect.addEventListener('change', function() {
        const order = this.value;
        if (order) {
            fetchTasks(order);
        }
    });
}

function fetchTasks(order) {
    fetch(`http://localhost:8080/task/list/orderedList?order=${order}`)
        .then(response => response.json())
        .then(data => {
            updateTask(data);
        })
        .catch(error => alert('Erro ao buscar tarefas:'));
}

function loadTasksForList(listId) {
    $.ajax({
        type: "GET",
        url: `http://localhost:8080/task/list?id=${listId}`,
        dataType: 'json',
        contentType: 'application/json',
        success: function(result) {
            document.getElementById('tasks').innerHTML = '';

            if (result.length > 0) {
                listIdUniversal = listId
                updateTask(result)
            }

            document.getElementById('createNewTask').classList.remove('d-none');
            document.getElementById('sortTasks').classList.remove('d-none');

            const saveTaskButton = document.getElementById('createTaskButton');
            saveTaskButton.setAttribute('data-list-id', listId);
        },
        error: function(xhr, status, error) {
            alert("Ocorreu um erro ao tentar listar suas tarefas.")
        }
    });
}

function updateTask(tasks) {
    const tasksContainer = document.getElementById('tasks');
    tasksContainer.innerHTML = '';

    if (tasks.length > 0) {
        tasks.forEach(task => {
            const listItemHTML = `
                    <li class="list-group-item task m-2" style="width: 98%;">
                        <h5>${formatDateForFrontend(task.creationDate)}</h5>
                        <p>${task.description}</p>
                        <small>Prioridade: ${formatPriorityForFrontend(task.priority)} | Status: ${task.status}</small>
                        <div>
                            <button type="button" class="btn btn-warning btn-sm me-2" onclick="openModalEditTask('${task.id}','${task.status}','${task.description}','${task.priority}','${task.creationDate}', '${listIdUniversal}')">Editar</button>
                            <button type="button" class="btn btn-danger btn-sm" onclick="deleteTask('${task.id}','${task.listId}')">Excluir</button>
                        </div>
                    </li>
                `;
            tasksContainer.innerHTML += listItemHTML;
        });
    }
}

function createTask() {
    let listId = document.getElementById('createTaskButton').getAttribute('data-list-id');
    let status = $('#taskStatus').val()
    let description = $('#taskDescription').val()
    let priority = $('#taskPriority').val()

    if (description.trim() == ""){
        alert("A descrição não pode ser vazia!")
        return
    } else if (description.length >200){
        alert("O título não deve ter mais do que 200 caracteres.")
        return
    }

    if (priority == ""){
        alert("É necessário informar uma prioridade.")
        return
    }

    if (status == ""){
        alert("É necessário informar um status.")
        return
    }

    let creationDate = formatDateForBackend(new Date());

    let jsonData = {
        "status": status,
        "description": description,
        "priority": priority,
        "creationDate": creationDate
    }

    $.ajax({
        type: "POST",
        url: `http://localhost:8080/task?taskListId=${listId}`,
        dataType: 'json',
        data: JSON.stringify(jsonData),
        contentType: 'application/json',
        processData: false,
        cache: false,
        success: function (response) {
            Swal.fire(
                {
                    title: 'Cadastrado!',
                    text: "A tarefa foi cadastrada com sucesso.",
                    icon: 'success',
                    showCancelButton: false,
                    confirmButtonColor: '#3A6332',
                    confirmButtonText: 'Confirmar'
                })

            cleanModalCreateTask()
            $('#createTaskModal').modal('hide');
            loadTasksForList(listId)
        },
        error: function (xhr, status, error) {
            Swal.fire({
                    title: 'Erro',
                    text: "Ocorreu um erro ao tentar criar a tarefa.",
                    icon: 'error',
                    showCancelButton: false,
                    confirmButtonColor: '#3A6332',
                    confirmButtonText: 'Confirmar'
                }
            );
        }
    });
}

function cleanModalCreateTask(){
    $('#taskStatus').val('')
    $('#taskDescription').val('')
    $('#taskPriority').val('')
}

function editTask(){
    let id = $('#editTaskId').val();
    let status = $('#editTaskStatus').val();
    let description = $('#editTaskDescription').val();
    let priority = $('#editTaskPriority').val();
    let listId = $('#taskListId').val();

    if (description.trim() == ""){
        alert("A descrição não pode ser vazia!")
        return
    } else if (description.length >200){
        alert("O título não deve ter mais do que 200 caracteres.")
        return
    }

    if (priority == ""){
        alert("É necessário informar uma prioridade.")
        return
    }

    if (status == ""){
        alert("É necessário informar um status.")
        return
    }

    let jsonEdit = {
        "id":id,
        "status": status,
        "description": description,
        "priority": priority
    }

    $.ajax({
        type: "PUT",
        url: `http://localhost:8080/task`,
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(jsonEdit),
        success: function(response) {
            Swal.fire(
                {
                    title: 'Editado!',
                    text: "A lista foi editada com sucesso.",
                    icon: 'success',
                    showCancelButton: false,
                    confirmButtonColor: '#3A6332',
                    confirmButtonText: 'Confirmar'
                })

            $('#editTaskModal').modal('hide');
            loadTasksForList(listId)

        },
        error: function(xhr, status, error) {
            alert("Erro ao salvar as alterações.");
        }
    });
}

function openModalEditTask(id,status,description,priority,creationDate,listId){
    $('#taskListId').val('')
    $('#editTaskId').val('')
    $('#editTaskStatus').val('')
    $('#editTaskDescription').val('')
    $('#editTaskPriority').val('')
    $('#editTaskCreationDate').val('')
console.log(listId)
    $('#taskListId').val(listId)
    $('#editTaskId').val(id)
    $('#editTaskStatus').val(status)
    $('#editTaskDescription').val(description)
    $('#editTaskPriority').val(priority)
    $('#editTaskCreationDate').val(creationDate)

    let myModal = new bootstrap.Modal(document.getElementById('editTaskModal'));

    myModal.show()
}

function deleteTask(taskId, listId) {
    Swal.fire({
        title: 'Tem certeza?',
        text: "Você não poderá reverter isso!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sim, exclua!',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                type: "DELETE",
                url: `http://localhost:8080/task?id=${taskId}`,
                dataType: 'json',
                contentType: 'application/json',
                processData: false,
                cache: false,
                success: function (response) {
                    Swal.fire(
                        {
                            title: 'Deletado!',
                            text: "A tarefa foi excluída com sucesso.",
                            icon: 'success',
                            showCancelButton: false,
                            confirmButtonColor: '#3A6332',
                            confirmButtonText: 'Confirmar'
                        })

                    loadTasksForList(listId)
                },
                error: function (xhr, status, error) {
                    Swal.fire({
                            title: 'Erro',
                            text: "Ocorreu um erro ao tentar deletar.",
                            icon: 'error',
                            showCancelButton: false,
                            confirmButtonColor: '#3A6332',
                            confirmButtonText: 'Confirmar'
                        }
                    );
                }
            });
        }
    });
}