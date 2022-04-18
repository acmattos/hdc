(() => {
   'use strict';

   const logger = new Logger('restricted/person/patient/patient-details.js');
   class Patient {
      constructor() {
         // Component's IDs
         this.personIdId = "#personId";
         this.fullNameId = "#fullName";
         this.dobId = "#dob";
         this.personTypeId = "#personType";
         this.cpfId = "#cpf";
         this.personalIdId = "#personalId";
         this.enabledId = "#enabled";
         // Attributes
         this.personId = '';
         this.fullName = '';
         this.dob = '';
         this.personType = '';
         this.cpf = '';
         this.personalId = '';
         this.addresses = [];
         this.contacts = [];
         this.healthInsurance = {};
         this.enabled = '';
         // Validators
         this.fullNameV = new StringValidator(this.fullNameId, {
            len: { min: 3, max: 100, message: '01FV2HXNN69FE3F55670TQCPE3' }
         });
         this.personTypeV = new SelectValidator(this.personTypeId, {
            select: { invalid: '-1', message: '01FV2J1YQ69FE3F55670TQCPE3' }
         });
         this.dobV = new SelectValidator(this.personTypeId, {
            select: { invalid: '-1', message: '' }
         });
         this.cpfV = new CpfValidator(this.cpfId, {
            format: { message: '01FVPVJN7G27PHS1MQ52NS823F' }
         });
         this.personalIdV = new StringValidator(this.personalIdId, {
            empty: {},
            len: { min: 5, max: 20, message: '01FVPVJN7H57RMXBZDKHVT7ZGJ' }
         });
         // let isValidAddressess = true;
         // this.addresses.forEach((item, index, array) => {
         //    isValidAddressess = isValidAddressess && array[index].validate();
         // });
         // let isValidContacts = true;
         // this.contacts.forEach((item, index, array) => {
         //    isValidContacts = isValidContacts && array[index].validate();
         // });
         // let isValidHealthInsurance = this.healthInsurance.validate();
         if (Array.isArray(arguments[0])) {logger.delete('OI',arguments[0][0])
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
         }
      }
// //          create() {
// //             resource.post(':7000/persons/', this)
// //                .fail((response) => {
// //                   toast.show(response);
// //                });
// //          }
      createRequest() {
         let patient = this;
         let request = {
            'full_name': patient.fullName,
            'dob': patient.dob,
            'person_type': patient.personType,
            'cpf': patient.cpf,
            'personal_id': patient.personalId,
            'addresses': patient.addresses,
            'contacts': patient.contacts,
            'health_insurance': patient.healthInsurance
         };
         return request;
      }
      create() {
         let deferred = $.Deferred();
         let promise = deferred.promise();
         if(this.fromPage()) {
            let request = this.createRequest();
            resource.post(':7000/persons', request)
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
      static read(personId) {
         var deferred = $.Deferred();
         var promise = deferred.promise();
         resource.get(':7000/persons/' + personId)
         .done((response) => {
            deferred.resolve(new Patient(response.data));
         })
         .fail((error) => {
            toast.show(error);
            deferred.reject(new Patient());
         });
         return promise;
      }
      update() {
         let deferred = $.Deferred();
         let promise = deferred.promise();
         if (this.fromPage()) {
            let patient = this;
            let request = this.createRequest();
            request.person_id = procedure.personId;
            request.enabled = procedure.enabled ? true : true// TODO ADJUST
            resource.put(':7000/persons/', request)
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
      }
      delete() {
         let deferred = $.Deferred();
         let promise = deferred.promise();
         resource.delete(':7000/persons/' + this.personId)
         .done((response) => {
            deferred.resolve(response);
         })
         .fail((error) => {
            deferred.reject(error);
         });
         return promise;
      }
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
         if(this.validate()) {
            this.personId = $.inputText(this.personIdId);
            this.fullName = this.fullNameV.value;
            this.personType = this.personTypeV.value;
            this.cpf = this.cpfV.value;
            this.personalId = this.personalIdV.value;
            this.enabled = $.checkBox(this.enabledId);
         }
      }
      validate() {
         let isValidAddressess = true;
         this.addresses.forEach((item, index, array) => {
            isValidAddressess = isValidAddressess && array[index].validate();
         });
         let isValidContacts = true;
         this.contacts.forEach((item, index, array) => {
            isValidContacts = isValidContacts && array[index].validate();
         });
         let isValidHealthInsurance = this.healthInsurance.validate();

         return this.fullNameV.validate() && this.personTypeV.validate()
            && this.dobV.validate() && this.cpfV.validate()
            && this.personalIdV.validate() && isValidAddressess
            && isValidContacts && isValidHealthInsurance;

         // let fullNameV = new StringValidator(this.fullNameId, {
         //    len: { min: 3, max: 100, message: '01FV2HXNN69FE3F55670TQCPE3' }
         // });
         // let personTypeV = new SelectValidator(this.personTypeId, {
         //    select: { invalid: '-1', message: '01FV2J1YQ69FE3F55670TQCPE3' }
         // });
         // let dobV = new SelectValidator(this.personTypeId, {
         //    select: { invalid: '-1', message: '' }
         // });
         // let cpfV = new CpfValidator(this.cpfId, {
         //    format: { message: '01FVPVJN7G27PHS1MQ52NS823F' }
         // });
         // let personalIdV = new StringValidator(this.personalIdId, {
         //    empty: {},
         //    len: { min: 5, max: 20, message: '01FVPVJN7H57RMXBZDKHVT7ZGJ' }
         // });
         // let isValidAddressess = true;
         // this.addresses.forEach((item, index, array) => {
         //    isValidAddressess = isValidAddressess && array[index].validate();
         // });
         // let isValidContacts = true;
         // this.contacts.forEach((item, index, array) => {
         //    isValidContacts = isValidContacts && array[index].validate();
         // });
         // let isValidHealthInsurance = this.healthInsurance.validate();
         // this.enabled = $.checkBox(this.enabledId);

         // let isValid = fullNameV.validate() && personTypeV.validate() &&
         //     cpfV.validate() && personalIdV.validate() &&
         //     isValidAddressess && isValidContacts && isValidHealthInsurance;
         //
         // if(isValid) {
         //    this.personId = $.inputText(this.personIdId);
         //    this.fullName = fullNameV.value;
         //    this.personType = personTypeV.value;
         //    this.cpf = cpfV.value;
         //    this.personalId = personalIdV.value;
         //    this.enabled = $.inputText(this.enabledId);
         // }
         // return isValid;
      }
   }
   window.Patient = Patient;
   class Address {
      constructor(address, index) {
         // Component's IDs
         this.streetId = '#street' + index;
         this.numberId = '#number' + index;
         this.complementId = '#complement' + index;
         this.zipCodeId = '#zipCode' + index;
         this.neighborhoodId = '#neighborhood' + index;
         this.stateId = '#state' + index;
         this.cityId = '#city' + index;
         // Attributes
         this.index = 0;
         this.street = '';
         this.number = '';
         this.complement = '';
         this.zipCode = '';
         this.neighborhood = '';
         this.state = '';
         this.city = '';
         // Validators
         this.streetV = new StringValidator(this.streetId, {
            len: {min: 3, max: 100, message: '01FVQ2NP5H3PG1JPAG867MJ6XZ'}
         });
         this.numberV = new StringValidator(this.numberId, {
            len: {min: 1, max: 10, message: '01FV2J1YQ69FE3F55670TQCPE3'}
         });
         this.complementV = new StringValidator(this.complementId, {
            len: {min: 1, max: 50, message: '01FVQ2NP5KGRN5Q8KG39FK090X'}
         });
         this.zipCodeV = new StringValidator(this.zipCodeId, {
            len: {min: 9, max: 9, message: '01FVPVJN7D9FE3F55670TQCPE3'}
         });
         this.neighborhoodV = new StringValidator(this.neighborhoodId, {
            len: {min: 3, max: 100, message: '01FVPVJN7FGRN5Q8KG39FK090X'}
         });
         this.stateV = new SelectValidator(this.stateId, {
            select: {invalid: '-1', message: '01FVPVJN7F19FBHY10QF033G7Y'}
         });
         this.cityV = new StringValidator(this.cityId, {
            len: {min: 3, max: 50, message: '01FVPVJN7F2XQ2K4549SYH51FN'}
         });
         if (arguments) {
            this.index = arguments[1];
            this.street = address.street;
            this.number = address.number;
            this.complement = address.complement;
            this.zipCode = address.zip_code;
            this.neighborhood = address.neighborhood;
            this.state = address.state;
            this.city = address.city;
         }
      }
      static read(addresses) {
         let result = [];
         addresses.forEach((item, index, array) => {
            result.push(new Address(array[index], index));
         });
         return result;
      }
      toPage(states) {
         $.inputText(this.streetId, this.street);
         $.inputText(this.numberId, this.number);
         $.inputText(this.complementId, this.complement);
         $.inputText(this.zipCodeId, this.zipCode, '00000-000');
         $.inputText(this.neighborhoodId, this.neighborhood);
         $.selectBuilder(this.stateId, this.state, states);
         $.inputText(this.cityId, this.city);
      }
      fromPage() {
         if (this.validate()) {
            this.street = this.streetV.value;
            this.number = this.numberV.value;
            this.complement = this.complementV.value;
            this.zipCode = this.zipCodeV.value;
            this.neighborhood = this.neighborhoodV.value;
            this.state = this.stateV.value;
            this.city = this.cityV.value;
            return true;
         }
         return false;
      }
      validate() {//logger.alert('ADDRESS UPD')
         // let streetV = new StringValidator(this.streetId, {
         //    len: { min: 3, max: 100, message: '01FVQ2NP5H3PG1JPAG867MJ6XZ' }
         // });
         // let numberV = new StringValidator(this.numberId, {
         //    len: { min: 1, max: 10, message: '01FV2J1YQ69FE3F55670TQCPE3' }
         // });
         // let complementV = new StringValidator(this.complementId, {
         //    len: { min: 1, max: 50, message: '01FVQ2NP5KGRN5Q8KG39FK090X' }
         // });
         // let zipCodeV = new StringValidator(this.zipCodeId, {
         //    len: { min: 9, max: 9, message: '01FVPVJN7D9FE3F55670TQCPE3' }
         // });
         // let neighborhoodV = new StringValidator(this.neighborhoodId, {
         //    len: { min: 3, max: 100, message: '01FVPVJN7FGRN5Q8KG39FK090X' }
         // });
         // let stateV = new SelectValidator(this.stateId, {
         //    select: { invalid: '-1', message: '01FVPVJN7F19FBHY10QF033G7Y' }
         // });
         // let cityV = new StringValidator(this.cityId, {
         //    len: { min: 3, max: 50, message: '01FVPVJN7F2XQ2K4549SYH51FN' }
         // });
         return this.streetV.validate() && this.numberV.validate()
            && this.zipCodeV.validate() && this.neighborhoodV.validate()
            && this.stateV.validate() && this.cityV.validate();
      }
   }
   class Contact{
      constructor(contact, index) {
         index = !index ? 0 : index;
         // Component's IDs
         this.infoId = '#info' + index;
         this.contactTypeId = '#contactType' + index;
         // Attributes
         this.index = 0;
         this.info = '';
         this.contactType = 'CELULAR';
         // Validators
         this.infoV = null;
         this.contactTypeV = new SelectValidator(this.contactTypeId, {
            select: { invalid: '-1', message: '01FVPVJN7F19FBHY10QF033G7Y' }
         });
         if (arguments[0]) {
            this.index = index;
            this.info = contact.info;
            this.contactType = contact.type;
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
         if(this.validate()) {
            this.info = this.infoV.value;
            this.contactType = this.contactTypeV.value;
            return true;
         }
         return false;
      }
      validate() {
         this.infoV = this.getInfoValidator();
         return this.infoV.validate() && this.contactTypeV.validate() ;
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
         // Component's IDs
         this.companyNameId = '#companyName';
         this.planNumberId = '#planNumber';
         this.planNameId = '#planName';
         // Attributes
         this.companyName = '';
         this.planNumber = '';
         this.planName = '';
         // Validators
         this.companyNameV = new StringValidator(this.companyNameId, { empty: {},
            len: { min: 2, max: 30, message: '01FVPVJN7HKKHVWHP57PKD8N62' }
         });
         this.planNumberV = new StringValidator(this.planNumberId, { empty: {},
            len: { min: 6, max: 20, message: '01FVQ2NP5FQ2MJX8PESM4KVKSP' }
         });
         this.planNameV = new StringValidator(this.planNameId, { empty: {},
            len: { min: 2, max: 50, message: '01FVQ2NP5G9FE3F55670TQCPE3' }
         });
         if (healthInsurance) {
            this.companyName = healthInsurance.companyName;
            this.planNumber = healthInsurance.planNumber;
            this.planName = healthInsurance.planName;
         }
      }
      static read(healthInsurance) {
         return new HealthInsurance(healthInsurance);
      }
      toPage(){
         $.inputText(this.companyNameId, this.companyName);
         $.inputText(this.planNumberId, this.planNumber);
         $.inputText(this.planNameId, this.planName);
      }
      fromPage() {
         if(this.validate()) {
            this.companyName = this.companyNameV.value;
            this.planNumber = this.planNumberV.value;
            this.planName = this.planNameV.value;
            return true;
         }
         return false;
      }
      validate() {//logger.alert('HEALTH INSURANCE UPD')
         // let companyNameV = new StringValidator(this.companyNameId, { empty: {},
         //    len: { min: 2, max: 30, message: '01FVPVJN7HKKHVWHP57PKD8N62' }
         // });
         // let planNumberV = new StringValidator(this.planNumberId, { empty: {},
         //    len: { min: 6, max: 20, message: '01FVQ2NP5FQ2MJX8PESM4KVKSP' }
         // });
         // let planNameV = new StringValidator(this.planNameId, { empty: {},
         //    len: { min: 2, max: 50, message: '01FVQ2NP5G9FE3F55670TQCPE3' }
         // });

         return this.companyNameV.validate() && this.planNumberV.validate()
            && this.planNameV.validate();
      }
   }
   class PatientDetails {
      constructor() {
         this.patient = null;
         this.personTypes = [];
      }
      initPage(id, contactTypes, personTypes, states) {
         if (id) {
            Patient.read(id)
            .done((patient) => {
               this.patient = patient;
               patient.toPage(contactTypes, personTypes, states);
            });
            // $.click('#updatePatient', () => {
            //    //logger.alert('#updatePatient click');
            //    this.updatePatient();
            // });
            // });
         } else {
            this.patient = new Patient();
         }
         // this.initCUDActions(id);
      }
      // initCUDActions(id) {
      //    this.initSaveAction(id);
      //    this.initCancelAction(id);
      //    this.initDeleteAction(id);
      // }
      // initSaveAction(id) {
      //    $.click('#save', (event) => {
      //       let promise = null;
      //       let message = null;
      //       if(!id) {
      //          promise = this.procedure.create();
      //          message = 'Procedimento Criado!';
      //       } else {
      //          promise = this.procedure.update();
      //          message = 'Procedimento Atualizado!';
      //       }
      //       promise
      //          .done((response) => {
      //             toast.show({
      //                'status': response.status,
      //                'code':'01FVT3QG3N9FE3F55670TQCPE3',
      //                'data': message
      //             });
      //             $('#newItem').remove();
      //             $.trigger('#filter');
      //          })
      //          .fail((error) => {
      //             toast.show(error);
      //          });
      //    });
      // }
      // initCancelAction(id) {
      //    $.click('#cancel', (event) => {
      //       $('#newItem').remove();
      //       //clicou = false;
      //    });
      // }
      // initDeleteAction(id) {
      //    $.click('#delete', (event) => {
      //       //clicou = false;
      //       let promise = null;
      //       let message = null;
      //       if(!id) {
      //          $('#newItem').remove();
      //       } else { alert('JUST DELETE ' + id)
      //          promise = this.procedure.delete();
      //          message = 'Procedimento Excluído!';
      //       }
      //       promise
      //          .done((response) => {
      //             toast.show({
      //                'status': response.status,
      //                'code':'01FVT3QG3N9FE3F55670TQCPE3',
      //                'data': message
      //             });
      //             $.trigger('#filter');
      //          })
      //          .fail((error) => {
      //             toast.show(error);
      //          });
      //    });
      // }
      ////////////////////
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
})();