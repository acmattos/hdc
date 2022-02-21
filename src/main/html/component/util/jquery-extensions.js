(function () {
   "use strict";

   $.fn.ajaxInterceptor = (config, done, fail, always) => {
      // if(USER) {
      //    config.headers = {
      //       "Authorization": getAuthorization()
      //    }
      // }
      return $.ajax(config)
         .done(function(data, textStatus, jqXHR){
            return done(data, textStatus, jqXHR);
         })
         .fail(function(jqXHR,  textStatus, errorThrown){
            return fail(jqXHR,  textStatus, errorThrown);
         })
         .always(function(dataOrjqXHR, textStatus, jqXHRorErrorThrown){
            return always(dataOrjqXHR, textStatus, jqXHRorErrorThrown);
         });
   };

   $.getCachedScript = (url, options) => {
      options = $.extend( options || {}, {
         dataType: "script",
         cache: false,
         url: url
      });
      // Return the jqXHR object so we can chain callbacks
      return $.ajax(options);
   };

   $.inputText = (component, value) => {
      if(!value) {
         let text = $(component).val();
         return !text ? '' : text;
      } else {
         $(component).val(value);
      }
   };

   $.checkBox = (component, value) => {
      if(value === 'undefined') {
         return $(component).is(':checked');
      } else {
         $(component).prop('checked', value);
      }
   };

   $.click = (component, selector, action) => {
      if(arguments.length === 2) {
         $(component).off('click')
            .on('click', selector,
               (event) => {
                  event.preventDefault();
                  event.stopImmediatePropagation();
                  action(event);
               });
      } else {
         $(component).off('click')
            .on('click', selector,
               (event) => {
                  event.preventDefault();
                  selector(event);
               });
      }
   };
   $.trigger = (component) =>  {
      $(component).trigger('click');
   }
})();