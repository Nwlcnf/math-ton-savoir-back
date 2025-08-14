package com.mathtonsavoir.backend.controller;

import com.mathtonsavoir.backend.dto.ReponseExerciceDTO;
import com.mathtonsavoir.backend.exception.ResourceNotFoundException;
import com.mathtonsavoir.backend.service.ReponseExerciceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reponses-exercices")
@RequiredArgsConstructor
@Tag(name = "ReponseExercice", description = "Opérations liées aux réponses aux exercices")
public class ReponseExerciceController {

    private final ReponseExerciceService reponseExerciceService;

    @GetMapping("/{id}")
    public ResponseEntity<ReponseExerciceDTO> getById(@PathVariable Long id) {
        ReponseExerciceDTO dto = reponseExerciceService.getById(id);
        if (dto == null) throw new ResourceNotFoundException("Réponse exercice " + id + " non trouvée");
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ReponseExerciceDTO>> getAll() {
        return new ResponseEntity<>(reponseExerciceService.getAll(), HttpStatus.OK);
    }
}
