# Test del controller 

Ejemplo utilizando Junit5 y Mockito

```java
package com.mmendoza.employee_system.presentation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmendoza.employee_system.bussines.services.IEmployeeService;
import com.mmendoza.employee_system.domain.dto.employee.EmployeeDto;
import com.mmendoza.employee_system.domain.dto.employee.EmployeeSaveDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeesController.class)
class EmployeesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IEmployeeService employeeService;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Test
    void getAllEmployees() throws Exception {
        List<EmployeeDto> employes = Arrays.asList(
                EmployeeDto
                        .builder()
                        .dni("123")
                        .name("Juan")
                        .lastName("Perez")
                        .build()
        );
        Mockito.when(employeeService.getAllEmployees()).thenReturn(employes);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) /*check the response body*/
                .andExpect(jsonPath("$[0].dni").value(123));
    }

    @Test
    void getAllEmployeesByContract() {
    }

    @Test
    void findEmployeeByDni() {
    }

    @Test
    void createEmployee() throws Exception {
        EmployeeSaveDto newEmployee = EmployeeSaveDto.builder()
                .dni("123")
                .name("Pepe")
                .lastName("Perez")
                .isHired(false)
                .build();

        Mockito.doNothing().when(employeeService).saveEmployee(newEmployee);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/employees/create")
                        .contentType(MediaType.APPLICATION_JSON).content(jacksonObjectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateEmployee() {
    }

    @Test
    void deleteEmployee() {
    }
}
```