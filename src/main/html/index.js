(() => {
   'use strict';

   $.getCachedScript('component/util/hdc-utils.js').done(() => {
      $.when(
         resource.script('component/toast/toast'),
         resource.script('component/util/validator'),
         resource.component('#workspace', 'restricted/person/patient/patient-list')
         //resource.component('#workspace', 'restricted/procedure/procedure-list')
      ).done(() => {
         new Logger("index.js").info('HDC is now loaded.', window.location);
         $('#modalCloseButton').off('click').on('click', (event) => {
            $.trigger('#modalCloseButton', 'close');
         });
      });
   });
})();

