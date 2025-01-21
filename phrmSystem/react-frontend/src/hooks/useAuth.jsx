import React, { useState, useEffect, useRef } from "react";
import Keycloak from "keycloak-js";

// Initialize Keycloak instance
const keycloakClient = new Keycloak({
    url: import.meta.env.VITE_KEYCLOAK_URL,
    realm: import.meta.env.VITE_KEYCLOAK_REALM,
    clientId: import.meta.env.VITE_KEYCLOAK_CLIENT,
});

const useAuth = () => {
    const isRun = useRef(false);
    const [isAuthenticated, setAuthenticated] = useState(false);
    const [roles, setRoles] = useState([]);
    const [token, setToken] = useState(null);

    useEffect(() => {
        if (isRun.current) return;
        isRun.current = true;

        keycloakClient
            .init({
                onLoad: "login-required",
                checkLoginIframe: false,
            })
            .then((authenticated) => {
                console.log("Authenticated:", authenticated);
                setAuthenticated(authenticated);

                if (authenticated) {
                    console.log("Token:", keycloakClient.token);
                    console.log("Token Parsed:", keycloakClient.tokenParsed);

                    setToken(keycloakClient.token);

                    // Extract roles from resource_access
                    const clientRoles =
                        keycloakClient.tokenParsed?.resource_access?.react_client?.roles || [];
                    console.log("Roles from Resource Access:", clientRoles);

                    setRoles(clientRoles);
                }
            })
            .catch((error) => {
                console.error("Keycloak initialization failed:", error);
            });
    }, []);

    return { isAuthenticated, roles, token };
};

export default useAuth;