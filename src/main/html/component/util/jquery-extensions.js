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

   $.text = (component, value) => {
      $(component).text(value);
   };

   $.attribute = (component, attribute, value) => {
      $(component).attr(attribute, value);
   }

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

   $.disabled = (component, disabled) => {
      $(component).prop('disabled', disabled);
   }

   $.trigger = (component) => {
      $(component).trigger('click');
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

   // var inicializaSelect = (idComponente, data, id) => {
   //    $(idComponente + " option[value!='']").each((index, element) => {
   //       if(element.value) {
   //          $(element).remove();
   //       }
   //    });
   //    var option = '', val1 = '', val2 = '', selected;
   //    $.each(data, (index, elem) => {
   //       selected = '';
   //       if(idComponente.includes('anormalidadeTipoTO')
   //          || (idComponente.includes('operacao')
   //             && !idComponente.includes('operacaoExecucao'))){
   //          val1 = JSON.stringify(elem).replace(/'/g, '');
   //          val2 = elem.nome;
   //          if(elem.id == id) {
   //             selected = 'SELECTED';
   //          }
   //       } else {
   //          val1 = elem;
   //          val2 = elem;
   //          if(elem === id) {
   //             selected = 'SELECTED';
   //          }
   //       }
   //       option += "<option value='" + val1  + "' " + selected
   //          + ">" + val2 + "</option>";
   //    });
   //    $(idComponente).append(option);
   //    $(idComponente).trigger(idComponente + '-loaded');
   //    $(idComponente).off('change').change((event) => {
   //       var valor = $(idComponente + ' option:selected').val();
   //       if(valor) {
   //          valor = valor.includes('{') ? JSON.parse(valor) : valor;
   //          $(idComponente).trigger(idComponente + '-valid', valor);
   //       } else {
   //          $(idComponente).trigger(idComponente + '-invalid');
   //       }
   //    });
   //    if($(idComponente + ' option').length === 1) {
   //       $(idComponente).prop('disabled', true);
   //    } else if($(idComponente + ' option').length === 2) {
   //       $(idComponente + ' :nth-child(2)').prop('selected', true);
   //       $(idComponente).trigger('change');
   //    }
   // }
   //
   // var inicializaSelectEscravo = (idComponenteMestre, idComponenteEscravo,
   //                                callbackComponenteEscravo) => {
   //    $('body')
   //       .off(idComponenteMestre + '-valid')
   //       .on(idComponenteMestre + '-valid', (event, selector, data) => {
   //          if(idComponenteMestre === '#setorOperacionalTO'){
   //             $("#rotaTO option:first").attr('selected', 'selected');
   //             $('#rotaTO').prop('disabled', true);
   //             $('#rotaTO').trigger('#rotaTO-invalid');
   //          } else if (idComponenteMestre === '#rotaTO') {
   //             $("#setorOperacionalTO option:first").attr('selected', 'selected');
   //             $('#setorOperacionalTO').prop('disabled', true);
   //             $('#setorOperacionalTO').trigger('#setorOperacionalTO-invalid');
   //          } else if(idComponenteMestre === '#paSetorOperacionalTO'){
   //             $("#paRotaTO option:first").attr('selected', 'selected');
   //             $('#paRotaTO').prop('disabled', true);
   //             $('#paRotaTO').trigger('#paRotaTO-invalid');
   //          } else if (idComponenteMestre === '#paRotaTO') {
   //             $("#paSetorOperacionalTO option:first").attr('selected','selected');
   //             $('#paSetorOperacionalTO').prop('disabled', true);
   //             $('#paSetorOperacionalTO').trigger('#paSetorOperacionalTO-invalid');
   //          }
   //
   //          $.when(callbackComponenteEscravo(selector))
   //             .done((tos) => {
   //                $('body')
   //                   .off(idComponenteEscravo + '-loaded')
   //                   .on(idComponenteEscravo + '-loaded', (event, selector, data) => {
   //                      $(idComponenteEscravo).prop('disabled',false);
   //                   });
   //                inicializaSelect(idComponenteEscravo, tos);
   //             });
   //       })
   //       .off(idComponenteMestre + '-invalid')
   //       .on(idComponenteMestre + '-invalid', (event) => {
   //          if(idComponenteMestre === '#setorOperacionalTO'){
   //             $('#rotaTO').prop('disabled', '');
   //          } else if (idComponenteMestre === '#rotaTO') {
   //             $('#setorOperacionalTO').prop('disabled', false);
   //          } else if(idComponenteMestre === '#paSetorOperacionalTO'){
   //             $('#paRotaTO').prop('disabled', '');
   //          } else if (idComponenteMestre === '#paRotaTO') {
   //             $('#paSetorOperacionalTO').prop('disabled', false);
   //          }
   //          $(idComponenteEscravo).prop('disabled',true);
   //          $(idComponenteEscravo + " option[value!='']").each((index, element) => {
   //             if(element.value) {
   //                $(element).remove();
   //             }
   //          });
   //          $(idComponenteEscravo).trigger(idComponenteEscravo + '-invalid');
   //       });
   // }

})();