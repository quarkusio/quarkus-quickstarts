import React, { useState } from 'react';
import Form from './Form';
import { FruitLoader } from './Loader';

export default function Fruit() {
  const [fruits, setFruits] = useState(undefined);
  return (
    <div>
      <h3>Add a fruit</h3>
      <Form callback={fruit =>
        fetch('/api/fruits', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(fruit),
        })
        .then(resp => resp.json())
        .then(fruits => setFruits(fruits))
      }>
      </Form>
      <h3>Fruit List</h3>
      <div className="row">
        <div className="col-4">Name</div>
        <div className="col-8">Description</div>
      </div>
      <FruitLoader>
        {fetchedFruits => (
          (fruits || fetchedFruits).map((fruit, i) => {
            return (
              <div className="row" key={i}>
                <div className="col-4">{fruit.name}</div>
                <div className="col-8">{fruit.description}</div>
              </div>
            );
          })
        )}
      </FruitLoader>
    </div>
  );
}