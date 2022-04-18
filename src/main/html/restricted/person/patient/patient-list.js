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
            this.fcompanyNameId = "#fcompanyName";
            this.pageNumberId = "#pageNumber"
            // Attributes
            this.fcpf = "";
            this.ffullName = "";
            this.fcontact = "";
            this.fcompanyName = "";
            this.table = {};
            this.contactTypes = []
            this.personTypes = []
            this.states = []
         }
         prefixId() { return '#patientList';  }
         newItemId() { return '#newItem'; }
         uri() { return ':7000/persons';  }
         queryString() {
            this.fcpf = $.inputText(this.fcpfId);;
            this.ffullName = $.inputText(this.ffullNameId);;
            this.fcontact = $.inputText(this.fcontactId);;
            this.fcompanyName = $.inputText(this.fcompanyNameId);;
            return "?f_cpf={0}&f_full_name={1}&f_contact={2}&f_company_name={3}"
               .format(this.fcpf, this.ffullName, this.fcontact,
                  this.fcompanyName);
         }
         columns() {
            return  [
               { 'class': 'details-control', 'defaultContent': '', 'orderable': false, 'data': null  },
               { 'data': 'full_name', 'defaultContent': '', 'orderable': true },
               { 'data': 'personal_id', 'defaultContent': '', 'orderable': true },
               { 'data': 'cpf', 'defaultContent': '', 'orderable': true},
               { 'className': "text-center", 'defaultContent': '', 'orderable': false }
            ];
         }
         columnDefs() {
            return [
               {
                  targets: [ 0 ],
                  //width: "30px",
                  createdCell: function (td, cellData, rowData, row, col) {
                     let html = "<input id='id_" +  row + col
                        + "' type='hidden' value='" + rowData.person_id.id
                        + "' class='id'/>"
                     $(td).html(html);
                  }
               },
               {
                  targets: [ 3 ],
                  createdCell: function (td, cellData, rowData, row, col) {
                     let html = rowData.contacts[0].info;
                     $(td).html(html);
                  }
               },
            ];
         }
         newLine() {
            return '<tr id="newItem1" role="row"><td colspan="5">' +
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
                  this.initComplete)).table();
            let table = this.table;
            let detailRows = [];
            let patientList = this;
            let contactTypes = this.contactTypes;
            let personTypes = this.personTypes;
            let states = this.states;
            let prefixId = this.prefixId();
            $(prefixId + ' tbody').off('click').on(
               'click', 'tr td.details-control', function () {
               let tr = $(this).closest('tr');
               let row = table.row( tr );
               //var idx = $.inArray( tr.attr('id'), detailRows );
               if ( row.child.isShown() ) {
                  row.child.remove();
                  tr.removeClass('shown');
                  tr.removeClass('selected');
               } else {
                  // $(this).prop('id', 'ID');
                  if ( table.row('.shown').length ) {
                     $('#newItem').remove();
                     $('.shown').find('.details-control').click();
                  }
                  patientList.addNewLine(() => {
                     row.child($(patientList.newLine())).show();
                  }, () => {
                     patientDetails.initPage(tr.find('.id').val(), contactTypes,
                        personTypes, states);
                  });
                  tr.addClass('shown');
                  tr.addClass('selected');
               }
            } );
            this.table.on('draw', () => {
               $.each(detailRows, (i, id) => {
                  $.trigger('#' + id + ' td.details-control');
               });
            });
            //    createdRow: function( row, data, dataIndex) {
            //       $(row).attr('data-toggle', 'modal');
            //       $(row).attr('data-target', '#modalEditar');
            //       $(row).attr('onclick', '_configuraModalEditaCheckList("'
            //          + data.id +'")');
            //    },
            // });
            // });
            return this.table;
         }
         filterList() {
            $.click('#filter', (event) => {
               this.datatable(this.uri() + this.queryString());
            });
            // $(this.fdescriptionId).off('keyup').on('keyup', (event) => {
            //    if($.inputText(this.fdescriptionId).length > 4) {
            //       $.trigger('#filter');
            //    }
            // });
         }
         setupNewItem() {
            let clicou = false;
            let procedureList = this;
            let contactTypes = this.contactTypes;
            let personTypes = this.personTypes;
            let states = this.states;
            $.click(this.prefixId() + 'New', (event) => {
               $(procedureList.newItemId()).remove();
               this.addNewLine(() => {
                  $(this.prefixId() + ' tr:first').before(this.newLine());
               }, () => {
                  patientDetails.initPage(null, contactTypes,
                     personTypes, states);
               });
            });
         }
         initPage() {
            $.when(this.initContactTypes(),this.initPersonTypes(),
               this.initStates())
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
         initPersonTypes() {
            var deferred = $.Deferred();
            var promise = deferred.promise();
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
            var deferred = $.Deferred();
            var promise = deferred.promise();
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
      }
      new PersonList().initPage();
   });
})();
