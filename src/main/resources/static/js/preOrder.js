

////////////////////////////////////////validation///////////////////////////////////////////

function validatePopup(obj, placeholder) {
	var obj = "#" + obj;
	var value = $(obj).val();

	$(obj).css("border-color", "red");
	$(obj).attr('placeholder', placeholder);
	$('body').append('<style>' + obj + '::placeholder{color:red !important;}</style>')
	return false;
}



function myTest(){

	var productCustomerName =document.getElementById("productCustomerName").value;
    var productCustomerNum =document.getElementById("productCustomerNum").value;
    var productCustomerProvince =document.getElementById("productCustomerProvince").value;
    var productCapacity =document.getElementById("productCapacity").value;
    var productColor=document.getElementById("productColor").value;
    var productCustomerId =document.getElementById("productCustomerId").value;
    if(productCustomerName==="" || productCustomerNum==="" || productCustomerId==="" || productCustomerProvince===""||productCapacity==="" ||productColor==="" ){
        validatePopup("productCustomerNum", "*Phone numer invalid");
        validatePopup("productCustomerId", "*Please input your national ID card/ Passport");
	validatePopup("productCustomerName", "*Please input customer name");
        validatePopup("productCustomerProvince", "");
        validatePopup("productCapacity", "");
        validatePopup("productColor", "");

    }
    else {
        modalReviewProduct();
    }




}


///////////////////Modal/////////////////////////////////////




// var btn = document.getElementById("btnVerificationCode");
//
//     var modalVerificationCode = document.getElementById("modalVerificationCode");
//
//
//     var span = document.getElementsByClassName("closeModal")[0];
//
//
// btn.onclick = function () {
//     modalVerificationCode.style.display = "block";
// }
//
//
//     span.onclick = function () {
//         modalVerificationCode.style.display = "none";
//     }


// window.onclick = function(event) {
//     if (event.target == modalVerificationCode) {
//         modalVerificationCode.style.display = "none";
//     }
// }
function modalReviewProduct() {
    var modal1 = document.getElementById("modalReviewProduct");
    modal1.style.display = "block";
    //Get the <span> element that closes the modal
    var span1 = document.getElementsByClassName("close2")[0];

//When the user clicks on <span> (x), close the modal
    span1.onclick = function () {
        modal1.style.display = "none";
    }


}




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
function setViewImage(img){
    document.getElementById("view_image_id").src = img;
    document.getElementsByClassName("js-image-zoom__zoomed-image")[0].style.backgroundImage = " url(" + img + ")";
    document.getElementsByClassName("js-image-zoom__zoomed-area")[0].style.height = "auto";
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
                          //  document.getElementById("productImage").style.display="block";
                          //  document.getElementById('productImage').src = obj[i].image;
                          document.getElementById('imageCategory').style.display = obj[i].image;
                          // document.getElementsByClassName("xzoom-container")[0].style.backgroundImage  =  " url("+ obj[i].image + ")";
                          // document.getElementsByClassName("xzoom-container")[0].style.height = "auto";
                          // var image= '<div className="xzoom-container">'+
                          //     '<img  class="xzoom" id="imageCategory" style=" "  src="'+ obj[i].image + ' " xoriginal="' + obj[i].image + '" />'+
                          //     '</div>';
                          //
                          // $("#xzoom-productSub").append(image);

                       //  var test = '<p style="width: 144px;height: 15px;margin-left: 9px;position: absolute;top:68px">'+obj[i].subCategoryTitle+'</p>';

                          // document.getElementById("imageCategory").src = obj[i].image;
                          // document.getElementsById("imageCategory")[0].style.backgroundImage = " src=("+ obj[i].image + ")";
                          // document.getElementsByClassName("xzoom")[0].style.height = "auto";
                        // console.log( "image is "+obj[i].image)
                    }
                    else {
                        option.append(
                            $('<option>').text(obj[i].subCategoryTitle).attr(
                                'value', obj[i].id),
                        )
                        document.getElementById("productImage").style.display="block";
                        document.getElementById('productImage').src = obj[i].image;
                          // document.getElementById("imageCategory").style.display="block";
                          // document.getElementById('imageCategory').src = obj[i].image;


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
                    var productHtml = '<button style="width: 246.62px;height:94.24px;position: relative;" type="button" id="btns" value="' + obj[i].id + '" onclick="productModel(this.value)">' +
                            '<p id="productName"  style=" font-weight: bold;font-size: 14px" >' +obj[i].product_name+
                        '</p>' +
                        '<span id="productModel" style="margin-top:80px;font-size: 14px"> '+obj[i].decription+'</span>' +
                        ' </button>'+' <img src="'+obj[i].image +'" id="myImg" alt="Girl in a jacket" width="70" height="60" style="display: none;">';

                    $("#btnClick").append(productHtml);

                }

            }
        },
        error: function (e) {
            console.log("Error !" + e);
        }
    });
}
function setViewImg(img) {
    // document.getElementById("myImg").style.display = "block";
    // document.getElementById('myImg').src = img;

    document.getElementById("imageCategory").src = img;
    // console.log(document.getElementById("view_image_id").getElementsByClassName("js-image-zoom__zoomed-image"))
    document.getElementsByClassName("xzoom")[0].style.backgroundImage = " xoriginal(" + img + ")";
    document.getElementsByClassName("xzoom")[0].style.height = "auto";
}

function productModel(id) {

    console.log(id);
    var productCapacity = $('#productCapacity');
    setLabelOption(productCapacity,"Select Capacity",null);
    var productColor = $('#productColor');
    setLabelOption(productColor,"Select Color",null);

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
            if (obj.length > 0) {
                for (var i = 0; i < obj.length; i++) {
                    productCapacity.append(
                        $('<option>').text(obj[i].capacity_name).attr(
                            'value', obj[i].id)

                    )
                }

            }

        },
        error: function (e) {
            console.log("Error !" + e);
        }
    });

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: CONFIG.pre_order_url + "productColor",
        data: JSON.stringify(data),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var obj = data["productColor"]
            if (obj.length > 0) {
                for (var i = 0; i < obj.length; i++) {
                    $('#productColor').append(
                        $('<option>').text(obj[i].color_name).attr(
                            'value', obj[i].id),
                    )


                    //document.getElementById("btns").style.borderColor="red";

                }


            }

        },
        error: function (e) {
            console.log("Error !" + e);
        }
    });

}

//    Select Change Color
$('#productColor').on("change", function () {
    setViewImg($("#productColor option:selected").attr('data-id'));
});

const popupTrigger = document.querySelector('.popup-trigger');
const popup = document.querySelector('.popup');
const popupClose = document.querySelector('.popup__close');

popupTrigger.addEventListener('click', (e) => {
    popup.classList.add('show');
    document.body.style.cssText = `overflow: hidden;`;

});
popupClose.addEventListener('click', (e) => {
    popup.classList.remove('show');
    document.body.style.cssText = '';
});

// close on click on overlay

popup.addEventListener('click', (e) => {
    if (e.target === popup) {
        popup.classList.remove('show');
        document.body.style.cssText = '';
    }
});
// close on press of escape button
document.addEventListener('keydown', (e) => {
    if (e.code === "Escape" && popup.classList.contains('show')) {
        popup.classList.remove('show');
        document.body.style.cssText = '';
    }
});
$(".view_model_id").click(function (e) {
    $("#view_image_id").attr("src", "images/" + $(this).val());
    $("#thumnailImage").attr("src", "images/" + $(this).val());
});
$('#product_id').change(function (e) {
    alert("werewsfdv");
});

function onlyNumberKey(evt) {
    // Only ASCII character in that range allowed
    var ASCIICode = (evt.which) ? evt.which : evt.keyCode
    if (ASCIICode > 31 && (ASCIICode < 48 || ASCIICode > 57))
        return false;
    return true;
}














