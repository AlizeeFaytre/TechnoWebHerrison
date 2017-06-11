/**
 * Created by jiawei on 10/06/2017.
 */

$.urlParam = function (name) {
    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    if (results == null) {
        return null;
    }
    else {
        return results[1] || 0;
    }
}

var condition = $.urlParam('groupe');


console.log(condition);
if (condition != 0) {
    $(window).on('load', function () {
        $('#modalAdd').modal('show');
    });
}


function autoComplete(id, value) {
    document.getElementById(id).value =id + "-" + value;
}


