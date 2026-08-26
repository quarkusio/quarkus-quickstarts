import React, { useState } from 'react';

export default function Form(props) {
  const [inputs, setInputs] = useState({});
  const handleSubmit = (event) => {
    if (event) {
      event.preventDefault();
    }
    props.callback(inputs);
    setInputs({})
  };
  const handleInputChange = (event) => {
    event.persist();
    setInputs(inputs => ({...inputs, [event.target.name]: event.target.value}));
  };
  return (
    <form onSubmit={handleSubmit}>
      <div className="row">
        <div className="col-6">
          <input type="text" placeholder="Name" name="name" size="60" onChange={handleInputChange} value={inputs.name || ''}/>
        </div>
      </div>
      <div className="row">
        <div className="col-6">
          <input type="text" placeholder="Description" name="description" size="60" onChange={handleInputChange} value={inputs.description || ''}/>
        </div>
      </div>
      <input type="submit" value="Save" />
    </form>
  );
}