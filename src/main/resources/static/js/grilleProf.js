/**
 * Created by jiawei on 16/05/2017.
 */

$.urlParam = function(name){
    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    if (results==null){
        return null;
    }
    else{
        return results[1] || 0;
    }
}

var condition = $.urlParam('domain');
console.log(condition);



if (condition == 0){
    $(window).on('load',function(){
        $('#table').hide();
    });
}