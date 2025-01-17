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
            </Routes>
        </Router>
    );
}

export default App;
