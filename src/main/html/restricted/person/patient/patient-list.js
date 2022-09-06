(() => {
   'use strict';

   const logger = new Logger('restricted/person/patient/patient-list.js');
   $(document).ready(() => {
      class PersonList {
         constructor() {
            // Component's IDs
            this.fcpfId = "#fcpf";
            this.ffullNameId = "#ffullName";
            this.fcontactId = "#fcontact";
            this.fdentalPlanNameId = "#fdentalPlanName";
            this.pageNumberId = "#pageNumber"
            // Attributes
            this.fcpf = "";
            this.ffullName = "";
            this.fcontact = "";
            this.fcompanyName = "";
            this.table = {};
            this.contactTypes = []
            this.genders = [];
            this.maritalStatuses = [];
            this.personTypes = [];
            this.states = [];
            this.statuses = [];
            this.table = {};
         }
         prefixId() { return '#patientList';  }
         newItemId() { return '#newItem'; }
         uri() { return ':7000/persons';  }
         queryString() {
            this.fcpf = $.inputText(this.fcpfId) + '*';;
            this.ffullName = $.inputText(this.ffullNameId) + '*';;
            this.fcontact = $.inputText(this.fcontactId);;
            this.fdentalPlanName = $.inputText(this.fdentalPlanNameId) + '*';
            return "?f_cpf={0}&f_full_name={1}&f_contact={2}&f_dental_plan_name={3}"
               .format(this.fcpf, this.ffullName, this.fcontact,
                  this.fdentalPlanName);
         }
         columns() {
            return  [
               { 'class': 'details-control', 'defaultContent': '', 'orderable': false, 'data': null },
               { 'data': 'full_name', 'defaultContent': '', 'orderable': false },
               { 'data': 'dob', 'defaultContent': '', 'orderable': false },
               { 'data': 'cpf', 'defaultContent': '', 'orderable': false },
               { 'data': 'contacts', 'defaultContent': '', 'orderable': false },
               { 'data': 'dentalPlan', 'defaultContent': '', 'orderable': false },
               { 'data': 'dentalPlan', 'defaultContent': '', 'orderable': false },
               { 'className': "text-center", 'defaultContent': '', 'orderable': false }
            ];
         }
         columnDefs() {
            return [
               {
                  targets: [ 0 ],
                  width: "30px",
                  createdCell: function (td, cellData, rowData, row, col) {
                     let html = "<input id='id_" +  row + col
                        + "' type='hidden' value='" + rowData.person_id.id
                        + "' class='id'/>"
                     $(td).html(html);
                  }
               },
               {
                  targets: [ 1 ],
                  title:'<span>Nome</span>',
               },
               {
                  targets: [ 2 ],
                  width: "10px",
                  title:'<span>Idade</span>',
                  createdCell: function (td, cellData, rowData, row, col) {
                     let html = new Date().getFullYear() - rowData.dob.toDate().getFullYear();
                     $(td).html(html);
                  }
               },
               {
                  targets: [ 3 ],
                  width: "100px",
                  title:'<span>CPF</span>',
               },
               {
                  targets: [ 4 ],
                  width: "120px",
                  title:'<span>Contato</span>',
                  createdCell: function (td, cellData, rowData, row, col) {
                     let html = null;
                     let whatsapp = ' <a role="button" target="_blank" '
                        + 'href="https://web.whatsapp.com/send?phone=55{0}&amp;text=Olá {1}"><i ' +
                        'class="fa-brands fa-whatsapp"></i></a>';
                     let contactMap = new Map();
                     let info = '';
                     rowData.contacts.forEach((item, index, array) => {
                        info = array[index].info;
                        if('CELLULAR' == array[index].type) {
                           info += whatsapp.format(info.replace(/\D/g,''),
                              rowData.full_name);
                        } else if('EMERGENCY' == array[index].type){
                           info += whatsapp.format(info.replace(/\D/g,''),
                              array[index].obs + ' contato de emergência de '
                              + rowData.full_name);
                        }
                        contactMap.set(array[index].type, info);
                        info = '';
                     });
                     html = contactMap.get('CELLULAR');
                     if(!html) {
                        html = contactMap.get('PHONE');
                     }
                     if(!html) {
                        html = contactMap.get('EMERGENCY');
                     }
                     if(!html) {
                        html = contactMap.get('EMAIL');
                     }
                     $(td).html(html);
                  }
               },
               {
                  targets: [ 5 ],
                  width: "100px",
                  title:'<span>Convênio</span>',
                  createdCell: function (td, cellData, rowData, row, col) {
                     let html = 'Particular';
                     if(rowData.dental_plan && rowData.dental_plan.name) {
                        html = rowData.dental_plan.name;
                     }
                     $(td).html(html);
                  }
               },
               {
                  targets: [ 6 ],
                  width: "100px",
                  title:'<span>Carteira</span>',
                  createdCell: function (td, cellData, rowData, row, col) {
                     let html = '-';
                     if(rowData.dental_plan && rowData.dental_plan.number) {
                        html = rowData.dental_plan.number;
                     }
                     $(td).html(html);
                  }
               },
               {
                  targets: [ 7 ],
                  width: "30px",
               },
            ];
         }
         newLine() {
            return '<tr id="newTrItem" role="row"><td colspan="7">' +
               '<div id="newItem" class="container-fluid border"></div></td></tr>';
         }
         addNewLine(addNewLineCallback, executeAfterLoadCallback) {
            addNewLineCallback();
            resource.component(this.newItemId(),
               'restricted/person/patient/patient-details')
               .done(() => {
                  executeAfterLoadCallback();
               });
         }
         datatable(uri) {
            this.table = new Datatable(this.prefixId(),
               new DtConfig(uri, this.columns(), this.columnDefs(),
                  this.initComplete));
            let table = this.table;
            let datatable = table.table();
            let detailRows = [];
            let patientList = this;
            let contactTypes = this.contactTypes;
            let genders = this.genders;
            let maritalStatuses = this.maritalStatuses;
            let personTypes = this.personTypes;
            let states = this.states;
            let statuses = this.statuses;
            let prefixId = this.prefixId();
         let newItemId = this.newItemId;
            $(prefixId + ' tbody').off('click').on(
               'click', 'tr td.details-control', function () {
               let tr = $(this).closest('tr');
               let row = datatable.row(tr);
               let dtTr = new DtTr(datatable, tr, newItemId());
               dtTr.toggleRow(
                  () => {
                     patientList.addNewLine(
                        () => {
                           row.child($(patientList.newLine())).show();
                        },
                        () => {
                           patientDetails.initPage(dtTr, tr.find('.id').val(), contactTypes,
                              genders, maritalStatuses, personTypes, states, statuses);
                        }
                     );
                  }
               );
            });
            datatable.on('draw', () => {
               $.each(detailRows, (i, id) => {
                  $.trigger('#' + id + ' td.details-control');
               });
            });
            return datatable;
         }
         filterList() {
            $.click('#filter', (event) => {
               this.datatable(this.uri() + this.queryString());
            });
            $.inputText(this.fcpfId, '', '999.999.999-99');
            $.keyup(this.fcpfId, 13, () => {$.trigger('#filter');});
            $.keyup(this.ffullNameId, 2, () => {$.trigger('#filter');});
            let maskBehavior = (val) => {
               let value = (''+val).replace(/\D/g, '');
               let regex = /\d/g;
               if(regex.test(value)) {
                  return (''+val).replace(/\D/g, '').length === 11
                     ? '(00) 00000-0000' : '(00) 0000-00009';
               } else {
                  return 'AAAAAAAAAAAAAAAA';
               }
            };
            let maskOptions = {
               onKeyPress: function(val, e, field, options) {
                  let value = (''+val).replace(/\D/g, '');
                  let regex = /\d/g;
                  if(regex.test(value)) {
                     $(field).mask(maskBehavior.apply({}, arguments), options);
                  } else {
                     $(field).unmask();
                  }
               }
            };
            $(this.fcontactId).mask(maskBehavior, maskOptions);
            $.keyup(this.fcontactId, 13, () => {$.trigger('#filter');});
            $.keyup(this.fdentalPlanNameId, 4, () => {$.trigger('#filter');});
         }
         setupNewItem() {
            let patientList = this;
            let contactTypes = this.contactTypes;
            let genders = this.genders;
            let maritalStatuses = this.maritalStatuses;
            let personTypes = this.personTypes;
            let states = this.states;
            let statuses = this.statuses;
            // let table = this.table;
            let table = new DtTr(null, $('td.details-control').closest('tr'), this.newItemId());
            $.click(this.prefixId() + 'New', (event) => {
               $(patientList.newItemId()).remove();
               this.addNewLine(() => {
                  $("#newTrItem").remove();
                  $(this.prefixId() + ' tr:first').before(this.newLine());
               }, () => {
                  patientDetails.initPage(table, null, contactTypes, genders,
                     maritalStatuses, personTypes, states, statuses);
               });
            });
         }
         initPage() {
            logger.debug('Initialize page...');
            $.when(this.initContactTypes(), this.initGenders(),
               this.initMaritalStatuses(), this.initPersonTypes(),
               this.initStates(), this.initStatuses())
            .done(() => {
               this.filterList();
               this.setupNewItem();
               $.trigger('#filter');
            });
         }
         initContactTypes() {
            let deferred = $.Deferred();
            let promise = deferred.promise();
            resource.get(':7000/persons/contact_types')
               .done((response) => {
                  response.data.forEach(element => {
                     this.contactTypes.push({"id": element, "text": message.get(element)})
                  });
                  deferred.resolve(this.contactTypes);
               })
               .fail((response) => {
                  toast.show(response);
                  deferred.reject([{"id":"INVALID", "text":"INVALID"}]);
               });
            return promise;
         }
         initGenders() {
            let deferred = $.Deferred();
            let promise = deferred.promise();
            resource.get(':7000/persons/genders')
            .done((response) => {
               response.data.forEach(element => {
                  this.genders.push({"id": element, "text": message.get(element)})
               });
               deferred.resolve(this.genders);
            })
            .fail((response) => {
               toast.show(response);
               deferred.reject([{"id":"INVALID", "text":"INVALID"}]);
            });
            return promise;
         }
         initMaritalStatuses() {
            let deferred = $.Deferred();
            let promise = deferred.promise();
            resource.get(':7000/persons/marital_statuses')
            .done((response) => {
               response.data.forEach(element => {
                  this.maritalStatuses.push({"id": element, "text": message.get(element)})
               });
               deferred.resolve(this.maritalStatuses);
            })
            .fail((response) => {
               toast.show(response);
               deferred.reject([{"id":"INVALID", "text":"INVALID"}]);
            });
            return promise;
         }
         initPersonTypes() {
            let deferred = $.Deferred();
            let promise = deferred.promise();
            resource.get(':7000/persons/person_types')
               .done((response) => {
                  response.data.forEach(element => {
                     this.personTypes.push({"id": element, "text": message.get(element)})
                  });
                  deferred.resolve(this.personTypes);
               })
               .fail((response) => {
                  toast.show(response);
                  deferred.reject([{"id":"INVALID", "text":"INVALID"}]);
               });
            return promise;
         }
         initStates() {
            let deferred = $.Deferred();
            let promise = deferred.promise();
            resource.get(':7000/persons/states')
            .done((response) => {
               response.data.forEach(element => {
                  this.states.push({"id": element, "text": message.get(element)})
               });
               deferred.resolve(this.states);
            })
            .fail((response) => {
               toast.show(response);
               deferred.reject([{"id":"INVALID", "text":"INVALID"}]);
            });
            return promise;
         }
         initStatuses() {
            let deferred = $.Deferred();
            let promise = deferred.promise();
            resource.get(':7000/persons/statuses')
            .done((response) => {
               response.data.forEach(element => {
                  this.statuses.push({"id": element, "text": message.get(element)})
               });
               deferred.resolve(this.statuses);
            })
            .fail((response) => {
               toast.show(response);
               deferred.reject([{"id":"INVALID", "text":"INVALID"}]);
            });
            return promise;
         }
      }
      new PersonList().initPage();
   });
})();
