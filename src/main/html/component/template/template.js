(() => {
   'use strict';

   const logger = new Logger('component/template/template.js');

   const cache = {};
   this.template = function template(templateId, json){
      let fcn = !/\W/.test(templateId)
         ? cache[templateId] = cache[templateId]
            || template(
                  document.getElementById(templateId).innerHTML.replace("'",'"')
               )
         : new Function("obj",
            "let p=[];"
            + "with(obj){p.push('"
            + templateId
                .replace(/[\r\t\n]/g, "")
                .split("<%").join("\t")
                .replace(/((^|%>)[^\t]*)/g, "$1\r")
                .replace(/\t=(.*?)%>/g, "', $1, '")
                .split("\t").join("');")
                .split("%>").join("p.push('")
                .split("\r").join("")
            + "');} return p.join('');"
           );
      return json ? fcn(json) : fcn;
   };
})();
