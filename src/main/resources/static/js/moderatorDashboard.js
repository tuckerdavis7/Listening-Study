let classData = [
    {
        "classID": "01",
        "className": "Music Theory Basics",
        "studentCount": "12",
        "playlistCount": "3"
    },
    {
        "classID": "02",
        "className": "Jazz Improvisation",
        "studentCount": "18",
        "playlistCount": "5"
    },
    {
        "classID": "03",
        "className": "Classical Compositions",
        "studentCount": "20",
        "playlistCount": "4"
    },
    {
        "classID": "04",
        "className": "Rock History 101",
        "studentCount": "25",
        "playlistCount": "6"
    },
    {
        "classID": "05",
        "className": "Electronic Music Production",
        "studentCount": "15",
        "playlistCount": "8"
    },
    {
        "classID": "06",
        "className": "Hip-Hop & Rap Culture",
        "studentCount": "22",
        "playlistCount": "7"
    },
    {
        "classID": "07",
        "className": "Orchestration Techniques",
        "studentCount": "14",
        "playlistCount": "3"
    },
    {
        "classID": "08",
        "className": "Film Score Analysis",
        "studentCount": "19",
        "playlistCount": "5"
    },
    {
        "classID": "09",
        "className": "Choir & Vocal Harmony",
        "studentCount": "30",
        "playlistCount": "6",
        "teacher": "John Johnson",
        "teacherEmail": "john.johnson@sru.edu"
    },
    {
        "classID": "10",
        "className": "Blues Guitar Essentials",
        "studentCount": "17",
        "playlistCount": "4",
        "teacher": "John Johnson",
        "teacherEmail": "john.johnson@sru.edu"
    },
    {
        "classID": "11",
        "className": "Music Business & Marketing",
        "studentCount": "21",
        "playlistCount": "9",
        "teacher": "John Johnson",
        "teacherEmail": "john.johnson@sru.edu"
    },
    {
        "classID": "12",
        "className": "Pop Songwriting",
        "studentCount": "16",
        "playlistCount": "5",
        "teacher": "John Johnson",
        "teacherEmail": "john.johnson@sru.edu"
    },
    {
        "classID": "13",
        "className": "Sound Engineering 101",
        "studentCount": "23",
        "playlistCount": "8",
        "teacher": "John Johnson",
        "teacherEmail": "john.johnson@sru.edu"
    },
    {
        "classID": "14",
        "className": "Folk & Acoustic Styles",
        "studentCount": "13",
        "playlistCount": "4",
        "teacher": "John Johnson",
        "teacherEmail": "john.johnson@sru.edu"
    },
    {
        "classID": "15",
        "className": "Reggae Rhythms & Culture",
        "studentCount": "20",
        "playlistCount": "5",
        "teacher": "John Johnson",
        "teacherEmail": "john.johnson@sru.edu"
    }
];

var classTable;

$(document).ready(function () {
    let classColumns = [
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
            <button class="btn btn-warning disable-btn">Disable</button>
            <button class ="btn btn-danger remove-btn">Remove</button>
        `},
    ];
    
    classTable = initializeDataTableWithFilters('#classTable', classData, classColumns, [4, 'asc']);

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

    $('#confirmClassBtn').on('click', function () {
        // Close the modal
        $('#addClassModal').modal('hide');
    });
})

