(() => {
   'use strict';

   const logger = new Logger('restricted/procedure/family-group-details.js');
   class FamilyMember {
      constructor(patient, familyMember) {
         // Component's IDs
         this.memberIdId = "#memberId";
         this.fullNameId = "#memberFullName";
         // Attributes
         this.memberId = '';
         this.fullName = '';
         // Validators
         this.memberIdV = new StringValidator(this.memberIdId, {
            len: { min: 26, max: 26, message: '01FWKTXVD63PG1JPAG867MJ6XZ' }
         });
         if (familyMember) {
            this.memberId = familyMember[0].person_id.id;
            this.fullName = familyMember[0].full_name;
         }
         this.patient = patient;
      }
      create() {
         let deferred = $.Deferred();
         let promise = deferred.promise();
         if(this.fromPage()) {
            this.patient.familyGroup.push(this.memberId);
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
      static read(memberId) {
         let deferred = $.Deferred();
         let promise = deferred.promise();
         resource.get(':7000/persons/' + memberId)
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
         this.patient.familyGroup.splice(
            this.patient.familyGroup.indexOf(this.memberId), 1);
      }
      toPage(){
         $.inputText(this.memberIdId, this.memberId);
         $.inputText(this.fullNameId, this.fullName);
         $.attribute(this.fullNameId, 'disabled', true);
      }
      fromPage(){
         if(this.validate()) {
            this.memberId =  this.memberIdV.value;
            return true;
         }
         return false;
      }
      validate() {
         return this.memberIdV.validate();
      }
   }
   class FamilyGroupDetails {
      constructor() {
         this.patient = null;
         this.familyMember = null;
         this.trId = '#newTrFamilyItem';
         this.table = {};
      }
      initPage(table, patient, id) {
         logger.debug('Initialize page..., [Family Group]', patient.familyGroup);
         this.patient = patient;
         this.table = table;
         if (id) {
            FamilyMember.read(id)
            .done((member) => {
               this.familyMember = new FamilyMember(patient, member);
               this.familyMember.toPage();
            });
         } else {
            this.familyMember = new FamilyMember(patient);
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
            $('#saveFM').remove();
         } else {
            let trId = this.trId;
            $.click('#saveFM', (event) => {
               this.familyMember.create();
               $(trId).remove();
               $.trigger('#filterFM');
            });
         }
      }
      initCancelAction(id) {
         let table = this.table;
         let trId = this.trId;
         $.click('#cancelFM', (event) => {
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
          $.click('#deleteFM', (event) => {
             if(id) {
                this.familyMember.delete();
                table.toggleRow();
                $.trigger('#filterFM');
             } else {
                $(trId).remove();
             }
          });
      }
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
               url: 'http://localhost:7000/persons?f_full_name=*%QUERY*',
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
   window.familyGroupDetails = new FamilyGroupDetails();
})();
