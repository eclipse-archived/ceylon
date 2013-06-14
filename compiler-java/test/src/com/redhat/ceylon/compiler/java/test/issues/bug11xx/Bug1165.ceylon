@noanno
void bug1165() {
    class B({String*} data) {
    }
    class P({String|B*} params) {
    }
    class Body(P p) {
    }
    class Html(Head head, Body body) {
    }
    class Head(String title) {
    }
    value html = Html {
        head = Head {
            title = "Hello";
        }; 
        body = Body {
            P {
                "Hello ", B { "JUDCon" }, " guys"
            };
        }; 
    };
    print(html);
}
