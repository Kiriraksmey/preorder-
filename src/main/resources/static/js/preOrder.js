//////Chose your product//////////////////////

// function displayProductCategory(id, elementValue) {
//     document.getElementById(id).style.display = elementValue.value == 1 ? 'block' : 'none';
// }

//////Chose your model//////////////////////


function selectIphone14ProMax() {
    var element = document.getElementById("iphone14max");
    console.log(element);
    element.style.borderColor = "#AEAEAE";
    element.style.color = "#000000";
    element.style.backgroundColor = "#FFFFFF";

    var element = document.getElementById("iphone14");
    console.log(element);
    element.style.borderColor = "#AEAEAE";
    element.style.color = "#000000";
    element.style.backgroundColor = "#FFFFFF";

    // document.getElementById("iphone").style.borderColor = "none";
    document.getElementById("iphone").style.borderColor = "red";
    document.getElementById("iphone").style.color = "#EB2227";
    document.getElementById("iphone").style.backgroundColor = "#FFEAEA";

}
function selectIphone14Pro() {
    var element = document.getElementById("iphone");
    console.log(element);
    element.style.borderColor = "#AEAEAE";
    element.style.color = "#000000";
    element.style.backgroundColor = "#FFFFFF";

    var element = document.getElementById("iphone14max");
    console.log(element);
    element.style.borderColor = "#AEAEAE";
    element.style.color = "#000000";
    element.style.backgroundColor = "#FFFFFF";

    document.getElementById("iphone14").style.borderColor = "red";
    document.getElementById("iphone14").style.color = "#EB2227";
    document.getElementById("iphone14").style.backgroundColor = "#FFEAEA";

}
function selectIphone14() {
    var element = document.getElementById("iphone");
    console.log(element);
    element.style.borderColor = "#AEAEAE";
    element.style.color = "#000000";
    element.style.backgroundColor = "#FFFFFF";

    var element = document.getElementById("iphone14");
    console.log(element);
    element.style.borderColor = "#AEAEAE";
    element.style.color = "#000000";
    element.style.backgroundColor = "#FFFFFF";

    document.getElementById("iphone14max").style.borderColor = "red";
    document.getElementById("iphone14max").style.color = "#EB2227";
    document.getElementById("iphone14max").style.backgroundColor = "#FFEAEA";

}

///////////////////Modal/////////////////////////////////////


function modalVerificationCode() {
    var modalVerificationCode = document.getElementById("modalVerificationCode");
    modalVerificationCode.style.display = "block";
    var span = document.getElementsByClassName("closeModal")[0];

    span.onclick = function () {
        modalVerificationCode.style.display = "none";
    }
}
window.onclick = function(event) {
    if (event.target == modalVerificationCode) {
        modalVerificationCode.style.display = "none";
    }
}
function modalReviewProduct() {
    var modal1 = document.getElementById("modalReviewProduct");
    modal1.style.display = "block";


// Get the <span> element that closes the modal
    var span1 = document.getElementsByClassName("close2")[0];

// When the user clicks on <span> (x), close the modal
    span1.onclick = function () {
        modal1.style.display = "none";
    }

// When the user clicks anywhere outside of the modal, close it
    window.onclick = function (event) {
        if (event.target == modal1) {
            modal1.style.display = "none";
        }
    }
}

function modalProductComfirmation() {

// Get the modal
    var modal2 = document.getElementById("productComfirmation");
    modal2.style.display = "block";

// Get the <span> element that closes the modal
    var span2 = document.getElementsByClassName("close2")[0];


// When the user clicks on <span> (x), close the modal
    span2.onclick = function () {
        modal2.style.display = "none";
    }

// When the user clicks anywhere outside of the modal, close it
    window.onclick = function (event) {
        if (event.target == modal2) {
            modal2.style.display = "none";
        }
    }
}

function modalCondition(){
// Get the modal
    var modal3 = document.getElementById("modalCondition");
    modal3.style.display = "block";

// Get the button that opens the modal

// Get the <span> element that closes the modal
    var span3 = document.getElementsByClassName("closeModal")[0];



// When the user clicks on <span> (x), close the modal
    span3.onclick = function() {
        modal3.style.display = "none";
    }

// When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
        if (event.target == modal3) {
            modal3.style.display = "none";
        }
    }
}

/////////////////SwettAlert////////////////////////////////////////////////////////

function sweetalertSucess(){
    Swal.fire({
        title: '<h6 style="color:  #39A912;">Transaction Successful</h6>',
        text: "Metfone will contact to pick up the phone. More info calls 1204",
        textColor:'#39A912',
        icon: 'success',
        confirmButtonColor: '#EB2227',
        confirmButtonText: 'Back to home'
    })


}
///////////////////////////////////////////AJax////////////////////////





var data = {};

$.ajax({
    type: "GET",
    contentType: "application/json",
    url: CONFIG.pre_order_url + "productsCategory",
    dataType: 'json',
    cache: false,
    timeout: 600000,
    success: function (data) {
        var obj = data["productCategory"];
        console.log(obj[0].values);
        //   if($(this).val() == 'select product'){
        //     document.getElementById("hideValuesOnSelect").style.display="none";
        //
        // }

        if (obj.length > 0) {
            console.log(obj);
            for (var i = 0; i < obj.length; i++) {


                if (obj[i].set_default == 1){
                    changeProduct(obj[i].id);
                   // obj[0].style.display="none";
                  //  document.getElementById("hideValuesOnSelect").style.display="none";
                    $('#productCategory').append(
                        $('<option>').text(obj[i].category_name).attr(
                            'value', obj[i].id).prop('selected', true),
                    )
//                                            document.getElementById("thumnailImage").style.display = "block";
//                                            document.getElementById('thumnailImage').src =obj[i].image;
                }

                else {
                    $('#productCategory').append(
                        $('<option>').text(obj[i].category_name).attr(
                            'value', obj[i].id),
                    )
                }

            }
        }

    },
    error: function (e) {
        console.log("Error !" + e);
    }
});

function setLabelOption(obj,label,value){

    obj.empty();
    obj.append(
        $('<option>').text(label).attr('value', value));


}



//productSubCategory//


function changeProduct(id)
{
    // if($('<option>').val() == 'select product'){
    //     document.getElementById("hideValuesOnSelect").style.display="none";
    //
    // }

    // document.getElementById("hideValuesOnSelect").style.display="none";

      var option = $('#productSubCategory');
    setLabelOption(option,"Select Device",null);

    var data = {};
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: CONFIG.pre_order_url + "productSubCategory?categoryId="+id,
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var obj = data["productSubCategory"];
            console.log(obj.length);
            if (obj.length > 0) {
                console.log( obj);
                for (var i = 0; i < obj.length; i++) {

                      if (obj[i].set_default == 1){
                        option.append(
                            $('<option>').text(obj[i].subCategoryTitle).attr(
                            'value', obj[i].categoryId).prop('selected', true),
                        )
                        document.getElementById("productImage").style.display="block";
                        document.getElementById('productImage').src = obj[i].image;
                        console.log( "image is "+obj[i].image)

                    }
                    else {
                        option.append(
                            $('<option>').text(obj[i].subCategoryTitle).attr(
                                'value', obj[i].categoryId),
                        )
                        document.getElementById("productImage").style.display="block";
                        document.getElementById('productImage').src = obj[i].image;


                    }




                }
            }


        },
        error: function (e) {
            console.log("Error !" + e);
        }
    });
}


function productDevice(subCategoryId){
    var data = {};
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: CONFIG.pre_order_url + "productModel?subCategoryId="+ subCategoryId,
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var obj = data["product"];
            console.log(obj.length);
            if (obj.length > 0) {
                for (var i = 0; i < obj.length; i++) {
                    $('#productModel').append(
                        $('<p>').text(obj[i].decription).attr(
                            'value', obj[i].id).prop('selected', true),
                    )

                }
            }

        }
    })

}



