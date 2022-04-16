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
            'processing': true,
            'serverSide': true,
            'stateSave': true,
            'ajax': {
               'url': resource.getFullUrl(this.uri),
               'type': 'GET',
               'dataType': 'json',
               'dataFilter': function(data) {
                  let json = jQuery.parseJSON( data );
                  json.recordsTotal = json.data.total;
                  json.recordsFiltered = json.data.total;
                  json.data = json.data.results;
                  return JSON.stringify(json);
               },
               'data': function(data, settings) {
                  let api = new $.fn.dataTable.Api(settings);
                  data.pn = Math.min(
                     Math.max(0, Math.round(data.start / api.page.len())),
                     api.page.info().pages
                  ) + 1 ;
                  data.ps = api.page.len();
                  $.inputText ('#pageSize', data.ps);
                  $.inputText('#pageNumber', data.pn);
               },
               'error': function(jqXHR, textStatus, errorThrown) {
                  toast.show({'status': 499 ,
                     'code': '01FVQ2NP5N57RMXBZDKHVT7ZGJ',
                     'data': '01FVQ2NP5N57RMXBZDKHVT7ZGJ'
                  });
               }
            },
            'lengthMenu': [[2,10, 25, 50, -1], [2,10, 25, 50, "Todos"]],
            'columns': this.columns,
            'columnDefs': this.columnDefs,
            'initComplete': function(settings, json) {
               initComplete(settings, json);
            },
            'language': {
               'processing':     'Carregando...',
               'search'    :     'Busca',
               'lengthMenu':     'Mostrar _MENU_ registros',
               'info'      :     'Exibindo _START_ a _END_ de _TOTAL_ resultados',
               'infoEmpty' :     'Exibindo 0 a 0 de 0 resultados',
               'loadingRecords': 'Carregando registros...',
               'emptyTable':     'Sem resultados',
               'zeroRecords':    'Nenhum resultado encontrado',
               'paginate': {
                   'first':      'Primeiro',
                   'previous':   'Prév.',
                   'next':       'Próx.',
                   'last':       'Último'
                },
            },
            'error':          'Erro na leitura!',
            'order':          [],
            'scrollY':        '72vh',
            'deferRender':    true,
            'paging':         true,
            'searching':      false,
            'scrollCollapse': true,
            'filter':         true,
            'info':           true,
            'lengthChange':   true
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
         let component = $(this.component);
         if($.fn.dataTable.isDataTable(component)){
            component.DataTable().destroy();
            component.find("tbody tr:gt(0)").remove();
         }

         return component.DataTable(this.dtConfig.config());
      }
   }
   window.Datatable = Datatable;
})();
