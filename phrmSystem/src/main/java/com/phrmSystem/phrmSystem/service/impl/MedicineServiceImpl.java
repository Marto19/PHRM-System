package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Diagnosis;
import com.phrmSystem.phrmSystem.data.entity.Medicine;
import com.phrmSystem.phrmSystem.data.repo.DiagnosisRepository;
import com.phrmSystem.phrmSystem.data.repo.MedicineRepository;
import com.phrmSystem.phrmSystem.dto.MedicineDTO;
import com.phrmSystem.phrmSystem.service.MedicineService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final DiagnosisRepository diagnosisRepository;

    public MedicineServiceImpl(MedicineRepository medicineRepository, DiagnosisRepository diagnosisRepository) {
        this.medicineRepository = medicineRepository;
        this.diagnosisRepository = diagnosisRepository;
    }

    @Override
    public MedicineDTO getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));
        return mapToDTO(medicine);
    }

    @Override
    public List<MedicineDTO> getAllMedicines() {
        return medicineRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MedicineDTO createMedicine(MedicineDTO medicineDTO) {
        Diagnosis diagnosis = diagnosisRepository.findById(medicineDTO.getDiagnosisId())
                .orElseThrow(() -> new RuntimeException("Diagnosis not found with id: " + medicineDTO.getDiagnosisId()));

        Medicine medicine = new Medicine();
        medicine.setMedicineName(medicineDTO.getMedicineName());
        medicine.setMedicineDescription(medicineDTO.getMedicineDescription());
        medicine.setDiagnosis(diagnosis);

        return mapToDTO(medicineRepository.save(medicine));
    }

    @Override
    public MedicineDTO updateMedicine(Long id, MedicineDTO medicineDTO) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));

        Diagnosis diagnosis = diagnosisRepository.findById(medicineDTO.getDiagnosisId())
                .orElseThrow(() -> new RuntimeException("Diagnosis not found with id: " + medicineDTO.getDiagnosisId()));

        medicine.setMedicineName(medicineDTO.getMedicineName());
        medicine.setMedicineDescription(medicineDTO.getMedicineDescription());
        medicine.setDiagnosis(diagnosis);

        return mapToDTO(medicineRepository.save(medicine));
    }

    @Override
    public void deleteMedicine(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));
        medicineRepository.delete(medicine);
    }

    private MedicineDTO mapToDTO(Medicine medicine) {
        return new MedicineDTO(
                medicine.getId(),
                medicine.getMedicineName(),
                medicine.getMedicineDescription(),
                medicine.getDiagnosis().getId()
        );
    }
}
