import React from 'react';
import Fruit from './Fruit';
import Legume from './Legume';

function App() {
  return (
    <div className="container">
      <h1>REST Service</h1>
      <Legume></Legume>
      <Fruit></Fruit>
    </div>
  );
}

export default App;
