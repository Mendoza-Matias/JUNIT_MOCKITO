package com.mmendoza.mockito.persistence.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPreguntaRepository {
    void saveAll(List<String>preguntas);
    List <String> findPreguntasPorExamenId(Long id);
}
