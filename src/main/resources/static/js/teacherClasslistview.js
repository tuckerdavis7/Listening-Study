const pathArr = location.pathname.split('/').filter(p => p.length > 0);
const userType = pathArr[pathArr.length - 3];
const classID = pathArr[pathArr.length - 1];

var studentTable;

$(document).ready(function () {
  let studentColumns = [            
    { data: "ID", class: "charcolumn", width: "2 rem" },
    { data: "Firstname", class: "charcolumn", width: "3 rem" },
    { data: "LastName", class: "charcolumn", width: "3 rem" },
    { data: "Email", class: "charcolumn", width: "1 rem" },
];

  $.ajax({
    url: `http://localhost:8080/api/teacherRoster?classID=${classID}`,
    type: 'GET',
    dataType: 'json',
    success: function (data) {
        if (!studentTable)
            studentTable = initializeDataTableWithFilters('#studentTable', data.data, studentColumns, [0, 'asc'], 10);
    },
    error: function (xhr, status, error) {
        console.error("Error fetching students:", error);
    }
  });
})
