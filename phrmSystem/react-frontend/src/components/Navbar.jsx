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
                    User List
                </Button>
                <Button color="inherit" onClick={() => navigate("/sick-days")}>
                    Sick Day List
                </Button>
                <Button color="inherit" onClick={() => navigate("/roles")}>
                    Role List
                </Button>
                <Button color="inherit" onClick={() => navigate("/illness-histories")}>
                    Illness History List
                </Button>
                <Button color="inherit" onClick={() => navigate("/illness-histories/create")}>
                    Create Illness History
                </Button>
            </Toolbar>
        </AppBar>
    );
};

export default Navbar;
