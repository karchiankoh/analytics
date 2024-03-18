const mysql = require('mysql2/promise');
const config = require('../config');

const connection = mysql.createPool(config.db);

async function callSp(sp) {
  const [results, ] = await connection.query(sp);

  return results;
}

async function query(sql, params) {
  const [results, ] = await connection.execute(sql, params);

  return results;
}

async function callSpNTierTotalSales(num) {
  const [results, ] = await connection.query('CALL sp_n_tier_total_sales(' + num + ')');

  return results;
}

async function callSpNTierDetails(num) {
  const [results, ] = await connection.query('CALL sp_n_tier_details(' + num + ')');

  return results;
}

async function callSpStats() {
  return callSp('CALL sp_stats()');
}

async function callSpTotalSalesByCity() {
  return callSp('CALL sp_total_sales_by_city()');
}

async function callSpTotalSalesByCityDistrict() {
  return callSp('CALL sp_total_sales_by_city_district()');
}

async function callSpTotalSalesByHourCst() {
  return callSp('CALL sp_total_sales_by_hour_cst()');
}

async function callSpTopCitiesByAvgOrderSales() {
  return callSp('CALL sp_top_cities_by_avg_order_sales()');
}

async function callSpTopCitiesByAvgDistrictSales() {
  return callSp('CALL sp_top_cities_by_avg_district_sales()');
}

async function callSpTopCitiesByPerHourSales() {
  return callSp('CALL sp_top_cities_by_per_hour_sales()');
}

module.exports = {
  query,
  callSpNTierTotalSales,
  callSpNTierDetails,
  callSpStats,
  callSpTotalSalesByCity,
  callSpTotalSalesByCityDistrict,
  callSpTotalSalesByHourCst,
  callSpTopCitiesByAvgOrderSales,
  callSpTopCitiesByAvgDistrictSales,
  callSpTopCitiesByPerHourSales
}