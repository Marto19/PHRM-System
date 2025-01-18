import React, { useEffect, useState } from "react";
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

const UserList = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        axios
            .get("/api/users")
            .then((response) => {
                setUsers(response.data);
                setLoading(false);
            })
            .catch((error) => {
                console.error("Error fetching users:", error);
                setError("Failed to fetch users.");
                setLoading(false);
            });
    }, []);

    const handleDelete = (id) => {
        if (window.confirm("Are you sure you want to delete this user?")) {
            axios
                .delete(`/api/users/${id}`)
                .then(() => {
                    setUsers(users.filter((user) => user.id !== id));
                })
                .catch((err) => {
                    console.error("Error deleting user:", err);
                    setError("Failed to delete user.");
                });
        }
    };

    if (loading) return <Typography>Loading users...</Typography>;
    if (error) return <Typography>{error}</Typography>;

    return (
        <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            minHeight="calc(100vh - 64px)" // Adjust for navbar height
        >
            <div style={{ padding: "20px", width: "90%", maxWidth: "1200px" }}>
                <Typography variant="h4" gutterBottom>
                    Users
                </Typography>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={() => navigate("/users/create")}
                    style={{ marginBottom: "20px" }}
                >
                    Create User
                </Button>
                <TableContainer component={Paper}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell>First Name</TableCell>
                                <TableCell>Last Name</TableCell>
                                <TableCell>Roles</TableCell>
                                <TableCell>Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {users.map((user) => (
                                <TableRow key={user.id}>
                                    <TableCell>{user.id}</TableCell>
                                    <TableCell>{user.firstName}</TableCell>
                                    <TableCell>{user.lastName}</TableCell>
                                    <TableCell>
                                        {user.roles.map((role) => role.roleName).join(", ")}
                                    </TableCell>
                                    <TableCell>
                                        <Button
                                            variant="outlined"
                                            color="primary"
                                            onClick={() => navigate(`/users/${user.id}/edit`)}
                                            style={{ marginRight: "10px" }}
                                        >
                                            Edit
                                        </Button>
                                        <Button
                                            variant="outlined"
                                            color="secondary"
                                            onClick={() => handleDelete(user.id)}
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

export default UserList;
