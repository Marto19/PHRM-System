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
import { useParams, useNavigate } from "react-router-dom";

const DoctorSpecializationForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [specialization, setSpecialization] = useState("");
    const [doctorIds, setDoctorIds] = useState([]);
    const [allDoctors, setAllDoctors] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        axios
            .get("/api/users")
            .then((response) => {
                const doctors = response.data.filter((user) =>
                    user.roles.some((role) => role.roleName === "DOCTOR")
                );
                setAllDoctors(doctors);
            })
            .catch((err) => {
                console.error("Error fetching doctors:", err);
                setError("Failed to fetch doctors.");
            });

        if (id) {
            axios
                .get(`/api/doctor-specializations/${id}`) // Correct endpoint
                .then((response) => {
                    const { specialization, doctorIds } = response.data;
                    setSpecialization(specialization);
                    setDoctorIds(doctorIds || []);
                })
                .catch((err) => {
                    console.error("Error fetching specialization:", err);
                    setError("Failed to load specialization details.");
                });
        }
    }, [id]);

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!specialization) {
            alert("Specialization name is required.");
            return;
        }

        const data = { specialization, doctorIds };

        const request = id
            ? axios.put(`/api/doctor-specializations/${id}`, data)
            : axios.post("/api/doctor-specializations", data);

        request
            .then(() => {
                navigate("/specializations");
            })
            .catch((err) => {
                console.error("Error saving specialization:", err);
                setError("Failed to save specialization.");
            });
    };

    if (error) return <Typography color="error">{error}</Typography>;

    return (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="calc(100vh - 64px)">
            <Paper style={{ padding: "20px", maxWidth: "600px", width: "100%" }}>
                <Typography variant="h5" gutterBottom>
                    {id ? "Edit Specialization" : "Create Specialization"}
                </Typography>
                <form onSubmit={handleSubmit}>
                    <TextField
                        label="Specialization"
                        value={specialization}
                        onChange={(e) => setSpecialization(e.target.value)}
                        fullWidth
                        margin="normal"
                    />
                    <FormControl fullWidth margin="normal">
                        <InputLabel>Doctors</InputLabel>
                        <Select
                            multiple
                            value={doctorIds}
                            onChange={(e) => setDoctorIds(e.target.value)}
                        >
                            {allDoctors.map((doctor) => (
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
                            {id ? "Update" : "Create"}
                        </Button>
                        <Button variant="outlined" onClick={() => navigate("/specializations")}>
                            Cancel
                        </Button>
                    </div>
                </form>
            </Paper>
        </Box>
    );
};

export default DoctorSpecializationForm;
