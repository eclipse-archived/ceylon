/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
native class StringTokens
    (str, separator, discardSeparators, groupSeparators, limit)
        satisfies {String+} {
    
    String str;
    Boolean separator(Character ch);
    Boolean discardSeparators;
    Boolean groupSeparators;
    Integer? limit;
    
    shared actual native Iterator<String> iterator();
    shared actual native Boolean empty;
    
}

native("js") class StringTokens
    (str, separator, discardSeparators, groupSeparators, limit)
        satisfies {String+} {
    
    String str;
    Boolean separator(Character ch);
    Boolean discardSeparators;
    Boolean groupSeparators;
    Integer? limit;
    
    shared actual native("js") Boolean empty => false;
    
    shared actual native("js") Iterator<String> iterator() {
        dynamic str = this.str;
        
        return object satisfies Iterator<String> {
            
            variable value first = true;
            variable value lastTokenWasSeparator = false;
            variable value count = 0;
            variable value index = 0;
            
            Integer currentCodePoint {
                dynamic {
                    return str.codePointAt(index);
                }
            }
            
            Integer currentCharCode {
                dynamic {
                    return str.codePointAt(index);
                }
            }
            
            void advanceToEnd() {
                dynamic {
                    index = str.length;
                }
            }
            
            void advance() {
                index += #d800 <= currentCharCode <= #dbff
                                    then 2 else 1;
            }
            
            value eof {
                dynamic {
                    return index >= str.length;
                }
            }
            
            value separator
                    => !eof
                    && outer.separator(currentCodePoint.character);
            
            value regular
                    => !eof
                    && !outer.separator(currentCodePoint.character);
            
            String token(Integer start, Boolean separator) {
                if (!separator) {
                    count ++;
                }
                first = false;
                lastTokenWasSeparator = separator;
                dynamic {
                    return str.substring(start, index);
                }
            }
            
            function emptyToken() {
                count ++;
                first = false;
                lastTokenWasSeparator = false;
                return "";
            }
            
            shared actual String|Finished next() {
                if (!eof) {
                    if (first || lastTokenWasSeparator,
                        separator) {
                        return emptyToken();
                    }
                    
                    if (separator) {
                        value start = index;
                        advance();
                        if (groupSeparators) {
                            while (separator) {
                                advance();
                            }
                        }
                        if (!discardSeparators) {
                            return token(start, true);
                        }
                    }
                    
                    value start = index;
                    if (exists limit, count>=limit) {
                        advanceToEnd();
                    }
                    else {
                        while (regular) {
                            advance();
                        }
                    }
                    return token(start, true);
                }
                else if (lastTokenWasSeparator) {
                    return emptyToken();
                }
                else {
                    return finished;
                }
            }
            
        };
    }
    
}

native("jvm") class StringTokens
    (str, separator, discardSeparators, groupSeparators, limit)
        satisfies {String+} {
    
    import java.lang { 
        JString=String, 
        JCharacter=Character
    }
    
    String str;
    Boolean separator(Character ch);
    Boolean discardSeparators;
    Boolean groupSeparators;
    Integer? limit;
    
    shared actual native("jvm") Boolean empty => false;
    
    shared actual native("jvm") Iterator<String> iterator()
            => object satisfies Iterator<String> {
        
        value string = JString(str);
        
        variable value first = true;
        variable value lastTokenWasSeparator = false;
        variable value count = 0;
        variable value index = 0;
        
        void advanceToEnd() {
            index = string.length();
        }
        
        void advance() {
            index += JCharacter.charCount(string.codePointAt(index));
        }
        
        value eof => index >= string.length();
        
        value separator
                => !eof
                && outer.separator(string.codePointAt(index).character);
        
        value regular
                => !eof
                && !outer.separator(string.codePointAt(index).character);
        
        String token(Integer start, Boolean separator) {
            if (!separator) {
                count ++;
            }
            first = false;
            lastTokenWasSeparator = separator;
            return string.substring(start, index);
        }
        
        String emptyToken() {
            count ++;
            first = false;
            lastTokenWasSeparator = false;
            return "";
        }
        
        shared actual String|Finished next() {
            if (!eof) {
                if (first || lastTokenWasSeparator,
                    separator) {
                    return emptyToken();
                }
                
                if (separator) {
                    value start = index;
                    advance();
                    if (groupSeparators) {
                        while (separator) {
                            advance();
                        }
                    }
                    if (!discardSeparators) {
                        return token(start, true);
                    }
                }
                
                value start = index;
                if (exists limit, count>=limit) {
                    advanceToEnd();
                }
                else {
                    while (regular) {
                        advance();
                    }
                }
                return token(start, true);
            }
            else if (lastTokenWasSeparator) {
                return emptyToken();
            }
            else {
                return finished;
            }
        }
        
    };
    
}