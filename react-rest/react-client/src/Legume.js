import React from 'react';
import { LegumeLoader } from './Loader';

export default function Legume() {
  return (
    <div>
      <h3>Legume List</h3>
      <div className="row">
        <div className="col-4">Name</div>
        <div className="col-8">Description</div>
      </div>
      <LegumeLoader>
        {legumes => (
          legumes.map((legume, i) => {
            return (
              <div className="row" key={i}>
                <div className="col-4">{legume.name}</div>
                <div className="col-8">{legume.description}</div>
              </div>
            );
          })
        )}
      </LegumeLoader>
    </div>
  );
}