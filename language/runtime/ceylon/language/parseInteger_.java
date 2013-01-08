package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
@Method
public final class parseInteger_
{
	
    @TypeInfo("ceylon.language::Null|ceylon.language::Integer")
    public static Integer parseInteger(@Name("string") java.lang.String string) {
    	final int length = string.length();
    	if (length == 0) {
    		return null;
    	}
    	long result = 0;
    	boolean negative = false;
    	int ii = 0;
		long limit = -Long.MAX_VALUE;
		long max = Long.MIN_VALUE / 10;
		
		// The sign
		char ch = string.charAt(ii);
		if (ch == '-') {
			negative = true;
			ii++;
			limit = Long.MIN_VALUE;
		} else if (ch == '+') {
			ii++;
		}
		
		// The actual number
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
    			int digit = java.lang.Character.digit(ch, 10);
    			if (result < max) {// overflow
    			    return null;
    			}
    			result *= 10;
    			if (result < limit + digit) {
    				return null;
    			}
    			// += would be much more obvious, but it doesn't work for Long.MIN_VALUE
    			result -= digit;
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
    	
    	// The magnitude
    	long factor = 1L;
        if (ii < length) {
            switch (string.charAt(ii++)) {
            case 'P':
                factor *= 1000L;
            case 'T':
                factor *= 1000L;
            case 'G':
                factor *= 1000L;
            case 'M':
                factor *= 1000L;
            case 'k':
                factor *= 1000L;
                break;
            default:
                return null;
            }
        }
        
        if (ii < length ||
                digitIndex == 0) {
            return null;
        }
        
        return Integer.instance(negative ? result*factor : -result*factor);
    }
    private parseInteger_(){}
}
