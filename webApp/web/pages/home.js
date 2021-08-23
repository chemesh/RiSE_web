
function loadTraderOptions(){
    $('<!--------------------- MANAGE ACCOUNT ---------------------->\n' +
        '            <section class="icon">\n' +
        '                <a href="home/account">\n' +
        '                    <img src="resources/SeekPng.com_user-png_730482.png" height="40px" width="40px">\n' +
        '                <br>My Account</br>\n' +
        '                </a>\n' +
        '            </section>\n' +
        '            <!--------------------- LOAD XML ---------------------->\n' +
        '            <section class="xmlLoader info-header">\n' +
        '                <p class="sub-title">Upload new XML Data file to RiSE</p>\n' +
        '                <form id="xml-getter" action="home/loadXML" method="post">\n' +
        '                    <input type="file" id="fileChooser" name="XMLdoc" accept=".xml">\n' +
        '                    <input type="submit" value="Upload">\n' +
        '                    <p id="XML-error" style="color: #ff0000; background-color: #fdcbd4"></p>\n' +
        '                    <p id="XML-success" style="color: darkgreen; background-color: lightgreen"></p>\n' +
        '                </form>\n' +
        '            </section>'+
        '           <!--------------------- Issue new Stock ---------------------->\n' +
        '            <section>\n' +
        '                <details class="info-header">\n' +
        '                    <summary class="sub-title">Issue New Corporation Stock</summary>\n' +
        '                    <form id="issue-stock" action="home/issueStock" method="post">\n' +
        '                           <p>Please enter the information needed to issue the stock</p>\n' +
        '                       <div class="detail-form">' +
        '                        <p>Company name:</p> <input type="text" name="company">\n' +
        '                       </div>' +
        '                       <div class="detail-form">' +
        '                        <p>Symbol:</p> <input type="text" name="symbol">\n' +
        '                       </div>' +
        '                       <div class="detail-form">' +
        '                        <p>Shares quantity on issue: </p><input type="number" name="quantity" min="1">\n' +
        '                       </div>' +
        '                       <div class="detail-form">' +
        '                        <p>Company total worth: </p><input type="number" name="company-worth" min="1">\n' +
        '                       </div>' +
        '                        <input type="submit" value="Issue Stock">\n' +
        '                    </form>\n' +
        '                </details>\n' +
        '            </section>').appendTo($("#role-options"));

    $("#xml-getter").submit(function (){
        const xmlFile = this[0].files[0];
        if (xmlFile !== undefined) {

            const formData = new FormData();
            formData.append("xmlData", xmlFile);

            $.ajax({
                data: formData,
                processData: false,
                contentType: false,
                type: this.method,
                url: this.action,
                error: function (errObj) {
                    $("#XML-error").text(errObj.responseText)
                },
                success: function (alerts) {
                    $("#XML-error").empty()
                    console.log("new stock issue error:", alerts);
                    alert("File Loaded Successfully\n" + alerts)
                }
            })
            return false;
        }
        else{
            alert("Please choose a File!")
            event.preventDefault();
            return false;
        }
    })
    $("#issue-stock").submit(function (){
        $.ajax({
            type: this.method,
            data: $(this).serialize(),
            url: this.action,
            timeout: 4000,
            error: function (errMsg){
                console.log(errMsg.responseText);
                alert(errMsg.responseText);
            },
            success(){
                console.log("issue new stock: success");
                alert("Stock was issued successfully");
            }
        })
        return false;

    })
}

function addRowToUsersTable(username, role){
    $('<tr class="info-table-row">\n' +
        '                            <td class=info-table-cell">\n' +
        '                                <a class="info-table-data">\n'+
                                            username+
        '                                </a>\n' +
        '                            </td>\n' +
        '                            <td class=info-table-cell">\n' +
        '                                <a class="info-table-data">\n' +
                                            role+
        '                                </a>\n' +
        '                            </td>\n' +
        '                        </tr>').appendTo($("#user-table"))
}

function addRowToStockTable(stockObj){

    const myHref = "data?sym="+stockObj.symbol;
    $('<tr class="info-table-row">\n' +
        '                        <td class=info-table-cell">\n' +
        '                            <a class="info-table-data" href='+myHref+'>\n' +
                                        stockObj.symbol +
        '                            </a>\n' +
        '                        </td>\n' +
        '                        <td class="stock-info-table-cell">\n' +
        '                            <a class="stock-info-table-data">\n' +
                                        stockObj.company +
        '                            </a>\n' +
        '                        </td>\n' +
        '                        <td class="stock-info-table-cell">\n' +
        '                            <a class="stock-info-table-data">\n' +
                                        stockObj.price +
        '                            </a>\n' +
        '                        </td>\n' +
        '                        <td class="stock-info-table-cell">\n' +
        '                            <a class="stock-info-table-data">\n' +
                                        stockObj.cycle +
        '                            </a>\n' +
        '                        </td>').appendTo($("#stock-table"))
}
function updateUserTable(userList){
    $("#user-table").empty();
    for (let userObj of userList){
        console.log(userObj);
        addRowToUsersTable(userObj.username,userObj.role);
    }
}

function updateStockTable(stockObjList){
    $("#stock-table").empty();
    for (let stockObj of stockObjList){
        console.log(stockObj)
        addRowToStockTable(stockObj);
    }

}

function ajaxUsersList() {
    $.ajax({
        url: "home/userList",
        success: function(usersObj) {
                console.log(usersObj);
                updateUserTable(usersObj);
        },
        error:  function (err){console.log(err);}
    });
}

function ajaxStockList(){
    $.ajax({
        url: "home/stockList",
        success: function (stocksObj) {
                console.log(stocksObj);
                updateStockTable(stocksObj);
        }
    })
}

$(function (){
    let username = getValueFromCookie("username")
    let role = getValueFromCookie("role")

    $("#username-field").text(username);

    switch (role){
        case "trader":
            loadTraderOptions();
            break;
        default:

    }
    setInterval(ajaxUsersList, refreshRate);
    setInterval(ajaxStockList, refreshRate);
})
