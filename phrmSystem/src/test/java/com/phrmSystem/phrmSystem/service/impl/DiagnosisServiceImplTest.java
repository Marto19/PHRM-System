package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.*;
import com.phrmSystem.phrmSystem.data.repo.*;
import com.phrmSystem.phrmSystem.dto.DiagnosisDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiagnosisServiceImplTest {

    private DiagnosisRepository diagnosisRepository;
    private SickDayRepository sickDayRepository;
    private DoctorAppointmentRepository doctorAppointmentRepository;
    private MedicineRepository medicineRepository;
    private DiagnosisServiceImpl diagnosisService;

    @BeforeEach
    void setUp() {
        diagnosisRepository = mock(DiagnosisRepository.class);
        sickDayRepository = mock(SickDayRepository.class);
        doctorAppointmentRepository = mock(DoctorAppointmentRepository.class);
        medicineRepository = mock(MedicineRepository.class);
        diagnosisService = new DiagnosisServiceImpl(diagnosisRepository, sickDayRepository, doctorAppointmentRepository, medicineRepository);
    }

    @Test
    void createDiagnosis_Success() {
        DiagnosisDTO diagnosisDTO = new DiagnosisDTO();
        diagnosisDTO.setDiagnosisName("Flu");
        diagnosisDTO.setDiagnosisDescription("Common flu diagnosis");
        diagnosisDTO.setSickDayIds(Set.of(1L));
        diagnosisDTO.setDoctorAppointmentId(2L);
        diagnosisDTO.setMedicineIds(Set.of(3L));

        SickDay sickDay = new SickDay();
        sickDay.setId(1L);

        DoctorAppointment doctorAppointment = new DoctorAppointment();
        doctorAppointment.setId(2L);

        Medicine medicine = new Medicine();
        medicine.setId(3L);

        when(sickDayRepository.findById(1L)).thenReturn(Optional.of(sickDay));
        when(doctorAppointmentRepository.findById(2L)).thenReturn(Optional.of(doctorAppointment));
        when(medicineRepository.findById(3L)).thenReturn(Optional.of(medicine));

        Diagnosis savedDiagnosis = new Diagnosis();
        savedDiagnosis.setId(1L);
        savedDiagnosis.setDiagnosisName("Flu");
        when(diagnosisRepository.save(any(Diagnosis.class))).thenReturn(savedDiagnosis);

        DiagnosisDTO result = diagnosisService.createDiagnosis(diagnosisDTO);

        assertNotNull(result);
        assertEquals("Flu", result.getDiagnosisName());
        verify(diagnosisRepository, times(1)).save(any(Diagnosis.class));
    }

    @Test
    void createDiagnosis_Failure_SickDayNotFound() {
        DiagnosisDTO diagnosisDTO = new DiagnosisDTO();
        diagnosisDTO.setDiagnosisName("Flu");
        diagnosisDTO.setSickDayIds(Set.of(1L));

        when(sickDayRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> diagnosisService.createDiagnosis(diagnosisDTO));
        assertEquals("Sick Day not found with ID: 1", exception.getMessage());
    }

    @Test
    void updateDiagnosis_Success() {
        Diagnosis existingDiagnosis = new Diagnosis();
        existingDiagnosis.setId(1L);
        existingDiagnosis.setDiagnosisName("Cold");

        DiagnosisDTO updatedDTO = new DiagnosisDTO();
        updatedDTO.setDiagnosisName("Flu");
        updatedDTO.setSickDayIds(Set.of(1L));

        SickDay sickDay = new SickDay();
        sickDay.setId(1L);

        when(diagnosisRepository.findById(1L)).thenReturn(Optional.of(existingDiagnosis));
        when(sickDayRepository.findById(1L)).thenReturn(Optional.of(sickDay));
        when(diagnosisRepository.save(existingDiagnosis)).thenReturn(existingDiagnosis);

        DiagnosisDTO result = diagnosisService.updateDiagnosis(1L, updatedDTO);

        assertEquals("Flu", result.getDiagnosisName());
        verify(diagnosisRepository, times(1)).save(existingDiagnosis);
    }

    @Test
    void updateDiagnosis_Failure_NotFound() {
        DiagnosisDTO updatedDTO = new DiagnosisDTO();
        updatedDTO.setDiagnosisName("Flu");

        when(diagnosisRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> diagnosisService.updateDiagnosis(1L, updatedDTO));
        assertEquals("Diagnosis not found with ID: 1", exception.getMessage());
    }

    @Test
    void deleteDiagnosis_Success() {
        when(diagnosisRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> diagnosisService.deleteDiagnosis(1L));
        verify(diagnosisRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteDiagnosis_Failure_NotFound() {
        when(diagnosisRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> diagnosisService.deleteDiagnosis(1L));
        assertEquals("Diagnosis not found with ID: 1", exception.getMessage());
    }

    @Test
    void getAllDiagnoses_Success() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setId(1L);
        diagnosis.setDiagnosisName("Flu");

        when(diagnosisRepository.findAll()).thenReturn(List.of(diagnosis));

        List<DiagnosisDTO> result = diagnosisService.getAllDiagnoses();

        assertEquals(1, result.size());
        assertEquals("Flu", result.get(0).getDiagnosisName());
    }

    @Test
    void getDiagnosisById_Success() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setId(1L);
        diagnosis.setDiagnosisName("Flu");

        when(diagnosisRepository.findById(1L)).thenReturn(Optional.of(diagnosis));

        DiagnosisDTO result = diagnosisService.getDiagnosisById(1L);

        assertEquals("Flu", result.getDiagnosisName());
    }

    @Test   //todo: same thing, 400 fix to be gracefully
    void getDiagnosisById_Failure_NotFound() {
        when(diagnosisRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> diagnosisService.getDiagnosisById(1L));
        assertEquals("Diagnosis not found with ID: 1", exception.getMessage());
    }
}
