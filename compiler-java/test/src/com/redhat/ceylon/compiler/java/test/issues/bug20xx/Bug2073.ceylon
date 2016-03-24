import java.awt.event { MouseEvent, KeyEvent }
import javax.swing { JMenu }
object bug2073 extends JMenu("MWE") {
}
object bug2073_2 extends JMenu("MWE") {
    shared actual void processKeyEvent(KeyEvent? keyEvent) 
            => super.processKeyEvent(keyEvent);
}
object bug2073_3 extends JMenu("MWE") {
    shared actual void processMouseEvent(MouseEvent? mouseEvent) 
            => super.processMouseEvent(mouseEvent);
}