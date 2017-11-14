native shared class NativeScopesWrong() {
    native shared void test();
}

native("jvm") shared class NativeScopesWrong() {
    native("jvm") shared void test() {
        void test2() {
            $error native("js") void test3() {
            }
        }
        class Bar() {
            class Bar2() {
                $error native("js") void test() {
                }
            }
        }
    }
}

native("js") shared class NativeScopesWrong() {
    native("js") shared void test() {
        void test2() {
            $error native("jvm") void test3() {
            }
        }
        class Bar() {
            class Bar2() {
                $error native("jvm") void test() {
                }
            }
        }
    }
}
