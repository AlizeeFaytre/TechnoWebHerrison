function presence(){
  $(".eleve").click(function(){
    console.log($(this));
    if ($(this).hasClass("borderRed")==true) {
      $(this).removeClass("borderRed");
    }
    else {
      $(this).addClass("borderRed");
    }
  });
}

presence();
