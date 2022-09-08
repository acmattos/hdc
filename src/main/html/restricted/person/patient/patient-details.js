(() => {
   'use strict';

   const logger = new Logger('restricted/person/patient/patient-details.js');
   class Patient {
      constructor(contactTypes, patient) {
         // Component's IDs
         this.personIdId = '#personId';
         this.fullNameId = '#fullName';
         this.dobId = '#dob';
         this.maritalStatusId = '#maritalStatus';
         this.genderId = '#gender';
         this.personTypeId = '#personType';
         this.cpfId = '#cpf';
         this.personalIdId = '#personalId';
         this.occupationId = '#occupation';
         this.responsibleForId = '#responsibleFor';
         this.indicatedById = '#indicatedBy';
         this.familyGroupId = '#familyGroup';
         this.statusId = '#status';
         this.lastAppointmentId = '#lastAppointment';
         this.enabledId = '#enabled';
         // Attributes
         this.personId = '';
         this.fullName = '';
         this.dob = '';
         this.maritalStatus = '';
         this.gender = '';
         this.personType = 'PATIENT';
         this.cpf = '';
         this.personalId = '';
         this.occupation = '';
         this.addresses = [];
         this.contacts = [];
         this.dentalPlan = {};
         this.responsibleFor = null;
         this.indicatedBy = '';
         this.familyGroup = [];
         this.status = '';
         this.lastAppointment = '';
         this.enabled = '';
         // Validators
         this.fullNameV = new StringValidator(this.fullNameId, {
            len: { min: 3, max: 100, message: '01FV2HXNN69FE3F55670TQCPE3' }
         });
         this.dobV = new DateValidator(this.dobId, {
            between: { min: '01/01/1900'.toDate(), max: new Date(),
               message: '01FWKSM5K19FE3F55670TQCPE3' }
         });
         this.maritalStatusV = new SelectValidator(this.maritalStatusId, {
            select: { invalid: '-1', message: '01FWKSM5K23PG1JPAG867MJ6XZ' }
         });
         this.genderV = new SelectValidator(this.genderId, {
            select: { invalid: '-1', message: '01FWKSM5K319FBHY10QF033G7Y' }
         });
         this.cpfV = new CpfValidator(this.cpfId, {
            format: { message: '01FVPVJN7G27PHS1MQ52NS823F' }
         });
         this.personalIdV = new StringValidator(this.personalIdId, {
            empty: {},
            len: { min: 5, max: 20, message: '01FVPVJN7H57RMXBZDKHVT7ZGJ' }
         });
         this.occupationV = new StringValidator(this.occupationId, {
            empty: {},
            len: { min: 3, max: 100, message: '01FVT5ENFP3PG1JPAG867MJ6XZ' }
         });
          this.indicatedByV = new StringValidator(this.indicatedById, {
            empty: {},
            len: { min: 3, max: 100, message: '01FWKSM5K552MV85FW4ZTYTE6F' }
         });
         this.statusV = new SelectValidator(this.statusId, {
            select: { invalid: '-1', message: '01FWKSM5K527PHS1MQ52NS823F' }
         });
         this.lastAppointmentV = new DateValidator(this.lastAppointmentId, {
            between: { min: '01/01/1900'.toDate(), max: new Date(),
               message: '01FWKTXVD752MV85FW4ZTYTE6F' }
         });
         // Extra
         this.contactTypes = contactTypes;
         if (patient && Array.isArray(patient)) {
            this.personId =  patient[0].person_id.id;
            this.fullName = patient[0].full_name;
            this.dob = patient[0].dob;
            this.gender = patient[0].gender;
            this.maritalStatus = patient[0].marital_status;
            this.personType = patient[0].person_type;
            this.cpf = patient[0].cpf;
            this.personalId = patient[0].personal_id;
            this.occupation = patient[0].occupation;
            this.addresses = Address.read(patient[0].addresses);
            this.contacts = Contact.read(contactTypes, patient[0].contacts);
            this.dentalPlan = DentalPlan.read(patient[0].dental_plan);
            this.responsibleFor = patient[0].responsible_for ? patient[0].responsible_for.id : null;
            this.indicatedBy = patient[0].indicated_by;
            this.familyGroup =  patient[0].family_group ? patient[0].family_group : [];
            this.status = patient[0].status;
            this.lastAppointment = patient[0].last_appointment;
            this.enabled = patient[0].enabled;
         } else {
            this.maritalStatus = 'MARRIED';
            this.addresses.push(new Address());
            contactTypes.forEach((item, index, array) => {
               this.contacts.push(new Contact(item.id, null, index));
            });
            this.dentalPlan = new DentalPlan();
            this.status = 'REGULAR';
            this.enabled = true;
         }
      }
      createRequest() {
         let patient = this;
         let request = {
            'full_name': patient.fullName,
            'dob': patient.dob,
            'marital_status': patient.maritalStatus,
            'gender': patient.gender,
            'person_type': patient.personType,
            'cpf': patient.cpf,
            'personal_id': patient.personalId,
            'occupation': patient.occupation,
            'addresses': this.addressesPayLoad(),
            'contacts': this.contactsPayLoad(),
            'dental_plan': this.dentalPlanPayLoad(),
            'responsible_for': patient.responsibleFor,
            'indicated_by': patient.indicatedBy,
            'family_group': patient.familyGroup,
            'status': patient.status,
            'last_appointment': patient.lastAppointment,
            'enabled': patient.enabled
         };
         logger.debug('Patient Request', request);
         return request;
      }
      addressesPayLoad() {
         let addresses = this.addresses;
         let payload = [];
         let request = {
            'street': addresses[0].street,
            'number': addresses[0].number,
            'complement': addresses[0].complement,
            'zip_code': addresses[0].zipCode,
            'neighborhood': addresses[0].neighborhood,
            'state': addresses[0].state,
            'city': addresses[0].city
         }
         payload.push(request);
         return payload;
      }
      contactsPayLoad() {
         let contacts = this.contacts;
         let payload = [];
         contacts.forEach((item, index, array) => {
            let contact = array[index];
            if(contact.info) {
               let request = {
                  'info': contact.info,
                  'type': contact.contactType,
                  'obs': contact.obs
               }
               payload.push(request);
            }
         });
         return payload;
      }
      dentalPlanPayLoad() {
         let dentalPlan = this.dentalPlan;
         if (dentalPlan.name) {
            let request = {
               'name': dentalPlan.name,
               'number': dentalPlan.number
            }
            return request;
         }
         return null;
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
      static read(contactTypes, personId) {
         var deferred = $.Deferred();
         var promise = deferred.promise();
         resource.get(':7000/persons/' + personId)
         .done((response) => {
            deferred.resolve(new Patient(contactTypes, response.data));
         })
         .fail((error) => {
            toast.show(error);
            deferred.reject(new Patient(contactTypes));
         });
         return promise;
      }
      update() {
         let deferred = $.Deferred();
         let promise = deferred.promise();
         if (this.fromPage()) {
            let patient = this;
            let request = this.createRequest();
            request.person_id = patient.personId;
            request.enabled = patient.enabled ? true : true// TODO ADJUST
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
         return promise;
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
      toPage(genders, maritalStatuses, personTypes, states, statuses) {
         $.inputText(this.personIdId, this.personId);
         $.inputText(this.fullNameId, this.fullName);
         $.inputDate(this.dobId, this.dob, this.dobV.rule.between.min,
            this.dobV.rule.between.max);
         $.selectBuilder(this.maritalStatusId, this.maritalStatus, maritalStatuses);
         $.selectBuilder(this.genderId, this.gender, genders);
         $.selectBuilder(this.personTypeId, this.personType, personTypes);
         $.inputText(this.cpfId, this.cpf, '999.999.999-99');
         $.inputText(this.personalIdId, this.personalId);
         $.inputText(this.occupationId, this.occupation);
         this.addresses.forEach((item, index, array) => {
            array[index].toPage(states);
         });
         this.contacts.forEach((item, index, array) => {
            array[index].toPage(this.contactTypes);
         });
         this.dentalPlan.toPage();
         $.inputText(this.indicatedById, this.indicatedBy);
         $.selectBuilder(this.statusId, this.status, statuses);
         $.inputDate(this.lastAppointmentId, this.lastAppointment,
            this.lastAppointmentV.rule.between.min,
            this.lastAppointmentV.rule.between.max);
         $.checkBox(this.enabledId, this.enabled);
      }
      fromPage(){
         if(this.validate()) {
            this.personId = $.inputText(this.personIdId);
            this.fullName = this.fullNameV.value;
            this.dob = this.dobV.value;
            this.maritalStatus = this.maritalStatusV.value;
            this.gender = this.genderV.value;
            this.cpf = this.cpfV.value;
            this.personalId = this.personalIdV.value;
            this.occupation = this.occupationV.value;
            this.addresses.forEach((item, index, array) => {
               array[index].fromPage();
            });
            this.contacts.forEach((item, index, array) => {
               array[index].fromPage();
            });
            this.dentalPlan.fromPage();
            this.indicatedBy = this.indicatedByV.value;
            this.status = this.statusV.value;
            this.lastAppointment = this.lastAppointmentV.value;
            this.enabled = $.checkBox(this.enabledId);
            return true;
         }
         return false;
      }
      validate() {
         let isValidFullName = this.fullNameV.validate();
         let isValidDob = this.dobV.validate();
         let isValidMaritalStatus = this.maritalStatusV.validate();
         let isValidGender = this.genderV.validate();
         let isValidCpf = this.cpfV.validate();
         let isValidPersonalId = this.personalIdV.validate();
         let isValidOccupation = this.occupationV.validate();
         let isValidAddresses = true;
         this.addresses.forEach((item, index, array) => {
            isValidAddresses = isValidAddresses && array[index].validate();
         });
         let isValidContacts = true;
         this.contacts.forEach((item, index, array) => {
            isValidContacts = isValidContacts && array[index].validate();
         });
         let isValidDentalPlan = this.dentalPlan.validate();
         // let isValid = this.responsibleForV.validate();
         let isValidIndicatedBy = this.indicatedByV.validate();
         //let isValid = this.familyGroup = ;
         let isValidStatus = this.statusV.validate();
         let isValidLastAppointment = this.lastAppointmentV.validate();

         return isValidFullName && isValidDob && isValidMaritalStatus
            && isValidGender && isValidCpf && isValidPersonalId
            && isValidOccupation && isValidAddresses && isValidContacts
            && isValidDentalPlan && isValidIndicatedBy && isValidStatus
            && isValidLastAppointment;
      }
   }
   window.Patient = Patient;
   class Address {
      constructor(address, index) {
         index = !index ? 0 : index;
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
            len: { min: 3, max: 100, message: '01FVQ2NP5H3PG1JPAG867MJ6XZ' }
         });
         this.numberV = new StringValidator(this.numberId, {
            empty: {},
            len: { min: 1, max: 10, message: '01FWKFTFMG9FE3F55670TQCPE3' }
         });
         this.complementV = new StringValidator(this.complementId, {
            empty: {},
            len: { min: 1, max: 50, message: '01FVQ2NP5KGRN5Q8KG39FK090X' }
         });
         this.zipCodeV = new StringValidator(this.zipCodeId, {
            len: { min: 9, max: 9, message: '01FVPVJN7D9FE3F55670TQCPE3' }
         });
         this.neighborhoodV = new StringValidator(this.neighborhoodId, {
            len: { min: 3, max: 100, message: '01FVPVJN7FGRN5Q8KG39FK090X' }
         });
         this.stateV = new SelectValidator(this.stateId, {
            select: { invalid: '-1', message: '01FVPVJN7F19FBHY10QF033G7Y' }
         });
         this.cityV = new StringValidator(this.cityId, {
            len: { min: 3, max: 100, message: '01FVPVJN7F2XQ2K4549SYH51FN' }
         });
         if (address) {
            this.index = index;
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
      validate() {
         let isValidStreet = this.streetV.validate();
         let isValidNumber = this.numberV.validate();
         let isValidComplement = this.complementV.validate();
         let isValidZipCode = this.zipCodeV.validate();
         let isValidNeighborhood = this.neighborhoodV.validate();
         let isValidState = this.stateV.validate();
         let isValidCity = this.cityV.validate();
         return isValidStreet && isValidNumber && isValidComplement
            && isValidZipCode && isValidNeighborhood && isValidState
            && isValidCity;
      }
   }
   class Contact{
      constructor(contactType, contact, index) {
         index = !index ? 0 : index;
         // Component's IDs
         this.infoId = '#info' + index;
         this.contactTypeId = '#contactType' + index;
         this.obsId = '#obs' + index;
         // Attributes
         this.index = 0;
         this.info = '';
         this.contactType = contactType;
         this.obs = null;
         // Validators
         this.infoV = null;
         this.obsV = new StringValidator(this.obsId, {
            empty: {},
            len: { min: 3, max: 20, message: '01FWKSM5K657RMXBZDKHVT7ZGJ' }
         });
         if (contact) {
            this.index = index;
            this.info = contact.info;
            this.contactType = contact.type;
            this.obs = contact.obs;
         }
      }
      static read(contactTypes,contacts) {
         let result = [];
         let contactMap = new Map();
         contacts.forEach((item, index, array) => {
            contactMap.set(array[index].type, array[index]);
         });
         contactTypes.forEach((item, index, array) => {
            result.push(new Contact(array[index].id,
               contactMap.get(array[index].id), index));
         });
         return result;
      }
      getContactMask() {
         if ('CELLULAR' == this.contactType) {
            return '(99) 09999-9999';
         } else if ('PHONE' == this.contactType) {
            return '(99) 9999-9999';
         } else if ('EMERGENCY' == this.contactType) {
            return '(99) 09999-9999';
         } else {
            return null;
         }
      }
      toPage(contactTypes) {
         $.inputText(this.infoId, this.info, this.getContactMask());
         $.text(this.infoId + 'Label', message.get(this.contactType));
         $.attribute(this.infoId, 'placeholder',
            message.get('placeholder_' + this.contactType));
         $.inputText(this.obsId, this.obs);
      }
      fromPage() {
         if(this.validate()) {
            this.info = this.infoV.value;
            this.obs = 'EMERGENCY' == this.contactType ? this.obsV.value : null;
            return true;
         }
         return false;
      }
      validate() {
         this.infoV = this.getInfoValidator();
         let isValidInfo = this.infoV.validate();
         let isValidObs = this.info ? this.obsV.validate() : true;
         return isValidInfo && isValidObs;
      }
      getInfoValidator(){
         if(this.contactType && this.contactType === 'EMAIL') {
            return new EmailValidator(this.infoId, {
               empty: {},
               format: { message: '01FVPVJN7CQ2MJX8PESM4KVKSP' }
            });
         } else {
            return new PhoneValidator(this.infoId, {
               empty: {},
               format: { message: '01FVPVJN7E3PG1JPAG867MJ6XZ' }
            });
         }
      }
   }
   class DentalPlan{
      constructor(dentalPlan) {
         // Component's IDs
         this.nameId = '#dentalPlanName';
         this.numberId = '#dentalPlanNumber';
         // Attributes
         this.name = '';
         this.number = '';
         // Validators
         this.nameV = new StringValidator(this.nameId, { empty: {},
            len: { min: 2, max: 30, message: '01FVPVJN7HKKHVWHP57PKD8N62' }
         });
         this.numberV = new StringValidator(this.numberId, { empty: {},
            len: { min: 6, max: 20, message: '01FVQ2NP5FQ2MJX8PESM4KVKSP' }
         });
         if (dentalPlan) {
            this.name = dentalPlan.name;
            this.number = dentalPlan.number;
         }
      }
      static read(dentalPlan) {
         return new DentalPlan(dentalPlan);
      }
      toPage(){
         $.inputText(this.nameId, this.name);
         $.inputText(this.numberId, this.number);
      }
      fromPage() {
         if(this.validate()) {
            this.name = this.nameV.value;
            this.number = this.numberV.value;
            return true;
         }
         return false;
      }
      validate() {
         let isValidName = this.nameV.validate();
         let isValidNumber = this.numberV.validate();
         return isValidName && isValidNumber;
      }
   }
   class PatientDetails {
      constructor() {
         this.patient = null;
         this.personTypes = [];
         this.trId = '#newItem';
         this.table = {};
      }
      initPage(table, id, contactTypes, genders, maritalStatuses, personTypes, states, statuses) {
         logger.debug('Initialize page..., [id]', id);
         this.table = table;
         if (id) {
            Patient.read(contactTypes, id)
            .done((patient) => {
               this.patient = patient;
               patient.toPage(genders, maritalStatuses, personTypes, states, statuses);
               this.initResponsibleForButton();
               this.initFamilyGroupButton();
            });
         } else {
            this.patient = new Patient(contactTypes);
            this.patient.toPage(genders, maritalStatuses, personTypes, states, statuses);
            this.initResponsibleForButton();
            this.initFamilyGroupButton();
         }
         this.initCUDActions(id);
      }
      initResponsibleForButton() {
         let patient = this.patient;
         $.text('#responsibleForButton',
            'Ver (' + (patient.responsibleFor ? 1 : 0) + ')' );
         $('#responsibleForButton').off('click').on('click', (event) => {
            resource.component('#modalBody',
               'restricted/person/patient/responsible-for-list')
            .done(() => {
               new ResponsibleForList(patientDetails.patient).initPage();
               $.bodyListener('#modalCloseButton',
                  (event, data) => {
                     $.text('#responsibleForButton',
                        'Ver (' + (patient.responsibleFor ? 1 : 0) + ')' );
                  },
                  null
               );
            });
         });
      }
      initFamilyGroupButton() {
         let patient = this.patient;
         let familyGroup = patient.familyGroup;
         $.text('#familyGroupButton', 'Ver (' + familyGroup.length + ')' );
         $('#familyGroupButton').off('click').on('click', (event) => {
            resource.component('#modalBody',
               'restricted/person/patient/family-group-list')
            .done(() => {
               new FamilyGroupList(patient).initPage();
               $.bodyListener('#modalCloseButton',
                  (event, data) => {
                     $.text('#familyGroupButton',
                        'Ver (' + familyGroup.length + ')' );
                  },
                  null
               );
            });
         });
      }
      initCUDActions(id) {
         this.initSaveAction(id);
         this.initCancelAction(id);
      //    this.initDeleteAction(id);
      }
      initSaveAction(id) {
         $.click('#save', (event) => {
            let promise = null;
            let message = null;
            if(!id) {
               promise = this.patient.create();
               message = 'Paciente Criado!';
            } else {
               promise = this.patient.update();
               message = 'Paciente Atualizado!';
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
   //    initDeleteAction(id) {
   //       $.click('#delete', (event) => {
   //          //clicou = false;
   //          let promise = null;
   //          let message = null;
   //          if(!id) {
   //             $('#newItem').remove();
   //    $('td.details-control').closest('tr')
   // .removeClass('shown').removeClass('selected').removeClass('dt-hasChild');
   //          } else { alert('JUST DELETE ' + id)
   //             promise = this.procedure.delete();
   //             message = 'Procedimento Excluído!';
   //          }
   //          promise
   //             .done((response) => {
   //                toast.show({
   //                   'status': response.status,
   //                   'code':'01FVT3QG3N9FE3F55670TQCPE3',
   //                   'data': message
   //                });
   //                $.trigger('#filter');
   //             })
   //             .fail((error) => {
   //                toast.show(error);
   //             });
   //       });
   //       let table = this.table;
   //       let trId = this.trId;
   //       $.click('#deleteFM', (event) => {
   //          if(id) {
   //             this.familyMember.delete();
   //             table.toggleRow();
   //             $.trigger('#filterFM');
   //          } else {
   //             $(trId).remove();
   //          }
   //       });
   //    }
   }
   window.patientDetails = new PatientDetails();
})();
