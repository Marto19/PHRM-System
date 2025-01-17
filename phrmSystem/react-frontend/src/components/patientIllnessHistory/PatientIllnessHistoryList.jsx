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

const PatientIllnessHistoryList = () => {
    const [histories, setHistories] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        axios
            .get("/api/illness-histories")
            .then((response) => {
                setHistories(response.data);
                setLoading(false);
            })
            .catch((err) => {
                console.error("Error fetching illness histories:", err);
                setError("Failed to fetch illness histories.");
                setLoading(false);
            });
    }, []);

    const handleDelete = (id) => {
        if (window.confirm("Are you sure you want to delete this illness history?")) {
            axios
                .delete(`/api/illness-histories/${id}`)
                .then(() => {
                    setHistories(histories.filter((history) => history.id !== id));
                })
                .catch((err) => {
                    console.error("Error deleting illness history:", err);
                    setError("Failed to delete illness history.");
                });
        }
    };

    if (loading) {
        return <Typography>Loading illness histories...</Typography>;
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
                    Patient Illness Histories
                </Typography>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={() => navigate("/illness-histories/create")}
                    style={{ marginBottom: "20px" }}
                >
                    Create Illness History
                </Button>
                <TableContainer component={Paper}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell>Illness Name</TableCell>
                                <TableCell>Start Date</TableCell>
                                <TableCell>End Date</TableCell>
                                <TableCell>Patient</TableCell>
                                <TableCell>Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {histories.length > 0 ? (
                                histories.map((history) => (
                                    <TableRow key={history.id}>
                                        <TableCell>{history.id}</TableCell>
                                        <TableCell>{history.illnessName || "N/A"}</TableCell>
                                        <TableCell>{history.startDate || "N/A"}</TableCell>
                                        <TableCell>{history.endDate || "N/A"}</TableCell>
                                        <TableCell>
                                            {history.patient?.firstName
                                                ? `${history.patient.firstName} ${history.patient.lastName}`
                                                : "No Patient Assigned"}
                                        </TableCell>
                                        <TableCell>
                                            <Button
                                                variant="outlined"
                                                color="primary"
                                                onClick={() => navigate(`/illness-histories/${history.id}/edit`)}
                                                style={{ marginRight: "10px" }}
                                            >
                                                Edit
                                            </Button>
                                            <Button
                                                variant="outlined"
                                                color="secondary"
                                                onClick={() => handleDelete(history.id)}
                                            >
                                                Delete
                                            </Button>
                                        </TableCell>
                                    </TableRow>
                                ))
                            ) : (
                                <TableRow>
                                    <TableCell colSpan={6} align="center">
                                        No illness histories found.
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

export default PatientIllnessHistoryList;
