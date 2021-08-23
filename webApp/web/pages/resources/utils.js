
const refreshRate = 2000; //milli seconds

function getValueFromCookie(key){
    let i = 0
    let userCookie = document.cookie.split("; ")[i];
    let userObj = userCookie.split("=");

    while(key !== userObj[0] && userObj[0] !== undefined){
        i++;
        userCookie = document.cookie.split("; ")[i];
        userObj = userCookie.split("=");
    }

    if (userObj === undefined)
        return;
    return userObj[1];
}