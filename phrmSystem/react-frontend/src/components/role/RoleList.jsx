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

const RoleList = () => {
    const [roles, setRoles] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        axios
            .get("/api/roles")
            .then((response) => {
                console.log("API Response:", response.data); // Debugging API response
                setRoles(response.data); // Set roles to state
            })
            .catch((err) => {
                console.error("Error fetching roles:", err);
                setError("Failed to fetch roles.");
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    const handleDelete = (id) => {
        if (window.confirm("Are you sure you want to delete this role?")) {
            axios
                .delete(`/api/roles/${id}`)
                .then(() => {
                    setRoles(roles.filter((role) => role.id !== id));
                })
                .catch((err) => {
                    console.error("Error deleting role:", err);
                    setError("Failed to delete role.");
                });
        }
    };

    if (loading) {
        return <Typography>Loading roles...</Typography>;
    }

    if (error) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" minHeight="calc(100vh - 64px)">
                <Typography variant="h6" color="error">
                    {error}
                </Typography>
            </Box>
        );
    }

    return (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="calc(100vh - 64px)">
            <div style={{ padding: "20px", width: "90%", maxWidth: "1200px" }}>
                <Typography variant="h4" gutterBottom>
                    Roles
                </Typography>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={() => navigate("/roles/create")}
                    style={{ marginBottom: "20px" }}
                >
                    Create Role
                </Button>
                <TableContainer component={Paper}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell>Role Name</TableCell>
                                <TableCell>Description</TableCell>
                                <TableCell>Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {roles.length > 0 ? (
                                roles.map((role) => (
                                    <TableRow key={role.id}>
                                        <TableCell>{role.id}</TableCell>
                                        <TableCell>{role.roleName || "No Role Name"}</TableCell>
                                        <TableCell>{role.description || "No Description"}</TableCell>
                                        <TableCell>
                                            <Button
                                                variant="outlined"
                                                color="primary"
                                                onClick={() => navigate(`/roles/${role.id}/edit`)}
                                            >
                                                Edit
                                            </Button>
                                            <Button
                                                variant="outlined"
                                                color="secondary"
                                                onClick={() => handleDelete(role.id)}
                                            >
                                                Delete
                                            </Button>
                                        </TableCell>
                                    </TableRow>
                                ))
                            ) : (
                                <TableRow>
                                    <TableCell colSpan={4} align="center">
                                        No roles found.
                                    </TableCell>
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>
            </div>
        </Box>
    );
};

export default RoleList;
