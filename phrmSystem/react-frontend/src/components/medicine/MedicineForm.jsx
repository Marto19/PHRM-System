import React, { useState, useEffect } from "react";
import {
    TextField,
    Button,
    Typography,
    Paper,
    Box,
} from "@mui/material";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";

const MedicineForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [medicineName, setMedicineName] = useState("");
    const [medicineDescription, setMedicineDescription] = useState("");
    const [diagnosisId, setDiagnosisId] = useState("");
    const [error, setError] = useState(null);

    useEffect(() => {
        if (id) {
            axios
                .get(`/api/medicines/${id}`)
                .then((response) => {
                    const { medicineName, medicineDescription, diagnosisId } = response.data;
                    setMedicineName(medicineName);
                    setMedicineDescription(medicineDescription);
                    setDiagnosisId(diagnosisId);
                })
                .catch((err) => {
                    console.error("Error fetching medicine details:", err);
                    setError("Failed to load medicine details.");
                });
        }
    }, [id]);

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!medicineName || !diagnosisId) {
            alert("Name and Diagnosis ID are required.");
            return;
        }

        const data = { medicineName, medicineDescription, diagnosisId };

        const request = id
            ? axios.put(`/api/medicines/${id}`, data)
            : axios.post("/api/medicines", data);

        request
            .then(() => {
                navigate("/medicines");
            })
            .catch((err) => {
                console.error("Error saving medicine:", err);
                setError("Failed to save medicine.");
            });
    };

    if (error) return <Typography color="error">{error}</Typography>;

    return (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="calc(100vh - 64px)">
            <Paper style={{ padding: "20px", maxWidth: "600px", width: "100%" }}>
                <Typography variant="h5" gutterBottom>
                    {id ? "Edit Medicine" : "Create Medicine"}
                </Typography>
                <form onSubmit={handleSubmit}>
                    <TextField
                        label="Medicine Name"
                        value={medicineName}
                        onChange={(e) => setMedicineName(e.target.value)}
                        fullWidth
                        margin="normal"
                    />
                    <TextField
                        label="Medicine Description"
                        value={medicineDescription}
                        onChange={(e) => setMedicineDescription(e.target.value)}
                        fullWidth
                        margin="normal"
                    />
                    <TextField
                        label="Diagnosis ID"
                        value={diagnosisId}
                        onChange={(e) => setDiagnosisId(e.target.value)}
                        fullWidth
                        margin="normal"
                    />
                    <div style={{ marginTop: "20px" }}>
                        <Button
                            variant="contained"
                            color="primary"
                            type="submit"
                            style={{ marginRight: "10px" }}
                        >
                            {id ? "Update" : "Create"}
                        </Button>
                        <Button variant="outlined" onClick={() => navigate("/medicines")}>
                            Cancel
                        </Button>
                    </div>
                </form>
            </Paper>
        </Box>
    );
};

export default MedicineForm;
