(()=>{
   'use strict';

   const logger = new Logger('private/procedure/procedure-list.js');
   $(document).ready(() => {
      class ProcedureList {
         constructor() {
            // Component's IDs
            this.fcodeId = "#fcode";
            this.fdescriptionId = "#fdescription";
            // Attributes
            this.fcode = "";
            this.fdescription = "";
            this.table = {};
         }
         prefixId() { return '#procedureList'; }
         newItemId() { return '#newItem'; }
         uri() { return '/procedures'; }
         columns() {
            return  [
               { 'class': 'details-control', 'defaultContent': '', 'orderable': false, 'data': null  },
               { 'data': 'code', 'defaultContent': '', 'orderable': true },
               { 'data': 'description', 'defaultContent': '', 'orderable': true },
               //{ 'data': 'enabled', 'defaultContent': '', 'orderable': true },
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
                        + "' type='hidden' value='" + rowData.procedure_id.id
                        + "' class='id'/>"
                     $(td).html(html);
                  }
               },
               {
                  targets: [ 1 ],
                  width: "100px",
                  title:'<span>Código</span>',
               },
               {
                  targets: [ 2 ],
                  title:'<span>Descrição</span>',
               },
               {
                  targets: [ 3 ],
                  width: "15px",
               },
            ];
         }
         initComplete(settings, json) {
            let api = new $.fn.dataTable.Api(settings);
            let columns = [];
         }
         newLine() {
            return '<tr id="newItem" role="row"></tr>';
         }
         addNewLine(addNewLineCallback, executeAfterLoadCallback) {
            addNewLineCallback();
            resource.component(this.newItemId(),
               'private/procedure/procedure-details')
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
            let procedureList = this;
            let prefixId = this.prefixId();
            let newItemId = this.newItemId;
            $(prefixId + ' tbody').off('click').on(
               'click', 'tr td.details-control', function () {
               let tr = $(this).closest('tr');
               let row = datatable.row(tr);
               let dtTr = new DtTr(datatable, tr, newItemId());
               dtTr.toggleRow(
                  () => {
                     procedureList.addNewLine(
                        () => {
                           row.child($(procedureList.newLine())).show();
                        },
                        () => {
                           procedureDetails.initPage(dtTr, tr.find('.id').val());
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
            $.click('#filter', (event) => {
               this.datatable(this.uri() + this.queryString());
            });
            $.keyup(this.fdescriptionId, 4, () => {$.trigger('#filter');});
         }
         queryString() {
            this.fcode = this.getQueryAttribute($.inputText(this.fcodeId));
            this.fdescription = this.getQueryAttribute($.inputText(this.fdescriptionId));
            return "?f_code={0}&f_description={1}".format(
               this.fcode, this.fdescription);
         }
         getQueryAttribute(value) {
            return value ? value + '*' : '';
         }
         setupNewItem() {
            let procedureList = this;
            let table = this.table;
            $.click(this.prefixId() + 'New', (event) => {
               $(procedureList.newItemId()).remove();
                  this.addNewLine(() => {
                     $(this.newItemId()).remove();
                     $(this.prefixId() + ' tr:first').before(this.newLine());
                  }, () => {
                     procedureDetails.initPage(table);
                  });
            });
         }
         initPage() {
            logger.debug('Initialize page...');
            this.setupNewItem();
            this.filterList();
            $.trigger('#filter');
         }
      }
      new ProcedureList().initPage();
   });
})();
