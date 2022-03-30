(() => {
   'use strict';

   class DtConfig {
      constructor(uri, columns, columnDefs, initComplete) {
         this.uri = uri;
         this.columns = columns;
         this.columnDefs = !columnDefs ? [] : columnDefs;
         this.initComplete = !initComplete ? () => {} : initComplete;
      }
      config() {
         let initComplete = this.initComplete;
         return {
             // 'processing': true,
             // 'serverSide': true,
            'ajax': {
               'url': resource.getFullUrl(this.uri),
               'type': 'GET',
               'dataType': 'json',
               'dataSrc': 'data.results',
               'error': function(jqXHR, textStatus, errorThrown) {
                  toast.show({'status': 499 ,
                     'code': '01FVQ2NP5N57RMXBZDKHVT7ZGJ',
                     'data': '01FVQ2NP5N57RMXBZDKHVT7ZGJ'
                  });
               }
            },
            'columns': this.columns,
            'columnDefs': this.columnDefs,
            'initComplete': function(settings, json) {
               initComplete(settings, json);
            },
            'language': {
               'emptyTable': 'Tabela vazia',
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