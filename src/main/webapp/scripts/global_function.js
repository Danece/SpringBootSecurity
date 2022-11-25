
var regex_shchedule_secAndMin = /^[0-5]?[0-9]$|^[0-5]?[0-9]\/[0-5]?[0-9]$|^\*$|^\-$|^\/$|^\,$/g;
var regex_shchedule_hour = /^\b([0-9]|1[0-9]|2[0-3])\b$|^\b([0-9]|1[0-9]|2[0-3])\b|\b([0-9]|1[0-9]|2[0-3])\b$|^\*$|^\-$|^\/$|^\,$/g;
var regex_shchedule_dayOfMonth = /^\b([0-9]|[12][0-9]|3[0-1])\b$|^\b([0-9]|[12][0-9]|3[0-1])\b\/\b([0-9]|[12][0-9]|3[0-1])\b$|^\?$|^\*$|^\-$|^\/$|^\,$/g;
var regex_shchedule_month = /^[0-7]$|^[0-7]|[0-7]$|^\*$|^\-$|^\/$|^\,$/g;
var regex_shchedule_datOfWeek = /^[0-7]$|^[0-7]|[0-7]$|^\?$|^\*$|^\-$|^\/$|^\,$|^\#$/g;

function checkout_shcheduleCron(cronList) {
    if ( null == cronList[0].match(regex_shchedule_secAndMin)
        || null == cronList[1].match(regex_shchedule_secAndMin)
        || null == cronList[2].match(regex_shchedule_hour)
        || null == cronList[3].match(regex_shchedule_dayOfMonth)
        || null == cronList[4].match(regex_shchedule_month)
        || null == cronList[5].match(regex_shchedule_datOfWeek)
    ) {
        return false;
    } else {
        return true;
    }
}