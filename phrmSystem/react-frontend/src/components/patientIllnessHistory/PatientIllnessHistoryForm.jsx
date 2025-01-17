import React, { useState, useEffect } from "react";
import {
    TextField,
    Button,
    Typography,
    Paper,
    Box,
    MenuItem,
    Select,
    FormControl,
    InputLabel,
} from "@mui/material";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";

const PatientIllnessHistoryForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [illnessName, setIllnessName] = useState("");
    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [patientId, setPatientId] = useState("");
    const [patients, setPatients] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        // Fetch patients for dropdown
        axios
            .get("/api/users")
            .then((response) => {
                setPatients(response.data.filter((user) => user.roles.some((role) => role.roleName === "PATIENT")));
            })
            .catch((err) => {
                console.error("Error fetching patients:", err);
                setError("Failed to load patients.");
            });

        if (id) {
            // Fetch illness history by ID
            axios
                .get(`/api/illness-histories/${id}`)
                .then((response) => {
                    const { illnessName, startDate, endDate, patient } = response.data;
                    setIllnessName(illnessName || "");
                    setStartDate(startDate || "");
                    setEndDate(endDate || "");
                    setPatientId(patient?.id || ""); // Optional chaining to prevent undefined error
                })
                .catch((err) => {
                    console.error("Error fetching illness history:", err);
                    setError("Failed to load illness history details.");
                });
        }
    }, [id]);

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!illnessName || !startDate || !endDate || !patientId) {
            alert("All fields are required.");
            return;
        }

        const data = { illnessName, startDate, endDate, patientId };

        const request = id
            ? axios.put(`/api/illness-histories/${id}`, data)
            : axios.post("/api/illness-histories", data);

        request
            .then(() => {
                navigate("/illness-histories");
            })
            .catch((err) => {
                console.error("Error saving illness history:", err);
                setError("Failed to save illness history.");
            });
    };

    if (error) return <Typography>{error}</Typography>;

    return (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="calc(100vh - 64px)">
            <Paper style={{ padding: "20px", maxWidth: "600px", width: "100%" }}>
                <Typography variant="h5" gutterBottom>
                    {id ? "Edit Illness History" : "Create Illness History"}
                </Typography>
                <form onSubmit={handleSubmit}>
                    <TextField
                        label="Illness Name"
                        value={illnessName}
                        onChange={(e) => setIllnessName(e.target.value)}
                        fullWidth
                        margin="normal"
                    />
                    <TextField
                        label="Start Date"
                        type="date"
                        value={startDate}
                        onChange={(e) => setStartDate(e.target.value)}
                        fullWidth
                        margin="normal"
                        InputLabelProps={{ shrink: true }}
                    />
                    <TextField
                        label="End Date"
                        type="date"
                        value={endDate}
                        onChange={(e) => setEndDate(e.target.value)}
                        fullWidth
                        margin="normal"
                        InputLabelProps={{ shrink: true }}
                    />
                    <FormControl fullWidth margin="normal">
                        <InputLabel>Patient</InputLabel>
                        <Select
                            value={patientId}
                            onChange={(e) => setPatientId(e.target.value)}
                        >
                            {patients.map((patient) => (
                                <MenuItem key={patient.id} value={patient.id}>
                                    {patient.firstName} {patient.lastName}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <div style={{ marginTop: "20px" }}>
                        <Button
                            variant="contained"
                            color="primary"
                            type="submit"
                            style={{ marginRight: "10px" }}
                        >
                            {id ? "Update" : "Create"} Illness History
                        </Button>
                        <Button variant="outlined" onClick={() => navigate("/illness-histories")}>
                            Cancel
                        </Button>
                    </div>
                </form>
            </Paper>
        </Box>
    );
};

export default PatientIllnessHistoryForm;
