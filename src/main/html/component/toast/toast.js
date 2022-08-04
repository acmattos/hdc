(() => {
   class Toast {
      constructor() {}
      show(response){
         if(!response.length) {
            response = [response];
         }
         response.forEach((item, index, array) => {
            resource.text('component/toast/toast.html').done((text) => {
               let id = new Date().getTime();
               let toastHtml = text.format(id, id, id);
               $('#toastHolder').prepend(toastHtml);
               $('#toast-code-' + id).text(array[index].status);
               $('#toast-body-' + id).text(message.get(array[index].code, array[index].data));
               new bootstrap.Toast($('#toast-' + id)).show();
            });
         });

      }
   }
   window.toast = new Toast();
})();