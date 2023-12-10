(() => {
   'use strict';

   const logger = new Logger('component/info/info-details.js');

   class Info {
      constructor(infoMap) {
          this.infoMap = {"infoMap": infoMap};
      }
      static read(infoMap) {
         let deferred = $.Deferred();
         let promise = deferred.promise();
         resource.script('component/template/template')
         .done(() => {
            deferred.resolve(infoMap);
         })
         .fail((error) => {
            toast.show(error);
            deferred.reject(null);
         });
         return promise;
      }
      toPage(){
         $.text("#modalTitle", 'Data e Hora de Manipulação');
         $("#infoDiv")
         .append(template("infoScript",  this.infoMap));
      }
   }
   class InfoDetails {
      constructor() {
         this.info = null;
      }
      initPage(infoMap) {
         resource.component('#modalBody', 'component/info/info-details')
         .done(() => {
            logger.debug('Initialize page..., [Info Details]', infoMap);
            this.infoMap = infoMap;
            Info.read(infoMap)
            .done((im) => {
               this.info = new Info(im);
               this.info.toPage();
            });
         });
      }
   }
   window.infoDetails = new InfoDetails();
})();
