import React from "react";
import Navigation from "./components/Navigation";
import {
    BrowserRouter as Router,
    Routes,
    Route,
} from "react-router-dom";
import Home from "./pages/Home";
import Tier from "./pages/Tier";

function App() {
  return (
    <Router>
      <Navigation />
      <Routes>
          <Route exact path="/" element={<Home />} />
          <Route path="/tier" element={<Tier />} />
      </Routes>
  </Router>
  );
}

export default App;
