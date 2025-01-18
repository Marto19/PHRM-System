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

const DoctorList = () => {
    const [doctors, setDoctors] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        axios
            .get("/api/doctors")
            .then((response) => {
                setDoctors(response.data);
                setLoading(false);
            })
            .catch((err) => {
                console.error("Error fetching doctors:", err);
                setError("Failed to fetch doctors.");
                setLoading(false);
            });
    }, []);

    const handleDelete = (id) => {
        if (window.confirm("Are you sure you want to delete this doctor?")) {
            axios
                .delete(`/api/doctors/${id}`)
                .then(() => {
                    setDoctors(doctors.filter((doctor) => doctor.id !== id));
                })
                .catch((err) => {
                    console.error("Error deleting doctor:", err);
                    setError("Failed to delete doctor.");
                });
        }
    };

    if (loading) return <Typography>Loading doctors...</Typography>;
    if (error) return <Typography color="error">{error}</Typography>;

    return (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="calc(100vh - 64px)">
            <div style={{ padding: "20px", width: "90%", maxWidth: "1200px" }}>
                <Typography variant="h4" gutterBottom>
                    Doctors
                </Typography>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={() => navigate("/doctors/create")}
                    style={{ marginBottom: "20px" }}
                >
                    Create Doctor
                </Button>
                <TableContainer component={Paper}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell>First Name</TableCell>
                                <TableCell>Last Name</TableCell>
                                <TableCell>Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {doctors.map((doctor) => (
                                <TableRow key={doctor.id}>
                                    <TableCell>{doctor.id}</TableCell>
                                    <TableCell>{doctor.firstName}</TableCell>
                                    <TableCell>{doctor.lastName}</TableCell>
                                    <TableCell>
                                        <Button
                                            variant="outlined"
                                            color="primary"
                                            onClick={() => navigate(`/doctors/${doctor.id}/edit`)}
                                        >
                                            Edit
                                        </Button>
                                        <Button
                                            variant="outlined"
                                            color="secondary"
                                            onClick={() => handleDelete(doctor.id)}
                                        >
                                            Delete
                                        </Button>
                                        <Button
                                            variant="outlined"
                                            onClick={() => navigate(`/doctors/${doctor.id}/specializations`)}
                                        >
                                            Specializations
                                        </Button>
                                        <Button
                                            variant="outlined"
                                            onClick={() => navigate(`/doctors/${doctor.id}/appointments`)}
                                        >
                                            Appointments
                                        </Button>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </div>
        </Box>
    );
};

export default DoctorList;
