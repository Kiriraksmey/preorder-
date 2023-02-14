
////////////////////////////////////////validation///////////////////////////////////////////

	// function validatePopup(obj, placeholder) {
	// 	var obj = "#" + obj;
	// 	var value = $(obj).val();
	//
	// 	$(obj).css("border-color", "red");
	// 	$(obj).attr('placeholder', placeholder);
	// 	$('body').append('<style>' + obj + '::placeholder{color:red !important;}</style>')
	// 	return false;
	// }
	//
	//
	//
	// function myTest(){
	// 	var test =document.getElementById("productCustomerName").value;
	// 	console.log(test);
	//
	// 	validatePopup("productCapacity", "");
	// 	validatePopup("productColor", "");
	// 	validatePopup("productCustomerName", "*Please input customer name");
	// 	validatePopup("productCustomerNum", "*Phone numer invalid");
	// 	validatePopup("productCustomerId", "*Please input your national ID card/ Passport");
	// 	validatePopup("productCustomerProvince", "");
	// }
	//





//////////////AJAX//////////////////////////////////////////////////////


		// var data = {};
		// $.ajax({
		// 	type: "GET",
		// 	contentType: "application/json",
		// 	url: "localhost:8080/productsCategory",
		// 	data: {
		// 		format: 'json'
		// 	},
		// 	dataType: 'jsonp',
		// 	cache: false,
		// 	timeout: 600000,
		// 	success: function (data) {
		// 		var obj = data["productCategory"];
		// 		if (obj.length > 0) {
		// 			var test =document.getElementById("productCategory").value;
		// 			console.log(test)
		// 			 for (var i = 0; i < obj.length; i++) {
		// 					productCategory(obj[i].id);
		// 				$('#productsCategory').append(
		// 						$('<option>').text(obj[i].category_name).attr(
		// 							'value', obj[i].id).prop('selected', true),
		// 					)
		// 			 }
		// 		}
		// 	},
		// 	error: function (e) {
		// 		console.log("Error !" + e);
		// 	}
		//
		// });
// function testProduct()
// {

	// var test = document.getElementById("productCategory").value;
	//if (test==0) {

	//}}

// var data = {};
// $.ajax({
// 	type: "GET",
// 	contentType: "application/json",
// 	url: CONFIG.pre_order_url + "productsCategory",
// 	dataType: 'json',
// 	cache: false,
// 	timeout: 600000,
// 	success: function (data) {
// 		var obj = data["productsCategory"];
// 		console.log(obj);
// 		if(obj.length > 0){
// 			for (var i = 0; i < obj.length; i++) {
// 				$('#productCategory').append(
// 					$('<option>').text(obj[i].category_name).attr(
// 						'value', obj[i].id).prop('selected', true),
// 				)
// 			}
// 		}
// 	},
// 	error: function (e) {
// 		console.log("Error !" + e);
// 	}
// });


