package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Diagnosis;
import com.phrmSystem.phrmSystem.data.entity.Medicine;
import com.phrmSystem.phrmSystem.data.repo.DiagnosisRepository;
import com.phrmSystem.phrmSystem.data.repo.MedicineRepository;
import com.phrmSystem.phrmSystem.dto.MedicineDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicineServiceImplTest {

    private MedicineRepository medicineRepository;
    private DiagnosisRepository diagnosisRepository;
    private MedicineServiceImpl medicineService;

    @BeforeEach
    void setUp() {
        medicineRepository = mock(MedicineRepository.class);
        diagnosisRepository = mock(DiagnosisRepository.class);
        medicineService = new MedicineServiceImpl(medicineRepository, diagnosisRepository);
    }

    @Test
    void getMedicineById_Success() {
        Medicine medicine = new Medicine();
        medicine.setId(1L);
        medicine.setMedicineName("Paracetamol");

        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));

        MedicineDTO result = medicineService.getMedicineById(1L);

        assertEquals("Paracetamol", result.getMedicineName());
    }

    @Test
    void getMedicineById_Failure_NotFound() {
        when(medicineRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> medicineService.getMedicineById(1L));
        assertEquals("Medicine not found with id: 1", exception.getMessage());
    }

    @Test
    void createMedicine_Success() {
        MedicineDTO medicineDTO = new MedicineDTO(null, "Paracetamol", "Pain reliever", 1L);
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setId(1L);

        when(diagnosisRepository.findById(1L)).thenReturn(Optional.of(diagnosis));

        Medicine medicine = new Medicine();
        medicine.setId(1L);
        medicine.setMedicineName("Paracetamol");
        medicine.setDiagnosis(diagnosis);

        when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);

        MedicineDTO result = medicineService.createMedicine(medicineDTO);

        assertEquals("Paracetamol", result.getMedicineName());
        verify(medicineRepository, times(1)).save(any(Medicine.class));
    }

    @Test
    void updateMedicine_Success() {
        Medicine existingMedicine = new Medicine();
        existingMedicine.setId(1L);
        existingMedicine.setMedicineName("Paracetamol");

        MedicineDTO updatedDTO = new MedicineDTO(1L, "Ibuprofen", "Anti-inflammatory", 1L);
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setId(1L);

        when(medicineRepository.findById(1L)).thenReturn(Optional.of(existingMedicine));
        when(diagnosisRepository.findById(1L)).thenReturn(Optional.of(diagnosis));

        Medicine updatedMedicine = new Medicine();
        updatedMedicine.setId(1L);
        updatedMedicine.setMedicineName("Ibuprofen");
        updatedMedicine.setDiagnosis(diagnosis);

        when(medicineRepository.save(existingMedicine)).thenReturn(updatedMedicine);

        MedicineDTO result = medicineService.updateMedicine(1L, updatedDTO);

        assertEquals("Ibuprofen", result.getMedicineName());
        verify(medicineRepository, times(1)).save(existingMedicine);
    }

    @Test
    void deleteMedicine_Success() {
        Medicine medicine = new Medicine();
        medicine.setId(1L);

        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));

        assertDoesNotThrow(() -> medicineService.deleteMedicine(1L));
        verify(medicineRepository, times(1)).delete(medicine);
    }

    @Test
    void deleteMedicine_Failure_NotFound() {
        when(medicineRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> medicineService.deleteMedicine(1L));
        assertEquals("Medicine not found with id: 1", exception.getMessage());
    }
}
