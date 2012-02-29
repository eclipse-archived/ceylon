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
 
 try{
  if (SyntaxHighlighter!= null) {
   var startend = location.hash.substr(1).split(',');
   var startLine = parseInt(startend[0]);
   var endLine = parseInt(startend[1]);
   var lines = [];
   for (var ii = startLine; ii <= endLine; ii++) {
    lines.push(ii);
   }
   SyntaxHighlighter.defaults['highlight'] = lines;
   SyntaxHighlighter.defaults['gutter'] = false;
   SyntaxHighlighter.defaults['toolbar'] = false; 
   SyntaxHighlighter.all();
   setTimeout(function() {
    jQuery('div.number'+startLine).each(function() {
 	this.scrollIntoView(true);
    });
   }, 10);
  }
 }catch(error){
	 if(error instanceof ReferenceError){
		 // ignore this one
	 } else
		 throw error;
 }

 // Search
 var matches = [];
 var previousSearch;
 function search(q){
     if(q)
         q = q.toLowerCase();
     // abort if nothing new
     if(previousSearch == q)
         return;

	 // reset
	 var results = jQuery("#results");
	 results.empty();
	 matches = [];
	 selected = 0;
	 previousSearch = q;

	 // if we're empty, leave now
	 if(!q)
		 return;
	 
	 // collect matches
	 jQuery.each(index, function(i, elem){
	     var matchedElement = null;
	     
	     if( elem.name.toLowerCase().indexOf(q) != -1 ) {	         
	         matchedElement = jQuery.extend(true, {}, elem); // clone
	         matchedElement.score = calculateScore(matchedElement.name, q);
	     }
	         
	     for(i=0;i<elem.tags.length; i++) {
	         var tag = elem.tags[i];
	         if( tag.toLowerCase().indexOf(q) != -1 ) {
	             if( !matchedElement ) {
	                 matchedElement = jQuery.extend(true, {}, elem); // clone
	                 matchedElement.score = 0;
	             }	        
	             matchedElement.score = matchedElement.score + calculateScore(tag, q);
	         }
	     }
	     
	     if( matchedElement ) {
	         matches.push(matchedElement);
	     }
	 });
	 // sort them
	 matches.sort(function(a, b){ 
       return b.score - a.score;
	 });
	 // display them
	 jQuery.each(matches, function(i, elem){
		var div = jQuery("<div/>").addClass("match");
		if(i == 0)
			div.addClass("selected");

		var elemLink = jQuery("<a/>").attr("href", elem.url).append(highlightMatch(elem.name, q));
		
        var tagsDiv = jQuery("<div/>").addClass("tags");
        for (i = 0; i < elem.tags.length; i++) {
            var tag = elem.tags[i];
            var tagLink = jQuery("<a/>").attr("href", "search.html?q=".concat(tag)).append(tag);
            if( tag.toLowerCase().indexOf(q) != -1 ) {
                tagLink.addClass("highlight")
            }
            tagsDiv.append(tagLink);
        }
		
		jQuery("<div/>").addClass("type").text(elem.type).appendTo(div);
		tagsDiv.appendTo(div);		
		jQuery("<div/>").addClass("name").append(elemLink).appendTo(div);
		jQuery("<div/>").addClass("doc").html(elem.doc).appendTo(div);
		
		results.append(div);
	 });
 }
 
 jQuery("#q").each(function(){
     var q = getUrlVars()['q'];
     if(q) {
         jQuery(this).val(q);         
         search(q);
     }
 }); 
 jQuery("#q").keyup(function(){
	 var q = jQuery(this).val();
	 search(q);
 }).bind("search", function(){
	 // we need this to catch the clearing of the search field using the cross UI
	 var q = jQuery(this).val();
	 search(q);
 }).keydown(function(event){
	 var evt = event || window.event;
	 if(evt.keyCode == 27){
		 // clear if we have something
		 if(previousSearch){
			 jQuery(this).val("");
			 search("");
		 }else{
			 // go to overview, we canceled the search
			 document.location = "index.html";
		 }
		 return false;
	 }else if(evt.keyCode == 40){
		 nextMatch();
		 return false;
	 }else if(evt.keyCode == 38){
		 previousMatch();
		 return false;
	 }else if(evt.keyCode == 13){
		 document.location = matches[selected].url;
		 return false;
	 }
 }).focus();
 
 function calculateScore(text, q) {
     var SCORE_EXACT      = 1000000000;
     var SCORE_START_WITH = 1000000;
     var SCORE_CONTAINS   = 1000;
     
     text = text.toLowerCase();
     var index = text.indexOf(q);
     
     if( text == q )
         return SCORE_EXACT;
     else if( index == 0 )
         return SCORE_START_WITH-text.length;
     else 
       return SCORE_CONTAINS-index;
 }
 
 function highlightMatch(text, q) {
     var matchStart = text.toLowerCase().indexOf(q);
     if( matchStart == -1 )
         return text;

     var before = text.substring(0, matchStart);
     var match = text.substring(matchStart, matchStart + q.length);
     var after = text.substring(matchStart + q.length);
     return jQuery("<span/>").append(before).append(jQuery("<span/>").addClass("highlight").text(match)).append(after);
 }
  
 function nextMatch() {
	 if(matches.length < 2)
		 return;
	 jQuery(".match.selected").removeClass("selected");
	 if(selected < matches.length-1)
		 selected++;
	 jQuery(".match:eq("+selected+")").addClass("selected");
 }
 
 function previousMatch() {
     if(matches.length < 2)
         return;
     jQuery(".match.selected").removeClass("selected");
     if(selected > 0)
         selected--;
     jQuery(".match:eq("+selected+")").addClass("selected");
 }

 function getUrlVars() {
     var vars = [];
     var hash;
     var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
     for ( var i = 0; i < hashes.length; i++) {
         hash = hashes[i].split('=');
         vars.push(hash[0]);
         vars[hash[0]] = hash[1];
     }
     return vars;
 } 

});