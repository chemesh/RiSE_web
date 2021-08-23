
function openList(listName) {
    let i, tabContent, tabLinks;
    tabContent = document.getElementsByClassName("tab-content");
    for (i = 0; i < tabContent.length; i++) {
        tabContent[i].style.display = "none";
    }
    tabLinks = document.getElementsByClassName("tab-link");
    for (i = 0; i < tabLinks.length; i++) {
        tabLinks[i].className = tabLinks[i].className.replace(" active", "");
    }
    document.getElementById(listName).style.display = "block";
    //evt.currentTarget.className += " active";
}

function addNewSellEntry(entry){
    $("#sellers-list").prepend($('<li>'+entry.log+'</li>'))
}

function addNewBuyEntry(entry){
    $("#buyers-list").prepend($('<li>'+entry.log+'</li>'))
}

function appendToSellList(entries){
    $("#sellers-list").empty();
    for (let entryLog of entries){
        addNewSellEntry(entryLog);
    }
}

function appendToBuyList(entries){
    $("#buyers-list").empty();
    for (let entryLog of entries){
        addNewBuyEntry(entryLog);
    }
}

function ajaxTradeListsLogContent() {
    $.ajax({
        url: "data/admin?sym="+document.getElementById('symbol').innerText,
        dataType: 'json',
        success: function(data) {
            console.log(data);
            appendToSellList(data.sellEntries);
            appendToBuyList(data.buyEntries);
            triggerAjaxTradeListsLogContent();
        },
        error: function(error) {
            console.log(error);
            triggerAjaxTradeListsLogContent();
        }
    });
}

function triggerAjaxTradeListsLogContent() {
    setTimeout(ajaxTradeListsLogContent, refreshRate);
}

$(function (){
    triggerAjaxTradeListsLogContent();
})