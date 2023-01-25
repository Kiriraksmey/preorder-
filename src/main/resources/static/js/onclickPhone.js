function displayDivDemo(id, elementValue) {
    document.getElementById(id).style.display = elementValue.value == 1 ? 'block' : 'none';
}
function validateForm() {
    let x = document.forms["myForm"]["fname"].value;
    if (x === "") {
        document.getElementById("myDiv").style.borderColor = "red";
        document.getElementById("myDiv1").style.borderColor = "red";
        document.getElementById("myDiv2").style.borderColor = "red";
        return false;
    }
}