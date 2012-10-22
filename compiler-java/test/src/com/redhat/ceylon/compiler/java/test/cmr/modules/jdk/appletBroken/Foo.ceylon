import java.beans { AppletInitializer }
import java.applet { Applet }

shared void foo(AppletInitializer i, Applet app){
    i.activate(app);
}