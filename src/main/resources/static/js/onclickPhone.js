function displayDivDemo(id, elementValue) {
    document.getElementById(id).style.display = elementValue.value == 1 ? 'block' : 'none';
}


// Defining a function to validate form
function validateForm() {
    // Retrieving the values of form elements
    var name = document.myForm.name.value;
    // var email = document.contactForm.email.value;
    // var mobile = document.contactForm.mobile.value;
    // var country = document.contactForm.country.value;
    // var gender = document.contactForm.gender.value;
    // var hobbies = [];
    // var checkboxes = document.getElementsByName("hobbies[]");
    // for(var i=0; i < checkboxes.length; i++) {
    //     if(checkboxes[i].checked) {
    //         // Populate hobbies array with selected values
    //         hobbies.push(checkboxes[i].value);
    //     }
    // }

    // Defining error variables with a default value


    // // Validate name
    // if(name == "") {
    //     document.getElementById("myDiv8").style.borderColor = "red";
    // } else {
    //     var regex = /^[a-zA-Z\s]+$/;
    //     if(regex.test(name) === false) {
    //         printError("nameErr", "Please enter a valid name");
    //     } else {
    //         printError("nameErr", "");
    //         name = false;
    //     }
    // }

};

function myFunction() {
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
function myFun() {
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
function myFunc() {
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