import java.lang { 
    JInteger=Integer,
    JFloat=Float,  
    JCharacter=Character,
    JShort=Short}

void bug2271(JInteger? ji, JFloat? jf, JCharacter? jc, JShort? js) {
    if (exists i = ji?.intValue(), true) {}
    else {}
    if (exists f = jf?.floatValue(), true) {}
    else {}
    if (exists c = jc?.charValue(), true) {}
    else {}
    if (exists s = js?.shortValue(), true) {}
    else {}
}