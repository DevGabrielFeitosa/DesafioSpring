$(document).ready(function () {
    getTaskLists();
});

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
                                                    <div>
                                                        <h5>${item.title}</h5>
                                                        <p>${item.description}</p>
                                                        <small>Prioridade: ${item.priority} | Data de Criação: ${item.creationDate}</small>
                                                    </div>
                                                    <div>
                                                        <button type="button" class="btn btn-warning btn-sm me-2">Editar</button>
                                                        <button type="button" class="btn btn-danger btn-sm">Excluir</button>
                                                    </div>
                                                `;

                            list.appendChild(listItem);
                        });

                    }else{

                    }
        },
        error: function (result) {
        }
    });
}

function deleteList(listId){
    $.ajax({
        type: "DELETE",
        url: 'http://localhost:8080/taskList',
        dataType: 'json',
        contentType: 'application/json',
        processData: false,
        cache: false,
        success: function (result) {
        },
        error: function (result) {
        }
    });
}