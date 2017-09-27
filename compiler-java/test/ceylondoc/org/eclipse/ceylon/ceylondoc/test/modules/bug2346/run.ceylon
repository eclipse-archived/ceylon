import com.google.common.escape {
    UnicodeEscaper
}
import java.lang {
    CharArray
}

class C() extends UnicodeEscaper() {
    shared actual CharArray escape(Integer int) => nothing;
}
