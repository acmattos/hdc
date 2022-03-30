(() => {
   'use strict';

   const logger = new Logger('restricted/person/patient/patient-details.js');
   class Patient {
      constructor() {
         this.personIdId = "#personId";
         this.fullNameId = "#fullName";
         this.dobId = "#dob";
         this.personTypeId = "#personType";
         this.cpfId = "#cpf";
         this.personalIdId = "#personalId";
         this.enabledId = "#enabled";
         if (Array.isArray(arguments[0])) {
            this.personId = arguments[0][0].person_id.id;
            this.fullName = arguments[0][0].full_name;
            this.dob = arguments[0][0].dob;
            this.personType = arguments[0][0].person_type;
            this.cpf = arguments[0][0].cpf;
            this.personalId = arguments[0][0].personalId;
            this.addresses = Address.read(arguments[0][0].addresses);
            this.contacts = Contact.read(arguments[0][0].contacts);
            this.healthInsurance = HealthInsurance.read(arguments[0][0].healthInsurance);
            this.enabled = arguments[0][0].enabled;
         } else {
            this.personId = $.inputText('#personId');
            this.fullName = $.inputText('#fullName');
            this.dob = $.inputText(this.dobId);
            this.personType = $.inputText('#personType');
            this.cpf = $.inputText('#cpf');
            this.personalId = $.inputText('#personalId');
            this.addresses = [];
            this.contacts = [];
            this.healthInsurance = new HealthInsurance();
            this.enabled = $.checkBox('#enabled');
         }
      }
// //          create() {
// //             resource.post(':7000/persons/', this)
// //                .fail((response) => {
// //                   toast.show(response);
// //                });
// //          }
      static read(personId) {
         var deferred = $.Deferred();
         var promise = deferred.promise();
//          if(!personId) {
//             deferred.resolve(new Patient());
//          } else {
            resource.get(':7000/persons/' + personId)
            .done((response) => {
               logger.delete('PATIENT (response.data): ', response.data[0]);
               deferred.resolve(new Patient(response.data));
            })
            .fail((response) => {
               toast.show(response);
               deferred.reject(new Patient());
            });
         // }
         return promise;
      }
      update() {
         if (this.fromPage()){
           // logger.alert('PATIENT UPDATED!!!!!!');
//          resource.put(':7000/persons/', this.fromPage())
//          .done((response) => {
//             toast.show(response);// TODO UPDATE SCREEN
//          })
//          .fail((response) => {
//             toast.show(response);
//          });
         }
      }
// //          delete() {
// //             resource.delete(':7000/persons/this.personId')
// //                .done((response) => {
// //                   toast.show(response);
// //                })
// //                .fail((response) => {
// //                   toast.show(response);
// //                });
// //          }
      toPage(contactTypes, personTypes, states){
         $.inputText(this.personIdId, this.personId);
         $.inputText(this.fullNameId, this.fullName);
         $.inputText(this.dobId, this.dob, 'Dd/Mm/YYYY', {
            onInvalid: (val, e, f, invalid, options) => {
               var error = invalid[0];
               logger.info("Digit: " + error.v + " is invalid for the position: " +
                  error.p + ". We expect something like: " + error.e);
            },
            translation: {
               'M': { pattern: /[0-1]/ },
               'm': { pattern: /[0-9]/ },
               'D': { pattern: /[0-3]/ },
               'd': { pattern: /[0-9]/ },
               'Y': { pattern: /[0-9]/ },
               '/': { pattern: /[\/]/, fallback:'/' }
            },
            reverse: false
         });
         $(this.dobId).datepicker({
            constrainInput: true,
            changeMonth: true,
            changeYear: true,
            showAnim: 'fade',
            // minDate: minDate,
            // maxDate: maxDate
            dateFormat: 'dd/mm/yy',
            dayNames: ['Domingo','Segunda','Terça','Quarta', 'Quinta','Sexta',
               'Sábado','Domingo'],
            dayNamesMin: ['D','S','T','Q','Q','S','S','D'],
            dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex', 'Sáb','Dom'],
            monthNames: ['Janeiro','Fevereiro','Março','Abril', 'Maio','Junho',
               'Julho','Agosto','Setembro','Outubro','Novembro', 'Dezembro'],
            monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun', 'Jul','Ago',
               'Set','Out','Nov','Dez']
         });
         let personTypeId = this.personTypeId;
         let personType = this.personType;
         $.selectBuilder(this.personTypeId, this.personType, personTypes);
         $.inputText(this.cpfId, this.cpf, '999.999.999-99');
         $.inputText(this.personalIdId, this.personalId);
         this.addresses.forEach((item, index, array) => {
            array[index].toPage(states);
         });
         this.contacts.forEach((item, index, array) => {
            array[index].toPage(contactTypes);
         });
         // new HealthInsurance();//                  healthInsuranc, this.healthInsurancee,
         $.checkBox(this.enabledId, this.enabled);
      }
      fromPage(){
         return this.validate();
      }
      validate() {
         let fullNameV = new StringValidator(this.fullNameId, {
            len: { min: 3, max: 100, message: '01FV2HXNN69FE3F55670TQCPE3' }
         });
         let personTypeV = new SelectValidator(this.personTypeId, {
            select: { invalid: '-1', message: '01FV2J1YQ69FE3F55670TQCPE3' }
         });
         let dobV = new SelectValidator(this.personTypeId, {
            select: { invalid: '-1', message: '' }
         });
         let cpfV = new CpfValidator(this.cpfId, {
            format: { message: '01FVPVJN7G27PHS1MQ52NS823F' }
         });
         let personalIdV = new StringValidator(this.personalIdId, {
            empty: {},
            len: { min: 5, max: 20, message: '01FVPVJN7H57RMXBZDKHVT7ZGJ' }
         });
         let isValidAddressess = true;
         this.addresses.forEach((item, index, array) => {
            isValidAddressess = isValidAddressess && array[index].validate();
         });
         let isValidContacts = true;
         this.contacts.forEach((item, index, array) => {
            isValidContacts = isValidContacts && array[index].validate();
         });
         let isValidHealthInsurance = this.healthInsurance.validate();
         this.enabled = $.checkBox(this.enabledId);

         let isValid = fullNameV.validate() && personTypeV.validate() &&
             cpfV.validate() && personalIdV.validate() &&
             isValidAddressess && isValidContacts && isValidHealthInsurance;

         if(isValid) {
            this.personId = $.inputText(this.personIdId);
            this.fullName = fullNameV.value;
            this.personType = personTypeV.value;
            this.cpf = cpfV.value;
            this.personalId = personalIdV.value;
            this.enabled = $.inputText(this.enabledId);
         }
         return isValid;
      }
   }
   window.Patient = Patient;
      class Address {
         constructor(address, index) {
            this.streetId = '#street' + index;
            this.numberId = '#number' + index;
            this.complementId = '#complement' + index;
            this.zipCodeId = '#zipCode' + index;
            this.neighborhoodId = '#neighborhood' + index;
            this.stateId = '#state' + index;
            this.cityId = '#city' + index;
            if (arguments) {
               this.index = arguments[1];
               this.street = address.street;
               this.number = address.number;
               this.complement = address.complement;
               this.zipCode = address.zip_code;
               this.neighborhood = address.neighborhood;
               this.state = address.state;
               this.city = address.city;
            } else {
               this.index = 0;
               this.street = $.inputText(this.streetId);
               this.number = $.inputText(this.numberId);
               this.complement = $.inputText(this.complementId);
               this.zipCode = $.inputText(this.zipCodeId);
               this.neighborhood = $.inputText(this.neighborhoodId);
               this.state = $.inputText(this.stateId);
               this.city = $.inputText(this.cityId);
            }
         }
         static read(addresses) {
            let result = [];
            addresses.forEach((item, index, array) => {
               result.push(new Address(array[index], index));
            });
            return result;
         }
         toPage(states){
            $.inputText(this.streetId, this.street);
            $.inputText(this.numberId, this.number);
            $.inputText(this.complementId, this.complement);
            $.inputText(this.zipCodeId, this.zipCode, '00000-000');
            $.inputText(this.neighborhoodId, this.neighborhood);
            $.selectBuilder(this.stateId, this.state, states);
            $.inputText(this.cityId, this.city);
         }
         fromPage() {
            return this.validate();
         }
         validate() {//logger.alert('ADDRESS UPD')
            let streetV = new StringValidator(this.streetId, {
               len: { min: 3, max: 100, message: '01FVQ2NP5H3PG1JPAG867MJ6XZ' }
            });
            let numberV = new StringValidator(this.numberId, {
               len: { min: 1, max: 10, message: '01FV2J1YQ69FE3F55670TQCPE3' }
            });
            let complementV = new StringValidator(this.complementId, {
               len: { min: 1, max: 50, message: '01FVQ2NP5KGRN5Q8KG39FK090X' }
            });
            let zipCodeV = new StringValidator(this.zipCodeId, {
               len: { min: 9, max: 9, message: '01FVPVJN7D9FE3F55670TQCPE3' }
            });
            let neighborhoodV = new StringValidator(this.neighborhoodId, {
               len: { min: 3, max: 100, message: '01FVPVJN7FGRN5Q8KG39FK090X' }
            });
            let stateV = new SelectValidator(this.stateId, {
               select: { invalid: '-1', message: '01FVPVJN7F19FBHY10QF033G7Y' }
            });
            let cityV = new StringValidator(this.cityId, {
               len: { min: 3, max: 50, message: '01FVPVJN7F2XQ2K4549SYH51FN' }
            });
            let isValid = streetV.validate() && numberV.validate() && zipCodeV.validate()
               && neighborhoodV.validate() && stateV.validate() && cityV.validate();
            if(isValid) {
               this.street = streetV.value;
               this.number = numberV.value;
               this.complement = complementV.value;
               this.zipCode = zipCodeV.value;
               this.neighborhood = neighborhoodV.value;
               this.state = stateV.value;
               this.city = cityV.value;
            }
            return isValid;
         }
      }
      class Contact{
         constructor(contact, index) {
            this.infoId = '#info' + index;
            this.contactTypeId = '#contactType' + index;
            if (arguments[0]) {
               this.index = arguments[1];
               this.info = contact.info;
               this.contactType = contact.type;
            } else {
               this.index = index;
               this.info = $.inputText('#info0');
               this.contactType = 'EMAIL';
            }
         }
         static read(contacts) {
            let result = [];
            contacts.forEach((item, index, array) => {
               result.push(new Contact(array[index], index));
            });
            if(contacts.length === 1) {
               result.push(new Contact(null, 1));
            }
            return result;
         }
         toPage(contactTypes){
            $.inputText(this.infoId, this.info);
            $.selectBuilder(this.contactTypeId, this.contactType, contactTypes,() => {
               if(this.info) {
                  $.disabled(this.contactTypeId, true);
               }
               $.text(this.infoId + 'Label', message.get(this.contactType));
            });
            let contactTypeId = this.contactTypeId;
            $.bodyListener(this.contactTypeId + '-changed',
               (event, data) => {
                  $.text(this.infoId + 'Label', message.get(data));
                  $.attribute(this.infoId, 'placeholder',
                     message.get('placeholder_' + data));
               }
            );
         }
         fromPage() {
            return this.validate();
         }
         validate() {
            let infoV = this.getInfoValidator();
            let contactTypeV = new SelectValidator(this.contactTypeId, {
               select: { invalid: '-1', message: '01FVPVJN7F19FBHY10QF033G7Y' }
            });
            let isValid = infoV.validate() && contactTypeV.validate() ;
            if(isValid) {
               this.info = infoV.value;
               this.contactType = contactTypeV.value;
            }
            return isValid;
         }
         getInfoValidator(){
            if(this.contactType && this.contactType === 'EMAIL') {
               return new EmailValidator(this.infoId, {
                  format: { message: '01FVPVJN7CQ2MJX8PESM4KVKSP' }
               });
            } else {
               return new PhoneValidator(this.infoId, {
                  format: { message: '01FVPVJN7E3PG1JPAG867MJ6XZ' }
               });
            }
         }
      }
      class HealthInsurance{
         constructor(healthInsurance) {
            this.companyNameId = '#companyName';
            this.planNumberId = '#planNumber';
            this.planNameId = '#planName';
            if (healthInsurance) {
               this.companyName = healthInsurance.companyName;
               this.planNumber = healthInsurance.planNumber;
               this.planName = healthInsurance.planName;
            } else {
               this.companyName = $.inputText('#companyName');
               this.planNumber = $.inputText('#planNumber');
               this.planName = $.inputText('#planName');
            }
         }
         static read(healthInsurance) {
            return new HealthInsurance(healthInsurance);
         }
         toPage(){
            $.inputText('#companyName', this.companyName);
            $.inputText('#planNumber', this.planNumber);
            $.inputText('#planName', this.planName);
         }
         fromPage() {
            return this.validate();
         }
         validate() {//logger.alert('HEALTH INSURANCE UPD')
            let companyNameV = new StringValidator(this.companyNameId, { empty: {},
               len: { min: 2, max: 30, message: '01FVPVJN7HKKHVWHP57PKD8N62' }
            });
            let planNumberV = new StringValidator(this.planNumberId, { empty: {},
               len: { min: 6, max: 20, message: '01FVQ2NP5FQ2MJX8PESM4KVKSP' }
            });
            let planNameV = new StringValidator(this.planNameId, { empty: {},
               len: { min: 2, max: 50, message: '01FVQ2NP5G9FE3F55670TQCPE3' }
            });

            let isValid = companyNameV.validate() && planNumberV.validate()
               && planNameV.validate();
            if(isValid) {
               this.companyName = companyNameV.value;
               this.planNumber = planNumberV.value;
               this.planName = planNameV.value;
            }
            return isValid;
         }
      }
//
      class PatientDetails {
         constructor() {
//             this.component = '#patient';
//             this.resource = 'restricted/person/patient/patient-details';
//             this.endpoint = ':7000/persons/';
            this.patient = null;
            this.personTypes = [];
         }
         initPage(personId, contactTypes, personTypes, states) {
            // logger.alert('PatientDetails#initPage');
            // $.when(this.initPersonTypes())
            // .done(() => {
               Patient.read(personId)
               .done((patient) => {
                  this.patient = patient;
                  logger.delete('LOADED: ', patient);
                  // configure actions and validations
                  // update page
                  patient.toPage(contactTypes, personTypes, states);
               });
               $.click('#updatePatient', () => {
                  //logger.alert('#updatePatient click');
                  this.updatePatient();
               });
            // });
         }
         // initPersonTypes() {
         //    var deferred = $.Deferred();
         //    var promise = deferred.promise();
         //    resource.get(':7000/persons/person_types')
         //    .done((response) => {
         //       response.data.forEach(element => {
         //          this.personTypes.push({"id": element, "text": message.get(element)})
         //       });
         //       deferred.resolve(this.personTypes);
         //    })
         //    .fail((response) => {
         //       toast.show(response);
         //       deferred.reject([{"id":"INVALID", "text":"INVALID"}]);
         //    });
         //    return promise;
         // }
         updatePatient() {
            this.patient.update();
         }
      }
      window.patientDetails = new PatientDetails();
//  logger.delete('patientDetails 1')
//  logger.delete('patientDetails 1', patientDetails)
// //
// //       resource.get(':7000/persons/', null, ()=>{/*CUSTOM FAIL*/}).fail((response) => {
// //
// //          //logger.delete('Message', response);
// //          //toast.show(response);
// //          //for (let i = 0; i < 10000; i++) {
// // //logger.delete(i)
// //          //}
// //          TABLE.DataTable();
// //          // {
// //          //    data: {nome: 'Teste'},//data.checkListTOs,
// //          //    columns: [
// //          //       { className: "position-relative text-center", defaultContent: '<i class="fas fa-pencil-alt"></i>', orderable: false },
// //          //       { data: 'id', defaultContent: '', orderable: true },
// //          //    ],
// //          //    columnDefs: [
// //          //       {
// //          //          targets: [ 0 ],
// //          //          width: '40px',
// //          //       },
// //          //       {
// //          //          targets: [ 1 ],
// //          //          title:'<span i18n="Nome">Nome</span>',
// //          //          createdCell: function (td, cellData, rowData, row, col) {
// //          //             var valor = rowData.nome;
// //          //             if (rowData.empresaTO == null){
// //          //                valor = rowData.nome + " ("
// //          //                   + obtemMensagem('global_lbl_generico', lingua)
// //          //                   + ")";
// //          //             }
// //          //             $(td).html(valor);
// //          //          }
// //          //       },
// //          //    ],
// //          //    createdRow: function( row, data, dataIndex) {
// //          //       $(row).attr('data-toggle', 'modal');
// //          //       $(row).attr('data-target', '#modalEditar');
// //          //       $(row).attr('onclick', '_configuraModalEditaCheckList("'
// //          //          + data.id +'")');
// //          //    },
// //          //    language: {
// //          //       //emptyTable: mensagemTabelaVazia
// //          //    },
// //          //    order:          [],
// //          //    scrollY:        '58vh',
// //          //    deferRender:    true,
// //          //    paging:         false,
// //          //    scrollCollapse: true,
// //          //    filter:         false,
// //          //    info:           false,
// //          //    lengthChange:   false
// //          // });
// //       });
// //    });
})();