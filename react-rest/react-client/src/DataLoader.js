import React, { useState, useEffect } from 'react';
import style from './DataLoader.css';

export default function DataLoader(props) {
  const [data, setData] = useState(undefined);
  const [error, setError] = useState();
  useEffect(() => {
    setData(undefined);
    const loadData = async () => {
      try {
        const result = await props.loader();
        setData({ result });
      } catch (e) {
        setError(e);
      }
    };

    loadData();
  }, [props]);
  if (!!data) {
    if (props.children instanceof Function) {
      return props.children(data.result);
    }
    return props.children;
  }
  return (<Loader error={error} aria-label="Loading data" />);
}

export function Loader(props) {
  return (
    <div className={style.loader} aria-label={props['aria-label']}>
      {!props || (!props.error && (
        <div className="center">
          <svg width="5%" height="5%" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" preserveAspectRatio="xMidYMid" className="lds-eclipse">
            <path ng-attr-d="{{config.pathCmd}}" ng-attr-fill="{{config.color}}" stroke="none" d="M10 50A40 40 0 0 0 90 50A40 42 0 0 1 10 50" fill="#eca611" transform="rotate(125.654 50 51)">
              <animateTransform attributeName="transform" type="rotate" calcMode="linear" values="0 50 51;360 50 51" keyTimes="0;1" dur="1s" begin="0s" repeatCount="indefinite"></animateTransform>
            </path>
          </svg>
        </div>
      ))}
      {props && props.error &&
        <div className="cards">
          <div className="card">
            <h5 className="card-header">Error</h5>
            <p className="card-body">{props.error}</p>
          </div>
        </div>
      }
    </div>
  );
}