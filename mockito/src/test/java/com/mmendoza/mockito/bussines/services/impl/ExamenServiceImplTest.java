package com.mmendoza.mockito.bussines.services.impl;

import com.mmendoza.mockito.bussines.services.IExamenService;
import com.mmendoza.mockito.domain.entity.Examen;
import com.mmendoza.mockito.persistence.repository.IExamenRepository;
import com.mmendoza.mockito.persistence.repository.IPreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    @InjectMocks
    private ExamenServiceImpl service;

    @Mock
    private IExamenRepository repository;

    @Mock
    private IPreguntaRepository preguntaRepository;

    @Captor
    private ArgumentCaptor<Long> captor;

    private List<Examen> examenes;
    private List<String> preguntas;
    private Examen examen;

    @BeforeEach
    void setUp() {
        examenes = Arrays.asList(
                Examen.builder().id(null).nombre("Naturales").build(),
                Examen.builder().id(1L).nombre("Matematicas").build(),
                Examen.builder().id(2L).nombre("Literatura").build(),
                Examen.builder().id(3L).nombre("Sociales").build()
        );

        preguntas = Arrays.asList("aritmetica", "geometria", "integrales", "filosofia");

        examen = Examen.builder()
                .id(null)
                .nombre("Fisica")
                .preguntas(preguntas)
                .build();
    }

    @Test
    @DisplayName("Método para buscar un examen por nombre")
    void testFindExamenPorNombre() {
        when(repository.findAll()).thenReturn(examenes);

        Examen examen = service.findExamenPorNombre("Matematicas");

        assertAll(
                () -> assertNotNull(examen),
                () -> assertEquals(1L, examen.getId()),
                () -> assertEquals("Matematicas", examen.getNombre())
        );
    }

    @Test
    @DisplayName("Obtener preguntas de un examen")
    void testPreguntasExamen() {
        when(repository.findAll()).thenReturn(examenes);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(4, examen.getPreguntas().size());
    }

    @Test
    @DisplayName("Verificar llamada a repository y obtener preguntas")
    void testPreguntasExamenVerify() {
        when(repository.findAll()).thenReturn(examenes);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(4, examen.getPreguntas().size());
        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    @DisplayName("Guardar examen")
    void testSave() {
        when(repository.save(any(Examen.class))).then(new Answer<Examen>() {
            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocation) {
                Examen examenInvocation = invocation.getArgument(0);
                return Examen.builder()
                        .id(secuencia++)
                        .nombre(examenInvocation.getNombre())
                        .build();
            }
        });

        Examen examenService = service.save(examen);

        verify(repository).save(any(Examen.class));
        verify(preguntaRepository).saveAll(anyList());
        assertEquals(8L, examenService.getId());
        assertEquals("Fisica", examenService.getNombre());
    }

    @Test
    @DisplayName("Manejo de exceptions")
    void testManejoException() {
        when(repository.findAll()).thenReturn(examenes);
        when(preguntaRepository.findPreguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.findExamenPorNombreConPreguntas("Naturales")
        );

        assertEquals(IllegalArgumentException.class, exception.getClass());
    }

    @Test
    @DisplayName("Argument matchers personalizados")
    void testArgumentMatchers() {
        when(repository.findAll()).thenReturn(examenes);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);

        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(preguntaRepository).findPreguntasPorExamenId(argThat(arg -> arg != null && arg.equals(1L)));
    }

    @Test
    @DisplayName("Argument captor")
    void testArgumentCapture() {
        when(repository.findAll()).thenReturn(examenes);

        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(preguntaRepository).findPreguntasPorExamenId(captor.capture());
        assertEquals(1L, captor.getValue());
    }

    @Test
    @DisplayName("doThrow para simular excepción en void")
    void testDoThrow() {
        doThrow(IllegalArgumentException.class).when(preguntaRepository).saveAll(anyList());

        assertThrows(IllegalArgumentException.class, () -> service.save(examen));
    }

    @Test
    @DisplayName("doAnswer para definir comportamiento según argumento")
    void testDoAnswer() {
        when(repository.findAll()).thenReturn(examenes);
        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 1L ? preguntas : Collections.emptyList();
        }).when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        Examen examenService = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(1L, examenService.getId());
        assertEquals("Matematicas", examenService.getNombre());
    }

    @Test
    @DisplayName("doCallRealMethod")
    void testLlamadoRealAlMetodo() {
        when(repository.findAll()).thenReturn(examenes);
        doCallRealMethod().when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        Examen examenService = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(1L, examenService.getId());
        assertEquals("Matematicas", examenService.getNombre());
    }

    @Test
    @DisplayName("spy (pendiente de implementación)")
    void testSpy() {
        IExamenRepository examenRepository = spy(IExamenRepository.class);
        // Aquí podrías completar con algún método real o verificación
    }
}
