(function () {
   'use strict';

   String.prototype.formatMessage = function (...elements) {
      let args = elements[0]; logger.alert('#formatMessage elements, args, this.replace', elements, args,this.replace(/{([0-9]+)}/g, function (match, index) {//logger.alert('#formatMessage', args[index], match)
         return typeof args[index] == 'undefined' ? match : args[index];
      }))
      return this.replace(/{([0-9]+)}/g, function (match, index) {//logger.alert('#formatMessage', args[index], match)
         return typeof args[index] == 'undefined' ? match : args[index];
      });
   };
   String.prototype.format = function () {
      let args = arguments;
      return this.replace(/{([0-9]+)}/g, function (match, index) {
         return typeof args[index] == 'undefined' ? match : args[index];
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
            logger.info('Messages loaded succseefuly!')
         });
         this.messageNotFoundKey = '01FVQ2NP5PKKHVWHP57PKD8N62'
      }
      get(...keys) {
         try {

            logger.debug('to Message#get', keys);
            return this.messages[keys[0]].formatMessage(keys);
         } catch (e) {
            logger.info('Message#get - [EXCEPTION] = ', e, keys);
            logger.alert('Message#get keys[0]', keys[0],this.messages[this.messageNotFoundKey].formatMessage(keys[0]));
            return this.messages[this.messageNotFoundKey].formatMessage(keys[0]);
         }
      }
   }
   window.message = new Message('component/i18n/messages_pt-BR.json');

   // "{"uid":"01FTK4VMJEKMR8GJMY4TH61D3F"," +
   // ""code":"01FK6PF0DWKTN1BYZW6BRHFZFJ","status":500," +
   // ""data":"ULID [1] does not match the expected size: 26 chars long!"}"
   class Response {
      constructor(response) {logger.delete('RAW',response)
         this.uid = response.uid;
         this.code = response.code;
         this.status = response.status;
         this.data = (this.status > 300)
            ? response.data
            : response.data.results;
      }
   }
   window.Response = Response;

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
         logger.debug(config.type, uri, config);
         return this.send(config, done, fail);
      }
      post(uri, payload, done, fail) {
         let config = $.extend( this.config || {}, {
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
         logger.debug(config.type, payload, config);
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
               /*return*/ $.getCachedScript(resource + '.js')
               .done((responseTextCs, textStatusCs,jqXHRcs) => {
                  loader.stop();
                  deferred.resolve(jqXHRcs);
               });
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
               logger.debug('DONE - 2XX', jqXHR.url, jqXHR.status, textStatus, data);
               deferred.resolve(new Response(data));
            },
            /*fail*/(jqXHR, textStatus, errorThrown) => {
               if(fail){
                  fail(jqXHR, textStatus, errorThrown);
               }
               logger.info("FAIL - 4|5XX", jqXHR.responseText, {"jqXHR_status": jqXHR.status,
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

   class Storage {
      constructor() {
         this.storage = sessionStorage;
      }
      set(key, value) {
         this.storage.setItem(key, value);
      }
      get(key) {
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