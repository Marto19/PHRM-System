import React from "react";
import { AppBar, Toolbar, Button, Typography } from "@mui/material";
import { Link } from "react-router-dom";

const Navbar = () => {
    return (
        <AppBar position="static">
            <Toolbar>
                <Typography variant="h6" style={{ flexGrow: 1 }}>
                    Management Portal
                </Typography>
                <Button color="inherit" component={Link} to="/users">
                    User List
                </Button>
                <Button color="inherit" component={Link} to="/users/create">
                    Create User
                </Button>
                <Button color="inherit" component={Link} to="/sick-days">
                    Sick Day List
                </Button>
                <Button color="inherit" component={Link} to="/sick-days/create">
                    Create Sick Day
                </Button>
                <Button color="inherit" component={Link} to="/roles">
                    Role List
                </Button>
                <Button color="inherit" component={Link} to="/roles/create">
                    Create Role
                </Button>

            </Toolbar>
        </AppBar>
    );
};

export default Navbar;
