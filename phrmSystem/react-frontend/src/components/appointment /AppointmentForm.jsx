import React, { useState, useEffect } from "react";
import {
    TextField,
    Button,
    Typography,
    Paper,
    Box,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
} from "@mui/material";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";

const AppointmentForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [date, setDate] = useState("");
    const [doctorId, setDoctorId] = useState("");
    const [patientId, setPatientId] = useState("");
    const [doctors, setDoctors] = useState([]);
    const [patients, setPatients] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        axios
            .get("/api/users")
            .then((response) => {
                const data = response.data;
                setDoctors(data.filter((user) => user.roles.some((role) => role.roleName === "DOCTOR")));
                setPatients(data.filter((user) => user.roles.some((role) => role.roleName === "PATIENT")));
            })
            .catch((err) => {
                console.error("Error fetching users:", err);
                setError("Failed to fetch users.");
            });

        if (id) {
            axios
                .get(`/api/appointments/${id}`)
                .then((response) => {
                    const { date, doctorId, patientId } = response.data;
                    setDate(new Date(date).toISOString().slice(0, 16));
                    setDoctorId(doctorId);
                    setPatientId(patientId);
                })
                .catch((err) => {
                    console.error("Error fetching appointment:", err);
                    setError("Failed to fetch appointment details.");
                });
        }
    }, [id]);

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!date || !doctorId || !patientId) {
            alert("All fields are required.");
            return;
        }

        const appointmentData = { date, doctorId, patientId };

        const request = id
            ? axios.put(`/api/appointments/${id}`, appointmentData)
            : axios.post("/api/appointments", appointmentData);

        request
            .then(() => {
                navigate("/appointments");
            })
            .catch((err) => {
                console.error("Error saving appointment:", err);
                setError("Failed to save appointment.");
            });
    };

    if (error) return <Typography color="error">{error}</Typography>;

    return (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="calc(100vh - 64px)">
            <Paper style={{ padding: "20px", maxWidth: "600px", width: "100%" }}>
                <Typography variant="h5" gutterBottom>
                    {id ? "Edit Appointment" : "Create Appointment"}
                </Typography>
                <form onSubmit={handleSubmit}>
                    <TextField
                        label="Date"
                        type="datetime-local"
                        value={date}
                        onChange={(e) => setDate(e.target.value)}
                        fullWidth
                        margin="normal"
                        InputLabelProps={{ shrink: true }}
                    />
                    <FormControl fullWidth margin="normal">
                        <InputLabel>Doctor</InputLabel>
                        <Select value={doctorId} onChange={(e) => setDoctorId(e.target.value)}>
                            {doctors.map((doctor) => (
                                <MenuItem key={doctor.id} value={doctor.id}>
                                    {doctor.firstName} {doctor.lastName}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <FormControl fullWidth margin="normal">
                        <InputLabel>Patient</InputLabel>
                        <Select value={patientId} onChange={(e) => setPatientId(e.target.value)}>
                            {patients.map((patient) => (
                                <MenuItem key={patient.id} value={patient.id}>
                                    {patient.firstName} {patient.lastName}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <div style={{ marginTop: "20px" }}>
                        <Button variant="contained" color="primary" type="submit" style={{ marginRight: "10px" }}>
                            {id ? "Update" : "Create"}
                        </Button>
                        <Button variant="outlined" onClick={() => navigate("/appointments")}>
                            Cancel
                        </Button>
                    </div>
                </form>
            </Paper>
        </Box>
    );
};

export default AppointmentForm;
