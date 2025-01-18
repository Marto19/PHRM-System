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

const SickDayForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [numberOfDays, setNumberOfDays] = useState(0);
    const [patientId, setPatientId] = useState("");
    const [doctorId, setDoctorId] = useState("");
    const [patients, setPatients] = useState([]);
    const [doctors, setDoctors] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        // Fetch patients and doctors for dropdowns
        axios
            .get("/api/users")
            .then((response) => {
                setPatients(response.data.filter((user) => user.roles.some((role) => role.roleName === "PATIENT")));
                setDoctors(response.data.filter((user) => user.roles.some((role) => role.roleName === "DOCTOR")));
            })
            .catch((err) => {
                console.error("Error fetching users:", err);
                setError("Failed to fetch user data.");
            });

        if (id) {
            axios
                .get(`/api/sick-days/${id}`)
                .then((response) => {
                    const { startDate, endDate, numberOfDays, patient, doctor } = response.data;
                    setStartDate(startDate || ""); // Default to empty string if undefined
                    setEndDate(endDate || ""); // Default to empty string if undefined
                    setNumberOfDays(numberOfDays || 0); // Default to 0 if undefined
                    setPatientId(patient?.id || ""); // Safely access patient.id
                    setDoctorId(doctor?.id || ""); // Safely access doctor.id
                })
                .catch((err) => {
                    console.error("Error fetching sick day:", err);
                    setError("Failed to load sick day details.");
                });
        }
    }, [id]);

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!startDate || !endDate || !numberOfDays || !patientId || !doctorId) {
            alert("All fields are required.");
            return;
        }

        const data = { startDate, endDate, numberOfDays, patientId, doctorId };

        const request = id
            ? axios.put(`/api/sick-days/${id}`, data)
            : axios.post("/api/sick-days", data);

        request
            .then(() => {
                navigate("/sick-days");
            })
            .catch((err) => {
                console.error("Error saving sick day:", err);
                setError("Failed to save sick day.");
            });
    };

    if (error) return <Typography color="error">{error}</Typography>;

    return (
        <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            minHeight="calc(100vh - 64px)"
        >
            <Paper style={{ padding: "20px", maxWidth: "600px", width: "100%" }}>
                <Typography variant="h5" gutterBottom>
                    {id ? "Edit Sick Day" : "Create Sick Day"}
                </Typography>
                <form onSubmit={handleSubmit}>
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
                    <TextField
                        label="Number of Days"
                        type="number"
                        value={numberOfDays}
                        onChange={(e) => setNumberOfDays(e.target.value)}
                        fullWidth
                        margin="normal"
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
                    <FormControl fullWidth margin="normal">
                        <InputLabel>Doctor</InputLabel>
                        <Select
                            value={doctorId}
                            onChange={(e) => setDoctorId(e.target.value)}
                        >
                            {doctors.map((doctor) => (
                                <MenuItem key={doctor.id} value={doctor.id}>
                                    {doctor.firstName} {doctor.lastName}
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
                            {id ? "Update" : "Create"} Sick Day
                        </Button>
                        <Button variant="outlined" onClick={() => navigate("/sick-days")}>
                            Cancel
                        </Button>
                    </div>
                </form>
            </Paper>
        </Box>
    );
};

export default SickDayForm;
