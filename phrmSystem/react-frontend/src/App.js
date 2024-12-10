import React from 'react';

import Protected from "./component/Protected"
import Public from "./component/Public"

import useAuth from "./hooks/UserAuth";

function App() {
  const isLoged = useAuth();

  return  isLoged ? <Protected /> : <Public />;
}

export default App;
