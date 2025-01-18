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

const PatientForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [uniqueIdentification, setUniqueIdentification] = useState("");
    const [insurancePaidLast6Months, setInsurancePaidLast6Months] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (id) {
            axios
                .get(`/api/patients/${id}`)
                .then((response) => {
                    const { firstName, lastName, uniqueIdentification, insurancePaidLast6Months } = response.data;
                    setFirstName(firstName || "");
                    setLastName(lastName || "");
                    setUniqueIdentification(uniqueIdentification || "");
                    setInsurancePaidLast6Months(insurancePaidLast6Months || false);
                })
                .catch((err) => {
                    console.error("Error fetching patient:", err);
                    setError("Failed to load patient details.");
                });
        }
    }, [id]);

    const handleSubmit = (e) => {
        e.preventDefault();

        const data = { firstName, lastName, uniqueIdentification, insurancePaidLast6Months };

        const request = id
            ? axios.put(`/api/patients/${id}`, data)
            : axios.post("/api/patients", data);

        request
            .then(() => {
                navigate("/patients");
            })
            .catch((err) => {
                console.error("Error saving patient:", err);
                setError("Failed to save patient.");
            });
    };

    if (error) return <Typography>{error}</Typography>;

    return (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="calc(100vh - 64px)">
            <Paper style={{ padding: "20px", maxWidth: "600px", width: "100%" }}>
                <Typography variant="h5" gutterBottom>
                    {id ? "Edit Patient" : "Create Patient"}
                </Typography>
                <form onSubmit={handleSubmit}>
                    <TextField
                        label="First Name"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                        fullWidth
                        margin="normal"
                        required
                    />
                    <TextField
                        label="Last Name"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                        fullWidth
                        margin="normal"
                        required
                    />
                    <TextField
                        label="Unique Identification"
                        value={uniqueIdentification}
                        onChange={(e) => setUniqueIdentification(e.target.value)}
                        fullWidth
                        margin="normal"
                    />
                    <div>
                        <label>
                            <input
                                type="checkbox"
                                checked={insurancePaidLast6Months}
                                onChange={(e) => setInsurancePaidLast6Months(e.target.checked)}
                            />
                            Insurance Paid Last 6 Months
                        </label>
                    </div>
                    <div style={{ marginTop: "20px" }}>
                        <Button
                            variant="contained"
                            color="primary"
                            type="submit"
                            style={{ marginRight: "10px" }}
                        >
                            {id ? "Update" : "Create"} Patient
                        </Button>
                        <Button variant="outlined" onClick={() => navigate("/patients")}>
                            Cancel
                        </Button>
                    </div>
                </form>
            </Paper>
        </Box>
    );
};

export default PatientForm;
