//El JS correrá solo si ya se cargó toda la página
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', ready)
} else {
    ready()
}

function cambioCantidad(context) {
    var form = document.getElementsByTagName("form")[1]
    form.action = context + '/ClientServlet?action=test'
    form.submit()
}

function ready() {
    //Busca cambios del quantity para recalcular totales y cantidades cada 500ms
    setInterval(updateCart, 500);

    //Obtiene los botones de borrar producto
    var removeProductButtons = document.getElementsByClassName("btn-danger")
    //Loop para recorrer los botones y darles funcionalidad
    for (var i = 0; i < removeProductButtons.length; i++) {
        var button = removeProductButtons[i]
        button.addEventListener('click', removeProduct)
    }

}

//Valida input de cantidad
function validar() {
    document.getElementsByClassName("cart-quantity")[0].addEventListener('keyup', function () {
        var quantity = document.getElementsByClassName("cart-quantity")[0]
        if (quantity.value < 0 || isNaN(quantity.value)) {
            quantity.value = 1
        }
    })
}

//Funcion para eliminar producto
function removeProduct(event) {
    //Obtenemos el boton al que le hicimos el evento 'click'
    var btnClicked = event.target
    //Obtenemos el div del producto y lo borramos
    btnClicked.parentElement.parentElement.remove()
    //Borramos también del resumen
    document.getElementById("item-resumen-" + btnClicked.id.replace("remove-", "")).remove()
    //Actualizar el resumen después del borrado
    updateCart()
}


//Función para actualizar totales y resumen
function updateCart() {
    var total = 0
    //Obtenemos los divs que contienen los productos de cada farmacia
    var cartItemContainers = document.getElementsByClassName("cart-items-container")
    var contador=0;
    //Loop de los listados de productos de las farmacia para obtener sus productos
    for (var i = 0; i < cartItemContainers.length; i++) {
        var cartItemContainer = cartItemContainers[i]
        //Obtenemos los productos de ese div
        var cartItems = cartItemContainer.getElementsByClassName("cart-item")
        //Loop de los productos de cada farmacia para obtener la información
        for (var j = 0; j < cartItems.length; j++) {
            var producto = cartItems[j]
            //Obtenemos los tags
            var price = producto.getElementsByClassName("cart-price")[0]
            validar()
            var quantity = producto.getElementsByClassName("cart-quantity")[0]
            //Obtenemos los valores de los tags
            price = parseFloat(price.innerText.replace("s/ ", ""))
            quantity = quantity.value

            console.log("precio-cantidad Producto "+contador+" de ubicacion "+i+"-"+j+" : "+price+" "+quantity)

            var subtotal = Math.round((price * quantity) * 100) / 100
            var fila = document.getElementById("item-resumen-"+contador)
            fila.cells.item(0).innerHTML = quantity
            fila.cells.item(3).innerHTML = "S/ " + subtotal

            var subtotalproducto = document.getElementsByClassName("cart-subtotal-"+contador)[0]
            subtotalproducto.innerHTML = "S/ " + subtotal

            total = total + subtotal
            contador++
        }
    }
    total = Math.round(total * 100) / 100
    document.getElementsByClassName("cart-total")[0].innerText = " s/ " + total
}