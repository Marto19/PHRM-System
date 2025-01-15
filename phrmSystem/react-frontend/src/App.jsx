import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import UserList from "./components/UserList";
import UserForm from "./components/UserForm";

function App() {
    return (
        <Router>
            <Navbar />
            <Routes>
                <Route path="/users" element={<UserList />} />
                <Route path="/users/create" element={<UserForm />} />
                <Route path="/users/:id/edit" element={<UserForm />} />
            </Routes>
        </Router>
    );
}

export default App;
