const db = require('./db');
const helper = require('../helper');
const config = require('../config');

async function getMultiple(){
  // const offset = helper.getOffset(page, config.listPerPage);
  const res = await db.query(
    `SELECT * FROM fact_city_amt ORDER BY rptg_amt DESC LIMIT 10` //${offset},${config.listPerPage}`
  );
  const rows = helper.emptyOrRows(res[0]);
  const data = helper.mapToArray(rows)
  return {
    data
  }
}

async function queryStats(){
  const res = await db.callSpStats();
  const rows = helper.emptyOrRows(res[0][0]);
  const data = helper.mapToTableData(rows);
  return {
    data
  }
}

async function queryNTierTotalSales(num){
  return querySp(db.callSpNTierTotalSales(num))
}

async function queryNTierDetails(num){
  return querySp(db.callSpNTierDetails(num))
}

async function queryTotalSalesByCity(){
  return querySp(db.callSpTotalSalesByCity())
}

async function queryTotalSalesByCityDistrict(){
  return querySp(db.callSpTotalSalesByCityDistrict())
}

async function queryTotalSalesByHourCst(){
  return querySp(db.callSpTotalSalesByHourCst())
}

async function queryTopCitiesByAvgOrderSales(){
  return querySp(db.callSpTopCitiesByAvgOrderSales())
}

async function queryTopCitiesByAvgDistrictSales(){
  return querySp(db.callSpTopCitiesByAvgDistrictSales())
}

async function queryTopCitiesByPerHourSales(){
  return querySp(db.callSpTopCitiesByPerHourSales())
}



async function querySp(sp){
  const res = await sp;
  const rows = helper.emptyOrRows(res[0]);
  const data = helper.mapToArray(rows)
  return {
    data
  }
}

module.exports = {
  getMultiple,
  queryNTierTotalSales,
  queryNTierDetails,
  queryStats,
  queryTotalSalesByCity,
  queryTotalSalesByCityDistrict,
  queryTotalSalesByHourCst,
  queryTopCitiesByAvgOrderSales,
  queryTopCitiesByAvgDistrictSales,
  queryTopCitiesByPerHourSales
}