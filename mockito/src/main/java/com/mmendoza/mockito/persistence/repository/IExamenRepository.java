package com.mmendoza.mockito.persistence.repository;
import com.mmendoza.mockito.domain.entity.Examen;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IExamenRepository {
    Examen save(Examen examen);
    List <Examen> findAll();

}
