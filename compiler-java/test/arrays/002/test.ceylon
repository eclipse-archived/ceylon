public void test(Process p) {
    Integer size = 10;

    class ArrayList2d<T> () {
        mutable Integer xmax := -1; 
        mutable Integer ymax := -1;
        ArrayList<ArrayList<T>> data =  ArrayList<ArrayList<T>>();
 
        void ensureCapacity(Integer x) {
            if (x > xmax) {
                mutable Integer tmp := xmax + 1;
                while (tmp <= x) {
                    data[x] :=  ArrayList<T>();
                    tmp++;
                }
                xmax := x;
            }
        }
       
        public ArrayList<T> value(Integer i) {
            ensureCapacity(i);
            return data[i];
        }
    }

    ArrayList2d<Integer> a = ArrayList2d<Integer>();

    mutable Integer i := 0;
    while (i <= size) {
        mutable Integer j := 0;
        while (j <= size) {
            a[i][j] := i*j;
            j++;
        }
        i++;
    }

    i := 0;
    while (i <= size) {
        mutable Integer j := 0;
        while (j <= size) {
            p.write(""a[i][j]" ");
            j++;
        }
        p.writeLine("");
        i++;
    }
}
