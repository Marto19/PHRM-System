import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import UserList from "./components/UserList";
import UserForm from "./components/UserForm";
import SickDayList from "./components/SickDayList";
import SickDayForm from "./components/SickDayForm";
import RoleList from "./components/role/RoleList";
import RoleForm from "./components/role/RoleForm";
import PatientIllnessHistoryList from "./components/patientIllnessHistory/PatientIllnessHistoryList.jsx";
import PatientIllnessHistoryForm from "./components/patientIllnessHistory/PatientIllnessHistoryForm.jsx";
import PatientList from "./components/patient/PatientList";
import PatientForm from "./components/patient/PatientForm";
import MedicineList from "./components/medicine/MedicineList.jsx";
import MedicineForm from "./components/medicine/MedicineForm.jsx";
import DoctorSpecializationForm from "./components/doctorSpecialization/DoctorSpecializationForm.jsx";
import DoctorSpecializationList from "./components/doctorSpecialization/DoctorSpecializationList.jsx";
import DoctorList from "./components/doctor/DoctorList.jsx";
import DoctorForm from "./components/doctor/DoctorForm.jsx";
import AppointmentList from "./components/appointment /AppointmentList"; // Adjust the path if needed
import AppointmentForm   from "./components/appointment /AppointmentForm.jsx"; // Adjust the path if needed


function App() {
    return (
        <Router>
            <Navbar />
            <Routes>
                <Route path="/users" element={<UserList />} />
                <Route path="/users/create" element={<UserForm />} />
                <Route path="/users/:id/edit" element={<UserForm />} />
                <Route path="/sick-days" element={<SickDayList />} />
                <Route path="/sick-days/create" element={<SickDayForm />} />
                <Route path="/sick-days/:id/edit" element={<SickDayForm />} />
                <Route path="/roles" element={<RoleList />} />
                <Route path="/roles/create" element={<RoleForm />} />
                <Route path="/roles/:id/edit" element={<RoleForm />} />
                <Route path="/illness-histories" element={<PatientIllnessHistoryList />} />
                <Route path="/illness-histories/create" element={<PatientIllnessHistoryForm />} />
                <Route path="/illness-histories/:id/edit" element={<PatientIllnessHistoryForm />} />
                <Route path="/patients" element={<PatientList />} />
                <Route path="/patients/create" element={<PatientForm />} />
                <Route path="/patients/:id/edit" element={<PatientForm />} />
                <Route path="/medicines" element={<MedicineList />} />
                <Route path="/medicines/create" element={<MedicineForm />} />
                <Route path="/medicines/:id/edit" element={<MedicineForm />} />
                <Route path="/specializations" element={<DoctorSpecializationList />} />
                <Route path="/specializations/create" element={<DoctorSpecializationForm />} />
                <Route path="/specializations/:id/edit" element={<DoctorSpecializationForm />} />
                <Route path="/doctors" element={<DoctorList />} />
                <Route path="/doctors/create" element={<DoctorForm />} />
                <Route path="/doctors/:id/edit" element={<DoctorForm />} />
                <Route path="/doctors/:id/specializations" element={<DoctorSpecializationList />} />
                <Route path="/doctors/:id/appointments" element={<AppointmentList />} />
                <Route path="/doctors/:id/specializations/edit" element={<DoctorSpecializationForm />} />
                <Route path="/appointments" element={<AppointmentList />} />
                <Route path="/appointments/create" element={<AppointmentForm />} />
                <Route path="/appointments/:id/edit" element={<AppointmentForm />} />
            </Routes>
        </Router>
    );
}

export default App;
