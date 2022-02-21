(() => {
   'use strict';

   const logger = new Logger('component/util/validator.js');
   // let ruleSample = {
   //    empty: { message: 'message id for empty' }, // TODO APPLY THE SAME AS BELLOW
   //    len: { min: 1, max: 50, message: 'messade id for invalid length' }
   // };
   String.prototype.validate = function(rule) {
      let value = this;
      let error = [];
      if (rule) {
         if(rule.empty && !value) {
            return null;
         } else {
            let isInvalidSize = rule && rule.len
               ?  value.length < rule.len.min
               || value.length > rule.len.max : false;
            if (isInvalidSize) {//logger.alert(rule.len.message);
               error.push({'status': 452 , 'code': rule.len.message, 'data': rule.len.message});//TODO CHANGE CODE 452-499	Unassigned
            }
            return error.length == 0 ? null : error;
         }
      } else {
         logger.alert('No available rule to execute String#validate!');
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
            return false;
         }
         return true;
      }
   }
   window.StringValidator = StringValidator;
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
            if(isInvalidEmail) {//logger.alert(rule.format.message);
               error.push({'status': 453 , 'code': rule.format.message, 'data': rule.format.message});
            }
            return error.length == 0 ? null : error;
         }
      } else {
         logger.alert('No available rule to execute String#validateEmail!');
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
            return false;
         }
         return true;
      }
   }
   window.EmailValidator = EmailValidator;
//
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
            let regex = /^(([9]{0,1}[0-9]{8})|([9]{0,1}[0-9]{4})-([0-9]{4}))$/;
            let isInvalidPhone = rule.format ? !regex.test(this) : false;
            if(isInvalidPhone) {//logger.alert(rule.format.message);
               error.push({'status': 454 , 'code': rule.format.message, 'data': rule.format.message});
            }
            return error.length == 0 ? null : error;
         }
      } else {
         logger.alert('No available rule to execute String#validatePhone!');
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
            return false;
         }
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
            if(isInvalidZipCode) {//logger.alert(rule.format.message);
               error.push({'status': 454, 'code': rule.format.message, 'data': rule.format.message});
            }
            return error.length == 0 ? null : error;
         }
      } else {
         logger.alert('No available rule to execute String#validateZipCode!');
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
            return false;
         }
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
         let cpf = value.trim().replace(/\./g, '').replace('-', '').split('');
         let valid = false;
         for (let i = 1; cpf.length > i; i++) {
            if (cpf[i - 1] != cpf[i]) {
               valid = true;
            }
         }

         if (!valid) {//logger.alert(rule.format.message);
            error.push({'status': 455, 'code': rule.format.message, 'data': rule.format.message});
         } else {
            let v1 = 0;
            for (let i = 0, p = 10; (cpf.length - 2) > i; i++, p--) {
               v1 += cpf[i] * p;
            }
            v1 = ((v1 * 10) % 11);
            if (v1 === 10) {
               v1 = 0;
            }
            if (v1 !== cpf[9]) {//logger.alert(rule.format.message);
               error.push({'status': 455, 'code': rule.format.message});
            } else {
               let v2 = 0;
               for (let i = 0, p = 11; (cpf.length - 1) > i; i++, p--) {
                  v2 += cpf[i] * p;
               }
               v2 = ((v2 * 10) % 11);
               if (v2 === 10) {
                  v2 = 0;
               }
               if (v2 !== cpf[10]) {//logger.alert(rule.format.message);
                  error.push({'status': 455, 'code': rule.format.message});
               }
            }
         }
         return error.length == 0 ? null : error;
      } else {
         logger.alert('No available rule to execute String#validateZipCode!');
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
            return false;
         }
         return true;
      }
   }
   window.CpfValidator = ZipCodeValidator;

   // let ruleSample = {
   //    select: { invalid: 'invalid value', message: 'messade id for invalid phonr format' }
   // };
   String.prototype.validateSelect = function(rule) {
      let value = this;
      let error = [];
      if (rule) {
         if(rule.invalid) {
            let regex = /^(([9]{0,1}[0-9]{8})|([9]{0,1}[0-9]{4})-([0-9]{4}))$/;
            let isInvalidSelected = !(value !== rule.invalid && value !== undefined);
            if(isInvalidSelected) {//logger.alert(rule.select.message);
               error.push({'status': 456 , 'code': rule.select.message, 'data': rule.select.message});
            }
            return error.length == 0 ? null : error;
         } else {
            logger.alert('No available rule.invalid to execute String#validateSelect!');
            return null;
         }
      } else {
         logger.alert('No available rule to execute String#validateSelect!');
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
            return false;
         }
         return true;
      }
   }
   window.SelectValidator = PhoneValidator;
})();