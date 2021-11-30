var buttonLoad4Districts;
var dropDownCity4Districts;
var dropDownDistricts;
var buttonAddDistrict;
var buttonUpdateDistrict;
var buttonDeleteDistrict;
var labelDistrictName;
var fieldDistrictName;

$(document).ready(function(){
    buttonLoad4Districts = $("#buttonLoadCitiesForDistricts");
    dropDownCity4Districts = $("#dropDownCitiesForDistricts");
    dropDownDistricts = $("#dropDownDistricts");
    buttonAddDistrict = $("#buttonAddDistrict");
    buttonUpdateDistrict = $("#buttonUpdateDistrict");
    buttonDeleteDistrict = $("#buttonDeleteDistrict");
    labelDistrictName = $("#labelDistrictName");
    fieldDistrictName = $("#fieldDistrictName");

    buttonLoad4Districts.click(function(){
        loadCities4Districts();
    });

    dropDownCity4Districts.on("change", function(){
        loadDistricts4City();
    });

    dropDownDistricts.on("change", function(){
        changeFormDistrictToSelectedDistrict();
    });

    buttonAddDistrict.click(function(){
        if(buttonAddDistrict.val() == "Add"){
            addDistrict();
        } else{
            changeFormDistrictToNewDistrict();
        }
    });

    buttonUpdateDistrict.click(function(){
        updateDistrict();
    });

    buttonDeleteDistrict.click(function(){
        deleteDistrict();
    });
});

function deleteDistrict(){
    districtId = dropDownDistricts.val();
    url = contextPath + "districts/delete/" + districtId;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function(xhr){
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function(){
        $("#dropDownDistricts option[value='"+ districtId + "']").remove();
        changeFormDistrictToNewDistrict();
        showToastMessage("The district has been deleted");
    }).fail(function(){
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function validateFormDistrict(){
    formDistrict = document.getElementById("formDistrict");
    if(!formDistrict.checkValidity()){
        formDistrict.reportValidity();
        return false;
    }
    return true;
}

function updateDistrict(){
    if(!validateFormDistrict()) return;

    url = contextPath + "districts/save";
    districtId = dropDownDistricts.val();
    districtName = fieldDistrictName.val();

    selectedCity = $("#dropDownCitiesForDistricts option:selected");
    cityId = selectedCity.val();
    cityName = selectedCity.text();

    jsonData = {id: districtId, name: districtName, city: {id: cityId, name: cityName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function(xhr){
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(districtId){
        $("#dropDownDistricts option:selected").text(districtName);
        showToastMessage("The district has been updated");

        changeFormDistrictToNewDistrict();
    }).fail(function(){
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function addDistrict(){
    if(!validateFormDistrict()) return;

    url = contextPath + "districts/save";
    districtName = fieldDistrictName.val();

    selectedCity = $("#dropDownCitiesForDistricts option:selected");
    cityId = selectedCity.val();
    cityName = selectedCity.text();

    jsonData = {name: districtName, city: {id: cityId, name: cityName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function(xhr){
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(districtId){
        selectNewlyAddedDistrict(districtId, districtName);
        showToastMessage("The new district has been added");
    }).fail(function(){
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function selectNewlyAddedDistrict(districtId, districtName){

    $("<option>").val(districtId).text(districtName).appendTo(dropDownDistricts);

    $("#dropDownDistricts option[value='"+ districtId + "']").prop("selected", true);

    fieldDistrictName.val("").focus();
}

function changeFormDistrictToNewDistrict(){
    buttonAddDistrict.val("Add");
    labelDistrictName.text("Tên quận/huyện: ");

    buttonUpdateDistrict.prop("disabled", true);
    buttonDeleteDistrict.prop("disabled", true);

    fieldDistrictName.val("").focus();
}

function changeFormDistrictToSelectedDistrict(){
    buttonAddDistrict.prop("value", "New");
    buttonUpdateDistrict.prop("disabled", false);
    buttonDeleteDistrict.prop("disabled", false);

    labelDistrictName.text("Selected District/Province:");
    selectedDistrictName = $("#dropDownDistricts option:selected").text();
    fieldDistrictName.val(selectedDistrictName);

}

function loadDistricts4City(){
    selectedCity = $("#dropDownCitiesForDistricts option:selected");
    cityId = selectedCity.val();
    url = contextPath + "districts/list_by_city/" + cityId;

    $.get(url, function(responseJSON){
        dropDownDistricts.empty();

        $.each(responseJSON, function(index, district){
            $("<option>").val(district.id).text(district.name).appendTo(dropDownDistricts);
        });
    }).done(function(){
        changeFormDistrictToNewDistrict();
        showToastMessage("All districts have been loaded for city " + selectedCity.text());
    }).fail(function(){
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function loadCities4Districts(){
    url = contextPath + "cities/list";
    $.get(url, function(responseJSON){
        dropDownCity4Districts.empty();
        dropDownDistricts.empty();

        $.each(responseJSON, function(index, city){
            $("<option>").val(city.id).text(city.name).appendTo(dropDownCity4Districts);
        });
    }).done(function(){
        buttonLoad4Districts.val("Refresh City List");
        showToastMessage("All cities have been loaded");
    }).fail(function(){
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function showToastMessage(message){
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}