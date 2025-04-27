var classTable, classColumns;

$(document).ready(function () {
    getClasses();

    classColumns = [
        {
            class: "viewColumn",
            data: null,
            render: function(data, type, row, meta) {
                var classID = row.classID;
                var dropdown = '<div class="dropdown show">' +
                    '<a class="btn-sm btn btn-info" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="fa fa-eye" aria-hidden="true"></span></a>' +
                    '<div class="dropdown-menu aria-labelledby="dropdownMenuLink">' +
                    `<a class="dropdown-item" href="./classlist/${classID}" data-rowindex ="' + meta.row + '">View Students</a>` + 
                    `<a class="dropdown-item" href="./teacherlist/${classID}" data-rowindex ="' + meta.row + '">View Teacher</a>` + 
                    '</div></div>';
                return dropdown;
            },
            orderable: false,
            width: "1 em"
        },
        { data: "classID", class: "charColumn", width: "2 rem"},
        { data: "className", class: "charColumn", width: "3 rem"},
        { data: "studentCount", class: "charColumn", width: "3 rem"},
        { data: "playlistCount", class: "charColumn", width: "1 rem"},
        { data: "null", class: "text-center", defaultContent: `
            <button class ="btn btn-danger remove-btn">Remove</button>
        `},
    ];

        //event listener for disable button
        $('#classTable tbody').on('click', '.disable-btn', function() {
            const button = $(this);
    
            if(button.hasClass('btn-warning')) {
                button.text('Enable');
                button.removeClass('btn-warning').addClass('btn-secondary');
            }
            else {
                button.text('Disable');
                button.removeClass('btn-secondary').addClass('btn-warning');
            }
        });
    
        //event listener for remove button
        let rowRemove;
    
        $('#classTable tbody').on('click', '.remove-btn', function() {
            const row = $(this).closest('tr');
            rowRemove = row;
    
            $('#removeConfirmation').modal('show');
        });
    
        $('#removeConfirmationButton').on('click', function() {
            if(rowRemove) {
                classTable.row(rowRemove).remove().draw();
                $('#removeConfirmation').modal('hide');
            }
        });

    $('#confirmClassBtn').on('click', function (event) {
        event.preventDefault();
        let valid = validateFormData();

        if (valid) {
            //gather form data
            let classData = $('#addClassForm').serializeArray().reduce(function (acc, item) {
                acc[item.name] = item.value;
                return acc;
            }, {});
        
            //check if serialization maps incorrect key value pairs
            if (classData.name && classData.value) {
                classData[classData.name] = classData.value;
                delete classData.name;
                delete classData.value;
            }

            console.log(classData);
            addClass(classData);
        }
    });
})

function getClasses() {
    $.ajax({
        url: 'http://localhost:8080/api/moderator/dashboard',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            classData = initializeDataTableWithFilters('#classTable', data.data, classColumns, [1, 'asc'], 10);
        },
        error: function (xhr, status, error) {
            console.error("Error fetching data from the API:", error);
        }
    });

}

function addClass(classData) {
    $.ajax({
        data: JSON.stringify(classData),
        url: 'http://localhost:8080/api/moderator/dashboard',
        method: 'POST',
        contentType: 'application/json',
        success: function (data) {
            console.log("class added");
            bootstrapAlert('success', "Class Added");
            $('#addClassModal').modal('hide');
        },
        error: function (xhr, status, error) {
            console.error("Class could not be created: ", error);
            bootstrapAlert('danger', 'Class could not be created: ' + error);
        }
    });
}

function validateFormData() {
    let allAreFilled = true;
    document.getElementById('addClassModal').querySelectorAll("[required]").forEach(function (i) {
        if (!allAreFilled) {
            return;
        }

        if (!i.value) {
            allAreFilled = false;
            return;
        }

    })
    if (!allAreFilled) {
        bootstrapAlert('danger', 'Fill all fields.');
        return false;
    }

    return true;
}