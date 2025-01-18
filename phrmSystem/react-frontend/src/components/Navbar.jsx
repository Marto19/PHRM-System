import React from "react";
import { AppBar, Toolbar, Button, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";

const Navbar = () => {
    const navigate = useNavigate();

    return (
        <AppBar position="static">
            <Toolbar>
                <Typography variant="h6" style={{ flexGrow: 1 }}>
                    Management Portal
                </Typography>
                <Button color="inherit" onClick={() => navigate("/users")}>
                    User
                </Button>
                <Button color="inherit" onClick={() => navigate("/sick-days")}>
                    Sick Day
                </Button>
                <Button color="inherit" onClick={() => navigate("/roles")}>
                    Role
                </Button>
                <Button color="inherit" onClick={() => navigate("/illness-histories")}>
                    Illness History
                </Button>
                <Button color="inherit" onClick={() => navigate("/patients")}>
                    Patient
                </Button>
                <Button color="inherit" onClick={() => navigate("/medicines")}>
                    Medicines
                </Button>
                <Button color="inherit" onClick={() => navigate("/specializations")}>
                    Specializations
                </Button>
            </Toolbar>
        </AppBar>
    );
};

export default Navbar;
