function init() {
    jQuery('html').keypress(function(evt){
        evt = evt || window.event;
        var keyCode = evt.keyCode || evt.which;
        if (keyCode == 63) {
            $('#infoDropdown > .dropdown-toggle').click();
        }
        if(keyCode == 105){
            document.location = 'index.html';
        }
        if(keyCode == 115){
            document.location = '#section-synopsis';
        }
        if(keyCode == 111){
            document.location = '#section-options';
        }
        if(keyCode == 100){
            document.location = '#section-description';
        }
    });
    enableInfoKeybordShortcut('\\?');
    enableInfoKeybordShortcut('i');
    enableInfoKeybordShortcut('s');
    enableInfoKeybordShortcut('o');
    enableInfoKeybordShortcut('d');
}

function enableInfoKeybordShortcut(key) {
    $("#"+key+" .key").addClass("badge-info");
    $("#"+key+" .info").addClass("text-info");
}

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
 * highlight anchor target
 */
$(document).ready(function() {
    $(window).bind('hashchange', function() {
        highlightAnchorTarget();
    });
    highlightAnchorTarget();

    function highlightAnchorTarget() {
        var highlightedSpan = $('td > span.highlight');
        if (highlightedSpan.length > 0) {
            var highlightedTd = highlightedSpan.parent();
            highlightedSpan.contents().appendTo(highlightedTd);
            highlightedSpan.remove();
        }

        var anchor = location.hash;
        if (anchor) {
            var td = $(anchor);
            if (td.is('td')) {
                td.wrapInner('<span class="highlight" />');
            }
        }
    }
});
