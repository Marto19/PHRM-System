import React, { useState } from "react";

const useAuth = () => {
    const [isLoged, setLogin] = useState(true)
    return isLoged;
};

export default useAuth;