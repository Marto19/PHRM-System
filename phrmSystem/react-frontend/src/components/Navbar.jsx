import React from "react";
import { AppBar, Toolbar, Button, Typography } from "@mui/material";
import { Link } from "react-router-dom";

const Navbar = () => {
    return (
        <AppBar position="static">
            <Toolbar>
                <Typography variant="h6" style={{ flexGrow: 1 }}>
                    User Management
                </Typography>
                <Button color="inherit" component={Link} to="/users">
                    User List
                </Button>
                <Button color="inherit" component={Link} to="/users/create">
                    Create User
                </Button>
                <Button color="inherit" component={Link} to="/">
                    Public Page
                </Button>
                <Button color="inherit" component={Link} to="/protected">
                    Protected Page
                </Button>
            </Toolbar>
        </AppBar>
    );
};

export default Navbar;
