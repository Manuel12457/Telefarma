let btn = document.querySelector("#btn-sidebar");
let sidebar = document.querySelector(".sidebar");

btn.onclick = function(){
    sidebar.classList.toggle("active");
}

function readURL(input) {
    if(input.files[0].size > 2097152){
        alert("El archivo es muy grande");
        input.value = "";
        $('#imagenPreview')
            .attr('src', "${pageContext.request.contextPath}/res/img/no-imagen.jpg")
            .width(100)
            .height(100);
    }
    else if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#imagenPreview')
                .attr('src', e.target.result)
                .width(100)
                .height(100);
        };

        reader.readAsDataURL(input.files[0]);
    }
}