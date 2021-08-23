


$(function (){ //this will run onload
    $("#login-form").submit(function (){
        const data =
            {
                type: this.method,
                data: $(this).serialize(),
                url:    this.action, // login
                timeout: 4000
            }
            console.log("data:", data);
        $.ajax({...data,
            error: function (xhr, ajaxOptions, errorObj)
            {
            window.console.log("this was an error");
            console.log("data: "+data)
            console.log("xhr: ",xhr, "ajaxOptions: ");
            console.log( "ajaxOptions: ", ajaxOptions);
            console.log( "errorObj: ",errorObj);
               //$("#error-placeholder").append(xhr.responseText)
                //$("#error-placeholder").innerHTML = xhr.responseText;
                document.getElementById("error-placeholder").innerHTML = xhr.responseText;
            },

            success: function (nextURL){
                window.location.replace(nextURL)
            }
        })
        return false;
    })
})

