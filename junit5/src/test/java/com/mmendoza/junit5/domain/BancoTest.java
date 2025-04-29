package com.mmendoza.junit5.domain;

import com.mmendoza.junit5.exception.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class BancoTest {

    private Cuenta c1;
    private Cuenta c2;

    @BeforeEach
    void initMetodoTest() {
        System.out.println("Iniciando MetodoTest");
        c1 = new Cuenta("Matias", new BigDecimal("1000"));
        c2 = new Cuenta("Jose", new BigDecimal("1000"));
    }

    @AfterEach
    void finalMetodoTest() {
        System.out.println("Finalizando MetodoTest");
    }

    @Tag("banco")
    @Nested
    class MetodosDeBancoTest {

        @Test
        @DisplayName("Probando metodo de transferir")
        void transferir() {
            Banco banco = new Banco("Santander");
            banco.transferir(c1, c2, new BigDecimal("500"));
            assertEquals("500", c1.getSaldo().toPlainString());
            assertEquals("1500", c2.getSaldo().toPlainString());
        }

        @Test
        @DisplayName("Verificando la relacion del banco y la cuenta")
        void testRelacionBancoCuenta() {
            Banco banco = new Banco("Santander");
            banco.addCuenta(c1);
            banco.addCuenta(c2);

            assertEquals(2, banco.getCuentas().size());
            assertEquals("Santander", c1.getBanco().getNombre(), () -> "El nombre del banco no es esperado");

            assertTrue(banco.getCuentas().stream()
                    .anyMatch(c -> c.getPersona().equals("Matias")));

            assertEquals("Matias", banco.getCuentas().stream()
                    .filter(c -> c.getPersona().equals("Matias"))
                    .findFirst().get().getPersona());
        }

        @Test
        @DisplayName("Verificando la relacion del banco y la cuenta simplificado")
        void testRelacionBancoCuentaSimplificado() {
            Banco banco = new Banco("Santander");
            banco.addCuenta(c1);
            banco.addCuenta(c2);

            assertAll(
                    () -> assertEquals(2, banco.getCuentas().size()),
                    () -> assertEquals("Santander", c1.getBanco().getNombre()),
                    () -> assertTrue(banco.getCuentas().stream()
                            .anyMatch(c -> c.getPersona().equals("Matias"))),
                    () -> assertEquals("Matias", banco.getCuentas().stream()
                            .filter(c -> c.getPersona().equals("Matias"))
                            .findFirst().get().getPersona())
            );
        }
    }

    @Nested
    class PruebasDeRequerimientosTest {

        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {
        }

        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void testVersionJava() {
        }

        @Test
        @DisabledOnJre(JRE.JAVA_16)
        void testNoJava16() {
        }

        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
        void solo64Bits() {
        }
    }

    @Nested
    class PruebasDeVariablesTest {

        @Test
        @DisplayName("transferir solo en Dev")
        void transferirDev() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            assumeTrue(esDev);

            Banco banco = new Banco("Santander");
            banco.transferir(c1, c2, new BigDecimal("500"));

            assertEquals("500", c1.getSaldo().toPlainString());
            assertEquals("1500", c2.getSaldo().toPlainString());
        }

        @Test
        @DisplayName("transferir Dev con assumingThat")
        void transferirDev2() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            Banco banco = new Banco("Santander");
            banco.transferir(c1, c2, new BigDecimal("500"));

            assumingThat(esDev, () -> {
                assertEquals("500", c1.getSaldo().toPlainString());
            });

            assertEquals("1500", c2.getSaldo().toPlainString());
        }
    }

    @DisplayName("Probando repeticion de test")
    @RepeatedTest(5)
    void testTransferir() {
        Banco banco = new Banco("Santander");
        banco.transferir(c1, c2, new BigDecimal("500"));
        assertEquals("500", c1.getSaldo().toPlainString());
        assertEquals("1500", c2.getSaldo().toPlainString());
    }

    @ParameterizedTest
    @CsvSource({"100,900,1100", "200,800,1200"})
    void testTransferirParametrizado(String saldo, String esperadoC1, String esperadoC2) {
        Banco banco = new Banco("Santander");
        banco.transferir(c1, c2, new BigDecimal(saldo));

        assertTrue(c1.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        assertEquals(esperadoC1, c1.getSaldo().toPlainString());
        assertEquals(esperadoC2, c2.getSaldo().toPlainString());
    }
}
