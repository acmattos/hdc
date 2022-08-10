(function () {
   'use strict';

   String.prototype.format = function() {
      let args = arguments;
      return this.replace(/{(\d+)}/g, function (match, index) {
         return typeof args[index] === 'undefined' ? match : args[index];
      });
   };

   class Logger {
      constructor(script) {
         this.script = script;
      }
      alert(...object) {
         alert(this.script + " - " + JSON.stringify(object));
      }
      debug(...object) {
         this.print(this.script ,  "-DBUG:", object);
      }
      delete(...object) {
         this.print(this.script ,  "-DELT:", object);
      }
      info(...object) {
         this.print(this.script ,  "-INFO:", object);
      }
      error(...object) {
         this.print(this.script ,  "-ERRO:", object);
      }
      print(script, level, object) {
         console.log(script + level, object)
      }
   }
   window.Logger = Logger;
   window.logger = new Logger('component/util/hdc-utils.js');

   class Message {
      constructor(filePath) {
         $.getJSON(filePath, (data) => {
            this.messages = data;
            logger.info('Messages loaded successfuly!', this.messages)
         });
         this.messageNotFoundKey = '01FVQ2NP5PKKHVWHP57PKD8N62'
      }
      get(key, ...replaceables) {
         try {
            return this.messages[key].format(replaceables);
         } catch (e) {
            logger.info('Message#get - [EXCEPTION] = ', e, key);
            return this.messages[this.messageNotFoundKey].format(key);
         }
      }
   }
   window.message = new Message('component/i18n/messages_pt-BR.json');

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
   }
   window.loader = new Loader();

   // "{"uid":"01FTK4VMJEKMR8GJMY4TH61D3F"," +
   // ""code":"01FK6PF0DWKTN1BYZW6BRHFZFJ","status":500," +
   // ""data":"ULID [1] does not match the expected size: 26 chars long!"}"
   class Response {
      constructor(response) {
         this.uid = response.uid;
         this.code = response.code;
         this.status = response.status;
         this.data = (this.status > 300)
            ? response.data
            : response.data.results;
      }
   }
   window.Response = Response;

   class Router {
      constructor() {
         this.routeMap = new Map();
         this.setupRoutes();
         this.listenToRouteEvents();
      }
      setupRoutes() {
         this.routeMap.set('PROCEDURE', 'restricted/procedure/procedure-list');
         this.routeMap.set('PATIENT', 'restricted/person/patient/patient-list');
      }
      listenToRouteEvents() {
         $.bodyListener('route',
            (event, data) => {
               resource.component('#workspace', this.routeMap.get(data));
            },
            (event) => {
               resource.component('#workspace', this.routeMap.get(data));
            }
         );
      }
      redirect(route) {
         $('#menu').trigger('route-valid', route);
      }
   }
   window.router = new Router();

   class Http {
      constructor() {
         this.baseUrl = window.location.protocol + '//' + window.location.hostname;// + ':8080/service';
         this.config = {
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            acceptEncoding: "gzip, deflate",
            beforeSend: function(jqXHR, settings) {
               jqXHR.url = settings.url;
            }
         };
      }
      getFullUrl(uri) {
         return this.baseUrl + uri
      }
      get(uri, done, fail) {
         let config = $.extend( this.config || {}, {
            type: 'GET',
            url: this.getFullUrl(uri)
         });
         //logger.debug(config.type, uri, config);
         return this.send(config, done, fail);
      }
      post(uri, payload, done, fail) {
         let config = $.extend( this.config || {}, {
            type: 'POST',
            url: this.getFullUrl(uri)
         });
         //var config = {
            // url: this.baseUrl + uri
            // type: method,
            // dataType: dataType,
            // contentType: "application/json; charset=utf-8",
            // acceptEncoding: "gzip, deflate",
            // beforeSend: function(jqXHR, settings) {
            //    jqXHR.url = settings.url;
            // }
         //}
         if(payload) {
            config.data = JSON.stringify(payload);
         }
         //logger.debug(config.type, payload, config);
         return this.send(config, done, fail);
      }
      put(uri, payload, done, fail) {
         let config = $.extend( this.config || {}, {
            type: 'PUT',
            url: this.getFullUrl(uri)
         });
         if(payload) {
            config.data = JSON.stringify(payload);
         }
         //logger.debug(config.type, payload, config);
         return this.send(config, done, fail);
      }
      delete(uri, payload, done, fail) {
         let config = $.extend( this.config || {}, {
            type: 'DELETE',
            url: this.getFullUrl(uri)
         });
         if(payload) {
            config.data = JSON.stringify(payload);
         }
         //logger.debug(config.type, payload, config);
         return this.send(config, done, fail);
      }
      script(resource) {
         loader.start();
         var deferred = $.Deferred();
         var promise = deferred.promise();
         $.getCachedScript(resource + '.js').done(() => {
            deferred.resolve();
            loader.stop();
         });
         return promise;
      }
      html(component, resource) {// TODO Redirect to Home if not in the right place: $('#workspace')[0].outerHTML
         loader.start();
         var deferred = $.Deferred();
         var promise = deferred.promise();
         $(component).load(resource + '.html', null,
            (responseText, textStatus, jqXHR) => {
               deferred.resolve(jqXHR);
               loader.stop();
            }
         );
         return promise;
      }
      text(resource) {
         loader.start();
         var deferred = $.Deferred();
         var promise = deferred.promise();
         $.get(resource, (data) => {
            deferred.resolve(data);
            loader.stop();
         }, 'text');
         return promise;
      }
      component(component, resource) {// TODO Redirect to Home if not in the right place: $('#workspace')[0].outerHTML
         loader.start();
         let deferred = $.Deferred();
         let promise = deferred.promise();
         $(component).load(resource + '.html', /*data*/null,
            /*complete*/(responseText, textStatus, jqXHR) => {
               if (textStatus == "error") {
                  logger.alert('404 - ' + resource + '.html');
                  loader.stop();
               } else {
                  $.getCachedScript(resource + '.js')
                  .done((responseTextCs, textStatusCs,jqXHRcs) => {
                     if (textStatusCs == "error") {
                        logger.alert('404 - ' + resource + '.js');
                     }
                     loader.stop();
                     deferred.resolve(jqXHRcs);
                  });
               }
            }
         );
         return promise;
      }
      send(config, done, fail) {
         loader.start();
         var deferred = $.Deferred();
         var promise = deferred.promise();
         $().ajaxInterceptor(config,
            /*done*/(data, textStatus, jqXHR) => {
               if(done){
                  done(data, textStatus, jqXHR);
               }
               //logger.debug('DONE - 2XX', jqXHR.url, jqXHR.status, textStatus, data);
               deferred.resolve(new Response(data));
            },
            /*fail*/(jqXHR, textStatus, errorThrown) => {
               if(fail){
                  fail(jqXHR, textStatus, errorThrown);
               }
               logger.info("FAIL - 4XX/5XX", jqXHR.responseText, {"jqXHR_status": jqXHR.status,
                  "jqXHR_responseText": jqXHR.responseText, "textStatus": textStatus,
                  "errorThrown": errorThrown.url }, jqXHR, errorThrown);

               // if(jqXHR.status === 401){
               //    //return ;// TODO REDIRECT TO LOGIN?
               // }
               deferred.reject(new Response(JSON.parse(jqXHR.responseText)));
            },
            /*always*/(dataOrjqXHR, textStatus, jqXHRorErrorThrown) => {
                loader.stop();
            }
         );
         return promise;
      }
   }
   window.http = new Http();

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
      put(uri, payload, done, fail) {
         return http.put(uri, payload, done, fail);
      }
      delete(uri, payload, done, fail) {
         return http.delete(uri, payload, done, fail);
      }
   }
   window.resource = new Resource();

   class Storage {
      constructor() {
         this.storage = sessionStorage;
      }
      set(key, value) {
         this.storage.setItem(key, value);
      }
      get(key) {alert(key)
         this.storage.get(key);
      }
      remove(key) {
         this.storage.removeItem(key);
      }
   }
   window.storage = new Storage();

   class Timer {
      constructor(begin) {
         this.begin = !begin ? performance.now() : begin;
         this.end = 0;
      }
      start() {
         this.begin = performance.now();
         return this;
      }
      stop() {
         this.end = performance.now();
         return this;
      }
      partial() {
         return performance.now() - this.begin;
      }
      elapsed() {
         this.stop();
         return this.end - this.begin;
      }
   }
   window.Timer = Timer;
   window.timer = new Timer();
})();

