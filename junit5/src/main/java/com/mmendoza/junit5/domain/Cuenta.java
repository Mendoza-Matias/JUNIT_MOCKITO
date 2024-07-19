package com.mmendoza.junit5.domain;
import com.mmendoza.junit5.exception.DineroInsuficienteException;

import java.math.BigDecimal;

public class Cuenta {

    private String persona;

    private BigDecimal saldo;

    private Banco banco;

    public Cuenta(String persona, BigDecimal saldo) {
        this.persona = persona;
        this.saldo = saldo;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    /*metodo para debitar saldo - quita dinero*/
    public void debito(BigDecimal saldo) {
        BigDecimal nuevoSaldo = this.saldo.subtract(saldo);
        if(nuevoSaldo.compareTo(BigDecimal.ZERO) < 0){ /*si es -1 entonces es verdadero*/
            throw new DineroInsuficienteException("Dinero insuficiente en la cuenta");
        }
        this.saldo = nuevoSaldo;
    }
    /*metodo para acreditar saldo - suma dinero*/
    public void credito(BigDecimal saldo) {
        this.saldo = this.saldo.add(saldo); /*realizar de esta manera porque BigDecimal es inmutable*/
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    @Override
    public boolean equals(Object obj) {
        /*si el objeto no es nulo*/
        if (!(obj instanceof Cuenta)){
            return false;
        }
        Cuenta c = (Cuenta) obj;
        /*si los datos no son nulos*/
        if(this.persona == null || this.saldo == null){
            return false;
        }
        return this.persona.equals(c.getPersona()) && this.saldo.equals(c.getSaldo());
    }
}
