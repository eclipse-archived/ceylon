import bug1062.javaA { JavaA }
import bug1062.javaB { JavaB }

void test(){
    JavaA ja = JavaB().method(JavaA());
    ja.methodA2();
    //JavaB().method(null).methodA2();
}
