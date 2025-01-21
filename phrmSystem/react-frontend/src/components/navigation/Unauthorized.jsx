import React from "react";
import { useNavigate } from "react-router-dom";

const Unauthorized = () => {
    const navigate = useNavigate();

    const goBack = () => {
        navigate(-1); // Navigate to the previous page
    };

    return (
        <div style={{ textAlign: "center", marginTop: "20px" }}>
            <h1>Unauthorized</h1>
            <p>You do not have permission to access this page.</p>
            <button onClick={goBack} style={{ padding: "10px 20px", marginTop: "10px" }}>
                Go Back
            </button>
        </div>
    );
};

export default Unauthorized;
