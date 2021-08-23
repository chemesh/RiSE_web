

function disablePriceOnMkt(){
    const type = document.getElementById('tradeType').value;
    if (type==="mkt"){
        $("#tradePrice").prop('disabled',true);
    }
    else{
        $("#tradePrice").prop('disabled',false);
    }
}

function updateStockPossession(amount){
    $("#stock-possession").text(amount);
}

function ajaxUpdatePossession(){
    $.ajax({
        url: "data/trader?sym="+document.getElementById('symbol').innerText,
        success: function (data){
            updateStockPossession(data)
        },
        error: function (err){
            console.log(err.responseText);
        }
    })
}

function checkPossession(){
    const action = document.getElementById('trade-action').value
    if (action === "sell") {
        const pos = parseInt(document.getElementById('stock-possession').innerText);
        const amount = parseInt(document.getElementById('tradeAmount').value);
        if (amount > pos)
            return false;
    }
    return true;
}

function checkValid(){
    return (
        document.getElementById('tradeType').value !== ""
        && document.getElementById('tradePrice').value !== ""
        && (
            document.getElementById('tradeAmount').value !== ""
            && (
                document.getElementById('tradeType').value !== "MKT"
                || document.getElementById('trade-action').value === "Buy"
            )
        )
    )
}


$(function (){
    $("#trade-order").submit(function (){
        if (!checkValid()){
            alert("Invalid trade order.\n" +
                "Please fill all necessary fields")
            return false;
        }
        else if (!checkPossession()){
            alert("You don't have enough shares from this stock to apply this trade order.\n" +
                "Please correct the amount of shares");
            event.preventDefault();
            return false;
        }
        else{
            $("#tradeSymbol").prop('disabled', false);
            const data ={
                data: $(this).serialize(),
                url: this.action,
                method: this.method,
                timeout: 4000
            }
            console.log(data);
            $("#tradeSymbol").prop('disabled', true);
            if(window.confirm("Send this Order?")) {
                $.ajax({
                    ...data,
                    error: function (errObj) {
                        console.log(errObj)
                        alert(errObj)
                    },
                    success: function (msg) {
                        alert(msg)
                    }
                })
                return false;
            }
            else {
                event.preventDefault();
                return false;
            }
        }
    })
    return false;
})

$(function (){
    setInterval(ajaxUpdatePossession,refreshRate);
})