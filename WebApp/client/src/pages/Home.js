import React from 'react';
import GChart from '../components/GChart'
import ResponsiveTable from '../components/ResponsiveTable'
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

function Home() {

  return (
    <Container className="p-3">
      <Row>
        <ResponsiveTable />
      </Row>
      <Row>
        <Col>
          <GChart uri='http://localhost:8080/queries/totalSalesByHourCst' chartType='LineChart' heading='Total Sales By Hour CST' />
        </Col>
        <Col>
          <GChart uri='http://localhost:8080/queries/totalSalesByCity' chartType='PieChart' heading='Total Sales By City' />
        </Col>
      </Row>
      <Row>
        <Col>
          <GChart uri='http://localhost:8080/queries/totalSalesByCityDistrict' chartType='PieChart' heading='Total Sales By District' />
        </Col>
        <Col>
          <GChart uri='http://localhost:8080/queries/topCitiesByPerHourSales' chartType='ColumnChart' heading='Top Cities By Per Hour Sales' />
        </Col>
      </Row>
      <Row>
        <Col>
          <GChart uri='http://localhost:8080/queries/topCitiesByAvgDistrictSales' chartType='ColumnChart' heading='Top Cities By Avg District Sales' />
        </Col>
        <Col>
          <GChart uri='http://localhost:8080/queries/topCitiesByAvgOrderSales' chartType='ColumnChart' heading='Top Cities By Avg Order Sales' />
        </Col>
      </Row>
    </Container>
  );
}

export default Home;
