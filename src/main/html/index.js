(() => {
   'use strict';

   $.getCachedScript('component/util/hdc-utils.js').done(() => {
      class Loader {
         constructor() {
            this.calls = [];
         }
         start() {
            $('#loader').addClass('is-active');
            this.calls.push("call");
            return this;
         }
         stop() {
            this.calls.shift();
            if(this.calls.length === 0) {
               $('#loader').removeClass('is-active');
            }
            return this;
         }
      };
      window.loader = new Loader();

      class Resource {
         constructor() {}
         getFullUrl(uri) {
            return http.getFullUrl(uri);
         }
         script(resource) {
            return http.script(resource);
         }
         html(component, resource) {
            return http.html(component, resource);
         }
         text(resource) {
            return http.text(resource);
         }
         component(component, resource) {// TODO Redirect to Home if not in the right place: $('#workspace')[0].outerHTML
            return http.component(component, resource);
         }
         get(uri, done, fail) {
            return http.get(uri, done, fail);
         }
         post(uri, payload, done, fail) {
            return http.post(uri, payload, done, fail);
         }
      }
      window.resource = new Resource();

      $.when(
         // resource.component('#toastHolder', 'component/toast/toast'),
         resource.script('component/toast/toast'),
         resource.script('component/util/validator'),
         resource.component('#workspace', 'restricted/person/patient/patient-list')
      ).done(() => {
         let logger = new Logger("index.js");
         logger.info('HDC is now loaded.', window.location);
      });
   });
})();

