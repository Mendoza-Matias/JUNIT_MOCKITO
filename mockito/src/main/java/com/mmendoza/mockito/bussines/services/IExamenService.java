package com.mmendoza.mockito.bussines.services;

import com.mmendoza.mockito.domain.entity.Examen;
import org.springframework.stereotype.Repository;

@Repository
public interface IExamenService {

    Examen findExamenPorNombre(String nombre);

    Examen findExamenPorNombreConPreguntas(String nombre);

    Examen save(Examen examen);
}
