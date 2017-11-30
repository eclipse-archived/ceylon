import com.vaadin.cdi {
    cdiView
}
import com.vaadin.navigator {
    View,
    ViewChangeListener {
        ViewChangeEvent
    }
}
import com.vaadin.ui {
    Label,
    VerticalLayout
}

cdiView ("unauthorized")
shared class InaccessibleErrorView() 
        extends VerticalLayout() 
        satisfies View {
    
    value text = "Sorry, view does not exist or you are not authorized to access it";
    
    enter(ViewChangeEvent viewChangeEvent) 
            => addComponent(Label(text));
    
}