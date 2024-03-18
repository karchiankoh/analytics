import React, { useState } from 'react';
import GChart from '../components/GChart'
import Select from '../components/Select'
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';


function Tier() {
  const [num, setNum] = useState(10);

  return (
    <Container className="p-3">
      <Row>
        <Col></Col>
        <Col>
          <Select changeFunction={e => setNum(parseInt(e.target.value))} />

        </Col>
        <Col></Col>
      </Row>
      <Row>
        <GChart uri={'http://localhost:8080/queries/nTierTotalSales/' + num} chartType='PieChart' heading='Tier Total Sales' />
      </Row>
      <Row>
        <GChart uri={'http://localhost:8080/queries/nTierDetails/' + num} chartType='Table' heading='Tier Details' />
      </Row>
    </Container>
  );
}

export default Tier;
