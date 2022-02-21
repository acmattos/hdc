(() => {
   class Toast {
      constructor() {}
      show(response){//TODO RESPONSE AS ARRAY!!!
         if(response.length === 'undefined') {
            response = [response];
         }
         response.forEach((item, index, array) => {

            resource.text('component/toast/toast.html').done((text) => {
               let id = new Date().getTime();
               let toastHtml = text.format(id, id, id);
               $('#toastHolder').prepend(toastHtml);
               $('#toast-code-' + id).text(array[index].status);logger.delete($('.toast-code-' + id).text())
               //logger.alert('Toast#show(response)', array[index], array[index].code);
               $('#toast-body-' + id).text(message.get(array[index].code, array[index].data));
               //logger.debug('Toast#show(response)', array[index].code,message.get('response.code'))
               new bootstrap.Toast($('#toast-' + id)).show();
            });
         });

      }
   }
   window.toast = new Toast();
})();