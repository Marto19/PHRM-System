import React, { useEffect, useState } from "react";
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

const MedicineList = () => {
    const [medicines, setMedicines] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        axios
            .get("/api/medicines")
            .then((response) => {
                setMedicines(response.data);
                setLoading(false);
            })
            .catch((error) => {
                console.error("Error fetching medicines:", error);
                setError("Failed to fetch medicines.");
                setLoading(false);
            });
    }, []);

    const handleDelete = (id) => {
        if (window.confirm("Are you sure you want to delete this medicine?")) {
            axios
                .delete(`/api/medicines/${id}`)
                .then(() => {
                    setMedicines(medicines.filter((medicine) => medicine.id !== id));
                })
                .catch((err) => {
                    console.error("Error deleting medicine:", err);
                    setError("Failed to delete medicine.");
                });
        }
    };

    if (loading) return <Typography>Loading medicines...</Typography>;
    if (error) return <Typography color="error">{error}</Typography>;

    return (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="calc(100vh - 64px)">
            <div style={{ padding: "20px", width: "90%", maxWidth: "1200px" }}>
                <Typography variant="h4" gutterBottom>
                    Medicines
                </Typography>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={() => navigate("/medicines/create")}
                    style={{ marginBottom: "20px" }}
                >
                    Create Medicine
                </Button>
                <TableContainer component={Paper}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell>Name</TableCell>
                                <TableCell>Description</TableCell>
                                <TableCell>Diagnosis ID</TableCell>
                                <TableCell>Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {medicines.map((medicine) => (
                                <TableRow key={medicine.id}>
                                    <TableCell>{medicine.id}</TableCell>
                                    <TableCell>{medicine.medicineName}</TableCell>
                                    <TableCell>{medicine.medicineDescription}</TableCell>
                                    <TableCell>{medicine.diagnosisId}</TableCell>
                                    <TableCell>
                                        <Button
                                            variant="outlined"
                                            color="primary"
                                            onClick={() => navigate(`/medicines/${medicine.id}/edit`)}
                                        >
                                            Edit
                                        </Button>
                                        <Button
                                            variant="outlined"
                                            color="secondary"
                                            onClick={() => handleDelete(medicine.id)}
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

export default MedicineList;
