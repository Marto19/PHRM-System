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

const DoctorSpecializationList = () => {
    const [specializations, setSpecializations] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        axios
            .get("/api/doctor-specializations")
            .then((response) => {
                setSpecializations(response.data);
                setLoading(false);
            })
            .catch((error) => {
                console.error("Error fetching doctor specializations:", error);
                setError("Failed to fetch doctor specializations.");
                setLoading(false);
            });
    }, []);

    const handleDelete = (id) => {
        if (window.confirm("Are you sure you want to delete this specialization?")) {
            axios
                .delete(`/api/doctor-specializations/${id}`) // Correct endpoint
                .then(() => {
                    setSpecializations(
                        specializations.filter((specialization) => specialization.id !== id)
                    );
                })
                .catch((err) => {
                    console.error("Error deleting specialization:", err);
                    setError("Failed to delete specialization.");
                });
        }
    };

    if (loading) return <Typography>Loading specializations...</Typography>;
    if (error) return <Typography color="error">{error}</Typography>;

    return (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="calc(100vh - 64px)">
            <div style={{ padding: "20px", width: "90%", maxWidth: "1200px" }}>
                <Typography variant="h4" gutterBottom>
                    Doctor Specializations
                </Typography>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={() => navigate("/specializations/create")}
                    style={{ marginBottom: "20px" }}
                >
                    Create Specialization
                </Button>
                <TableContainer component={Paper}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell>Specialization</TableCell>
                                <TableCell>Associated Doctors</TableCell>
                                <TableCell>Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {specializations.map((specialization) => (
                                <TableRow key={specialization.id}>
                                    <TableCell>{specialization.id}</TableCell>
                                    <TableCell>{specialization.specialization}</TableCell>
                                    <TableCell>
                                        {specialization.doctorIds?.join(", ") || "None"}
                                    </TableCell>
                                    <TableCell>
                                        <Button
                                            variant="outlined"
                                            color="primary"
                                            onClick={() => navigate(`/specializations/${specialization.id}/edit`)}
                                        >
                                            Edit
                                        </Button>
                                        <Button
                                            variant="outlined"
                                            color="secondary"
                                            onClick={() => handleDelete(specialization.id)}
                                        >
                                            Delete
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

export default DoctorSpecializationList;
