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
    document.getElementById(id).style.borderColor= "red";
}