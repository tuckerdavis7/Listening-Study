
// let classData = [
//     {
//         "classID": "01",
//         "className": "Music Theory Basics",
//         "studentCount": "12",
//         "playlistCount": "3"
//     },
//     {
//         "classID": "02",
//         "className": "Jazz Improvisation",
//         "studentCount": "18",
//         "playlistCount": "5"
//     },
//     {
//         "classID": "03",
//         "className": "Classical Compositions",
//         "studentCount": "20",
//         "playlistCount": "4"
//     },
//     {
//         "classID": "04",
//         "className": "Rock History 101",
//         "studentCount": "25",
//         "playlistCount": "6"
//     },
//     {
//         "classID": "05",
//         "className": "Electronic Music Production",
//         "studentCount": "15",
//         "playlistCount": "8"
//     },
//     {
//         "classID": "06",
//         "className": "Hip-Hop & Rap Culture",
//         "studentCount": "22",
//         "playlistCount": "7"
//     },
//     {
//         "classID": "07",
//         "className": "Orchestration Techniques",
//         "studentCount": "14",
//         "playlistCount": "3"
//     }
// ];

var classTable;

$(document).ready(function () {
    

    let classColumns = [
        {
            class: "viewColumn",
            data: null,
            render: function(data, type, row, meta) {
                var classID = row.class_id;
                var dropdown = `
                    <div class="dropdown show">
                        <a class="btn-sm btn btn-info" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span class="fa fa-eye" aria-hidden="true"></span>
                        </a>
                        <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                            <a class="dropdown-item" href="./classlist/${classID}" data-rowindex="${meta.row}">View Class Students</a>
                            <a class="dropdown-item" href="./playlists" data-rowindex="${meta.row}">View Your Playlists</a>
                        </div>
                    </div>`;
                return dropdown;
            },
            orderable: false,
            width: "1 em"
        },
        { data: "class_id", class: "charcolumn", width: "2 rem"},
        { data: "classname", class: "charcolumn", width: "3 rem"},
        { data: "students_count", class: "charcolumn", width: "3 rem"},
        { data: "playlist_count", class: "charcolumn", width: "1 rem"},
    ]

    $.ajax({
        url: `http://localhost:8080/api/teacherClasslist`,
        type: 'GET',
        dataType: 'json',
        success: function (data) {                        
            
            if (!classTable)
                classTable = initializeDataTableWithFilters('#classTable', data.data, classColumns, [1, 'asc'], 10);
        },
        error: function (xhr, status, error) {
            console.error("Error fetching data from the API:", error);
        }
    });
})