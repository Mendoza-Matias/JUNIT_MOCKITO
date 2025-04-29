package com.mmendoza.mockito.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Examen {
    private Long id;
    private String nombre;
    private List<String> preguntas;
}
