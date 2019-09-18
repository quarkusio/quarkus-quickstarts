import React from 'react';
import DataLoader from './DataLoader';

export function FruitLoader(props) {
  return ListLoader({...props, url: '/api/fruits/'})
}

export function LegumeLoader(props) {
  return ListLoader({...props, url: '/api/legumes/'})
}

function ListLoader(props) {
  const loader = async () => {
    return await fetch(props.url).then(res => res.json());
  };
  return (
    <DataLoader loader={loader}>
      {props.children}
    </DataLoader>
  );
}