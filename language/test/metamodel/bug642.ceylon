shared serializable class Bug642(destino,items,impuesto=16.0,descuento=0.0) {
    shared Object destino;
    shared [Object +] items;
    shared Float impuesto;
    shared Float descuento;
}
@test
shared void bug642() {
    value x = `Bug642`.declaration;
}
