(() => {
   'use strict';

   const logger = new Logger('restricted/person/patient/responsible-for-list.js');
   class ResponsibleForList {
      constructor(patient) {
         // Attributes
         this.patient = patient;
         this.responsibleForDetails = null;
         this.table = {};
      }
      fromPatient() {
         return this.patient.responsibleFor ? this.patient.responsibleFor : "";
      }
      toPatient(personId) {
         this.patient.responsibleFor = personId;
      }
      prefixId() { return '#responsibleForList'; }
      newItemId() { return '#newTrResponsibleItem'; }
      uri() { return '/persons'; }
      queryString() {
         return "?f_person_ids={0}".format(this.fromPatient());
      }
      columns() {
         return  [
            { 'class': 'details-control', 'defaultContent': '', 'orderable': false, 'data': null  },
            { 'data': 'full_name', 'defaultContent': '', 'orderable': true },
            { 'className': "text-center", 'defaultContent': '', 'orderable': false }
         ];
      }
      columnDefs() {
         return [
            {
               targets: [ 0 ],
               width: "30px",
               createdCell: function (td, cellData, rowData, row, col) {
                  let html = "<input id='id_" +  row + col
                     + "' type='hidden' value='" + rowData.person_id.id
                     + "' class='id'/>"
                  $(td).html(html);
               }
            },
            {
               targets: [ 1 ],
               title:'<span>Nome</span>',
            },
            {
               targets: [ 2 ],
               width: "15px",
            },
         ];
      }
      newLine() {
         return '<tr id="newTrResponsibleItem" role="row"></tr>';
      }
      addNewLine(addNewLineCallback, executeAfterLoadCallback) {
         addNewLineCallback();
         resource.component(this.newItemId(),
            'restricted/person/patient/responsible-for-details')
            .done(() => {
               executeAfterLoadCallback();
            });
      }
      datatable(uri) {
         this.table = new Datatable(this.prefixId(),
            new DtConfig(uri, this.columns(), this.columnDefs(),
               this.initComplete));
         let table = this.table;
         let datatable = table.table();
         let detailRows = [];
         let patient = this.patient;
         let responsibleForList = this;
         let prefixId = this.prefixId();
         let newItemId = this.newItemId;
         $(prefixId + ' tbody').off('click').on(
            'click', 'tr td.details-control', function () {
               let tr = $(this).closest('tr');
               let row = datatable.row(tr);
               let dtTr = new DtTr(datatable, tr, newItemId());
               dtTr.toggleRow(
                  () => {
                     responsibleForList.addNewLine(
                        () => {
                           row.child($(responsibleForList.newLine())).show();
                        },
                        () => {
                           responsibleForDetails.initPage(dtTr, patient, tr.find('.id').val());
                        }
                     );
                  }
               );
            });
         datatable.on('draw', () => {
            $.each(detailRows, (i, id) => {
               $.trigger('#' + id + ' td.details-control');
            });
         });
         return datatable;
      }
      filterList() {
         $.click('#filterRF', (event) => {
            this.datatable(this.uri() + this.queryString());
         });
      }
      setupNewItem() {
         let responsibleForList = this;
         let patient = this.patient;
         let table = this.table;
         $.click(this.prefixId() + 'New', (event) => {
            $(responsibleForList.newItemId()).remove();
            this.addNewLine(() => {
               $(this.newItemId()).remove();
               $(this.prefixId() + ' tr:first').before(this.newLine());
            }, () => {
               responsibleForDetails.initPage(table, patient);
            });
         });
      }
      initPage() {
         logger.debug('Initialize page...');
         this.setupNewItem();
         this.filterList();
         $('#filterRF').hide();
         $.trigger('#filterRF');
      }
   }
   window.ResponsibleForList = ResponsibleForList;
})();
