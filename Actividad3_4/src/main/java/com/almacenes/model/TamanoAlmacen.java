package com.almacenes.model;

public enum TamanoAlmacen {
    G("Grande"),
    M("Mediano"),
    P("Pequeño");
    
    private final String descripcion;
    
    TamanoAlmacen(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
