(() => {
   'use strict';

   const logger = new Logger('restricted/person/patient/patient-list.js');
   $(document).ready(() => {
      class PersonList {
         constructor() {
            this.table = 'NONE';
         }
         tableId() { return '#patientList';  }
         uri() { return ':7000/persons';  }
         columns() {
            return  [
               { 'class': 'details-control', 'orderable': false, 'data': null, 'defaultContent': ''  },
               { 'data': 'full_name', 'defaultContent': '', 'orderable': true },
               { 'data': 'personal_id', 'defaultContent': '', 'orderable': true },
               { 'data': 'cpf', 'defaultContent': '', 'orderable': true},
               { 'className': "text-center", 'defaultContent': '', 'orderable': false }
            ];
         }
         columnDefs() {
            return [
               {
                  targets: [ 0 ],
                  createdCell: function (td, cellData, rowData, row, col) {
                     let html = "<input id='id_" +  row + col
                        + "' type='hidden' value='" + rowData.person_id.id
                        + "' class='id'/>"
                     $(td).html(html);
                  }
               },
               {
                  targets: [ 3 ],
                  createdCell: function (td, cellData, rowData, row, col) {
                     let html = rowData.contacts[0].info;
                     $(td).html(html);
                  }
               },
            ];
         }
         datatableConfig() {
            return new DtConfig(this.uri(), this.columns(), this.columnDefs());
         }
         datatable() {
            this.table = new Datatable(this.tableId(), this.datatableConfig()).table();
            let table = this.table;
            // Array to track the ids of the details displayed rows
            var detailRows = [];
            $('#patientList tbody').off('click').on( 'click', 'tr td.details-control', function () {
               var tr = $(this).closest('tr');
               var row = table.row( tr );
               var idx = $.inArray( tr.attr('id'), detailRows );

               if ( row.child.isShown() ) {
                  row.child.remove();
                  tr.removeClass('shown');
                  tr.removeClass('selected');
               } else {
                  $(this).prop('id', 'ID');
                  if ( table.row('.shown').length ) {
                     $('#patientDetailsDiv').parent().parent().remove();
                     $('.shown').find('.details-control').click();
                  }
                  row.child('<div id="patientDetailsDiv" class="container-fluid border"></div>').show();
                  resource.component('#patientDetailsDiv','restricted/person/patient/patient-details').done(() => {
                     patientDetails.initPage(tr.find('.id').val());
                  });
                  tr.addClass('shown');
                  tr.addClass('selected');
               }
            } );
            // On each draw, loop over the `detailRows` array and show any child rows
            this.table.on('draw', () => {
               $.each(detailRows, (i, id) => {
                  $.trigger('#'+id+' td.details-control');
               });
            });
            //    createdRow: function( row, data, dataIndex) {
            //       $(row).attr('data-toggle', 'modal');
            //       $(row).attr('data-target', '#modalEditar');
            //       $(row).attr('onclick', '_configuraModalEditaCheckList("'
            //          + data.id +'")');
            //    },
            // });
            // });
            return this.table;
         }
         initPage() {
            this.datatable();
         }
      }
      const personList = new PersonList();
      personList.initPage();
   });
})();