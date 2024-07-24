package com.mmendoza.junit5.domain;
import com.mmendoza.junit5.exception.DineroInsuficienteException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*; /*metodos de assertions*/

class CuentaTest {

    @Test /*Test engine de JUnit*/
    void testNombreCuenta () {
        Cuenta cuenta = new Cuenta("Matias",new BigDecimal("1000.12345"));
                            /*expect - real*/
        assertFalse (cuenta.getPersona().isEmpty());
        assertEquals("Matias", cuenta.getPersona());
        assertEquals(new BigDecimal("1000.12345"), cuenta.getSaldo());
    }

    @Test
    void testSaldoCuenta () {
        Cuenta cuenta = new Cuenta("Carlos", new BigDecimal("1000.12345"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345,cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0); /*espera un false*/
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0); /*espera un true*/
    }

    @Test
    void testTranferenciaCuenta () {
        Cuenta cuenta = new Cuenta("Carlos", new BigDecimal("1000.12345"));
        Cuenta cuenta2 = new Cuenta("Carlos", new BigDecimal("1000.12345"));

        assertEquals(cuenta,cuenta2); /*compara si no son iguales ambas cuentas*/

    }

    @Test
    void testDebitoCuenta() {
        Cuenta cuenta = new Cuenta("Matias", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100)); /*le resta saldo*/
        assertNotNull(cuenta.getSaldo()); /*el saldo no debe ser nulo*/
        assertEquals(900,cuenta.getSaldo().intValue()); /*revisar saldo como un int*/
        assertEquals("900.12345",cuenta.getSaldo().toPlainString()); /*revisar saldo un string*/
    }

    @Test
    void testCreditoCuenta() {
        Cuenta cuenta = new Cuenta("Matias", new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal(100)); /*le resta saldo*/
        assertNotNull(cuenta.getSaldo()); /*el saldo no debe ser nulo*/
        assertEquals(1100,cuenta.getSaldo().intValue()); /*revisar saldo como un int*/
        assertEquals("1100.12345",cuenta.getSaldo().toPlainString()); /*revisar saldo un string*/
    }

    @Test /*validamos si se lanza la exception*/
    void testDineroInsuficienteExceptionCuenta(){
        Cuenta cuenta = new Cuenta("Matias",new BigDecimal("1000.12345"));
        /*simulamos el error*/
        DineroInsuficienteException exception = assertThrows(DineroInsuficienteException.class,()->{
            /*invocamos el metodo que va a lanzar la exception*/
            cuenta.debito(new BigDecimal(1500));
        });
        /*valido si es mensaje que se recibe es el esperado*/
        assertEquals("Dinero insuficiente en la cuenta",exception.getMessage());
    }

    @Test /*crear test segund condiciones del ambiente*/
    @EnabledOnOs(OS.WINDOWS) /*se ejecutara este test solo en windows*/
    void testSoloWindows(){

    }

    /*@Nested
    class testTimeOut{
        @Test
        @Timeout(value = 1000,unit = TimeUnit.MILLISECONDS)
        void pruebaTImeOut2() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(1100);
        }
    }*/

}