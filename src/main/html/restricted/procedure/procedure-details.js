(() => {
   'use strict';

   const logger = new Logger('restricted/procedure/procedure-details.js');
   class Procedure {
      constructor() {
         // Component's IDs
         this.procedureIdId = "#procedureId";
         this.codeId = "#code";
         this.descriptionId = "#description";
         this.enabledId = "#enabled";
         // Attributes
         this.procedureId = '';
         this.code = '';
         this.description = '';
         this.enabled = '';
         // Validators
         this.codeV = new IntValidator(this.codeId, {
            between: { min: 81000014, max: 87000199, message: '01FVQ2NP5PKKHVWHP57PKD8N62' }
         });
         this.descriptionV = new StringValidator(this.descriptionId, {
            len: { min: 3, max: 120, message: '01FVT3QG3MQ2MJX8PESM4KVKSP' }
         });
         if (Array.isArray(arguments[0])) {logger.delete(arguments[0][0].procedure_id.id)
            this.procedureId = arguments[0][0].procedure_id.id;
            this.code = arguments[0][0].code;
            this.description = arguments[0][0].description;
            this.enabled = arguments[0][0].enabled;
         }
      }
      create() {
         let deferred = $.Deferred();
         let promise = deferred.promise();
         if(this.fromPage()) {
            let procedure = this;
            let request = {
               'code': procedure.code,
               'description': procedure.description
            };
            resource.post(':7000/procedures', request)
            .done((response) => {
               deferred.resolve(response);
            })
            .fail((error) => {
               deferred.reject(error);
            });
         } else {
            deferred.reject({
               'status': 499,
               'code':'01FVT3QG3N3PG1JPAG867MJ6XZ',
               'data':'Criação não realizada devido a falha na validação!'
            });
         }
         return promise;
      }
      static read(procedureId) {
         let deferred = $.Deferred();
         let promise = deferred.promise();
         resource.get(':7000/procedures/' + procedureId)
         .done((response) => {
            deferred.resolve(new Procedure(response.data));
         })
         .fail((error) => {
            toast.show(error);
            deferred.reject(new Procedure());
         });
         return promise;
      }
      update() {
         let deferred = $.Deferred();
         let promise = deferred.promise();
         if (this.fromPage()) {
            let procedure = this;
            let request = {
               'procedure_id': procedure.procedureId,
               'code': procedure.code,
               'description': procedure.description,
               'enabled': procedure.enabled ? true : true// TODO ADJUST
            };
            resource.put(':7000/procedures', request)
            .done((response) => {
               deferred.resolve(response);
            })
            .fail((error) => {
               deferred.reject(error);
            });
         } else {
            deferred.reject({
               'status': 499,
               'code':'01FVT3QG3N3PG1JPAG867MJ6XZ',
               'data':'Atualização não realizada devido a falha na validação!'
            });
         }
         return promise;
      }
      delete() {
         let deferred = $.Deferred();
         let promise = deferred.promise();
         resource.delete(':7000/procedures/' + this.procedureId)
         .done((response) => {
            deferred.resolve(response);
         })
         .fail((error) => {
            deferred.reject(error);
         });
         return promise;
      }
      toPage(){
         $.inputText(this.procedureIdId, this.procedureId);
         $.inputText(this.codeId, this.code);
         $.inputText(this.descriptionId, this.description);
         $.checkBox(this.enabledId, this.enabled);
      }
      fromPage(){
         if(this.validate()) {
            this.procedureId = $.inputText(this.procedureIdId);
            this.code = this.codeV.value;
            this.description = this.descriptionV.value;
            this.enabled = $.checkBox(this.enabledId);
            return true;
         }
         return false;
      }
      validate() {
         return this.codeV.validate() && this.descriptionV.validate();
      }
   }

   class ProcedureDetails {
      constructor() {
         this.procedure = null;
      }
      initPage(id) {
         if (id) {
            Procedure.read(id)
            .done((procedure) => {
               this.procedure = procedure;
               procedure.toPage();
            });
         } else {
            this.procedure = new Procedure();
         }
         this.initCUDActions(id);
      }
      initCUDActions(id) {
         this.initSaveAction(id);
         this.initCancelAction(id);
         this.initDeleteAction(id);
      }
      initSaveAction(id) {
         $('#save').off('click').on('click', (event) => {
            let promise = null;
            let message = null;
            if(!id) {
               promise = this.procedure.create();
               message = 'Procedimento Criado!';
            } else {
               promise = this.procedure.update();
               message = 'Procedimento Atualizado!';
            }
            promise
            .done((response) => {
               toast.show({
                  'status': response.status,
                  'code':'01FVT3QG3N9FE3F55670TQCPE3',
                  'data': message
               });
               $('#newItem').remove();
               //$('#filtrar').trigger('click');TODO RELOAD
            })
            .fail((error) => {
               toast.show(error);
            });
         });
      }
      initCancelAction(id) {
         $('#cancel').off('click').on('click', (event) => {
            $('#newItem').remove();
            //clicou = false;
         });
      }
      initDeleteAction(id) {
         $('#delete').off('click').on('click', (event) => {alert()
            event.preventDefault();

            //clicou = false;
            let promise = null;
            let message = null;
            if(!id) {alert('JUST REMOVE LINE' + id)
               $('#newItem').remove();
            } else { alert('JUST DELETE ' + id)
               promise = this.procedure.delete();
               message = 'Procedimento Excluído!';
            }
            promise
            .done((response) => {
               toast.show({
                  'status': response.status,
                  'code':'01FVT3QG3N9FE3F55670TQCPE3',
                  'data': message
               });
               //$('#filtrar').trigger('click');TODO RELOAD
            })
            .fail((error) => {
                toast.show(error);
            });
         });
      }
   }
   window.procedureDetails = new ProcedureDetails();
})();
