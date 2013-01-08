package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
@Method
public final class parseFloat_
{
    
    private static int exponentFromSuffix(char suffix) {
        switch (suffix) {
        case 'P':
            return 15;
        case 'T':
            return 12;
        case 'G':
            return 9;
        case 'M':
            return 6;
        case 'k':
            return 3;
        case 'm':
            return -3;
        case 'u':
            return -6;
        case 'n':
            return -9;
        case 'p':
            return -12;
        case 'f':
            return -15;
        }
        throw new RuntimeException();
    }
    
    @TypeInfo("ceylon.language::Null|ceylon.language::Float")
    public static Float parseFloat(@Name("string") java.lang.String string) {
    	final int length = string.length();
    	if (length == 0) {
    		return null;
    	}

        int ii = 0;
		java.lang.StringBuilder sb = new java.lang.StringBuilder();

        // Sign
        char ch = string.charAt(ii);
        if (ch == '-') {
            sb.append('-');
            ii++;
        } else if (ch == '+') {
            ii++;
        }
        
        int sep = -1;
        int digitIndex = 0;
        loop: for (; ii < length; ii++, digitIndex++) {
            ch = string.charAt(ii);
            switch(ch) {
            case '_':
                if (sep != -1 && (digitIndex - sep) % 4 != 0) {//
                    return null;
                }
                if (sep == -1) {// at most three digits before the first _
                    if (digitIndex > 3) {
                        return null;
                    }
                    sep = digitIndex;
                }
                break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                if (sep != -1 &&
                        (digitIndex - sep) % 4 == 0) {// missing a _ after the first
                    return null;
                }
                sb.append(ch);
                break;
            default: // non-digit, so not a number
                break loop;
            }
        }
        
        // check for insufficient digits after the last _
        if (sep != -1 
                && ((digitIndex - sep) % 4) != 0) {
            return null;
        } else if (digitIndex == 0) {
            return null;
        }
        
        if (ii < length && '.' == string.charAt(ii)) {
            ii++;
            sb.append('.');
            // The fractional part
            sep = -1;
            digitIndex = 0;
            loop: for (; ii < length; ii++, digitIndex++) {
                ch = string.charAt(ii);
                switch(ch) {
                case '_':
                    if (sep != -1 && ((digitIndex+1) % 4) != 0) {//
                        return null;
                    }
                    if (sep == -1) {// at most three digits before the first _
                        sep = digitIndex + 1;
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    if (sep != -1 &&
                            ((digitIndex+1) % 4) == 0) {// missing a _ after the first
                        return null;
                    }
                    sb.append(ch);
                    break;
                default: // non-digit, so not a number
                    break loop;
                }
            }
            
            // check for insufficient digits after the last _
            if (sep != -1 
                    && ((digitIndex + 1) % 4) != 0) {
                return null;
            } else if (digitIndex == 0) {
                return null;
            }
            
            if (ii < length) {
                // The Exponent
                ch = string.charAt(ii++);
                if (ch == 'e' || ch == 'E') {
                    sb.append("E");
                    if ('+' == string.charAt(ii)) {
                        ii++;
                    } else if ('-' == string.charAt(ii)) {
                        sb.append('-');
                        ii++;
                    }
                    sep = -1;
                    digitIndex = 0;
                    loop: for (; ii < length; ii++, digitIndex++) {
                        ch = string.charAt(ii);
                        switch(ch) {
                        case '_':
                            if (sep != -1 && (digitIndex - sep) % 4 != 0) {//
                                return null;
                            }
                            if (sep == -1) {// at most three digits before the first _
                                if (digitIndex > 3) {
                                    return null;
                                }
                                sep = digitIndex;
                            }
                            break;
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            if (sep != -1 &&
                                    (digitIndex - sep) % 4 == 0) {// missing a _ after the first
                                return null;
                            }
                            sb.append(ch);
                            break;
                        default: // non-digit, so not a number
                            break loop;
                        }
                    }
                    
                    // check for insufficient digits after the last _
                    if (sep != -1 
                            && ((digitIndex - sep) % 4) != 0) {
                        return null;
                    }
                } else if ("PTGMkmunpf".indexOf(ch) != -1) {
                    sb.append("E").append(exponentFromSuffix(ch));
                } else {
                    return null;
                }
            }
        } else if ("munpf".indexOf(ch) != -1) {
            ii++;
            sb.append("E").append(exponentFromSuffix(ch));
        }
        
        if (ii < length) {
            return null;
        }
        
        // XXX Allocating a String to pass to parseDouble() sucks: We should be 
        // able to construct a double bits and use Double.longBitsToDouble() 
        // but that's quite a lot of work. See 
        // "How to Read Floating Point Numbers Accurately" William D Clinger  
    	return Float.instance(Double.parseDouble(sb.toString()));
    }
    private parseFloat_(){}
}
