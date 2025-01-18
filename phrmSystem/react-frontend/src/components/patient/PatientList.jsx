import React, { useState, useEffect } from "react";
import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Button,
    Typography,
    Box,
} from "@mui/material";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const PatientList = () => {
    const [patients, setPatients] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        axios
            .get("/api/patients")
            .then((response) => {
                console.log("Fetched Patients:", response.data); // Debugging log
                setPatients(response.data);
                setLoading(false);
            })
            .catch((err) => {
                console.error("Error fetching patients:", err);
                setError("Failed to fetch patients.");
                setLoading(false);
            });
    }, []);


    const handleDelete = (id) => {
        if (window.confirm("Are you sure you want to delete this patient?")) {
            axios
                .delete(`/api/patients/${id}`)
                .then(() => {
                    setPatients(patients.filter((patient) => patient.id !== id));
                })
                .catch((err) => {
                    console.error("Error deleting patient:", err);
                    setError("Failed to delete patient.");
                });
        }
    };

    if (loading) {
        return <Typography>Loading patients...</Typography>;
    }

    if (error) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" minHeight="calc(100vh - 64px)">
                <Typography variant="h6" color="error">
                    {error}
                </Typography>
            </Box>
        );
    }

    return (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="calc(100vh - 64px)">
            <div style={{ padding: "20px", width: "90%", maxWidth: "1200px" }}>
                <Typography variant="h4" gutterBottom>
                    Patients
                </Typography>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={() => navigate("/patients/create")}
                    style={{ marginBottom: "20px" }}
                >
                    Create Patient
                </Button>
                <TableContainer component={Paper}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell>First Name</TableCell>
                                <TableCell>Last Name</TableCell>
                                <TableCell>Unique Identification</TableCell>
                                <TableCell>Insurance Paid</TableCell>
                                <TableCell>Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {patients.length > 0 ? (
                                patients.map((patient) => (
                                    <TableRow key={patient.id}>
                                        <TableCell>{patient.id}</TableCell>
                                        <TableCell>{patient.firstName}</TableCell>
                                        <TableCell>{patient.lastName}</TableCell>
                                        <TableCell>{patient.uniqueId || "N/A"}</TableCell>
                                        <TableCell>{patient.insurancePaidLast6Months ? "Yes" : "No"}</TableCell>
                                        <TableCell>
                                            <Button
                                                variant="outlined"
                                                color="primary"
                                                onClick={() => navigate(`/patients/${patient.id}/edit`)}
                                            >
                                                Edit
                                            </Button>
                                            <Button
                                                variant="outlined"
                                                color="secondary"
                                                onClick={() => handleDelete(patient.id)}
                                            >
                                                Delete
                                            </Button>
                                        </TableCell>
                                    </TableRow>
                                ))
                            ) : (
                                <TableRow>
                                    <TableCell colSpan={6} align="center">
                                        No patients found.
                                    </TableCell>
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>
            </div>
        </Box>
    );
};

export default PatientList;
