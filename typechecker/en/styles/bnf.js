/**
* BNF patterns
*/
Rainbow.extend( "bnf", [
  {
    name: "string",
    pattern: /"[^"]*"/g
  },
  {
    name: "variable.global",
    pattern: /\b[A-Z]\w*\b/g
  }
], true );
