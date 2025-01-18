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

const AppointmentList = () => {
    const [appointments, setAppointments] = useState([]);
    const [doctors, setDoctors] = useState({});
    const [patients, setPatients] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchAppointments = axios.get("/api/appointments");
        const fetchDoctors = axios.get("/api/doctors");
        const fetchPatients = axios.get("/api/patients");

        Promise.all([fetchAppointments, fetchDoctors, fetchPatients])
            .then(([appointmentsResponse, doctorsResponse, patientsResponse]) => {
                setAppointments(appointmentsResponse.data);

                // Convert doctors and patients to lookup maps for quick access
                const doctorsMap = {};
                doctorsResponse.data.forEach((doctor) => {
                    doctorsMap[doctor.id] = `${doctor.firstName} ${doctor.lastName}`;
                });
                setDoctors(doctorsMap);

                const patientsMap = {};
                patientsResponse.data.forEach((patient) => {
                    patientsMap[patient.id] = `${patient.firstName} ${patient.lastName}`;
                });
                setPatients(patientsMap);

                setLoading(false);
            })
            .catch((err) => {
                console.error("Error fetching data:", err);
                setError("Failed to fetch appointments or related data.");
                setLoading(false);
            });
    }, []);

    const handleDelete = (id) => {
        if (window.confirm("Are you sure you want to delete this appointment?")) {
            axios
                .delete(`/api/appointments/${id}`)
                .then(() => {
                    setAppointments(appointments.filter((appointment) => appointment.id !== id));
                })
                .catch((err) => {
                    console.error("Error deleting appointment:", err);
                    setError("Failed to delete appointment.");
                });
        }
    };

    if (loading) return <Typography>Loading appointments...</Typography>;
    if (error) return <Typography color="error">{error}</Typography>;

    return (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="calc(100vh - 64px)">
            <div style={{ padding: "20px", width: "90%", maxWidth: "1200px" }}>
                <Typography variant="h4" gutterBottom>
                    Doctor Appointments
                </Typography>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={() => navigate("/appointments/create")}
                    style={{ marginBottom: "20px" }}
                >
                    Create Appointment
                </Button>
                <TableContainer component={Paper}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell>Doctor</TableCell>
                                <TableCell>Patient</TableCell>
                                <TableCell>Date</TableCell>
                                <TableCell>Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {appointments.map((appointment) => (
                                <TableRow key={appointment.id}>
                                    <TableCell>{appointment.id}</TableCell>
                                    <TableCell>{doctors[appointment.doctorId] || "Unknown Doctor"}</TableCell>
                                    <TableCell>{patients[appointment.patientId] || "Unknown Patient"}</TableCell>
                                    <TableCell>{new Date(appointment.date).toLocaleString()}</TableCell>
                                    <TableCell>
                                        <Button
                                            variant="outlined"
                                            color="primary"
                                            onClick={() => navigate(`/appointments/${appointment.id}/edit`)}
                                        >
                                            View
                                        </Button>
                                        <Button
                                            variant="outlined"
                                            color="secondary"
                                            onClick={() => handleDelete(appointment.id)}
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

export default AppointmentList;
