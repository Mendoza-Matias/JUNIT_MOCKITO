## Test del repositorio 

Ejemplo de codigo realizado con Junit5 y Mockito

```java
package com.mmendoza.employee_system.persistence.repository;

import com.mmendoza.employee_system.domain.entity.Employee;
import com.mmendoza.employee_system.domain.enums.Contract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class IEmployeeRepositoryTest {

    @Autowired
    private IEmployeeRepository repository;

    @Autowired
    private TestEntityManager testManager; /*persist in memory*/

    private Employee effective;

    private Employee hired;

    @BeforeEach
    void setUp() {
        effective = Employee.builder()
                .dni("44547239")
                .name("Juan")
                .lastName("Perez")
                .salary(new BigDecimal("500.000"))
                .contract(Contract.EFFECTIVE)
                .build();

        hired = Employee.builder()
                .dni("123578936")
                .name("Marcos")
                .lastName("Rodriguez")
                .salary(new BigDecimal("600.000"))
                .contract(Contract.HIRED)
                .build();

        testManager.persist(effective); /*persist employee before testing*/
        testManager.persist(hired);
    }

    @Test
    void findAll() {
        List<Employee> employees = repository.findAll();
        assertFalse(employees.isEmpty());
        assertEquals(3,employees.size());
    }

    @Test
    void findByDni() {
        Optional<Employee> employee = repository.findByDni("44547239");
        assertTrue(employee.isPresent());
        assertEquals("Juan", employee.get().getName());
        assertEquals("Perez", employee.get().getLastName());
        assertEquals(new BigDecimal("500.000"), employee.get().getSalary());
        assertEquals(Contract.EFFECTIVE, employee.get().getContract());
    }

    @Test
    void existByDni() {
        Boolean exist = repository.existsById(44547239);
        assertTrue(exist);
    }

    @Test
    void findAllByContractHired() {
        List<Employee> employees = repository.getAllByContract(Contract.HIRED);
        assertEquals(2, employees.size());
        assertEquals("Juan", employees.get(0).getName());
        assertEquals("Perez", employees.get(0).getLastName());
        assertEquals(new BigDecimal("600.000"), employees.get(0).getSalary());
        assertEquals(Contract.HIRED, employees.get(0).getContract());
    }

    @Test
    void findAllByContractEffective() {
        List<Employee> employees = repository.getAllByContract(Contract.EFFECTIVE);
        assertEquals(1, employees.size());
        assertEquals("Juan", employees.get(0).getName());
        assertEquals("Perez", employees.get(0).getLastName());
        assertEquals(new BigDecimal("500.000"), employees.get(0).getSalary());
        assertEquals(Contract.EFFECTIVE, employees.get(0).getContract());
    }
}

```