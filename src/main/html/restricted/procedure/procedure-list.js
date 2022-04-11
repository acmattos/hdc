(()=>{
   'use strict';

   const logger = new Logger('restricted/procedure/procedure-list.js');
   $(document).ready(() => {
      class ProcedureList {
         constructor() {
            this.table = {};
         }
         prefixId() { return '#procedureList'; }
         newItemId() { return '#newItem'; }
         uri() { return ':7000/procedures'; }
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
               // {
               //    targets: [ 3 ],
               //    title:'<span>Habilitado</span>',
               // },
               {
                  targets: [ 3 ],
                  width: "15px",
               },
            ];
         }
         initComplete(settings, json) {
            logger.delete(settings, json)
            let api = new $.fn.dataTable.Api(settings);
            let columns = [];
            // var span = '';
            // colunas.push(3);
            // if(data.ordemServicoSituacao === 'ATRASADA'){
            //    colunas.push(5);
            // } else if(data.ordemServicoSituacao === 'ABERTA') {
            //    colunas.push(5);
            // } else if(data.ordemServicoSituacao === 'ANDAMENTO') {
            //    colunas.pop();
            //    span = '<span i18n="Data Andamento">Data Andamento</span>';
            // } else if(data.ordemServicoSituacao === 'EXECUTADA') {
            //    span = '<span i18n="Data Fechamento">Data Fechamento</span>';
            // } else if(data.ordemServicoSituacao === 'REPROGRAMADA') {
            //    span =
            //       '<span i18n="Data Reprogramação">Data Reprogramação</span>';
            // } else if(data.ordemServicoSituacao === 'CANCELADA') {
            //    span =
            //       '<span i18n="Data Cancelamento">Data Cancelamento</span>';
            // }
            // $('.tp').tooltip();
            // api.columns(colunas).visible(false);
            // $(componente.DataTable().columns(5).header()).empty().append(span);
         }
         newLine() {
            return '<tr id="newItem" role="row"></tr>';
         }
         addNewLine(addNewLineCallback, executeAfterLoadCallback) {
            addNewLineCallback();
            resource.component(this.newItemId(),
               'restricted/procedure/procedure-details')
            .done(() => {
               executeAfterLoadCallback();
            });
         }
         datatable() {
            this.table = new Datatable(this.prefixId(),
               new DtConfig(this.uri(), this.columns(), this.columnDefs(),
                  this.initComplete)).table();
            let table = this.table;
            let detailRows = [];
            let procedureList = this;
            let prefixId = this.prefixId();
            $(prefixId + ' tbody').off('click').on(
               'click', 'tr td.details-control', function () {
               let tr = $(this).closest('tr');
               let row = table.row(tr);
               if (row.child.isShown()) {
                  row.child.remove();
                  tr.removeClass('shown');
                  tr.removeClass('selected');
               } else {
                  if (table.row('.shown').length) {
                     $('#newItem').remove();
                     $('.shown').find('.details-control').click();
                  }
                  procedureList.addNewLine(() => {
                     row.child($(procedureList.newLine())).show();
                  }, () => {
                     procedureDetails.initPage(tr.find('.id').val());
                  });
                  tr.addClass('shown');
                  tr.addClass('selected');
               }
            });
            this.table.on('draw', () => {
               $.each(detailRows, (i, id) => {
                  $.trigger('#' + id + ' td.details-control');
               });
            });
            return this.table;
         }
         setupNewProcedure() {
            let clicou = false;
            let procedureList = this;
            $(this.prefixId() + 'New').off('click').on('click', (event) => {
               $(procedureList.newItemId()).remove();
               //tr.removeClass('shown');
               //if (!clicou) {
                  this.addNewLine(() => {
                     $(this.prefixId() + ' tr:first').before(this.newLine());
                  }, () => {
                     procedureDetails.initPage();
                     // $('#save').off('click').on('click', (e) => {alert('SALVAR')
                     //    var payload = _montaUnidadeVO();
                     //    payload['unidadeTO']['nome'] = $('#nome').val();
                     //    payload['unidadeTO']['prefixoOS'] =
                     //       $('#prefixo').val();
                     //    payload['unidadeTO']['setorOperacionalTO'] =
                     //       JSON.parse($('.setor option:selected').val());
                     //    payload['unidadeTO']['status'] = true;
                     //
                     //    if(_validaCampos(payload)){
                     //       httpClient(
                     //          '/unidade/criar',
                     //          'POST',
                     //          'text',
                     //          payload,
                     //          (data, textStatus, jqXHR) => {
                     //             $('#newItem').remove();
                     //             clicou = false;
                     //             return exibeModalSucesso(data,() => {
                     //                $('#filtrar').trigger('click');
                     //             });
                     //          },
                     //          fail,
                     //          always
                     //       );
                     //    }
                     // });
                     // $('#cancel').off('click').click( (e) => {
                     //    $('#newItem').remove();
                     //    clicou = false;
                     // });
                     // $('#delete').off('click').click( (e) => {
                     //    e.preventDefault();
                     //    $('#newItem').remove();
                     //    clicou = false;
                     // });
                     // clicou = true;
                  });
               //}
            });
         }
         initPage() {
            this.datatable();
            this.setupNewProcedure();
         }
      }
      const list = new ProcedureList();
      list.initPage();
   });
})();
