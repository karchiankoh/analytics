import axios from 'axios';
import React, { useState, useEffect } from 'react';
import Table from 'react-bootstrap/Table';

function ResponsiveTable() {
  const [statsData, setStatsData] = useState(false);

  useEffect(() => {
    axios.get('http://localhost:8080/queries/stats')
    .then(res => setStatsData(res.data))
    .catch(err => console.log(err));
  }, [])

  return (
    <Table responsive>
      <thead>
        <tr>
          { statsData && statsData.data.map((stats, index) => (
            <th key={index}>{stats[1].toLocaleString() + ' ' + stats[0]}</th>
          ))}
        </tr>
      </thead>
    </Table>
  );
}

export default ResponsiveTable;