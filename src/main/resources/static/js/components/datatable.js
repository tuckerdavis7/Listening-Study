function initializeDataTableWithFilters(tableSelector, data, columns, orderColumn, pageLength, ignoredColumns=[0]) {
    let $table = $(tableSelector);
    let $thead = $table.find('thead');
    
    if ($thead.find('tr.filters').length === 0) {
        let filterRow = $('<tr class="filters"></tr>');
        
        $thead.find('tr:first-child th').each(function(index) {
            let title = $(this).text().trim();
            
            if ($(this).hasClass('no-filter') || ignoredColumns.includes(index)) {
                filterRow.append('<th></th>');
            }
            else {
                filterRow.append(`<th><input type="text" class="form-control form-control-sm" placeholder="Filter ${title}" /></th>`);
            }
        });
        
        $thead.append(filterRow);
        
        $thead.find('tr.filters th').addClass('sorting_disabled');
    }
    
    let dataTable = $table.DataTable({
        data: data,
        dom: "<'row'<'col-sm-12 col-md-12 text-end'B>>" +
            "<'row'<'col-sm-12'tr>>" +
            "<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7'p>><'#bottomLink'>",
        filter: true,
        info: false,
        lengthChange: false,
        orderCellsTop: true,
        columns: columns,
        pageLength: pageLength,
        initComplete: function() {
            let api = this.api();
            
            //add column with filter
            $thead.find('tr.filters th').each(function(i) {
                let $input = $(this).find('input');
                if ($input.length) {
                    $input.on('keyup change', function() {
                        api.column(i).search(this.value).draw();
                    });
                }
            });
            
            //force redraw after a delay to ensure everything is properly sized
            setTimeout(function() {
                api.columns.adjust().draw();
            }, 100);
        }
    });
    
    if (orderColumn) {
        dataTable.order([orderColumn]).draw();
    }
    
    return dataTable;
}