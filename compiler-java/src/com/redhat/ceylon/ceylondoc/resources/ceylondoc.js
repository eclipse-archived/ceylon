function enableInfoKeybordShortcut(key) {
	$("#"+key+" .key").addClass("badge-info");
	$("#"+key+" .info").addClass("text-info");
}


/*
 * HIGHLIGHT LINES IN RAINBOW
 */
if( window.Rainbow ) {
    function highlightLinesInRainbow() {
        var startend = location.hash.substr(1).split(',');
        var startLine = parseInt(startend[0]);
        var endLine = parseInt(startend[1]);

        if( !isNaN(startLine) && !isNaN(endLine) ) {
            var tr = $('.line-'+startLine);
            if( tr.length > 0 ) {
                $(document.body).scrollTop(tr.position().top);
                for(i = startLine; i <= endLine; i++) {
                    tr.addClass('highlight');
                    tr = tr.next();
                }
            }
        }
    } 
    window.Rainbow.onLineNumbersComplete = highlightLinesInRainbow;
}


/*
 * COLLAPSIBLE TYPE HIERARCHY
 */
$(".hierarchy-arrow-down").each(function() {
	var hierarchyArrow = $(this);
	var hierarchyArrowCont = hierarchyArrow.parent();
	var hierarchyLevel = hierarchyArrowCont.parent();
	var subhierarchy = $(".subhierarchy", hierarchyLevel);

	hierarchyArrowCont.click(function() {
		hierarchyArrow.toggleClass("hierarchy-arrow-down");
		hierarchyArrow.toggleClass("hierarchy-arrow-right");
		subhierarchy.toggle();    
    });
});



/*
 * COLLAPSIBLE TABLE
 */
$("table").each(function() {
	var thead = $("thead", this);
	var tbody = $("tbody", this);

	thead.click(function() {
		if (tbody.is(":visible")) {
			var icon = $(".icon-expand", thead);
			icon.removeClass("icon-expand");
			icon.addClass("icon-collapse");
		} else {
			var icon = $(".icon-collapse", thead);
			icon.removeClass("icon-collapse");
			icon.addClass("icon-expand");
		}
		tbody.toggle();
	});
});


/*
 * COLLAPSIBLE SPECIFIER EXPRESSION 
 */
$(".specifier-ellipsis").each(function() {
	var specifierEllipsis = $(this);
	var specifierRest = specifierEllipsis.next();

	specifierEllipsis.click(function() {
		specifierEllipsis.toggle();
		specifierRest.toggle();
		return false;
	});
});


/*
 * COLLAPSIBLE DESCRIPTION
 */
$('tbody .description').each(function() {
	var descDiv = $(this);
	var descHeightLong = descDiv.height();
	var descHeightShort = descDiv.addClass('description-collapsed').height();

	if (descHeightLong - descHeightShort > 16) {
		var iconDecoration = $('<i/>').addClass('icon-decoration-expand');
		var descDecoration = $('<div/>').addClass('description-decoration').append(iconDecoration);            
		var iconExpand = $('<i/>').addClass('icon-expand2');
		var iconCollapse = $('<i/>').addClass('icon-collapse2');
		var collapsibleLink = $('<a/>').attr('href', '#').text('Expand').addClass('link-collapsible').prepend(iconExpand);

		var collapsibleHandler = function(event) {
			var target = $(event.target);
			if( target.is('a') && !target.is('.link-collapsible') ) {
				return true;
			}
			if( target.is('i') && (target.is('.icon-link') || target.is('.icon-source-code')) ) {
				return true;
			}
			if( target.is('b') && target.is('.caret') ) {
				return true;
			}

			var isCollapsed = descDiv.hasClass('description-collapsed');
			if( isCollapsed ) {
				collapsibleLink.text('Collapse');
				collapsibleLink.prepend(iconCollapse);
				iconDecoration.removeClass('icon-decoration-expand').addClass('icon-decoration-collapse');
			}
			else {
				collapsibleLink.text('Expand');
				collapsibleLink.prepend(iconExpand);
				iconDecoration.removeClass('icon-decoration-collapse').addClass('icon-decoration-expand');
			}
			descDiv.toggleClass('description-collapsed');
			
			if( target.is('a') && target.is('.link-collapsible') ) {
				return false;
			} else {
				return true;
			}
		};

		collapsibleLink.click(collapsibleHandler);

		var sourceCodeLink = $('.link-source-code', descDiv.parent());
		if( sourceCodeLink.length == 0 ) {
			descDiv.parent().prepend(collapsibleLink);
		}
		else {
			sourceCodeLink.after(collapsibleLink);
		}

		descDiv.parent().append(descDecoration);
		descDiv.parent().parent().addClass("row-collapsible");
		descDiv.parent().parent().click(collapsibleHandler);
	}
	else {
		descDiv.removeClass('description-collapsed');
	}

});


/* 
 * FILTER DROPDOWN MENU   
 */
$(document).ready(function() {
    
	if( tagIndex.length == 0 ) {
		$("#f").hide();
		return;
	}
    if( $('#filterDropdownPanel').length == 0 ) {
        return;
    }
    $('#filterDropdown').show();
    
    // disabled for now, F is in collision with jump to functions shortcut, and this feature isn't used much
    // initFilterKeyboardShortcuts();
    
    initFilterActionAll();
    initFilterActionNone();
    initFilterActionMore();
    initFilterDropdownPanel();
    initTagsOnClickHandler();
    executeFilter();

    function initFilterKeyboardShortcuts() {
        $('html').keypress(function(evt) {
            evt = evt || window.event;
            var keyCode = evt.keyCode || evt.which;
            if ( !evt.ctrlKey && !evt.altKey && keyCode == 102) {
                $('#filterDropdown > .dropdown-toggle').click();
            }
        });
        enableInfoKeybordShortcut('f');
    };

    function initFilterActionAll() {
        $('#filterActionAll').click(function() {
            $('#filterDropdownPanelTags .tag:visible').addClass('tagSelected');
            $('#filterDropdownPanelTags input[type="checkbox"]:visible').attr('checked', true);
            $('tbody a.tag').addClass('tagSelected');
            executeFilter();
        });
    };
    
    function initFilterActionNone() {
        $('#filterActionNone').click(function() {
            $('#filterDropdownPanelTags .tag').removeClass('tagSelected');
            $('#filterDropdownPanelTags input[type="checkbox"]').attr('checked', false);
            $('tbody a.tag').removeClass('tagSelected');
            executeFilter();                    
        });
    };
    
    function initFilterActionMore() {
        $('#filterActionMore').click(function() {
            var filterActionMore = $(this);
            if( filterActionMore.hasClass('tagOccurrenceZero') ) {
                filterActionMore.removeClass('tagOccurrenceZero');
                filterActionMore.html('Show more');
                $('#filterDropdownPanelTags .tagOccurrenceZero').hide();
            }
            else {
                filterActionMore.addClass('tagOccurrenceZero');
                filterActionMore.html('Show less');
                $('#filterDropdownPanelTags .tagOccurrenceZero').show();                        
            }
            executeFilter();
        });
    };
    
    function initFilterDropdownPanel() {
        $('#filterDropdownPanel').click(function(e) {
            e.stopPropagation();
        });
    	
        var filterDropdownPanelTags = $('#filterDropdownPanelTags');
        var tagOccurrencesCount = {};
        
        for (i = 0; i < tagIndex.length; i++) {
            var tagName = tagIndex[i];
            
            tagOccurrencesCount[tagName] = $('.tags a[name="' + tagName + '"]').length;

            var tagDiv = $('<div/>');
            var tagCheckbox = $('<input/>').attr('name', tagName).attr('type', 'checkbox').appendTo(tagDiv);
            var tagLabel = $('<a/>').addClass("tag label").attr('name', tagName).append(tagName).appendTo(tagDiv);
            $('<span/>').addClass('tagOccurrenceCount').append(tagOccurrencesCount[tagName]).appendTo(tagDiv);

            var eventHandler = function(event) {
                var tagName = $(event.target).attr('name');
                var tagCheckbox = $('#filterDropdownPanelTags input[name="'+tagName+'"]');
                var tagLabel = $('#filterDropdownPanelTags a[name="'+tagName+'"]');
                var isSelected = tagLabel.hasClass('tagSelected')
                if( isSelected ) {
                    tagLabel.removeClass('tagSelected');
                    tagCheckbox.attr('checked', false);
                    $('tbody a.tag[name="'+tagName+'"]').removeClass('tagSelected');
                }
                else {
                    tagLabel.addClass('tagSelected');
                    tagCheckbox.attr('checked', true);
                    $('tbody a.tag[name="'+tagName+'"]').addClass('tagSelected');
                }
                executeFilter();
            };
            tagCheckbox.click(eventHandler);
            tagLabel.click(eventHandler);
            
            if( tagOccurrencesCount[tagName] == 0 ) {
                tagDiv.addClass('tagOccurrenceZero');
                tagDiv.hide();
            }

            tagDiv.appendTo(filterDropdownPanelTags);
        }
        
        var tagOccurrenceZeroCount = $('#filterDropdownPanelTags .tagOccurrenceZero').length;
        if( tagOccurrenceZeroCount == 0 ) {
            $('#filterActionMore').hide();  
        }
        if( tagOccurrenceZeroCount == tagIndex.length ) {
            $('#filterDropdownPanelInfo').html('No tagged declarations on this page.');
        }
    };

    function initTagsOnClickHandler() {
        $('tbody a.tag').each(function() {
            var tagLabel = $(this);
            tagLabel.click(function() {
                var tagName = tagLabel.attr('name');
                $('#filterDropdownPanelTags a[name="'+tagName+'"]').click();
            });
        });
    }
    
    function executeFilter() {
        var selectedTags = [];
        $('#filterDropdownPanelTags .tagSelected').each(function() {
            selectedTags.push($(this).attr('name'));
        });
        
        var declTotal = 0;
        var declAccepted = 0;
        $('tbody tr').each(function() {
        	declTotal++;
        	var tr = $(this);
        	if (isAccepted(tr, selectedTags)) {
        		declAccepted++;
        		tr.show();
        		tr.removeClass('tagHidden');
        	} else {
        		tr.hide();
        		tr.addClass('tagHidden');
        	}
        });
        
        if( declTotal == declAccepted ) {
        	$('#filterDropdownLinkInfo').html('');
        } else {
        	$('#filterDropdownLinkInfo').html('('+declAccepted+'/'+declTotal+')');
        }
        
        $('table').each(function() {
        	var table = $(this);
        	var rowsCount = $('tbody tr', table).length;
        	var hiddenRowsCount = $('tbody .tagHidden', table).length;
        	if( rowsCount == hiddenRowsCount ) {
        		table.hide();
        	} else {
        		table.show();
        	}
        });
    };
    
    function isAccepted(tr, selectedTags) {
    	if( selectedTags.length == 0 ) {
    		return true;
    	} else {
    		var isAccepted = false;     
    		jQuery(".tags a", tr).each(function() {
    			if( !isAccepted ) {
    				isAccepted = (selectedTags.indexOf(jQuery(this).attr('name')) != -1);
    			}
    		});     
    		return isAccepted;
    	}
    };
    
});


/*
 * SEARCH PAGE
 */
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
		var tr = jQuery("<tr/>");
		var td = jQuery("<td/>");

		if(i == 0 ) {
			tr.addClass("warning");
		}

		var firstTagIcon = null;
		var prevTagIcon = null;
		for (i = 0; i < elem.icons.length; i++) {
			var icon = elem.icons[i];
			var tagIcon = jQuery("<i/>").addClass(icon);
			if(firstTagIcon == null) {
				firstTagIcon = tagIcon;
			}
			if(prevTagIcon != null) {
				prevTagIcon.append(tagIcon);
			}
			prevTagIcon = tagIcon;
		}
		if(firstTagIcon != null) {
			firstTagIcon.appendTo(td);
		}
		
        var tagsDiv = jQuery("<div/>").addClass("tags section");
        for (i = 0; i < elem.tags.length; i++) {
            var tag = elem.tags[i];
            var tagLink = jQuery("<a/>").addClass("tag label").attr("name", tag).attr("href", "search.html?q="+tag).append(tag);
            if( tag.toLowerCase().indexOf(q) != -1 ) {
                tagLink.addClass("tagSelected")
            }
            tagsDiv.append(tagLink);
        }
        tagsDiv.appendTo(td);
		
		var text = highlightMatch(elem.name, q);
		if(elem.aliasFor){
		  text = text.append(" → "+elem.aliasFor);
		}
        var elemLink = jQuery("<a/>").addClass("link").attr("href", elem.url).append(text);
        elemLink.appendTo(td);

		var elemDesc = jQuery("<div/>").addClass("description").html(elem.doc);
		elemDesc.appendTo(td);

		tr.append(td);
		results.append(tr);
	});
}

jQuery("#q").each(function(){
	
	enableInfoKeybordShortcut('\\?');
    $('html').keypress(function(evt) {
        evt = evt || window.event;
        var keyCode = evt.keyCode || evt.which;
        if ( !evt.ctrlKey && !evt.altKey && keyCode == 63) {
            $('#infoDropdown > .dropdown-toggle').click();
        }
    });
	
	enableInfoKeybordShortcut('enter');
	enableInfoKeybordShortcut('esc');
	enableInfoKeybordShortcut('up');
	enableInfoKeybordShortcut('down');
	
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
	jQuery(".warning").removeClass("warning");
	if(selected < matches.length-1)
		selected++;
	jQuery("tr:eq("+selected+")").addClass("warning");
}

function previousMatch() {
	if(matches.length < 2)
		return;
	jQuery(".warning").removeClass("warning");
	if(selected > 0)
		selected--;
	jQuery("tr:eq("+selected+")").addClass("warning");
}

function getUrlVars() {
	var vars = [];
	var hash;
	var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
	for ( var i = 0; i < hashes.length; i++) {
		hash = hashes[i].split('=');
		var name = decodeURIComponent(hash[0]);
		vars.push(name);
		vars[name] = decodeURIComponent(hash[1]);
	}
	return vars;
}


/*
 * highlight and expand anchor target
 */
$(document).ready(function() {
    $(window).bind('hashchange', function() {
        highlightAndExpandAnchorTarget();
    });
    highlightAndExpandAnchorTarget();

    function highlightAndExpandAnchorTarget() {
        var highlightedRows = $('tr.highlight');
        if (highlightedRows.length > 0) {
        	highlightedRows.removeClass('highlight');
        }

        var anchor = location.hash;
        if (anchor) {
            // if we're linking to a method parameter, get rid of the parameter suffix for now
            // until we know how to highlight the parameter
            anchor = anchor.replace(/^(#[^-]+)-.*$/, "$1");
            var td = $(anchor);
            var tr = td.parent();
            if (tr.is('tr') ) {
                tr.addClass('highlight');
                if (tr.hasClass('row-collapsible')) {
                    if ($('.description-collapsed', tr).length > 0) {
                        $('.link-collapsible', tr).click();
                        $(document.body).scrollTop(tr.position().top);
                    }
                }
            }
        }
    }
    
});


/*
 * expand all / collapse all
 */
$(document).ready(function() {
    if( $('.expand-all').length == 0 ) {
        return;
    }

    initKeyboardShortcuts();
    initClickHandlers();
    enableInfoKeybordShortcut('\\+');
    enableInfoKeybordShortcut('\\-');

    function initKeyboardShortcuts() {
        $('html').keypress(function(evt) {
            evt = evt || window.event;
            var keyCode = evt.keyCode || evt.which;
            if( !evt.ctrlKey && !evt.altKey ) {
	            if (keyCode == 43 /* + */) {
	                expandAll();
	            }
	            if (keyCode == 45 /* - */) {
	                collapseAll();
	            }
            }
        });
    };

    function initClickHandlers() {
        $('.expand-all').click(expandAll);
        $('.collapse-all').click(collapseAll);
    };
    
    function expandAll() {
        execute(true);  
    };
    
    function collapseAll() {
        execute(false);  
    };

    function execute(expand) {
        $('.row-collapsible').each(function() {
            var tr = $(this);
            if( ($('.description-collapsed', tr).length > 0) == expand ) {
                $('.link-collapsible', tr).click();
            }
        });
        $('table').each(function() {
            var thead = $('thead', this);
            var tbody = $('tbody', this);
            if (tbody.is(':visible') != expand ) {
                thead.click();
            }
        });
    };
});


/*
 * easter egg
 */
var konami = 0;
var konamiCode = [38,38,40,40,37,39,37,39,66,65,13];
jQuery("html").keydown(function (event){
    var evt = event || window.event;
    var keyCode = evt.keyCode || evt.which;
    if(keyCode == konamiCode[konami]){
        konami++;
        if(konami == konamiCode.length)
            document.location = "http://ceylon-lang.org/easter-egg/";
    }else
        konami = 0;
});
