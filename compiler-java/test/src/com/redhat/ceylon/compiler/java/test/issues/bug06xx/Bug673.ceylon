@nomodel
shared abstract class Bug673_Event() {
}
@nomodel
shared abstract class Bug673_GridEvent() extends Bug673_Event() {
}
@nomodel
shared abstract class Bug673_StoreEvent() extends Bug673_Event()   {
}
@nomodel
shared interface Bug673_EventObserver<Evento>
                    given Evento satisfies Bug673_Event {
    default shared void on( Evento event, Boolean() action ) {
        //Add no mapa
    }
}
@nomodel
shared interface Bug673_GridBehavior satisfies Bug673_EventObserver<Bug673_StoreEvent|Bug673_GridEvent> {
    shared actual void on( Bug673_StoreEvent|Bug673_GridEvent event, Boolean() action ) {
        throw;
    }
}
@nomodel
shared class Bug673_PaisController() satisfies Bug673_GridBehavior {
}
