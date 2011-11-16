jQuery(function(){
 function toggleLongDoc(elem){
  jQuery(".short, .long", elem).toggle();
 }

 jQuery(".category tr, .collapsible").each(function(){
  var tr = jQuery(this);
  var short = jQuery(".short", this);
  var long = jQuery(".long", this);
  if(short.size() > 0 && long.size() > 0 && short.html() != long.html()){
   tr.addClass("more");
   short.attr("title", "Click to expand");
   long.attr("title", "Click to hide");
   tr.click(function(){toggleLongDoc(this)});
  }
 });
});