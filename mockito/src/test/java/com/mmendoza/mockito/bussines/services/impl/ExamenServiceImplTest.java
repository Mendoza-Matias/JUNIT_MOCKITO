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

@ExtendWith(MockitoExtension.class) /*habilito el uso de anotaciones*/
class ExamenServiceImplTest {
    /*simulamos una instancia de la clase service y le inyectamos nuestras dependencias*/
    @InjectMocks
    private ExamenServiceImpl service; /*tipo concreto de la clase*/
    @Mock
    private IExamenRepository repository;
    @Mock /*creamos el mock de nuestra dependencia*/
    private IPreguntaRepository preguntaRepository;

    /*captor con anotaciones*/
    @Captor
    private ArgumentCaptor<Long> captor;

    private List<Examen> examenes;
    private List<String> preguntas;
    private Examen examen;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this); /*habilitamos el uso de anotaciones*/
        /*simulo mis datos*/
        examenes = Arrays.asList(
                Examen.builder().id(null).nombre("Naturales").build(),
                Examen.builder().id(1L).nombre("Matematicas").build(),
                Examen.builder().id(2L).nombre("Literatura").build(),
                Examen.builder().id(3L).nombre("Sociales").build());

        preguntas = Arrays.asList("aritemetica", "geometria", "integrales", "filosofia");
        examen = Examen.builder()
                .id(null)
                .nombre("Fisica")
                .preguntas(preguntas)
                .build();
    }

    @Test
    @DisplayName("metodo para buscar un examen por nombre")
    void testFindExamenPorNombre() {

        /*cuando se llama al metodo findAll - retorna los datos | dependencia del service*/
        when(repository.findAll()).thenReturn(examenes);

        Examen examen = service.findExamenPorNombre("Matematicas");

        assertAll(
                () -> {
                    assertNotNull(examen);
                },
                () -> {
                    assertEquals(1L, examen.getId());
                },
                () -> {
                    assertEquals("Matematicas", examen.getNombre());
                }
        );

    }

    @Test
    @DisplayName("obtener preguntas de un examen")
    void testPreguntasExamen() {
        /*traigo todos mis examenes*/
        when(repository.findAll()).thenReturn(examenes);
        /*busco un examen con id 1 - el argumento debe ser un long y se aplica para cualquier id*/
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
        /*le paso el nombre de ese examen y este me lo devolvera con sus preguntas*/
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(4, examen.getPreguntas().size());
    }

    @Test
    @DisplayName("verificar si se llama al repository y obtener preguntas de un examen ")
    void testPreguntasExamenVerify() {
        /*traigo todos mis examenes*/
        when(repository.findAll()).thenReturn(examenes);
        /*busco un examen con id 1 - el argumento debe ser un long y se aplica para cualquier id*/
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
        /*le paso el nombre de ese examen y este me lo devolvera con sus preguntas*/
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(4, examen.getPreguntas().size());

        verify(repository).findAll(); /*verifico si se llama al repositorio*/
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    @DisplayName("guardar examen")
    void testSave() {
        //||--GIVEN--Preparo mi entorno -> invocamos ||
        /*llamo al repositorio con el metodo guardar*/
        when(repository.save(any(Examen.class))).then(new Answer<Examen>() {
            Long secuencia = 8L;

            /*simulamos la creación del id dentro del metodo de test - |CUANDO HAGO VARIOS LLAMADOS AL SERVICE|*/
            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examenInvocation = invocationOnMock.getArgument(0);
                examenInvocation = Examen.builder()
                        .id(secuencia++)
                        .nombre(examenInvocation.getNombre())
                        .build();
                return examenInvocation;
            }
        });
        /*|--WHEN--|*/
        /*aca llama al service el cual dentro llama al repository y guarda un examen y retorna este mismo*/
        Examen examenService = service.save(examen); /**/

        verify(repository).save(any(Examen.class)); /*verifico que se llame el repository*/
        verify(preguntaRepository).saveAll(anyList()); /*se llama solo cuando el examen tiene preguntas*/
        assertEquals(8L, examenService.getId());
        assertEquals("Fisica", examen.getNombre());
    }

    @Test
    @DisplayName("manejo de exceptions")
    void testManejoException() {

        when(repository.findAll()).thenReturn(examenes);

        /*cualquier argumento que sea null , va a lanzar la exception - |ARGUMENT MATCHER| */
        when(preguntaRepository.findPreguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.findExamenPorNombreConPreguntas("Naturales") /*Busca y no lo encuentra*/
        );

        assertEquals(IllegalArgumentException.class, exception.getClass());

    }

    @Test
    @DisplayName("argument matchers personalizados")
    void testArgumentMatchers() {
        when(repository.findAll()).thenReturn(examenes);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);

        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(repository).findAll();
        /*verficamos que el argumento que se le pasa al metodo es igual al valor dado*/
        verify(preguntaRepository).findPreguntasPorExamenId(argThat(arg -> arg != null && arg.equals(1L)));
    }

    @Test
    @DisplayName("argument capture")
    void testArgumentCapture() {
        when(repository.findAll()).thenReturn(examenes);

        service.findExamenPorNombreConPreguntas("Matematicas");

        /*capturamos el argumento cuando se llama al metodo*/
        //ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

        verify(preguntaRepository).findPreguntasPorExamenId(captor.capture());
        assertEquals(1L, captor.getValue()); /*verificamos si el id capturado es igual a 1L*/
    }

    @Test
    @DisplayName("do throw")
    void testDoThrow() {
        /*cuando no retorna nada y queremos validar una exception - cuando del objeto repository se llama al metodo */
        doThrow(IllegalArgumentException.class).when(preguntaRepository).saveAll(anyList());

        assertThrows(IllegalArgumentException.class, () -> {
            service.save(examen);
        });
    }

    @Test
    @DisplayName("do answer")
    void testDoAnswerd() {
        when(repository.findAll()).thenReturn(examenes);
        //when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
        doAnswer(invocation -> { /*cuando se llama al metodo del mock , se lanza la invocacion*/
            Long id = invocation.getArgument(0); /*capturamos el id*/
            return id == 1L ? preguntas : Collections.emptyList();
        }).when(preguntaRepository).findPreguntasPorExamenId(anyLong());
        Examen examenService = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(1L, examenService.getId());
        assertEquals("Matematicas", examenService.getNombre());
    }

    @Test
    @DisplayName("llamado al metodo real")
    void testLlamadoRealAlMetodo() {
        when(repository.findAll()).thenReturn(examenes);

        /*cuando usamos do algo , llamamos al mock - |INVOCAMOS AL METODO REAL |
         * PARA ESTO NECESITAMOS UNA CLASE CONCRETA
         * */
        doCallRealMethod().when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        Examen examenService = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(1L, examenService.getId());
        assertEquals("Matematicas", examenService.getNombre());
    }

    @Test
    @DisplayName("spy")
    void testSpy() {
        /*simulamos el llamado a metodos reales - |USO DE UNA CLASE CONCRETA|*/
        IExamenRepository examenRepository = spy(IExamenRepository.class);
    }
}