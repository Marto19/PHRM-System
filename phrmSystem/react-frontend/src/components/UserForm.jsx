import React, { useState, useEffect } from "react";
import { TextField, Button, Typography, Paper, Box } from "@mui/material";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";

const UserForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [error, setError] = useState(null);

    useEffect(() => {
        if (id) {
            axios
                .get(`/api/users/${id}`)
                .then((response) => {
                    setFirstName(response.data.firstName);
                    setLastName(response.data.lastName);
                })
                .catch((err) => {
                    console.error("Error fetching user:", err);
                    setError("Failed to load user details.");
                });
        }
    }, [id]);

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!firstName || !lastName) {
            alert("First Name and Last Name are required.");
            return;
        }

        const request = id
            ? axios.put(`/api/users/${id}`, { firstName, lastName })
            : axios.post("/api/users", { firstName, lastName });

        request
            .then(() => {
                navigate("/users");
            })
            .catch((err) => {
                console.error("Error saving user:", err);
                setError("Failed to save user.");
            });
    };

    if (error) {
        return <Typography>{error}</Typography>;
    }

    return (
        <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            minHeight="calc(100vh - 64px)" // Adjust for navbar height
        >
            <Paper style={{ padding: "20px", maxWidth: "600px", width: "100%" }}>
                <Typography variant="h5" gutterBottom>
                    {id ? "Edit User" : "Create User"}
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
                            {id ? "Update" : "Create"} User
                        </Button>
                        <Button variant="outlined" onClick={() => navigate("/users")}>
                            Cancel
                        </Button>
                    </div>
                </form>
            </Paper>
        </Box>
    );
};

export default UserForm;
