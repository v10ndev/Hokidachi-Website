var dropdownCities;
var dropdownDistricts;

$(document).ready(function (){
    dropdownCities = $("#city");
    dropdownDistricts = $("#listDistricts");

    dropdownCities.on("change", function (){
        loadDistricts4City();
        $("#district").val("").focus();
    });

    loadDistricts4City();

    $("#buttonCancel").click(function (){
        window.location = moduleURL;
    })
});

function loadDistricts4City(){
    selectedCity = $("#city option:selected");
    cityId = selectedCity.val();
    url = contextPath + "districts/list_by_city/" + cityId;

    $.get(url, function(responseJSON){
        dropdownDistricts.empty();

        $.each(responseJSON, function(index, district){
            $("<option>").val(district.name).text(district.name).appendTo(dropdownDistricts);
        });
    }).fail(function(){
        showErrorModal("Error loading districts/provinces for the selected city.");
    });
}