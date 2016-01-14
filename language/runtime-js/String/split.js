function(sep, discard, group) {

    //TODO: return a stream
    var tokens = [];

    // shortcut for empty input
    if (this.length === 0) {
        tokens.push(this);
        return $arr$(tokens,{t:$_String});
    }

    if (sep === undefined) {sep = function(c){return c.value in Character.WS$;}}
    if (discard === undefined) {discard = true}
    if (group === undefined) {group = true}

    var tokenBegin = 0;
    var tokenBeginCount = 0;
    var count = 0;
    var value = this;
    var separator = true;

    function pushToken(tokenEnd) {
        tokens.push($_String(value.substring(tokenBegin, tokenEnd)));
    }
    
    for (var i=0; i<this.length; ++count) {
        var j = i;
        var cp = this.charCodeAt(i++);
        if ((cp&0xfc00)===0xd800 && i<this.length) {
            cp = (cp<<10) + this.charCodeAt(i++) - 0x35fdc00;
        }

        if (sep(Character(cp))) {
            if (!group) {
                // ungrouped separator: store preceding token
                pushToken(j);
                if (!discard) {
                    // store separator as token
                    tokens.push($_String(this.substring(j, i)));
                }
                // next token begins after this character
                tokenBegin = i;
                tokenBeginCount = count + 1;
            } else if (!separator || (j == 0)) {
                // begin of grouped separator: store preceding token
                pushToken(j);
                // separator token begins at this character
                tokenBegin = j;
                tokenBeginCount = count;
            }
            separator = true;

        } else if (separator) {
            // first non-separator after separators or at beginning
            if (!discard && (tokenBegin != j)) {
                // store preceding grouped separator (if group=false then tokenBegin=j)
                pushToken(j);
            }
            // non-separator token begins at this character
            tokenBegin = j;
            tokenBeginCount = count;
            separator = false;
        }
    }

    if ((tokenBegin != i) && !(separator && discard)) {
        // store preceding token (may be a grouped separator)
        pushToken(i);
    }
    if (separator) {
        // if last character was a separator then there's another empty token
        tokens.push("");
    }

    return $arr$(tokens,{t:$_String});
}
