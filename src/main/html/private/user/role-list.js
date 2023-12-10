(()=>{
   'use strict';

   const logger = new Logger('private/role/role-list.js');
   $(document).ready(() => {
      class RoleList {
         constructor() {
            // Component's IDs
            this.fnameId = "#fname";
            this.fdescriptionId = "#fdescription";
            // Attributes
            this.fname = "";
            this.fdescription = "";
            this.table = {};
         }
         prefixId() { return '#roleList'; }
         newItemId() { return '#newItem'; }
         uri() { return '/roles'; }
         columns() {
            return  [
               { 'class': 'details-control', 'defaultContent': '', 'orderable': false, 'data': null  },
               { 'data': 'name', 'defaultContent': '', 'orderable': true },
               { 'data': 'description', 'defaultContent': '', 'orderable': true },
               { 'data': 'enabled', 'defaultContent': '', 'orderable': true },
               { 'className': "text-center", 'defaultContent': '', 'orderable': false }
            ];
         }
         columnDefs() {
            return [
               {
                  targets: [ 0 ],
                  width: "45px",
                  createdCell: function (td, cellData, rowData, row, col) {
                     let html = "<input id='id_" +  row + col
                        + "' type='hidden' value='" + rowData.role_id.id
                        + "' class='id'/>"
                     $(td).html(html);
                  }
               },
               {
                  targets: [ 1 ],
                  width: "100px",
                  title:'<span>Nome</span>',
               },
               {
                  targets: [ 2 ],
                  title:'<span>Descrição</span>',
               },
               {
                  targets: [ 3 ],
                  title:'<span>Ativo</span>',
                  createdCell: function (td, cellData, rowData, row, col) {
                     $(td).html(cellData ? 'Ativo' : 'Inativo');
                  }
               },
               {
                  targets: [ 4 ],
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
            $.when(
               resource.component(this.newItemId(), 'private/user/role-details'),
               resource.script('component/info/info-details')
            )
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
            let roleList = this;
            let prefixId = this.prefixId();
            let newItemId = this.newItemId;
            $(prefixId + ' tbody').off('click').on(
               'click', 'tr td.details-control', function () {
               let tr = $(this).closest('tr');
               let row = datatable.row(tr);
               let dtTr = new DtTr(datatable, tr, newItemId());
               dtTr.toggleRow(
                  () => {
                     roleList.addNewLine(
                        () => {
                           row.child($(roleList.newLine())).show();
                        },
                        () => {
                           roleDetails.initPage(dtTr, tr.find('.id').val());
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
            $.keyup(this.fnameId, 3, () => {$.trigger('#filter');});
            $.keyup(this.fdescriptionId, 4, () => {$.trigger('#filter');});
         }
         queryString() {
            this.fname = this.getQueryAttribute($.inputText(this.fnameId));
            this.fdescription = this.getQueryAttribute($.inputText(this.fdescriptionId));
            return "?f_name={0}&f_description={1}".format(
               this.fname, this.fdescription);
         }
         getQueryAttribute(value) {
            return value ? value + '*' : '';
         }
         setupNewItem() {
            let roleList = this;
            let table = this.table;
            $.click(this.prefixId() + 'New', (event) => {
               $(roleList.newItemId()).remove();
                  this.addNewLine(() => {
                     $(this.newItemId()).remove();
                     $(this.prefixId() + ' tr:first').before(this.newLine());
                  }, () => {
                     roleDetails.initPage(table);
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
      new RoleList().initPage();
   });
})();
