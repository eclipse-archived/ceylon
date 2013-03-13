/**
 * SyntaxHighlighter
 * http://alexgorbatchev.com/SyntaxHighlighter
 *
 * SyntaxHighlighter is donationware. If you are using it, please donate.
 * http://alexgorbatchev.com/SyntaxHighlighter/donate.html
 *
 * @version
 * 3.0.83 (July 02 2010)
 * 
 * @copyright
 * Copyright (C) 2004-2010 Alex Gorbatchev.
 *
 * @license
 * Dual licensed under the MIT and GPL licenses.
 */
;(function()
{
    // CommonJS
    typeof(require) != 'undefined' ? SyntaxHighlighter = require('shCore').SyntaxHighlighter : null;
    
    function Brush()
    {
        var keywords = 'module package import alias class interface object given value assign void function of ' +
                       'extends satisfies adapts abstracts in out return break continue throw ' +
                       'assert dynamic if else switch case for while try catch finally then ' +
                       'this outer super is exists nonempty';
        var annotations = 'shared abstract formal default actual variable deprecated small late ' +
                          'literal doc by see throws optional license';
        
        this.regexList = [
            { regex: /\/\/.*/gi, css: 'color3' },                                               // line end comments
            { regex: /\/\*([\s\S]*?)?\*\//gm, css: 'color3' },                                  // multiline comments
            { regex: /"""([^"]|"[^"]|""[^"])*"""/gm, css: 'string' },                           // verbatim strings
            { regex: /(``|")([^"\\`]|\\.|`[^`"])*(`"|``|")/gm, css: 'string' },                 // strings
            { regex: /'([^'\\\n]|\\.)*'/gm, css: 'string' },                                    // characters
            { regex: new RegExp(this.getKeywords(keywords), 'gm'), css: 'keyword' },            // keywords
            { regex: /(#|\$)[a-zA-Z0-9_]+\b/gi, css: 'value' },                                 // hex/bin numbers
            { regex: /\b[A-Z][a-zA-Z0-9_]*/g, css: 'color1' },                                  // types
            { regex: new RegExp(this.getKeywords(annotations), 'gm'), css: 'color2' },          // annotations
            { regex: /\b(\d|_)+(\.(\d|_)+)?((E|e)(\+|\-)?\d+)?[munpfkMGTP]?\b/g, css: 'value' }  // decimal numbers
        ];
        
        this.forHtmlScript({
            left    : /(&lt;|<)%[@!=]?/g, 
            right    : /%(&gt;|>)/g 
        });
    };
    
    Brush.prototype    = new SyntaxHighlighter.Highlighter();
    Brush.aliases    = ['ceylon'];
    
    SyntaxHighlighter.brushes.Ceylon = Brush;
    
    // CommonJS
    typeof(exports) != 'undefined' ? exports.Brush = Brush : null;
})();
