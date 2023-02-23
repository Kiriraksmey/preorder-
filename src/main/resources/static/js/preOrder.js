//////Chose your product//////////////////////

// function displayProductCategory(id, elementValue) {
//     document.getElementById(id).style.display = elementValue.value == 1 ? 'block' : 'none';
// }

//////Chose your model//////////////////////
// function selectIphone14Pro() {
//     var element = document.getElementById("iphone");
//     console.log(element);
//     element.style.borderColor = "#AEAEAE";
//     element.style.color = "#000000";
//     element.style.backgroundColor = "#FFFFFF";
//
//     var element = document.getElementById("iphone14max");
//     console.log(element);
//     element.style.borderColor = "#AEAEAE";
//     element.style.color = "#000000";
//     element.style.backgroundColor = "#FFFFFF";
//
//     document.getElementById("iphone14").style.borderColor = "red";
//     document.getElementById("iphone14").style.color = "#EB2227";
//     document.getElementById("iphone14").style.backgroundColor = "#FFEAEA";
//
// }
// function selectIphone14() {
//     var element = document.getElementById("iphone");
//     console.log(element);
//     element.style.borderColor = "#AEAEAE";
//     element.style.color = "#000000";
//     element.style.backgroundColor = "#FFFFFF";
//
//     var element = document.getElementById("iphone14");
//     console.log(element);
//     element.style.borderColor = "#AEAEAE";
//     element.style.color = "#000000";
//     element.style.backgroundColor = "#FFFFFF";
//
//     document.getElementById("iphone14max").style.borderColor = "red";
//     document.getElementById("iphone14max").style.color = "#EB2227";
//     document.getElementById("iphone14max").style.backgroundColor = "#FFEAEA";
//
// }

///////////////////Modal/////////////////////////////////////


// function modalVerificationCode() {
//     var modalVerificationCode = document.getElementById("modalVerificationCode");
//     modalVerificationCode.style.display = "block";
//     var span = document.getElementsByClassName("closeModal")[0];
//
//     span.onclick = function () {
//         modalVerificationCode.style.display = "none";
//     }
// }
// window.onclick = function(event) {
//     if (event.target == modalVerificationCode) {
//         modalVerificationCode.style.display = "none";
//     }
// }
// function modalReviewProduct() {
//     var modal1 = document.getElementById("modalReviewProduct");
//     modal1.style.display = "block";
// }


// Get the <span> element that closes the modal
//     var span1 = document.getElementsByClassName("close2")[0];

// When the user clicks on <span> (x), close the modal
//     span1.onclick = function () {
//         modal1.style.display = "none";
//     }

// When the user clicks anywhere outside of the modal, close it
//     window.onclick = function (event) {
//         if (event.target == modal1) {
//             modal1.style.display = "none";
//         }
//     }
// }

// function modalProductComfirmation() {
//
// // Get the modal
//     var modal2 = document.getElementById("productComfirmation");
//     modal2.style.display = "block";
//
// // Get the <span> element that closes the modal
//     var span2 = document.getElementsByClassName("close2")[0];
//
//
// // When the user clicks on <span> (x), close the modal
// //     span2.onclick = function () {
// //         modal2.style.display = "none";
// //     }
//
// // When the user clicks anywhere outside of the modal, close it
// //     window.onclick = function (event) {
// //         if (event.target == modal2) {
// //             modal2.style.display = "none";
// //         }
// //     }
// // }

// function modalCondition(){
// // Get the modal
//     var modal3 = document.getElementById("modalCondition");
//     modal3.style.display = "block";
//
// // Get the button that opens the modal
//
// // Get the <span> element that closes the modal
//     var span3 = document.getElementsByClassName("closeModal")[0];
//
//
//
// // When the user clicks on <span> (x), close the modal
// //     span3.onclick = function() {
// //         modal3.style.display = "none";
// //     }
//
// // When the user clicks anywhere outside of the modal, close it
// //     window.onclick = function(event) {
// //         if (event.target == modal3) {
// //             modal3.style.display = "none";
// //         }
// //     }
//  }

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
    type: "POST",
    contentType: "application/json",
    url: CONFIG.pre_order_url + "productsCategory",
    data: JSON.stringify(data),
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
                            'value', obj[i].id).prop('selected', true),//khi click on category-name no se push categoryID
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


function changeProduct(id) {
    console.log(id);
    var option = $('#productSubCategory');
    setLabelOption(option,"Select Device",null);
    var data = {};
    data["categoryId"]= id ;
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: CONFIG.pre_order_url + "productSubCategory",
        data: JSON.stringify(data),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var obj = data["productSubCategory"];
            if (obj.length > 0) {
                for (var i = 0; i < obj.length; i++) {
                      if (obj[i].set_default == 1){
                        option.append(
                            $('<option>').text(obj[i].subCategoryTitle).attr(
                            'value', obj[i].id).prop('selected', true),
                        )
                          document.getElementById("productImage").style.display="block";
                          document.getElementById('productImage').src = obj[i].image;
                       //  var test = '<p style="width: 144px;height: 15px;margin-left: 9px;position: absolute;top:68px">'+obj[i].subCategoryTitle+'</p>';
                        console.log( "image is "+obj[i].image)
                    }
                    else {
                        option.append(
                            $('<option>').text(obj[i].subCategoryTitle).attr(
                                'value', obj[i].id),
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


function productDevice(subId){
    var prData = {};
    prData["subCategoryId"] = subId;
    $("#btnClick").empty();
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: CONFIG.pre_order_url + "productModel",
        data: JSON.stringify(prData),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (prData) {
            var obj = prData["product"];
            console.log(obj)
            // var panel_model_id = document.getElementById("panel_model_id");
            // var append_model_id = document.getElementById("append_model_id");
            // panel_model_id.style.display = "";
            if (obj.length >= 1) {
                for (var i = 0; i < obj.length; i++) {
                    var productHtml = '<button type="button" id="btns" value="' + obj[i].id + '" onclick="productModel(this.value)">' +

                            '<p id="productName"  style="margin-bottom: 0px;font-weight: bold" >' +obj[i].product_name+
                        '</p>' +
                        '<span id="productModel"> '+obj[i].decription+'</span>' +

                        ' </button>';

                    $("#btnClick").append(productHtml)
                }
            }
        },
        error: function (e) {
            console.log("Error !" + e);
        }
    });
}
function productModel(id) {

    var data = {};
    data["modelId"] = id;
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: CONFIG.pre_order_url + "productCapacity",
        data: JSON.stringify(data),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var obj = data["productCapacity"];
            console.log(obj.length);
            if (obj.length > 0) {
                for (var i = 0; i < obj.length; i++) {
                    $('#productCapacity').append(
                        $('<option>').text(obj[i].capacity_name).attr(
                            'value', obj[i].id).prop('selected', true),
                    )


                }


            }

        }
    });
}

//     $.ajax({
//         type: "POST",
//         contentType: "application/json",
//         url: CONFIG.pre_order_url + "productColor",
//         data: JSON.stringify(data),
//         dataType: 'json',
//         cache: false,
//         timeout: 600000,
//         success: function (data) {
//             var obj = data["productColor"];
//             console.log(obj.length);
//             if (obj.length > 0) {
//                 for (var i = 0; i < obj.length; i++) {
//                     $('#productColor').append(
//                         $('<option>').text(obj[i].color_name).attr(
//                             'value', obj[i].id).prop('selected', true),
//                     )
//
//
//                 }
//
//
//             }
//
//         }
//     })
//
// }
// function changeColor(test) {
//     console.log(modelId);
//     var element = document.getElementById("color");
//
//     if (element.id == "color") {
//         element.style.borderColor = "red";
//         element.style.color = "#EB2227";
//         element.style.backgroundColor = "#FFEAEA";
//
//     }
//     else {
//         element.style.borderColor = "";
//         element.style.color = "";
//         element.style.backgroundColor = "";
//
//     }
//
// }



//
//     function productCapacity(modelId) {
//     console.log(modelId);
//
//     var data = {};
//     $.ajax({
//         type: "GET",
//         contentType: "application/json",
//         url: CONFIG.pre_order_url + "productCapacity?modelId="+modelId,
//         dataType: 'json',
//         cache: false,
//         timeout: 600000,
//         success: function (data) {
//             var obj = data["productCapacity"];
//             console.log(obj.length);
//             if (obj.length > 0) {
//                 for (var i = 0; i < obj.length; i++) {
//                     $('#productCapacity').append(
//                         $('<option>').text(obj[i].capacity_name).attr(
//                             'value', obj[i].id).prop('selected', true),
//                     )
//
//
//                 }
//
//
//             }
//
//         }
//     })
//
// }
//
//
//








    // var parent = element.parentNode;
    // alert(parent.id);
    // var color = parent.querySelector("button");
    // alert(color.id);
















