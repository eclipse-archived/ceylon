function(sep, discard, group) {
    // shortcut for empty input
    if (this.length === 0) {return Singleton(this, {Element$Singleton:{t:$_String}}); }

    if (sep === undefined) {sep = function(c){return c.value in Character.WS$;}}
    if (discard === undefined) {discard = true}
    if (group === undefined) {group = true}

    //TODO: return an iterable which determines the next token on demand
    var tokens = [];
    var tokenBegin = 0;
    var tokenBeginCount = 0;
    var count = 0;
    var value = this;
    var separator = true;

    function pushToken(tokenEnd) {
        tokens.push($_String(value.substring(tokenBegin, tokenEnd), count-tokenBeginCount));
    }
    if (is$(sep, {t:Iterable})) {
        var sepChars = {}
        var it = sep.iterator();
        var c; while ((c=it.next()) !== getFinished()) {sepChars[c.value] = true}
        for (var i=0; i<this.length;) {
            var j = i;
            var cp = this.charCodeAt(i++);
            if ((cp&0xfc00)===0xd800 && i<this.length) {
                cp = (cp<<10) + this.charCodeAt(i++) - 0x35fdc00;
            }
            if (cp in sepChars) {
                if (!group) {
                    // ungrouped separator: store preceding token
                    pushToken(j);
                    if (!discard) {
                        // store separator as token
                        tokens.push($_String(this.substring(j, i), 1));
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
        if (tokenBegin != i) {
            pushToken(i);
        }
    } else {
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
                        tokens.push($_String(this.substring(j, i), 1));
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
    }

    this.codePoints = count;
    return tokens.reifyCeylonType({t:$_String});
}
