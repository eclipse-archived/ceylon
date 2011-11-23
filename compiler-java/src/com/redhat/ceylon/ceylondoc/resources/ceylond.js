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
		if(elem.name.toLowerCase().indexOf(q) != -1)
			matches.push(elem);
	 });
	 // sort them
	 matches.sort(function(a, b){ 
		 var resA = matchScore(a.name, q);
		 var resB = matchScore(b.name, q);
		 if(resA[0] == resB[0])
			 return resA[1] - resB[1];
		 return resA[0] - resB[0];
	 });
	 // display them
	 jQuery.each(matches, function(i, elem){
		var div = jQuery("<div/>").addClass("match");
		if(i == 0)
			div.addClass("selected");
		var name = elem.name;
		var matchStart = name.indexOf(q);
		var before = name.substring(0, matchStart);
		var match = name.substring(matchStart, matchStart + q.length);
		var after = name.substring(matchStart + q.length);
		var matchSpan = jQuery("<span/>").addClass("highlight").text(match);
		var link = jQuery("<a/>").attr("href", elem.url).append(before).append(matchSpan).append(after);
		jQuery("<div/>").addClass("type").text(elem.type).appendTo(div);
		jQuery("<div/>").addClass("name").append(link).appendTo(div);
		jQuery("<div/>").addClass("doc").html(elem.doc).appendTo(div);
		results.append(div);
	 });
 }

 function matchScore(name, q){
	 if(name == q)
		 return [0,0];
	 // favour those that start with the query string
	 var index = name.toLowerCase().indexOf(q);
	 if(index == 0)
		 return [1, name.length]; // discriminator is smallest remaining string
	 // discriminator is how close to the start we are
	 return [2, index];
 }
 
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
 
 function nextMatch(){
	 if(matches.length < 2)
		 return;
	 jQuery(".match.selected").removeClass("selected");
	 if(selected < matches.length-1)
		 selected++;
	 jQuery(".match:eq("+selected+")").addClass("selected");
 }
 function previousMatch(){
	 if(matches.length < 2)
		 return;
	 jQuery(".match.selected").removeClass("selected");
	 if(selected > 0)
		 selected--;
	 jQuery(".match:eq("+selected+")").addClass("selected");
 }
});

