import Protected from "./components/Protected.jsx";
import Public from "./components/Public.jsx";

import useAuth from "./hooks/useAuth.jsx";
import {useState} from "react";


function App() {
  const isLogin = useAuth();
  return isLogin ? <Protected/> : <Public/>
}

export default App;
