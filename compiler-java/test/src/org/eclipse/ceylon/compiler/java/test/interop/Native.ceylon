import java.lang{jni=native}

@noanno
class Native(/*s, shared jni String u*/) {
    //shared jni String s;
    shared jni String t;
    shared jni variable String t2;
    shared jni String m(String f="");
    jni String m2(String f="");
}
@noanno
interface Native2 {
    shared jni String m(String f="");
    //jni String m2(String f="");
}

@noanno
shared jni variable String nativeValue;
//shared jni String nativeGetter;
//assign nativeGetter {}
@noanno
shared jni String nativeFunction(String f="");