$(document).ready(function () {
    getTaskLists();
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

//Falta o else
function getTaskLists() {
    $.ajax({
        type: "GET",
        url: 'http://localhost:8080/taskList',
        dataType: 'json',
        contentType: 'application/json',
        processData: false,
        cache: false,
        success: function (result) {
            if (result.length > 0) {
                const list = document.getElementById('lists');
                list.innerHTML = '';

                result.forEach(item => {
                    const listItem = document.createElement('li');
                    listItem.className = 'list-group-item d-flex justify-content-between align-items-center';

                    listItem.innerHTML = `
                                                    <div class="list-item-container">
                                                        <h5 class="title">${item.title}</h5>
                                                        <p>${item.description}</p>
                                                        <small>Prioridade: ${formatPriorityForFrontend(item.priority)} | Data de Criação: ${formatDateForFrontend(item.creationDate)}</small>
                                                    </div>
                                                    <div>
                                                        <button type="button" class="btn btn-warning btn-sm me-2" onclick="openModalEdit('${item.id}','${item.title}','${item.description}','${item.priority}')">Editar</button>
                                                        <button type="button" class="btn btn-danger btn-sm" onclick="deleteList('${item.id}')">Excluir</button>
                                                    </div>
                                                `;

                    list.appendChild(listItem);
                });

            } else {

            }
        },
        error: function (result) {
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

    console.log(id)

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

function openModalEdit(id,title,description,priority,creationDate){
    $('#editListId').val('')
    $('#editListTitle').val('')
    $('#editListDescription').val('')
    $('#editListPriority').val('')
    $('#editCreationDate').val('')

    $('#editListId').val(id)
    $('#editListTitle').val(title)
    $('#editListDescription').val(description)
    $('#editListPriority').val(priority)
    $('#editCreationDate').val(creationDate)

    let myModal = new bootstrap.Modal(document.getElementById('editListModal'));

    myModal.show()
}

function cleanModalCreateTask(){
    $('#listTitle').val('')
    $('#listDescription').val('')
    $('#listPriority').val('')
}
