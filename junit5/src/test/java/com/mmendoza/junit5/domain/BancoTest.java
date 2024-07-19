package com.mmendoza.junit5.domain;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class BancoTest {

    private Cuenta c1;

    private Cuenta c2;

    @BeforeEach /*Inicializa los objetos antes de cada prueba asi se podran utilizar*/
    void initMetodoTest(){
        /*De esta manera podemos reutilizar nuestro codigo en varias pruebas*/
        System.out.println("Iniciando MetodoTest");
        c1 = new Cuenta("Matias", new BigDecimal("1000"));
        c2 = new Cuenta("Jose", new BigDecimal("1000"));
    }

    @AfterEach /*Se inicializa al finalizar cada metodo test*/
    void finalMetodoTest(){
        System.out.println("Finalizando MetodoTest");
    }

    @Tag("banco") //Utilizando el tag podemos ejecutar solo un tipo de pruebas , || personalizar configuracion
    @Nested /*contexto separado*/
    class metodosDeBancoTest{

        @Test
        @DisplayName("Probando metodo de transferir")
        void transferir() {
            Banco banco = new Banco("Santander");
            banco.transferir(c1, c2, new BigDecimal("500"));
            assertEquals("2000", c1.getSaldo().toPlainString());
            assertEquals("1500", c2.getSaldo().toPlainString());
        }

        @Test
        @DisplayName("Verificando la relacion del banco y la cuenta") /*nombre del test*/
        void testRelacionBancoCuenta() {
            Banco banco = new Banco("Santander");
            banco.addCuenta(c1);
            banco.addCuenta(c2);

            assertEquals(2, banco.getCuentas().size());
            /*Ingresando un string al final se mostrara el mensaje informando el problema*/
            assertEquals("Santander", c1.getBanco().getNombre(),()->"El nombre del banco no es esperado");

            assertTrue(banco.getCuentas().stream().
                    anyMatch(c -> c.getPersona() /*revisa si hay una cuenta con nombre Matias*/
                            .equals("Matias")));

            assertEquals("Matias",
                    banco.getCuentas().stream()
                            .filter(c -> c.getPersona().equals("Matias"))
                            .findFirst().get().getPersona()); /*obtiene un objeto cuenta y el valor de la persona y compara si existe*/
        }

        @Test
        @DisplayName("Verificando la relacion del banco y la cuenta")
        void testRelacionBancoCuentaSimplificado() {
            Banco banco = new Banco("Santander");
            banco.addCuenta(c1);
            banco.addCuenta(c2);

            assertAll(
                    () -> assertEquals(2, banco.getCuentas().size())
                    ,() -> assertEquals("Santander", c1.getBanco().getNombre())
                    ,() -> { assertTrue(banco.getCuentas().stream()
                            .anyMatch(c -> c.getPersona().equals("Matias")));
                        /*revisa si hay una cuenta con nombre Matias*/
                    }
                    , () -> {
                        assertEquals("Matias", banco.getCuentas().stream()
                                .filter(c -> c.getPersona().equals("Matias"))
                                .findFirst().get().getPersona());
                    }
                    /*obtiene un objeto cuenta y el valor de la persona y compara si existe*/
            );

        }
    }

    /*clase anidada - de esta manera englobamos pruebas de un unico tipo*/
    @Nested
    class pruebasDeRequerimientesTest {
        @Test /*crear test segund condiciones del ambiente*/
        @EnabledOnOs(OS.WINDOWS) /*se ejecutara este test solo en windows*/
        void testSoloWindows(){

        }

        @Test /*crear test segund condiciones del ambiente*/
        @EnabledOnJre(JRE.JAVA_8) /*se ejecutara este test solo en java 8 */
        void testVersionJava(){
        }

        @Test
        @DisabledOnJre(JRE.JAVA_16) /*se ejecuta el test si no es java16*/
        void testNoJava16(){

        }

        @ Test
        @DisabledIfSystemProperty(named = "os.arch",matches = ".*64.*") /*usamos enable si no queremos que se ejecute*/
        void solo64Bits(){

        }
    }

    @Nested
    class pruebasDeVariablesTest{
        @Test
        @DisplayName("transferir Dev")
        void transferirDev() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            assumeTrue(esDev);
            Banco banco = new Banco("Santander");
            banco.transferir(c1, c2, new BigDecimal("500"));
            assertEquals("2000", c1.getSaldo().toPlainString());
            assertEquals("1500", c2.getSaldo().toPlainString());
        }

        @Test
        @DisplayName("transferir Dev")
        void transferirDev2() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            Banco banco = new Banco("Santander");
            banco.transferir(c1, c2, new BigDecimal("500"));
            assumingThat(esDev , ()-> { /*ejecuta en el caso que se cumpla que el tipo de variable es dev*/
                assertEquals("200", c1.getSaldo().toPlainString());
            });
            assertEquals("1500", c2.getSaldo().toPlainString());
        }
    }

    @DisplayName("Probando repeticion de test")
    @RepeatedTest(5) /*repetir un test varias veces*/
    void testTransferir() {
        Banco banco = new Banco("Santander");
        banco.transferir(c1, c2, new BigDecimal("500"));
        assertEquals("2000", c1.getSaldo().toPlainString());
        assertEquals("1500", c2.getSaldo().toPlainString());
    }

    /*metodos parametrizados con varios valore*/
    //@ValueSource - asigna un parametro
    @ParameterizedTest
    @CsvSource({"100,900,1100","200,800,1200"}) /*le paso distintos valores || estilo lista || */
    void testTransferirParametrizado(String saldo , String actualc1 , String actualc2) { /*le paso el parametro - me setea los valore que establece*/
        Banco banco = new Banco("Santander");
        banco.transferir(c1, c2, new BigDecimal(saldo));

       assertTrue(c1.getSaldo().compareTo(BigDecimal.ZERO) > 0);
       assertEquals(actualc1, c1.getSaldo().toPlainString());
       assertEquals(actualc2, c2.getSaldo().toPlainString());

    }
}