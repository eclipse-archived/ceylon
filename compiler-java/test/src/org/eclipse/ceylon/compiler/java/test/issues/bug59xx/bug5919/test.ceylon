import butterknife { ButterKnife, bindView }
import android.app { ListActivity }
import android.widget { ArrayAdapter, ListAdapter,
    ListView, TextView }

shared class Foo() extends ListActivity() {
    @packageProtected
    shared late bindView(1) TextView textView;
    
}