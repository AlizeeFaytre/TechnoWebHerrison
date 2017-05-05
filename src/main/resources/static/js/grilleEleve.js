function filtres(){
  $(".filtre").click(function(){
    var index = $(this).index(".filtre");
    console.log(index);
    if ($(this).is(':checked') ){
      if (index == 0) {
        $(".0").removeClass("collapse");
        $(".0bis").addClass("collapse");
        $(".1").removeClass("collapse");
      }
      if (index == 1){
        $("."+index+"").removeClass("collapse");
        $(".0bis").addClass("collapse");
      }
      else {
        $("."+index+"").removeClass("collapse");
      }

    }
    else {
      if (index == 0) {
        $(".0").addClass("collapse");
        $(".0bis").removeClass("collapse");
        $(".1").addClass("collapse");
      }
      if (index == 1) {
        $("."+index+"").addClass("collapse");
        $(".0bis").addClass("collapse");
      }
      else {
        $("."+index+"").addClass("collapse");
      }
    }

  });
}

$(document).ready(function(){
  filtres();
});
