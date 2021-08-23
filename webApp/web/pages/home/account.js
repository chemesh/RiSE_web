
let ACTIVITY_LOG_VERSION = 0;

const USERNAME = getValueFromCookie("username");

function updateCreditBalance(newBalance){

    $("#credit-balance").text(newBalance);
}

function addNewActivityLog(entry){

    $("#account-activity").prepend($('<li>'+entry.log+'</li>'));
}

function appendToLogArea(entries) {

    for (let entryLog of entries){
        addNewActivityLog(entryLog);
    }
}

//call the server and get the account activity log version
//we also send it the current log version so in case there was a change
//in the content, we will get the new logs as well
function ajaxLogContent() {
    $.ajax({
        url: "account/log",
        data: "logVersion=" + ACTIVITY_LOG_VERSION,
        dataType: 'json',
        success: function(data) {
            /*
             data will arrive in the next form:
             {
                "entries": [
                    {
                        log:"type: charge| timestamp: 13/23/8930 12:23:123| cost: +420| old balance: 69,000| new balance: 69,420"
                        newBalance: "69,420"
                    },
                    {
                        log:"type: buy| timestamp: 13/23/8930 23:23:123| cost: +69| old balance: 489| new balance: 420"
                        newBalance: "420"
                    }
                ],
                "version":1
             }
             */
            console.log("Server log version: " + data.version + ", Current chat version: " + ACTIVITY_LOG_VERSION);
            if (data.version !== ACTIVITY_LOG_VERSION) {
                ACTIVITY_LOG_VERSION = data.version;
                appendToLogArea(data.entries);
                updateCreditBalance(data.entries[data.entries.length-1].newBalance)
            }
            triggerAjaxLogContent();
        },
        error: function(error) {
            console.log(error);
            triggerAjaxLogContent();
        }
    });
}

function triggerAjaxLogContent() {
    setTimeout(ajaxLogContent, refreshRate);
}

$(function (){
    $("#user-name-label").text(USERNAME);
    $("#charge-credit").submit(function (){
            const data = {
                url: this.action,
                data: $(this).serialize(),
                timeout:4000
            }
            console.log(data);
            $.ajax({...data,
                error: function(errObj){
                    console.log(errObj);
                },
                success: function(msg){
                    alert("Credit Charge finished successfully");
                }
            })
        return false;
        })

    triggerAjaxLogContent();

})