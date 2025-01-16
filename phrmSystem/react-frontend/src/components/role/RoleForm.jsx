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

const RoleForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [roleName, setRoleName] = useState("");
    const [description, setDescription] = useState("");
    const [error, setError] = useState(null);

    useEffect(() => {
        if (id) {
            axios
                .get(`/api/roles/${id}`)
                .then((response) => {
                    const { roleName, description } = response.data || {};
                    setRoleName(roleName || ""); // Fallback to empty string if undefined
                    setDescription(description || "");
                })
                .catch((err) => {
                    console.error("Error fetching role:", err);
                    setError("Failed to load role details.");
                });
        }
    }, [id]);



    const handleSubmit = (e) => {
        e.preventDefault();

        if (!roleName) {
            alert("Role name is required.");
            return;
        }

        const data = { roleName, description };

        const request = id
            ? axios.put(`/api/roles/${id}`, data)
            : axios.post("/api/roles", data);

        request
            .then(() => {
                navigate("/roles");
            })
            .catch((err) => {
                console.error("Error saving role:", err);
                setError("Failed to save role.");
            });
    };

    if (error) return <Typography>{error}</Typography>;

    return (
        <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            minHeight="calc(100vh - 64px)"
        >
            <Paper style={{ padding: "20px", maxWidth: "600px", width: "100%" }}>
                <Typography variant="h5" gutterBottom>
                    {id ? "Edit Role" : "Create Role"}
                </Typography>
                <form onSubmit={handleSubmit}>
                    <TextField
                        label="Role Name"
                        value={roleName}
                        onChange={(e) => setRoleName(e.target.value)}
                        fullWidth
                        margin="normal"
                    />
                    <TextField
                        label="Description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
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
                            {id ? "Update" : "Create"} Role
                        </Button>
                        <Button variant="outlined" onClick={() => navigate("/roles")}>
                            Cancel
                        </Button>
                    </div>
                </form>
            </Paper>
        </Box>
    );
};

export default RoleForm;
