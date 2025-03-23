let students = [
    {
      "studentId": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com"
    },
    {
      "studentId": 2,
      "firstName": "Jane",
      "lastName": "Smith",
      "email": "jane.smith@example.com"
    },
    {
      "studentId": 3,
      "firstName": "Alice",
      "lastName": "Johnson",
      "email": "alice.johnson@example.com"
    },
    {
      "studentId": 4,
      "firstName": "Bob",
      "lastName": "Brown",
      "email": "bob.brown@example.com"
    },
    {
      "studentId": 5,
      "firstName": "Charlie",
      "lastName": "Davis",
      "email": "charlie.davis@example.com"
    }
  ]

  var studentTable;

$(document).ready(function () {
  let studentColumns = [            
    { data: "studentId", class: "charcolumn", width: "2 rem"},
    { data: "firstName", class: "charcolumn", width: "3 rem"},
    { data: "lastName", class: "charcolumn", width: "3 rem"},
    { data: "email", class: "charcolumn", width: "1 rem"},
]

  studentTable = initializeDataTableWithFilters('#studentTable', students, studentColumns, [0, 'asc'], 10, []);
})