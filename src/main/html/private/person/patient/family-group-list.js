(() => {
   'use strict';

   const logger = new Logger('private/person/patient/family-group-list.js');
   class FamilyGroupList {
      constructor(patient) {
         // Attributes
         this.patient = patient;
         this.familyGroupDetails = null;
         this.table = {};
      }
      fromPatient() {
         let personIds = '';
         this.patient.familyGroup.forEach((item, index, array) => {
            personIds += (array[index].id ? array[index].id : array[index]) + ',';
         });
         return personIds;
      }
      toPatient(personId) {
         this.patient.familiGroup.push(personId);
      }
      prefixId() { return '#familyGroupList'; }
      newItemId() { return '#newTrFamilyItem'; }
      uri() { return '/persons'; }
      queryString() {
         return "?f_person_ids={0}".format(this.fromPatient());
      }
      columns() {
         return  [
            { 'class': 'details-control', 'defaultContent': '', 'orderable': false, 'data': null  },
            { 'data': 'full_name', 'defaultContent': '', 'orderable': true },
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
               width: "15px",
            },
         ];
      }
      newLine() {
         return '<tr id="newTrFamilyItem" role="row"></tr>';
      }
      addNewLine(addNewLineCallback, executeAfterLoadCallback) {
         addNewLineCallback();
         resource.component(this.newItemId(),
            'private/person/patient/family-group-details')
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
         let patient = this.patient;
         let familyGroupList = this;
         let prefixId = this.prefixId();
         let newItemId = this.newItemId;
         $(prefixId + ' tbody').off('click').on(
            'click', 'tr td.details-control', function () {
               let tr = $(this).closest('tr');
               let row = datatable.row(tr);
               let dtTr = new DtTr(datatable, tr, newItemId());
               dtTr.toggleRow(
                  () => {
                     familyGroupList.addNewLine(
                        () => {
                           row.child($(familyGroupList.newLine())).show();
                        },
                        () => {
                           familyGroupDetails.initPage(dtTr, patient, tr.find('.id').val());
                        }
                     );
                  }
               );
            }
         );
         datatable.on('draw', () => {
            $.each(detailRows, (i, id) => {
               $.trigger('#' + id + ' td.details-control');
            });
         });
         return datatable;
      }
      filterList() {
         $.click('#filterFM', (event) => {
            this.datatable(this.uri() + this.queryString());
         });
      }
      setupNewItem() {
         let familyGroupList = this;
         let patient = this.patient;
         let table = this.table;
         $.click(this.prefixId() + 'New', (event) => {
            $(familyGroupList.newItemId()).remove();
            this.addNewLine(() => {
               $(this.newItemId()).remove();
               $(this.prefixId() + ' tr:first').before(this.newLine());
            }, () => {
               familyGroupDetails.initPage(table, patient);
            });
         });
      }
      initPage() {
         logger.debug('Initialize page...');
         this.setupNewItem();
         this.filterList();
         $('#filterFM').hide();
         $.trigger('#filterFM');
      }
   }
   window.FamilyGroupList = FamilyGroupList;
})();
