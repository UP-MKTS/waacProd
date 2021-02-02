function showDetails(rowNum, id) {

    let tbody = document.getElementById('data-view').getElementsByTagName('tbody')[0];
    let detailsCaret = tbody.rows[rowNum - 1].cells[0].getElementsByTagName('i')[0];

    if (detailsCaret.classList.contains('fa-caret-right')) {
        detailsCaret.classList.replace('fa-caret-right', 'fa-caret-down');
        let row = tbody.insertRow(rowNum);

        let detailCell = row.insertCell(0);
        detailCell.setAttribute('colSpan', '10');
        detailCell.appendChild(createDetailTable(id));
    } else {
        detailsCaret.classList.replace('fa-caret-down', 'fa-caret-right');
        tbody.deleteRow(rowNum);
    }
}

function createDetailTable(id) {
    let table = document.createElement('table');
    table.classList.add('details-view');
    let thead = table.createTHead();
    let row0  = thead.insertRow(0);
    let tbody = table.createTBody();
    let row1  = tbody.insertRow(0);

    let detailTableHeader = {
        contractNumber: 'Номер договора',
        contractDate: 'Дата договора',
        transportationDate: 'Дата перевозки',
        carModel: 'Марка автомобиля',
        carNumber: 'Номер автомобиля',
        driverFio: 'ФИО водителя',
        wasteDangerousPowName: 'Степень опасности',
        wasteDangerousClassName: 'Класс опасности',
        boxing: 'Упаковка',
    };

    // Порядок следования столбцов определяется порядком следования полей в AccompPasspDetailsDto.class
    fillDetailsTable(id, detailTableHeader, row0, tbody);

    return table
}

function fillDetailsTable(id, detailTableHeader, row0, tbody) {
    fetch('api/accomp-passp/details/' + id,{
        method: 'GET',
        headers: {'Content-Type': 'application/json; charset=utf-8'}})
        .then(response => response.json())
        .then(result => {
            let i = 0;
            for (let key in result) {
                if(key == "departmentDtos")
                {
                    continue;
                }
                if(key == "accompPasspWasteDtoList")
                {
                    row0.insertCell(i).innerText = detailTableHeader["wasteDangerousPowName"];
                    i++;
                    row0.insertCell(i).innerText = detailTableHeader["wasteDangerousClassName"];
                    i++;
                    continue;
                }
                row0.insertCell(i).innerText = detailTableHeader[key];
                i++;
            }
            let j=0;
            for(let keyy in result["accompPasspWasteDtoList"]) {
                i=0;
                let row1 = tbody.insertRow(j);
                for (let key in result) {
                    if (key == "accompPasspWasteDtoList") {
                        row1.insertCell(i).innerText = result["accompPasspWasteDtoList"][keyy]["wasteTypeId"]["dangerousPowName"];
                        i++;
                        row1.insertCell(i).innerText = result["accompPasspWasteDtoList"][keyy]["wasteTypeId"]["dangerousClassName"];
                        i++;
                        continue;
                    }
                    if(key == "departmentDtos"){
                        continue;
                    }

                    row1.insertCell(i).innerText = result[key];
                    i++;
                }
                j++;
            }
        });
}

let idRec = 0;
function addRecord(selectName, listName) {
    let selectValue = document.getElementById(selectName);
    let td = document.getElementById(listName);

    let div = document.createElement('div');
    div.setAttribute('class', 'addRecord');
    div.setAttribute('name', selectName);
    let idObj = selectValue.options[selectValue.selectedIndex].value;
    let idName = selectName + '-' + idObj;
    div.setAttribute('id', idName);
    div.innerText = selectValue.options[selectValue.selectedIndex].text;

    let i = document.createElement('i');
    i.setAttribute('class', 'fa fa-close');
    i.setAttribute('onclick', "removeRecord('" + idName + "')");

    div.appendChild(i);

    if (document.getElementById(idName) === null){
        td.appendChild(div);
    } else {
        alert('Запись "' + selectValue.options[selectValue.selectedIndex].text + '" уже добавлена.')
    }
}
function showRecord(selectName, wastes, listName, nameFirst, nameSecond, nameThreed) {
    for (let waste of wastes) {
        let td = document.getElementById(listName);

        let div = document.createElement('div');
        div.setAttribute('class', 'addRecord');
        div.setAttribute('name', selectName);
        let idObj = waste[nameFirst][nameSecond];
        let idName = selectName + '-' + idObj;
        div.setAttribute('id', idName);
        div.innerText = waste[nameFirst][nameThreed];

        let i = document.createElement('i');
        i.setAttribute('class', 'fa fa-close');
        i.setAttribute('onclick', "removeRecord('" + idName + "')");

        div.appendChild(i);
        td.appendChild(div);
    }
}


function removeRecord(idName) {
    document.getElementById(idName).remove();
}