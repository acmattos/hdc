(()=>{
   'use strict';

   const logger = new Logger('restricted/procedure/procedure-list.js');
   $(document).ready(() => {
      class ProcedureList {
         constructor() {
            // Component's IDs
            this.fcodeId = "#fcode";
            this.fdescriptionId = "#fdescription";
            this.pageNumberId = "#pageNumber"
            // Attributes
            this.codeFilter = "";
            this.descriptionFilter = "";
            this.pageNumber = 1;
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
         datatable(uri) {
            this.table = new Datatable(this.prefixId(),
               new DtConfig(uri, this.columns(), this.columnDefs(),
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
            $.click(this.prefixId() + 'New', (event) => {
            //$(this.prefixId() + 'New').off('click').on('click', (event) => {
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
         read() {
            let deferred = $.Deferred();
            let promise = deferred.promise();
            resource.get(':7000/procedures' + this.queryString())
            .done((response) => {
               //deferred.resolve(new Procedure(response.data));
            })
            .fail((error) => {
               toast.show(error);
               //deferred.reject(new Procedure());
            });
            return promise;
         }
         queryString() {
            this.fcode = $.inputText(this.fcodeId);
            this.fdescription = $.inputText(this.fdescriptionId);
            this.pageNumber = $.inputText(this.pageNumberId);
            logger.alert("?f_code={0}&f_description={1}&pn={2}".format(
               this.fcode, this.fdescription, this.pageNumber))
            return "?f_code={0}&f_description={1}&pn={2}".format(
               this.fcode, this.fdescription, this.pageNumber);
         }
         filterList() {
            $.click('#filter', (event) => {
               // this.read()
               // .done((response) => {
                  $.inputText(this.pageNumberId, this.pageNumber);
                  this.datatable(this.uri() + this.queryString());
                  // toast.show({
                  //    'status': response.status,
                  //    'code':'01FVT3QG3N9FE3F55670TQCPE3',
                  //    'data': message
                  // });
                  // $('#newItem').remove();
                  // $.trigger('#filter');
            //    })
            //    .fail((error) => {
            //       toast.show(error);
            //    });
            });
            $.trigger('#filter');
         }
         previousPage() {}
         nextPage() {
         }
         initPage() {
            this.filterList();
            // this.datatable(this.uri() + this.queryString());
            this.setupNewProcedure();
         }
      }
      const list = new ProcedureList();
      list.initPage();
   });
})();
