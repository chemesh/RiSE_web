let COMP_LIST_LOG_VERSION = 0;

function addNewCompTradeLog(entry){
    $("#comp-trades-list").prepend($('<li>'+entry.log+'</li>'));
}

function appendToCompTradeLogArea(entries) {

    for (let entryLog of entries){
        addNewCompTradeLog(entryLog);
    }
}

function updateStockPriceAndCycle(price,cycle){
    $("#curr-price").text(price);
    $("#curr-cycle").text(cycle);
}


function ajaxCompTradeLogContent() {
    $.ajax({
        url: "data/completed?sym="+document.getElementById('symbol').innerText,
        data: "logVersion=" + COMP_LIST_LOG_VERSION,
        dataType: 'json',
        success: function(data) {
            console.log(data);
            if (data.version !== COMP_LIST_LOG_VERSION) {
                COMP_LIST_LOG_VERSION = data.version;
                appendToCompTradeLogArea(data.entries);
                updateStockPriceAndCycle(data.price,data.cycle);
            }
            triggerAjaxCompTradeLogContent();
        },
        error: function(error) {
            console.log(error);
            triggerAjaxCompTradeLogContent();
        }
    });
}

function triggerAjaxCompTradeLogContent() {
    setTimeout(ajaxCompTradeLogContent, refreshRate);
}

$(function (){
    triggerAjaxCompTradeLogContent();
})