
window.onload = function() {
   var form = document.getElementById("delete");

   form.color = "red";

   form.addEventListener('submit', (e) => {
          e.preventDefault();
          fetch("", { method: 'DELETE' });
        });
};
