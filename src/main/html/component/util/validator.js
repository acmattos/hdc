(() => {
   'use strict';

   const logger = new Logger('component/util/validator.js');
   // let ruleSample = {
   //    empty: {},
   //    len: { min: 1, max: 50, message: 'messade id for invalid length' },
   //    regex: { exp: /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i, message: 'messade id for invalid length' },
   //    space: { inner: "N", message: 'messade id for invalid length' }
   // };
   String.prototype.validate = function(rule) {
      let value = this;
      let error = [];
      if (rule) {
         if(rule.empty && !value) {
            return null;
         } else {
            let isInvalidSize = rule.len
               ?  value.length < rule.len.min
               || value.length > rule.len.max : false;
            if (isInvalidSize) {
               error.push({'status': 452 , 'code': rule.len.message, 'data': rule.len.message});
            }
            let hasInvalidSpace = rule.space && rule.space.inner
               ? rule.space.inner.toLowerCase() == "n"
                  ? value.split(' ').length != 1
                  : false
               : false;
            if (hasInvalidSpace) {
               error.push({'status': 452 , 'code': rule.space.inner.message, 'data': rule.space.inner.message});
            }
            let noRegexMatch = rule.regex && rule.regex.exp
               ? !value.match(rule.regex)
               : false;
            if (noRegexMatch) {
               error.push({'status': 452 , 'code': rule.regex.message, 'data': rule.regex.message});
            }
            return error.length == 0 ? null : error;
         }
      } else {
         logger.alert('No available <rule> to execute String#validate!');
         return null;
      }
   };
   class StringValidator {
      constructor(component, rule) {
         this.component = component;
         this.rule = rule;
         this.value = null;
      }
      validate(disableToast) {
         this.value = $.inputText(this.component);
         let errors = this.value.validate(this.rule);
         if(errors) {
            if(!disableToast) {
               toast.show(errors);
            }
            $(this.component).addClass('invalid');
            return false;
         }
         $(this.component).removeClass('invalid');
         this.value = ("" == this.value) ? null : this.value;
         return true;
      }
   }
   window.StringValidator = StringValidator;
   // let ruleSample = {
   //    between: { min: 1, max: 50, message: 'messade id for invalid length' }
   // };
   String.prototype.validateInt = function(rule) {
      let error = [];
      try {
         let value = parseInt(this);
         if (rule) {
            if(isNaN(value)) {
               error.push({'status': 453 , 'code': rule.between.message, 'data': rule.between.message});
               logger.info('Invalid int number!', this);
               return error;
            }
            if(rule.between) {
               let isInvalidRange =  value < rule.between.min
                  || value > rule.between.max;
               if (isInvalidRange) {
                  error.push({'status': 453 , 'code': rule.between.message, 'data': rule.between.message});
               }
               return error.length == 0 ? null : error;
            }
         } else {
            logger.alert('No available <rule> to execute String#validateInt!');
            return null;
         }
      } catch(e) {
         error.push({'status': 453 , 'code': rule.between.message, 'data': rule.between.message});
         logger.info('Invalid int number!', this, e);
         return  error;
      }
   };
   class IntValidator {
      constructor(component, rule) {
         this.component = component;
         this.rule = rule;
         this.value = null;
      }
      validate(disableToast) {
         this.value = $.inputText(this.component);
         let errors = this.value.validateInt(this.rule);
         if(errors) {
            if(!disableToast) {
               toast.show(errors);
            }
            $(this.component).addClass('invalid');
            return false;
         }
         $(this.component).removeClass('invalid');
         this.value = parseInt(this.value);
         return true;
      }
   }
   window.IntValidator = IntValidator;
   // let ruleSample = {
   //    empty: {},
   //    format: { message: 'messade id for invalid email format' }
   // };
   String.prototype.validateEmail = function(rule) {
      let value = this;
      let error = [];
      if (rule) {
         if(rule.empty && !value) {
            return null;
         } else {
            let regex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            let isInvalidEmail = rule.format ? !regex.test(this) : false;
            if(isInvalidEmail) {
               error.push({'status': 454 , 'code': rule.format.message, 'data': rule.format.message});
            }
            return error.length == 0 ? null : error;
         }
      } else {
         logger.alert('No available <rule> to execute String#validateEmail!');
         return null;
      }
   };
   class EmailValidator {
      constructor(component, rule) {
         this.component = component;
         this.rule = rule;
         this.value = null;
      }
      validate(disableToast) {
         this.value = $.inputText(this.component);
         let errors = this.value.validateEmail(this.rule);
         if(errors) {
            if(!disableToast) {
               toast.show(errors);
            }
            $(this.component).addClass('invalid');
            return false;
         }
         $(this.component).removeClass('invalid');
         return true;
      }
   }
   window.EmailValidator = EmailValidator;

   // let ruleSample = {
   //    empty: {},
   //    format: { message: 'messade id for invalid phonr format' }
   // };
   String.prototype.validatePhone = function(rule) {
      let value = this;
      let error = [];
      if (rule) {
         if(rule.empty && !value) {
            return null;
         } else {
            let regex = /^(\(\d{2}\))( )(\d{4,5})(-)(\d{4})$/;
            let isInvalidPhone = rule.format ? !regex.test(this) : false;
            if(isInvalidPhone) {
               error.push({'status': 455 , 'code': rule.format.message, 'data': rule.format.message});
            }
            return error.length == 0 ? null : error;
         }
      } else {
         logger.alert('No available <rule> to execute String#validatePhone!');
         return null;
      }
   };
   class PhoneValidator {
      constructor(component, rule) {
         this.component = component;
         this.rule = rule;
         this.value = null;
      }
      validate(disableToast) {
         this.value = $.inputText(this.component);
         let errors = this.value.validatePhone(this.rule);
         if(errors) {
            if(!disableToast) {
               toast.show(errors);
            }
            $(this.component).addClass('invalid');
            return false;
         }
         $(this.component).removeClass('invalid');
         return true;
      }
   }
   window.PhoneValidator = PhoneValidator;

   // let ruleSample = {
   //    empty: {},
   //    format: { message: 'messade id for invalid phonr format' }
   // };
   String.prototype.validateZipCode = function(rule) {
      let value = this;
      let error = [];
      if (rule) {
         if(rule.empty && !value) {
            return null;
         } else {
            let regex = /^(([0-9]{8})|([0-9]{3})-([0-9]{5}))$/;
            let isInvalidZipCode = rule.format ? !regex.test(this) : false;
            if(isInvalidZipCode) {
               error.push({'status': 456, 'code': rule.format.message, 'data': rule.format.message});
            }
            return error.length == 0 ? null : error;
         }
      } else {
         logger.alert('No available <rule> to execute String#validateZipCode!');
         return null;
      }
   };
   class ZipCodeValidator {
      constructor(component, rule) {
         this.component = component;
         this.rule = rule;
         this.value = null;
      }
      validate(disableToast) {
         this.value = $.inputText(this.component);
         let errors = this.value.validateZipCode(this.rule);
         if(errors) {
            if(!disableToast) {
               toast.show(errors);
            }
            $(this.component).addClass('invalid');
            return false;
         }
         $(this.component).removeClass('invalid');
         return true;
      }
   }
   window.ZipCodeValidator = ZipCodeValidator;
   // let ruleSample = {
   //    format: { message: 'messade id for invalid phonr format' }
   // };
   String.prototype.validateCpf = function(rule) {
      let value = this;
      let error = [];
      if (rule) {
         let cpf = value.trim().replace(/\./g, '').replace('-', '');
         let valid = false;
         for (let i = 1; cpf.length > i; i++) {
            if (cpf[i - 1] != cpf[i]) {
               valid = true;
            }
         }
         if (!valid) {
            error.push({'status': 457, 'code': rule.format.message, 'data': rule.format.message});
         } else {
            let v1 = 0;
            for (let i = 0, p = 10; (cpf.length - 2) > i; i++, p--) {
               v1 += cpf[i] * p;
            }
            v1 = ((v1 * 10) % 11);
            if (v1 === 10) {
               v1 = 0;
            }
            if (v1 != cpf[9]) {
               error.push({'status': 457, 'code': rule.format.message});
            } else {
               let v2 = 0;
               for (let i = 0, p = 11; (cpf.length - 1) > i; i++, p--) {
                  v2 += cpf[i] * p;
               }
               v2 = ((v2 * 10) % 11);
               if (v2 === 10) {
                  v2 = 0;
               }
               if (v2 != cpf[10]) {
                  error.push({'status': 457, 'code': rule.format.message});
               }
            }
         }
         return error.length == 0 ? null : error;
      } else {
         logger.alert('No available <rule> to execute String#validateZipCode!');
         return null;
      }
   };
   class CpfValidator {
      constructor(component, rule) {
         this.component = component;
         this.rule = rule;
         this.value = null;
      }
      validate(disableToast) {
         this.value = $.inputText(this.component);
         let errors = this.value.validateCpf(this.rule);
         if(errors) {
            if(!disableToast) {
               toast.show(errors);
            }
            $(this.component).addClass('invalid');
            return false;
         }
         $(this.component).removeClass('invalid');
         return true;
      }
   }
   window.CpfValidator = CpfValidator;

   // let ruleSample = {
   //    select: { invalid: 'invalid value', message: 'messade id for invalid phonr format' }
   // };
   String.prototype.validateSelect = function(rule) {
      let value = this;
      let error = [];
      if (rule) {
         if(rule.select.invalid) {
            let regex = /^(([9]{0,1}[0-9]{8})|([9]{0,1}[0-9]{4})-([0-9]{4}))$/;
            let isInvalidSelected = !(value !== rule.invalid && value !== undefined);
            if(isInvalidSelected) {
               error.push({'status': 458 , 'code': rule.select.message, 'data': rule.select.message});
            }
            return error.length == 0 ? null : error;
         } else {
            logger.alert('No available <rule.select.invalid> to execute String#validateSelect: ' + value);
            return null;
         }
      } else {
         logger.alert('No available <rule> to execute String#validateSelect: ' + value);
         return null;
      }
   };
   class SelectValidator {
      constructor(component, rule) {
         this.component = component;
         this.rule = rule;
         this.value = null;
      }
      validate(disableToast) {
         this.value = $(this.component + ' option:selected').val();
         let errors = this.value.validateSelect(this.rule);
         if(errors) {
            if(!disableToast) {
               toast.show(errors);
            }
            $(this.component).addClass('invalid');
            return false;
         }
         $(this.component).removeClass('invalid');
         return true;
      }
   }
   window.SelectValidator = SelectValidator;
   // let ruleSample = {
   //    empty: {},
   //    between: { min: dateMin, max: dateMax, message: 'messade id for invalid date' }
   // };
   String.prototype.validateDate = function(rule) {
      let value = this;
      let error = [];
      if (rule) {
         if(rule.empty && !value) {
            return null;
         } else if (rule.between) {
            value = value.toDate();
            let isInvalidRange = value.getTime() < rule.between.min.getTime()
               || value.getTime() > rule.between.max.getTime();
            if ('Invalid Date' == value || isInvalidRange) {
               error.push({'status': 459 , 'code': rule.between.message, 'data': rule.between.message});
            }
            return error.length == 0 ? null : error;
         }
      } else {
         logger.alert('No available <rule> to execute String#validateZipCode!');
         return null;
      }
   };
   String.prototype.toDate = function() {
      let value = this;
      if(value.includes("/")) {
         let elements = value.split("/");
         return new Date(elements[2], parseInt(elements[1]) - 1, elements[0],
            0, 0, 0);
      } else {
         return new Date(value);
      }
   }
   String.prototype.toLocalDate = function() {
      let value = this;
      if(value.includes("/")) {
         return value.toDate().toISOString().split("T")[0];
      } else {
         return new Date(value);
      }
   }
   String.prototype.fromLocalDate = function() {
      let value = this;
      if(value.includes("-")) {
         let date = new Date(value);
         return date.toBrString();
      } else {
         return new Date(value);
      }
   }
   String.prototype.fromLocalDateTime = function() {
      let value = this;
      if(value.includes("-")) {
         let date = new Date(value);
         return date.toBrString() + ' ' + date.toLocaleTimeString('pt-BR');
      } else {
         return '';
      }
   }
   Date.prototype.toBrString = function () {
      let value = this;
      let month = '0' + (value.getMonth() + 1);
      month = month.substr(month.length - 2);
      let day = '0' + this.getDate();
      day = day.substr(day.length -2);
      return day + "/" + month + "/" + value.getFullYear();
   }
   class DateValidator {
      constructor(component, rule) {
         this.component = component;
         this.rule = rule;
         this.value = null;
      }
      validate(disableToast) {
         this.value = $.inputText(this.component);
         let errors = this.value.validateDate(this.rule);
         if(errors) {
            if(!disableToast) {
               toast.show(errors);
            }
            $(this.component).addClass('invalid');
            return false;
         }
         $(this.component).removeClass('invalid');
         this.value = this.value.toLocalDate();
         return true;
      }
   }
   window.DateValidator = DateValidator;
   // let ruleSample = {
   //    empty: {},
   //    min: { value='1', message: 'messade id for invalid phonr format' },
   //    max: { value='10', message: 'messade id for invalid phonr format' },
   // };
   Array.prototype.validate = function(rule) {
      let value = this;
      let error = [];
      if (rule) {
         if(rule.empty && !value.length) {
            return null;
         } else {
            //logger.alert(rule.min.value)
            //logger.alert('value.length=', value.length)
            //logger.alert(value.length < rule.min.value)
            if (rule.min && value.length < rule.min.value) {
               error.push({'status': 460, 'code': rule.min.message, 'data': rule.min.message});
               logger.info('Invalid array min size!', this);
               // return error;
            }
            if (rule.max && value.length > rule.max.value) {
               error.push({'status': 460, 'code': rule.max.message, 'data': rule.max.message});
               logger.info('Invalid array max size!', this);
               // return error;
            }
            return error.length == 0 ? null : error;
         }
      } else {
         logger.alert('No available <rule> to execute String#validateZipCode!');
         return null;
      }
   };
   class ArrayValidator {
      constructor(component, rule) {
         this.component = component;
         this.rule = rule;
         this.value = null;
      }
      validate(disableToast) {
         this.value = this.component;
         let errors = this.value.validate(this.rule);
         if(errors) {
            if(!disableToast) {
               toast.show(errors);
            }
            $(this.component).addClass('invalid');
            return false;
         }
         $(this.component).removeClass('invalid');
         return true;
      }
   }
   window.ArrayValidator = ArrayValidator;
   // return {
   //    'status': 461,
   //    'code':'01FWKTXVD727PHS1MQ52NS823F',
   //    'data':'01FWKTXVD727PHS1MQ52NS823F'
   // };
   class FunctionValidator {
      constructor(value, componentsCB, validateCB) {
         this.value = value;
         this.components = componentsCB();
         this.validateCB = validateCB;
      }
      validate(disableToast) {
         let errors = this.validateCB(this.value);
         if(errors) {
            if(!disableToast) {
               toast.show(errors);
            }
            this.components.forEach((item, index, array) => {
               $(array[index]).addClass('invalid');
            });
            return false;
         }
         this.components.forEach((item, index, array) => {
            $(array[index]).removeClass('invalid');
         });
         return true;
      }
   }
   window.FunctionValidator = FunctionValidator;
})();