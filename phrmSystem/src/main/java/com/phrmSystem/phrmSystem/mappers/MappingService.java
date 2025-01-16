package com.phrmSystem.phrmSystem.mappers;

import com.phrmSystem.phrmSystem.data.entity.Diagnosis;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.dto.DiagnosisDTO;
import com.phrmSystem.phrmSystem.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public class MappingService {

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
    public DiagnosisDTO mapToDiagnosisDTO(Diagnosis diagnosis) {
        if (diagnosis == null) {
            return null;
        }
        DiagnosisDTO diagnosisDTO = new DiagnosisDTO();
        diagnosisDTO.setId(diagnosis.getId());
        diagnosisDTO.setDiagnosisName(diagnosis.getDiagnosisName());
        diagnosisDTO.setDiagnosisDescription(diagnosis.getDiagnosisDescription());
        return diagnosisDTO;
    }
}
