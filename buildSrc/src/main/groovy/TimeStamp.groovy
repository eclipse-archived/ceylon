import groovy.transform.CompileStatic
import java.text.SimpleDateFormat

@CompileStatic
class TimeStamp {
    static final String BUILD = {
        Date today = new Date ()
        SimpleDateFormat df = new SimpleDateFormat ("'v'yyyyMMdd-HHmm")
        return df.format (today)
    }.call()
}
