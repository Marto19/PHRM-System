package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Diagnosis;
import com.phrmSystem.phrmSystem.data.entity.Medicine;
import com.phrmSystem.phrmSystem.data.repo.DiagnosisRepository;
import com.phrmSystem.phrmSystem.data.repo.MedicineRepository;
import com.phrmSystem.phrmSystem.dto.MedicineDTO;
import com.phrmSystem.phrmSystem.service.MedicineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of MedicineService for managing medicines and their relationships with diagnoses.
 */
@Service
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final DiagnosisRepository diagnosisRepository;

    public MedicineServiceImpl(MedicineRepository medicineRepository, DiagnosisRepository diagnosisRepository) {
        this.medicineRepository = medicineRepository;
        this.diagnosisRepository = diagnosisRepository;
    }

    /**
     * Retrieves a medicine by its ID.
     *
     * @param id the ID of the medicine.
     * @return the MedicineDTO representation of the medicine.
     */
    @Override
    public MedicineDTO getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));
        return mapToDTO(medicine);
    }

    /**
     * Retrieves all medicines in the system.
     *
     * @return a list of MedicineDTOs.
     */
    @Override
    public List<MedicineDTO> getAllMedicines() {
        return medicineRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new medicine and associates it with a diagnosis.
     *
     * @param medicineDTO the DTO containing medicine details.
     * @return the created MedicineDTO.
     */
    @Override
    @Transactional
    public MedicineDTO createMedicine(MedicineDTO medicineDTO) {
        validateMedicineDTO(medicineDTO);

        Diagnosis diagnosis = diagnosisRepository.findById(medicineDTO.getDiagnosisId())
                .orElseThrow(() -> new RuntimeException("Diagnosis not found with id: " + medicineDTO.getDiagnosisId()));

        Medicine medicine = new Medicine();
        medicine.setMedicineName(medicineDTO.getMedicineName());
        medicine.setMedicineDescription(medicineDTO.getMedicineDescription());
        medicine.setDiagnosis(diagnosis);

        return mapToDTO(medicineRepository.save(medicine));
    }

    /**
     * Updates an existing medicine by ID.
     *
     * @param id          the ID of the medicine to update.
     * @param medicineDTO the DTO containing updated medicine details.
     * @return the updated MedicineDTO.
     */
    @Override
    @Transactional
    public MedicineDTO updateMedicine(Long id, MedicineDTO medicineDTO) {
        validateMedicineDTO(medicineDTO);

        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));

        Diagnosis diagnosis = diagnosisRepository.findById(medicineDTO.getDiagnosisId())
                .orElseThrow(() -> new RuntimeException("Diagnosis not found with id: " + medicineDTO.getDiagnosisId()));

        medicine.setMedicineName(medicineDTO.getMedicineName());
        medicine.setMedicineDescription(medicineDTO.getMedicineDescription());
        medicine.setDiagnosis(diagnosis);

        return mapToDTO(medicineRepository.save(medicine));
    }

    /**
     * Deletes a medicine by ID.
     *
     * @param id the ID of the medicine to delete.
     */
    @Override
    @Transactional
    public void deleteMedicine(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));

        try {
            medicineRepository.delete(medicine);
        } catch (Exception ex) {
            if (ex.getMessage().contains("constraint")) {
                throw new RuntimeException("Cannot delete medicine due to active dependencies.");
            }
            throw ex;
        }
    }

    /**
     * Validates a MedicineDTO for required fields.
     *
     * @param medicineDTO the DTO to validate.
     */
    private void validateMedicineDTO(MedicineDTO medicineDTO) {
        if (medicineDTO.getMedicineName() == null || medicineDTO.getMedicineName().isBlank()) {
            throw new RuntimeException("Medicine name cannot be null or blank.");
        }
        if (medicineDTO.getDiagnosisId() == null) {
            throw new RuntimeException("Diagnosis ID must be provided.");
        }
    }

    /**
     * Maps a Medicine entity to its DTO representation.
     *
     * @param medicine the Medicine entity.
     * @return the corresponding MedicineDTO.
     */
    private MedicineDTO mapToDTO(Medicine medicine) {
        return new MedicineDTO(
                medicine.getId(),
                medicine.getMedicineName(),
                medicine.getMedicineDescription(),
                medicine.getDiagnosis() != null ? medicine.getDiagnosis().getId() : null
        );
    }
}
