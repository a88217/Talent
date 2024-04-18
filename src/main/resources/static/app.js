
function form_check() {
    var inputs = document.querySelectorAll('input');
    inputs.forEach((inp) => {

        if(inp.value === "") {
            inp.disabled = true;
        }
    })

}

