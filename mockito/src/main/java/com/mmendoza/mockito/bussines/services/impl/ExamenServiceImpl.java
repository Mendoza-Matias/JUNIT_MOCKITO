package com.mmendoza.mockito.bussines.services.impl;

import com.mmendoza.mockito.bussines.services.IExamenService;
import com.mmendoza.mockito.domain.entity.Examen;
import com.mmendoza.mockito.persistence.repository.IExamenRepository;
import com.mmendoza.mockito.persistence.repository.IPreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamenServiceImpl implements IExamenService {

    private IExamenRepository examenRepository;

    private IPreguntaRepository preguntaRepository;

    public ExamenServiceImpl(IExamenRepository examenRepository, IPreguntaRepository preguntaRepository) {
        this.examenRepository = examenRepository;
        this.preguntaRepository = preguntaRepository;
    }


    @Override
    public Examen findExamenPorNombre(String nombre) {
        return examenRepository.findAll().
                stream().filter(e -> e.getNombre().equals(nombre))
                .findFirst().orElse(null);
    }

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre) {
        Examen examen = findExamenPorNombre(nombre);
        List <String> preguntas = preguntaRepository.findPreguntasPorExamenId(examen.getId());
        return Examen.builder()
                .id(examen.getId())
                .nombre(examen.getNombre())
                .preguntas(preguntas)
                .build();
    }

    @Override
    public Examen save(Examen examen) {
        if (!examen.getPreguntas().isEmpty()){
            preguntaRepository.saveAll(examen.getPreguntas()); /*guardo mis preguntas*/
        }
        return examenRepository.save(examen); /*guarda el examen*/
    }

}
