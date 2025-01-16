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

const SickDayList = () => {
    const [sickDays, setSickDays] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        axios
            .get("/api/sick-days")
            .then((response) => {
                console.log("Fetched Sick Days Data:", response.data); // Log API response
                setSickDays(response.data);
                setLoading(false);
            })
            .catch((error) => {
                console.error("Error fetching sick days:", error); // Log error details
                setError("Failed to fetch sick days.");
                setLoading(false);
            });
    }, []);




    const handleDelete = (id) => {
        if (window.confirm("Are you sure you want to delete this sick day?")) {
            axios
                .delete(`/api/sick-days/${id}`)
                .then(() => {
                    setSickDays(sickDays.filter((sickDay) => sickDay.id !== id));
                })
                .catch((err) => {
                    console.error("Error deleting sick day:", err);
                    setError("Failed to delete sick day.");
                });
        }
    };

    if (loading) return <Typography>Loading sick days...</Typography>;
    if (error) return <Typography>{error}</Typography>;

    return (
        <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            minHeight="calc(100vh - 64px)"
        >
            <div style={{ padding: "20px", width: "90%", maxWidth: "1200px" }}>
                <Typography variant="h4" gutterBottom>
                    Sick Days
                </Typography>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={() => navigate("/sick-days/create")}
                    style={{ marginBottom: "20px" }}
                >
                    Create Sick Day
                </Button>
                <TableContainer component={Paper}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell>Start Date</TableCell>
                                <TableCell>End Date</TableCell>
                                <TableCell>Number of Days</TableCell>
                                <TableCell>Patient</TableCell>
                                <TableCell>Doctor</TableCell>
                                <TableCell>Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {sickDays.map((sickDay) => (
                                <TableRow key={sickDay.id}>
                                    <TableCell>{sickDay.id}</TableCell>
                                    <TableCell>{sickDay.startDate}</TableCell>
                                    <TableCell>{sickDay.endDate}</TableCell>
                                    <TableCell>{sickDay.numberOfDays}</TableCell>
                                    <TableCell>
                                        {sickDay.patientId ? sickDay.patientId : "No patient assigned"}
                                    </TableCell>
                                    <TableCell>
                                        {sickDay.doctorId ? sickDay.doctorId : "No doctor assigned"}
                                    </TableCell>
                                    <TableCell>
                                        <Button
                                            variant="outlined"
                                            color="primary"
                                            onClick={() => navigate(`/sick-days/${sickDay.id}/edit`)}
                                        >
                                            Edit
                                        </Button>
                                        <Button
                                            variant="outlined"
                                            color="secondary"
                                            onClick={() => handleDelete(sickDay.id)}
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

export default SickDayList;
