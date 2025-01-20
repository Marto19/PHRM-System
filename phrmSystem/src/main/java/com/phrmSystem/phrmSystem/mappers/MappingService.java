package com.phrmSystem.phrmSystem.mappers;

import com.phrmSystem.phrmSystem.data.entity.Diagnosis;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.repo.DiagnosisRepository;
import com.phrmSystem.phrmSystem.dto.DiagnosisDTO;
import com.phrmSystem.phrmSystem.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MappingService {

    private final DiagnosisRepository diagnosisRepository;

    public MappingService(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }

    // Map User entity to UserDTO
    public UserDTO mapToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        return userDTO;
    }

    // Map Diagnosis entity to DiagnosisDTO
    private Set<Diagnosis> mapDiagnosisIdsToEntities(Set<Long> diagnosisIds) {
        if (diagnosisIds == null || diagnosisIds.isEmpty()) {
            return Set.of(); // Return an empty set if no IDs are provided
        }

        return diagnosisIds.stream()
                .map(this::findDiagnosisById)
                .filter(diagnosis -> diagnosis != null) // Skip any null diagnoses
                .collect(Collectors.toSet());
    }

    /**
     * Finds a Diagnosis by its ID. Logs a warning and returns null if not found.
     *
     * @param id the ID of the Diagnosis to find.
     * @return the Diagnosis entity or null if not found.
     */
    private Diagnosis findDiagnosisById(Long id) {
        return diagnosisRepository.findById(id)
                .orElseGet(() -> {
                    System.out.println("Diagnosis with ID " + id + " not found. Skipping this ID.");
                    return null;
                });
    }

}
