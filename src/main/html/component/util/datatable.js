(() => {
   'use strict';

   class DtConfig {
      constructor(uri, columns, columnDefs) {
         this.uri = uri;
         this.columns = columns;
         this.columnDefs = !columnDefs ? [] : columnDefs;
      }
      config() {
         return {
            'processing': true,
            'serverSide': true,
            "processing": true,
            'ajax': {
               'url': resource.getFullUrl(this.uri),
               'type': 'GET',
               'dataType': 'json',
               'dataSrc': 'data.results',
               'error': function(jqXHR, textStatus, errorThrown){ toast.show(jqXHR.responseJSON)}
            },
            'columns': this.columns,
            'columnDefs': this.columnDefs,
            'language': {
               'emptyTable': 'Nenhum resultado encontrado',
               'zeroRecords': 'Nenhum resultado encontrado',
               'processing': 'Carregando...'//TODO USE loader
            },
            'error': 'Erro na leitura!',
            'order':          [],
            'scrollY':        '72vh',
            'deferRender':    true,
            'paging':         true,
            'scrollCollapse': true,
            'filter':         false,
            'info':           false,
            'lengthChange':   false
         }
      }
   }
   window.DtConfig = DtConfig;
   class Datatable {
      constructor(component, dtConfig) {
         this.component = component;
         this.dtConfig = dtConfig;
      }
      table() {
         return $(this.component).DataTable(this.dtConfig.config());
      }
   }
   window.Datatable = Datatable;
})();