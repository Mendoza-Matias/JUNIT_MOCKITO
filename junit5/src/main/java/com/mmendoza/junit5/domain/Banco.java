package com.mmendoza.junit5.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Banco {

    private String nombre;
    private List<Cuenta> cuentas;

    // Constructor
    public Banco(String nombre) {
        this.nombre = nombre;
        this.cuentas = new ArrayList<>();
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    // Agregar una cuenta al banco
    public void addCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
        cuenta.setBanco(this);
    }

    // Transferencia entre cuentas
    public void transferir(Cuenta origen, Cuenta destino, BigDecimal monto) {
        origen.debito(monto); // Retira el dinero de la cuenta origen
        destino.credito(monto); // Deposita el dinero en la cuenta destino
    }
}
