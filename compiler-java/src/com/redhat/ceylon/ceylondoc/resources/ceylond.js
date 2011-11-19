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
 
 if (SyntaxHighlighter!= null) {
  var startend = location.hash.substr(1).split(',');
  var startLine = parseInt(startend[0]);
  var endLine = parseInt(startend[1]);
  var lines = [];
  for (var ii = startLine; ii <= endLine; ii++) {
   lines.push(ii);
  }
  SyntaxHighlighter.defaults['highlight'] = lines;
  SyntaxHighlighter.defaults['toolbar'] = false; 
  SyntaxHighlighter.all();
  setTimeout(function() {
   jQuery('div.number'+startLine).each(function() {
	this.scrollIntoView(true);
   });
  }, 10);
 }
});