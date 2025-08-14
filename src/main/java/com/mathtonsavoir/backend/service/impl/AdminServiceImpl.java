package com.mathtonsavoir.backend.service.impl;

import com.mathtonsavoir.backend.dto.UtilisateurDTO;
import com.mathtonsavoir.backend.mapper.UtilisateurMapper;
import com.mathtonsavoir.backend.model.Admin;
import com.mathtonsavoir.backend.repository.AdminRepository;
import com.mathtonsavoir.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public UtilisateurDTO getById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin introuvable"));
        return UtilisateurMapper.toDto(admin);
    }

    @Override
    public List<UtilisateurDTO> getAll() {
        return adminRepository.findAll()
                .stream()
                .map(UtilisateurMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UtilisateurDTO create(UtilisateurDTO dto) {
        Admin admin = new Admin();
        UtilisateurMapper.updateEntityFromDto(admin, dto);
        Admin saved = adminRepository.save(admin);
        return UtilisateurMapper.toDto(saved);
    }

    @Override
    public UtilisateurDTO update(Long id, UtilisateurDTO dto) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin introuvable"));
        UtilisateurMapper.updateEntityFromDto(admin, dto);
        Admin updated = adminRepository.save(admin);
        return UtilisateurMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        adminRepository.deleteById(id);
    }
}
