package com.phrmSystem.phrmSystem.service;

import com.phrmSystem.phrmSystem.dto.MedicineDTO;

import java.util.List;

public interface MedicineService {
    MedicineDTO getMedicineById(Long id);
    List<MedicineDTO> getAllMedicines();
    MedicineDTO createMedicine(MedicineDTO medicineDTO);
    MedicineDTO updateMedicine(Long id, MedicineDTO medicineDTO);
    void deleteMedicine(Long id);
}
