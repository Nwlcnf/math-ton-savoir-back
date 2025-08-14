package com.mathtonsavoir.backend.repository;

import com.mathtonsavoir.backend.model.Lecon;
import com.mathtonsavoir.backend.model.enums.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LeconRepository extends JpaRepository<Lecon, Long> {
    List<Lecon> findByChapitre_IdChapitre(Long idChapitre);
    List<Lecon> findByChapitre_NomChapitre(String nomChapitre);
    @Query("SELECT l FROM Lecon l JOIN FETCH l.chapitre WHERE l.nomLecon = :nomLecon")
    Optional<Lecon> findByNomLecon(@Param("nomLecon") String nomLecon);

    @Query("SELECT l FROM Lecon l WHERE l.chapitre.classe = :classe")
    List<Lecon> findByChapitreClasse(@Param("classe") Classe classe);
}
