(() => {
   'use strict';

   const logger = new Logger('private/procedure/responsible-for-details.js');
   class Responsible {
      constructor(patient, responsible) {
         // Component's IDs
         this.responsibleIdId = "#responsibleId";
         this.fullNameId = "#responsibleFullName";
         // Attributes
         this.responsibleId = '';
         this.fullName = '';
         // Validators
         this.responsibleIdV = new StringValidator(this.responsibleIdId, {
            len: { min: 26, max: 26, message: '01FWKTXVD619FBHY10QF033G7Y' }
         });
         if (responsible) {
            this.responsibleId = responsible[0].person_id.id;
            this.fullName = responsible[0].full_name;
         }
         this.patient = patient;
      }
      create() {
         let deferred = $.Deferred();
         let promise = deferred.promise();
         if(this.fromPage()) {
            this.patient.responsibleFor = this.responsibleId;
            deferred.resolve(true);
         } else {
            deferred.reject({
               'status': 499,
               'code':'01FVT3QG3N3PG1JPAG867MJ6XZ',
               'data':'Criação não realizada devido a falha na validação!'
            });
         }
         return promise;
      }
      static read(responsibleId) {
         let deferred = $.Deferred();
         let promise = deferred.promise();
         resource.get('/persons/' + responsibleId)
         .done((response) => {
            deferred.resolve(response.data);
         })
         .fail((error) => {
            toast.show(error);
            deferred.reject(null);
         });
         return promise;
      }
      delete() {
         this.patient.responsibleFor = null;
      }
      toPage(){
         $.inputText(this.responsibleIdId, this.responsibleId);
         $.inputText(this.fullNameId, this.fullName);
         $.attribute(this.fullNameId, 'disabled', true);
      }
      fromPage(){
         if(this.validate()) {
            this.responsibleId =  this.responsibleIdV.value;
            return true;
         }
         return false;
      }
      validate() {
         return this.responsibleIdV.validate();
      }
   }
   class ResponsibleForDetails {
      constructor() {
         this.patient = null;
         this.responsible = null;
         this.trId = '#newTrResponsibleItem';
         this.table = {};
      }
      initPage(table, patient, id) {
         logger.debug('Initialize page..., [Responsible For]', patient.responsibleFor);
         this.patient = patient;
         this.table = table;
         if (id) {
            Responsible.read(id)
            .done((responsible) => {
               this.responsible = new Responsible(patient, responsible);
               this.responsible.toPage();
            });
         } else {
            this.responsible = new Responsible(patient);
         }
         this.initTypehead();
         this.initCUDActions(id);
      }
      initCUDActions(id) {
          this.initSaveAction(id);
          this.initCancelAction(id);
          this.initDeleteAction(id);
      }
      initSaveAction(id) {
         if(id) {
            $('#saveRF').remove();
         } else {
            let trId = this.trId;
            $.click('#saveRF', (event) => {
               this.responsible.create();
               $(trId).remove();
               $.trigger('#filterRF');
            });
         }
      }
      initCancelAction(id) {
         let table = this.table;
         let trId = this.trId;
         $.click('#cancelRF', (event) => {
            if(id) {
               table.toggleRow();
            } else {
               $(trId).remove();
            }
         });
      }
      initDeleteAction(id) {
         let table = this.table;
         let trId = this.trId;
         $.click('#deleteRF', (event) => {
            if(id) {
               this.responsible.delete();
               table.toggleRow();
               $.trigger('#filterRF');
             } else {
               $(trId).remove();
             }
          });
      }
      initTypehead() {
         let responsible = this.responsible;
         let patient = this.patient;
         this.getTypeahead().on('typeahead:select', function(obj, datum, name) {
            if(!responsible.patient.responsibleFor) {
               if(patient.personId != datum.person_id) {
                  $.inputText(responsible.responsibleIdId, datum.person_id);
                  $('#saveRF').focus();
               } else {
                  alert('A pessoa não pode ser responsável por si mesma! Não adicionada!');// TODO FIX ME
               }
            } else {
               alert('Já existe um responsável! Não adicionado!');// TODO FIX ME
            }
         });
      }
      getDataSource() {
         return new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('full_name'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            limit: 10,
            remote: {
               url: http.getFullUrl('/persons?f_full_name=*%QUERY*'),
               wildcard: '%QUERY',
               transform: function(response) {
                  return $.map(response.data.results, function(patient) {
                     return {
                        person_id: patient.person_id.id,
                        full_name: patient.full_name
                     };
                  });
               },
            },
         });
      }
      getTypeahead() {
         let dataSource = this.getDataSource();
         return $('.typeahead').typeahead({
            highlight: true,
            minLength: 0,
         }, {
            source: dataSource,
            display: 'full_name',
            templates: {
               empty: [
                  '<div class="empty-message">',
                  'Não existem pacientes que possuam o nome pesquisado',
                  '</div>'
               ].join('\n'),
            }
         });
      }
   }
   window.responsibleForDetails = new ResponsibleForDetails();
})();
