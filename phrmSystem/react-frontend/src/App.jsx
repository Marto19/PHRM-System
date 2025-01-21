import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/navigation/Navbar.jsx";
import UserList from "./components/user/UserList.jsx";
import UserForm from "./components/user/UserForm.jsx";
import SickDayList from "./components/sickDay/SickDayList.jsx";
import SickDayForm from "./components/sickDay/SickDayForm.jsx";
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
import AppointmentList from "./components/appointment /AppointmentList"; // Adjusted the path
import AppointmentForm from "./components/appointment /AppointmentForm.jsx"; // Adjusted the path
import Protected from "./components/Protected.jsx"; // Import the Protected component
import Unauthorized from "./components/navigation/Unauthorized"; // Add an Unauthorized component for handling unauthorized access

function App() {
    return (
        <Router>
            <Navbar />
            <Routes>
                {/* Public Route for Unauthorized Access */}
                <Route path="/unauthorized" element={<Unauthorized />} />

                {/* Protected Routes */}
                <Route
                    path="/users"
                    element={
                        <Protected allowedRoles={["client_admin"]}>
                            <UserList />
                        </Protected>
                    }
                />
                <Route
                    path="/users/create"
                    element={
                        <Protected allowedRoles={["client_admin"]}>
                            <UserForm />
                        </Protected>
                    }
                />
                <Route
                    path="/users/:id/edit"
                    element={
                        <Protected allowedRoles={["client_admin"]}>
                            <UserForm />
                        </Protected>
                    }
                />
                <Route
                    path="/sick-days"
                    element={
                        <Protected allowedRoles={["client_admin", "client_doctor"]}>
                            <SickDayList />
                        </Protected>
                    }
                />
                <Route
                    path="/sick-days/create"
                    element={
                        <Protected allowedRoles={["client_admin"]}>
                            <SickDayForm />
                        </Protected>
                    }
                />
                <Route
                    path="/sick-days/:id/edit"
                    element={
                        <Protected allowedRoles={["client_admin"]}>
                            <SickDayForm />
                        </Protected>
                    }
                />
                <Route
                    path="/roles"
                    element={
                        <Protected allowedRoles={["client_admin"]}>
                            <RoleList />
                        </Protected>
                    }
                />
                <Route
                    path="/roles/create"
                    element={
                        <Protected allowedRoles={["client_admin"]}>
                            <RoleForm />
                        </Protected>
                    }
                />
                <Route
                    path="/roles/:id/edit"
                    element={
                        <Protected allowedRoles={["client_admin"]}>
                            <RoleForm />
                        </Protected>
                    }
                />
                <Route
                    path="/illness-histories"
                    element={
                        <Protected allowedRoles={["client_admin", "client_doctor"]}>
                            <PatientIllnessHistoryList />
                        </Protected>
                    }
                />
                <Route
                    path="/illness-histories/create"
                    element={
                        <Protected allowedRoles={["client_admin", "client_doctor"]}>
                            <PatientIllnessHistoryForm />
                        </Protected>
                    }
                />
                <Route
                    path="/illness-histories/:id/edit"
                    element={
                        <Protected allowedRoles={["client_admin", "client_doctor"]}>
                            <PatientIllnessHistoryForm />
                        </Protected>
                    }
                />
                <Route
                    path="/patients"
                    element={
                        <Protected allowedRoles={["client_admin", "client_doctor"]}>
                            <PatientList />
                        </Protected>
                    }
                />
                <Route
                    path="/patients/create"
                    element={
                        <Protected allowedRoles={["client_admin"]}>
                            <PatientForm />
                        </Protected>
                    }
                />
                <Route
                    path="/patients/:id/edit"
                    element={
                        <Protected allowedRoles={["client_admin"]}>
                            <PatientForm />
                        </Protected>
                    }
                />
                <Route
                    path="/medicines"
                    element={
                        <Protected allowedRoles={["client_admin", "client_doctor"]}>
                            <MedicineList />
                        </Protected>
                    }
                />
                <Route
                    path="/medicines/create"
                    element={
                        <Protected allowedRoles={["client_admin"]}>
                            <MedicineForm />
                        </Protected>
                    }
                />
                <Route
                    path="/medicines/:id/edit"
                    element={
                        <Protected allowedRoles={["client_admin"]}>
                            <MedicineForm />
                        </Protected>
                    }
                />
                <Route
                    path="/specializations"
                    element={
                        <Protected allowedRoles={["client_admin", "client_doctor"]}>
                            <DoctorSpecializationList />
                        </Protected>
                    }
                />
                <Route
                    path="/specializations/create"
                    element={
                        <Protected allowedRoles={["client_admin"]}>
                            <DoctorSpecializationForm />
                        </Protected>
                    }
                />
                <Route
                    path="/specializations/:id/edit"
                    element={
                        <Protected allowedRoles={["client_admin"]}>
                            <DoctorSpecializationForm />
                        </Protected>
                    }
                />
                <Route
                    path="/doctors"
                    element={
                        <Protected allowedRoles={["client_admin", "client_doctor"]}>
                            <DoctorList />
                        </Protected>
                    }
                />
                <Route
                    path="/doctors/create"
                    element={
                        <Protected allowedRoles={["client_admin"]}>
                            <DoctorForm />
                        </Protected>
                    }
                />
                <Route
                    path="/doctors/:id/edit"
                    element={
                        <Protected allowedRoles={["client_admin"]}>
                            <DoctorForm />
                        </Protected>
                    }
                />
                <Route
                    path="/doctors/:id/appointments"
                    element={
                        <Protected allowedRoles={["client_admin", "client_doctor"]}>
                            <AppointmentList />
                        </Protected>
                    }
                />
                <Route
                    path="/appointments"
                    element={
                        <Protected allowedRoles={["client_admin", "client_doctor"]}>
                            <AppointmentList />
                        </Protected>
                    }
                />
                <Route
                    path="/appointments/create"
                    element={
                        <Protected allowedRoles={["client_admin", "client_doctor"]}>
                            <AppointmentForm />
                        </Protected>
                    }
                />
            </Routes>
        </Router>
    );
}

export default App;
