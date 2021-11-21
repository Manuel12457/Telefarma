//El JS correrá solo si ya se cargó toda la página
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', ready)
} else {
    ready()
}

function ready() {
    //Busca cambios del quantity para recalcular totales y cantidades cada 100ms
    setInterval(updateCart, 100);

    //Obtiene los botones de borrar producto
    var removeProductButtons = document.getElementsByClassName("btn-danger")
    //Loop para recorrer los botones y darles funcionalidad
    for (var i = 0; i < removeProductButtons.length; i++) {
        var button = removeProductButtons[i]
        button.addEventListener('click', removeProduct)
    }

    // //Obtiene los inputs de quantity para cambiar dinámicamente el precio
    // var quantityInputs = document.getElementsByClassName("cart-quantity")
    // //Loop de los inputs para darles funcionalidad
    // for (var i = 0; i < quantityInputs.length; i++) {
    //     var input = quantityInputs[i]
    //     //Se añadi el eventListener cuando hay un cambio en el valor del input
    //     input.addEventListener('change', quantityChanged)
    // }

}

//Funcion para hacer dinámico el qty y el precio
function quantityChanged(event) {
    var input = event.target
    //Validamos si el input es un número y es positivo
    if (isNaN(input.value) || input.value <= 0) {
        input.value = 1 //si no lo es le ponemos 1, pues es la min cantidad
    }
    updateCart()
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
            var quantity = producto.getElementsByClassName("cart-quantity")[0]
            //Obtenemos los valores de los tags
            price = parseFloat(price.innerText.replace("s/ ", ""))
            quantity = quantity.value
            //Actualizamos el qunatity en el resumen
            document.getElementsByClassName("cart-quantity-resumen")[j].innerText = quantity
            console.log(quantity)
            //Calculos
            var subtotal = Math.round((price * quantity) * 100)/100
            document.getElementsByClassName("cart-subtotal")[j].innerText = "s/ " + subtotal
            document.getElementsByClassName("cart-subtotal-resumen")[j].innerText = "s/ " + subtotal
            total = total + subtotal
        }
    }
    total = Math.round(total * 100)/100
    document.getElementsByClassName("cart-total")[0].innerText = " s/ " + total

}

