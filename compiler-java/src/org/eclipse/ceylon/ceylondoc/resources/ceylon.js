/**
* Ceylon patterns
*
* @author Gavin King
* @version 1.0.0
*/
Rainbow.extend( "ceylon", [
  /*{
    name: "constant",
    pattern: /\b(false|null|true|[A-Z_]+)\b/g
  },*/
  {
    matches: {
      1: "keyword",
      3: "support.namespace"
    },
    pattern: /(import|module|package)(\s)*((\w|\.)+)/g
  },
  {
    name: "keyword",
    pattern: /\b(assembly|module|package|import|alias|class|interface|object|given|value|assign|void|function|new|of|extends|satisfies|abstracts|in|out|return|break|continue|throw|assert|dynamic|if|else|switch|case|for|while|try|catch|finally|then|let|this|outer|super|is|exists|nonempty|while)\b/g
  },
  {
    name: "char",
    pattern: /'([^'\\\n]|\\.)*'/gm
  },
  {
    name: "string",
    pattern: /"""([^"]|"[^"]|""[^"])*"""|(``|(?:^|[^'])")([^"\\`]|\\.|`[^`"])*(`"|``|")/gm
  },
  {
    name: "constant.numeric",
    pattern: /\b(\d|_)+(\.(\d|_)+)?((E|e)(\+|\-)?\d+)?[munpfkMGTP]?\b|(#|\$)[a-zA-Z0-9_]+\b/g
  },
  {
    name: "comment",
    pattern: /\/\*[\s\S]*?\*\/|(\/\/).*?$|(\#\!).*?$/gm
  },
  {
    name: "entity.function",
    pattern: /\b(shared|abstract|static|formal|default|actual|variable|deprecated|small|late|literal|doc|by|see|since|throws|optional|license|tagged|final|native|annotation|sealed|serializable|static|service|suppressWarnings)\b/g
  },
  {
    name: "entity.class",
    pattern: /\\I\w*\b|\b[A-Z]\w*\b/g
  },
  {
    matches: {
      1: "variable.instance"
    },
    pattern: /\.(\\i\w*\b|[a-z_]\w*)\b/g
  },
  {
    name: "variable.global",
    pattern: /\\i\w*\b|\b(?!(gt|lt|amp);)[a-z_]\w*\b/g
  }
], true );
