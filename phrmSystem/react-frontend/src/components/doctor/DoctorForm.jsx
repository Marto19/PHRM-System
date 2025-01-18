import React, { useState, useEffect } from "react";
import { TextField, Button, Typography, Paper, Box } from "@mui/material";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";

const DoctorForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [error, setError] = useState(null);

    useEffect(() => {
        if (id) {
            axios
                .get(`/api/doctors/${id}`)
                .then((response) => {
                    const { firstName, lastName } = response.data;
                    setFirstName(firstName);
                    setLastName(lastName);
                })
                .catch((err) => {
                    console.error("Error fetching doctor:", err);
                    setError("Failed to load doctor details.");
                });
        }
    }, [id]);

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!firstName || !lastName) {
            alert("Both first name and last name are required.");
            return;
        }

        const data = { firstName, lastName };

        const request = id
            ? axios.put(`/api/doctors/${id}`, data)
            : axios.post("/api/doctors", data);

        request
            .then(() => {
                navigate("/doctors");
            })
            .catch((err) => {
                console.error("Error saving doctor:", err);
                setError("Failed to save doctor.");
            });
    };

    if (error) return <Typography color="error">{error}</Typography>;

    return (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="calc(100vh - 64px)">
            <Paper style={{ padding: "20px", maxWidth: "600px", width: "100%" }}>
                <Typography variant="h5" gutterBottom>
                    {id ? "Edit Doctor" : "Create Doctor"}
                </Typography>
                <form onSubmit={handleSubmit}>
                    <TextField
                        label="First Name"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                        fullWidth
                        margin="normal"
                    />
                    <TextField
                        label="Last Name"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
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
                        <Button variant="outlined" onClick={() => navigate("/doctors")}>
                            Cancel
                        </Button>
                    </div>
                </form>
            </Paper>
        </Box>
    );
};

export default DoctorForm;
