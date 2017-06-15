/**
 * Created by alizeefaytre on 15/06/2017.
 */

function tableau(){
    for (var i = 0; i<$('.domain').length; i++){
        var count = $(this).siblings().length;
        while (count<10) {
            $(this).append("<td> </td>");
            count = count + 1 ;
        }
    }
}

$(document).ready(function(){
    $('.mySelect').select2();
    tableau();
});