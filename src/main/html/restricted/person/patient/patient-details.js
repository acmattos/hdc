(() => {
   'use strict';

   const logger = new Logger('restricted/person/patient/patient-details.js');
   class Patient {
      constructor() {
         this.personIdId = "#personId";
         this.fullNameId = "#personId";
         this.personTypeId = "#personTypeId";
         this.cpfId = "#cpfId";
         this.personalId = "#personalIdId";
         this.enabledId = "#enabledId";
         if (Array.isArray(arguments[0])) {
            this.personId = arguments[0][0].person_id.id;
            this.fullName = arguments[0][0].full_name;
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
      toPage(){
         $.inputText('#personId', this.personId);
         $.inputText('#fullName', this.fullName);
         $.inputText('#personType', this.personType);
         $.inputText('#cpf', this.cpf);
         $.inputText('#personalId', this.personalId);
         this.addresses.forEach((item, index, array) => {
            array[index].toPage();
         });
         this.contacts.forEach((item, index, array) => {
            array[index].toPage();
         });
         // new HealthInsurance();//                  healthInsuranc, this.healthInsurancee,
         $.checkBox('#enabled', this.enabled);
      }
      fromPage(){
         // var deferred = $.Deferred();
         // var promise = deferred.promise();
         // this.fullName = $.inputText('#fullName');
         // this.personType = $.inputText('#personType');
         // this.cpf = $.inputText('#cpf');
         // this.personalId = $.inputText('#personalId');
         // this.addresses.forEach((item, index, array) => {
         //    array[index].toPage();
         // });
         // this.contacts.forEach((item, index, array) => {
         //    array[index].toPage();
         // });
         // // new HealthInsurance();//                  healthInsuranc, this.healthInsurancee,
         // $.checkBox('#enabled', this.enabled);
         // return this;
         // return promise;
         return this.validate();
      }
      validate() {//logger.alert('PATIENT UPD')
         let fullNameV = new StringValidator(this.fullNameId, {
            len: { min: 3, max: 100, message: '01FV2HXNN69FE3F55670TQCPE3' }
         });
         let personTypeV = new SelectValidator(this.personTypeId, {
            select: { invalid: '', message: '01FV2J1YQ69FE3F55670TQCPE3' }
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
               this.zipCode = address.zipCode;
               this.neighborhood = address.neighborhood;
               this.state = address.state;
               this.city = address.city;
            } else {
               this.index = 0;
               this.street = $.inputText(this.streetId + this.index);
               this.number = $.inputText(this.numberId + this.index);
               this.complement = $.inputText(this.complementId + this.index);
               this.zipCode = $.inputText(this.zipCodeId + this.index);
               this.neighborhood = $.inputText(this.neighborhoodId + this.index);
               this.state = $.inputText(this.stateId + this.index);
               this.city = $.inputText(this.cityId + this.index);
            }
         }
         static read(addresses) {
            let result = [];
            addresses.forEach((item, index, array) => {
               result.push(new Address(array[index], index));
            });
            return result;
         }
         toPage(){
            $.inputText(this.streetId, this.street);
            $.inputText(this.numberId, this.number);
            $.inputText(this.complementId, this.complement);
            $.inputText(this.zipCodeId, this.zipCode);
            $.inputText(this.neighborhoodId, this.neighborhood);
            $.inputText(this.stateId, this.state);
            $.inputText(this.cityId, this.city);
         }
         fromPage() {
            return this.validate();
         }
         validate() {//logger.alert('ADDRESS UPD')
            let streetV = new StringValidator(this.streetId + this.index, {
               len: { min: 3, max: 100, message: '01FV2HXNN69FE3F55670TQCPE3' }
            });
            let numberV = new StringValidator(this.numberId + this.index, {
               len: { min: 1, max: 10, message: '01FV2J1YQ69FE3F55670TQCPE3' }
            });
            let zipCodeV = new StringValidator(this.zipCodeId + this.index, {
               len: { min: 9, max: 9, message: '01FVPVJN7D9FE3F55670TQCPE3' }
            });
            let neighborhoodV = new StringValidator(this.neighborhoodId + this.index, {
               len: { min: 3, max: 100, message: '01FVPVJN7FGRN5Q8KG39FK090X' }
            });
            let stateV = new SelectValidator(this.stateId, {
               select: { invalid: '', message: '01FVPVJN7F19FBHY10QF033G7Y' }
            });
            let cityV = new StringValidator(this.cityId + this.index, {
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
            if (arguments) {
               this.index = arguments[1];
               this.info = contact.info;
               this.contactType = contact.type;
            } else {
               this.index = 0;
               this.info = $.inputText('#info0');
               this.contactType = $.inputText('#type0');
            }
         }
         static read(contacts) {
            let result = [];
            contacts.forEach((item, index, array) => {
               result.push(new Contact(array[index], index));
            });
            return result;
         }
         toPage(){
            $.inputText(this.infoId, this.info);
            $.inputText(this.contactTypeId, this.contactType);
         }
         fromPage() {
            return this.validate();
         }
         validate() {//logger.alert('CONTACT UPD')
            let infoV = this.getInfoValidator();
            let contactTypeV = new SelectValidator(this.contactTypeId, {
               select: { invalid: '', message: '01FVPVJN7F19FBHY10QF033G7Y' }
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
         }
         initPage(personId) {
            //logger.alert('PatientDetails#initPage');
             Patient.read(personId)
            .done((patient) => {
               this.patient = patient;
               logger.delete('LOADED: ', patient);
               // configure actions and validations
               // update page
               patient.toPage();
            });
            $.click('#updatePatient', () => {
               //logger.alert('#updatePatient click');
               this.updatePatient();
            });
         }
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