(() => {
   'use strict';

   const logger = new Logger('private/role/role-details.js');
   class Role {
      constructor(role) {
         // Component's IDs
         this.roleIdId = "#roleId";
         this.nameId = "#name";
         this.descriptionId = "#description";
         this.enabledId = "#enabled";
         this.createdAtId = "#createdAt";
         this.updatedAtId = "#updatedAt";
         this.deletedAtId = "#deletedAt";
         // Attributes
         this.roleId = '';
         this.name = '';
         this.description = '';
         this.enabled = true;
         this.createdAt = "";
         this.updatedAt = "";
         this.deletedAt = "";
         // Validators
         this.nameV = new StringValidator(this.nameId, {
            space: { inner: "N", message: '' },
            len: { min: 3, max: 30, message: '' }
         });
         this.descriptionV = new StringValidator(this.descriptionId, {
            len: { min: 3, max: 120, message: '' }
         });
         if (role && Array.isArray(role)) {
            this.roleId = role[0].role_id.id;
            this.name = role[0].name;
            this.description = role[0].description;
            this.enabled = role[0].enabled;
            this.createdAt = role[0].created_at;
            this.updatedAt = role[0].updated_at;
            this.deletedAt = role[0].deleted_at;
         }  else {
            this.enabled = true;
         }
      }
      createRequest() {
         let role = this;
         let request = {
            'name': role.name,
            'description': role.description
         };
         return request;
      }
      create() {
         let deferred = $.Deferred();
         let promise = deferred.promise();
         if(this.fromPage()) {
            let request = this.createRequest();
            resource.post('/roles', request)
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
      static read(roleId) {
         let deferred = $.Deferred();
         let promise = deferred.promise();
         resource.get('/roles/' + roleId)
         .done((response) => {logger.delete(response)
            deferred.resolve(new Role(response.data));
         })
         .fail((error) => {
            toast.show(error);
            deferred.reject(new Role());
         });
         return promise;
      }
      update() {
         let deferred = $.Deferred();
         let promise = deferred.promise();
         if (this.fromPage()) {
            let role = this;
            let request = this.createRequest();
            request.role_id = role.roleId;
            request.enabled = role.enabled ? true : false;
            resource.put('/roles', request)
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
         resource.delete('/roles/' + this.roleId)
         .done((response) => {
            deferred.resolve(response);
         })
         .fail((error) => {
            deferred.reject(error);
         });
         return promise;
      }
      toPage(){
         $.inputText(this.roleIdId, this.roleId);
         $.inputText(this.nameId, this.name);
         $.inputText(this.descriptionId, this.description);
         $.checkBox(this.enabledId, this.enabled);
      }
      fromPage(){
         if(this.validate()) {
            this.roleId = $.inputText(this.roleIdId);
            this.name = this.nameV.value;
            this.description = this.descriptionV.value;
            this.enabled = $.checkBox(this.enabledId);
            return true;
         }
         return false;
      }
      validate() {
         return this.nameV.validate() && this.descriptionV.validate();
      }
   }
   class RoleDetails {
      constructor() {
         this.role = null;
         this.trId = '#newItem';
         this.table = {};
      }
      initPage(table, id) {
         logger.debug('Initialize page..., [id]', id);
         this.table = table;
         if (id) {
            Role.read(id)
            .done((role) => {
               this.role = role;
               role.toPage();
            });
         } else {
            this.role = new Role();
         }
         this.initCUDActions(id);
      }
      initCUDActions(id) {
         this.initSaveAction(id);
         this.initCancelAction(id);
         this.initDeleteAction(id);
         this.initInfoButton();
      }
      initSaveAction(id) {
         $.click('#save', (event) => {
            let promise = null;
            let message = null;
            if(!id) {
               promise = this.role.create();
               message = 'Papel Criado!';
            } else {
               promise = this.role.update();
               message = 'Papel Atualizado!';
            }
            promise
            .done((response) => {
               toast.show({
                  'status': response.status,
                  'code':'01FVT3QG3N9FE3F55670TQCPE3',
                  'data': message
               });
               $('#newItem').remove();
               $.trigger('#filter');
            })
            .fail((error) => {
               toast.show(error);
            });
         });
      }
      initCancelAction(id) {
         let table = this.table;
         let trId = this.trId;
         $.click('#cancel', (event) => {
            if(id) {
               table.toggleRow();
            } else {
               $(trId).remove();
            }
         });
      }
      initDeleteAction(id) {
         $.click('#delete', (event) => {
            let promise = null;
            let message = null;
            if(!id) {
               $('#newItem').remove();
            } else {
               promise = this.role.delete();
               message = 'Papel Excluído!';
            }
            promise
            .done((response) => {
               toast.show({
                  'status': response.status,
                  'code':'01FVT3QG3N9FE3F55670TQCPE3',
                  'data': message
               });
               $.trigger('#filter');
            })
            .fail((error) => {
                toast.show(error);
            });
         });
      }
      initInfoButton() {
         $.click('#info', (event) => {
            infoDetails.initPage(Info2Map.build(this.role).toMap());
         });
      }
      /*
      initTypehead() {
         let familyMember = this.familyMember;
         let patient = this.patient;
         this.getTypeahead().on('typeahead:select', function(obj, datum, name) {
            if(!familyMember.patient.familyGroup.includes(datum.person_id)) {
               if(patient.personId != datum.person_id) {
                  $.inputText(familyMember.memberIdId, datum.person_id);
                  $('#saveFM').focus();
               } else {
                  alert('A pessoa não pode ser um membro familiar de si mesma! Não adicionada!');// TODO FIX ME
               }
            } else {
               alert('A pessoa já é um membro familiar! Não adicionada!');// TODO FIX ME
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
      */
   }
   window.roleDetails = new RoleDetails();
})();
