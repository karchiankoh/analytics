import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { Chart } from "react-google-charts";

function GChart({ uri, chartType, heading }) {
  const [chartData, setChartData] = useState([]);

  useEffect(() => {
    axios.get(uri)
    .then(res => setChartData(res.data))
    .catch(err => console.log(err));
  }, [uri])

  return (
    <Chart
      chartType={chartType}
      data={chartData.data}
      options={{title: heading}}
      width={"100%"}
      height={"400px"}
    />
  );
}

export default GChart