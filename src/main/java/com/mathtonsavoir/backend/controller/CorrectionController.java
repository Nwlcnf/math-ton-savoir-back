package com.mathtonsavoir.backend.controller;

import com.mathtonsavoir.backend.dto.CorrectionDTO;
import com.mathtonsavoir.backend.exception.ResourceNotFoundException;
import com.mathtonsavoir.backend.service.CorrectionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/corrections")
@RequiredArgsConstructor
@Tag(name = "Correction", description = "Opérations liées aux corrections")
public class CorrectionController {

    private final CorrectionService correctionService;

    @GetMapping("/{id}")
    public ResponseEntity<CorrectionDTO> getById(@PathVariable Long id) {
        CorrectionDTO dto = correctionService.getById(id);
        if (dto == null) {
            throw new ResourceNotFoundException("Correction avec ID " + id + " non trouvée");
        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CorrectionDTO>> getAll() {
        List<CorrectionDTO> corrections = correctionService.getAll();
        return new ResponseEntity<>(corrections, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CorrectionDTO> create(@RequestBody CorrectionDTO dto) {
        CorrectionDTO created = correctionService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CorrectionDTO> update(@PathVariable Long id, @RequestBody CorrectionDTO dto) {
        CorrectionDTO updated = correctionService.update(id, dto);
        if (updated == null) {
            throw new ResourceNotFoundException("Correction avec ID " + id + " non trouvée");
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        correctionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
