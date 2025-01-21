import React from "react";
import { Navigate } from "react-router-dom";
import useAuth from "../hooks/useAuth";

const Protected = ({ allowedRoles, children }) => {
    const { isAuthenticated, roles } = useAuth();

    console.log("Is Authenticated:", isAuthenticated);
    console.log("Roles:", roles);
    console.log("Allowed Roles:", allowedRoles);

    if (!isAuthenticated) {
        return <div>Loading... Redirecting to login...</div>;
    }

    const hasAccess = roles.some((role) => allowedRoles.includes(role));
    if (!hasAccess) {
        return (
            <div>
                <h1>Unauthorized</h1>
                <p>You do not have permission to access this page.</p>
                <button onClick={() => window.history.back()}>Go Back</button>
            </div>
        );
    }

    return <>{children}</>;
};

export default Protected;
