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

   $.maskInputText = (component, mask, maskOptions) => {
      if(mask && maskOptions) {
         $(component).mask(mask, maskOptions);
      } else if(mask) {
         $(component).mask(mask);
      }
   }

   $.inputText = (component, value, mask, maskOptions) => {
      if(!value) {
         let text = $(component).val();
         $.maskInputText(component, mask, maskOptions);
         return !text ? '' : text;
      } else {
         $(component).val(value);
         $.maskInputText(component, mask, maskOptions);
      }
   };

   $.inputDate = (component, value, minDate, maxDate) => {
      let mask = 'Dd/Mm/YYYY';
      let maskOptions = {
         onInvalid: (val, e, f, invalid, options) => {
            var error = invalid[0];
            logger.info("Digit: " + error.v + " is invalid for the position: " +
               error.p + ". We expect something like: " + error.e);
         },
         translation: {
            'M': {pattern: /[0-1]/},
            'm': {pattern: /[0-9]/},
            'D': {pattern: /[0-3]/},
            'd': {pattern: /[0-9]/},
            'Y': {pattern: /[0-9]/},
            '/': {pattern: /[\/]/, fallback: '/'}
         },
         reverse: false
      };
      let datepickerConfig = {
         constrainInput: true,
         changeMonth: true,
         changeYear: true,
         showAnim: 'fade',
         dateFormat: 'dd/mm/yy',
         dayNames: ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta',
            'Sábado', 'Domingo'],
         dayNamesMin: ['D', 'S', 'T', 'Q', 'Q', 'S', 'S', 'D'],
         dayNamesShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb', 'Dom'],
         monthNames: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
            'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
         monthNamesShort: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago',
            'Set', 'Out', 'Nov', 'Dez']
      };
      if (minDate) {
         datepickerConfig.minDate = minDate;
      }
      if(maxDate) {
         datepickerConfig.maxDate = maxDate;
      }
      if(!value) {
         let text = $(component).val();
         $.maskInputText(component, mask, maskOptions);
         $(component).datepicker(datepickerConfig);
         return !text ? '' : text.toLocalDate();
      } else {
         $(component).val(value.fromLocalDate());
         $.maskInputText(component, mask, maskOptions);
         $(component).datepicker(datepickerConfig);
      }
   }

   $.text = (component, value) => {
      $(component).text(value);
   };

   $.textDate = (component, value) => {
      let date = value ? value.fromLocalDateTime() : '-';
      $(component).text(date);
   };

   $.attribute = (component, attribute, value) => {
      $(component).attr(attribute, value);
   }

   $.checkBox = (component, value) => {
      if(!value) {
         return $(component).is(':checked');
      } else {
         $(component).prop('checked', value);
      }
   };

   $.click = (component, selector, action) => {
      if(!action) {
         $(component).off('click')
            .on('click',
               (event) => {
                  event.preventDefault();
                  event.stopImmediatePropagation();
                  selector(event);
               });
      } else {
         $(component).off('click')
            .on('click', selector,
               (event) => {
                  event.preventDefault();
                  event.stopImmediatePropagation();
                  action(event);
               });
      }
   };

   $.keyup = (component, length, action) => {
      $(component).off('keyup').on('keyup', (event) => {
         if($.inputText(component).length > length) {
            action();
         }
      });
   };

   $.disabled = (component, disabled) => {
      $(component).prop('disabled', disabled);
   }

   $.trigger = (component, value) => {
     if(!value) {
        $(component).trigger('click');
     } else {
        $(component).trigger(component + '-valid', value);
     }
   }

   //values = [{"id":"id_here", "text":"text_here"}]
   $.selectBuilder = (component, value, values, callback) => {
      let options = '',
         selected = '';
      values.forEach(element => {
         selected = value && value === element.id ? 'SELECTED' : '';
         options += '<option value="' + element.id  + '" ' + selected + '>'
            + element.text + '</option>';
      });
      $(component).append(options);
      $(component).trigger(component + '-loaded');
      $(component).off('change').on('change', (event) => {
         let value = $(component + ' option:selected').val();
         if(value) {
            $(component).trigger(component + '-changed-valid', value);
         } else {
            $(component).trigger(component + '-changed-invalid');
         }
      });
      if(callback) {
         callback();
      }
   }

   $.bodyListener = (suffixEventName, actionCallbackValid, actionCallbackInvalid) => {
      $('body').off(suffixEventName)
      .on(suffixEventName + '-valid', (event,data) => {
         actionCallbackValid(event, data);
      })
      .on(suffixEventName + '-invalid', (event, data) => {
         if(actionCallbackInvalid) {
            actionCallbackInvalid(event, data);
         }
      });
   }


})();