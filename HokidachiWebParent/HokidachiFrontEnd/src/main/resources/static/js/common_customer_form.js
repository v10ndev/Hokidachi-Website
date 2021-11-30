var dropDownCity;
var dataListDistrict;
var fieldDistrict;

$(document).ready(function (){
    dropDownCity = $("#city");
    dataListDistrict = $("#listDistricts");
    fieldDistrict = $("#district")

    dropDownCity.on("change", function (){
        loadDistrictsForCity();
        fieldDistrict.val("").focus();
    })
})

function loadDistrictsForCity(){
    selectedCity = $("#city option:selected");
    cityId = selectedCity.val();
    url = contextPath + "settings/list_districts_by_city/" + cityId;

    $.get(url, function (responseJSON){
        dataListDistrict.empty();

        $.each(responseJSON, function (index, district){
            $("<option>").val(district.name).text(district.name).appendTo(dataListDistrict);
        });

    }).fail(function (){
        alert("failed to connect to the server!");
    });
}

function checkPasswordMatch(confirmPassword){
    if(confirmPassword.value != $("#password").val()){
        confirmPassword.setCustomValidity("Passwords do not match!");
    } else {
        confirmPassword.setCustomValidity("");
    }
}