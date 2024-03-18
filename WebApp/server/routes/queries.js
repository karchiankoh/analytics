const express = require('express');
const router = express.Router();
const queries = require('../services/queries');

/* GET */
router.get('/', async function(req, res, next) {
  try {
    res.json(await queries.getMultiple());
  } catch (err) {
    console.error(`Error while retrieving `, err.message);
    next(err);
  }
});

router.get('/nTierTotalSales/:num', async function(req, res, next) {
  try {
    res.json(await queries.queryNTierTotalSales(req.params.num));
  } catch (err) {
    console.error(`Error while retrieving `, err.message);
    next(err);
  }
});

router.get('/nTierDetails/:num', async function(req, res, next) {
  try {
    res.json(await queries.queryNTierDetails(req.params.num));
  } catch (err) {
    console.error(`Error while retrieving `, err.message);
    next(err);
  }
});

router.get('/stats', async function(req, res, next) {
  try {
    res.json(await queries.queryStats());
  } catch (err) {
    console.error(`Error while querying `, err.message);
    next(err);
  }
});

router.get('/totalSalesByCity', async function(req, res, next) {
  try {
    res.json(await queries.queryTotalSalesByCity());
  } catch (err) {
    console.error(`Error while querying `, err.message);
    next(err);
  }
});

router.get('/totalSalesByCityDistrict', async function(req, res, next) {
  try {
    res.json(await queries.queryTotalSalesByCityDistrict());
  } catch (err) {
    console.error(`Error while querying `, err.message);
    next(err);
  }
});

router.get('/totalSalesByHourCst', async function(req, res, next) {
  try {
    res.json(await queries.queryTotalSalesByHourCst());
  } catch (err) {
    console.error(`Error while querying `, err.message);
    next(err);
  }
});

router.get('/topCitiesByAvgOrderSales', async function(req, res, next) {
  try {
    res.json(await queries.queryTopCitiesByAvgOrderSales());
  } catch (err) {
    console.error(`Error while querying `, err.message);
    next(err);
  }
});

router.get('/topCitiesByAvgDistrictSales', async function(req, res, next) {
  try {
    res.json(await queries.queryTopCitiesByAvgDistrictSales());
  } catch (err) {
    console.error(`Error while querying `, err.message);
    next(err);
  }
});

router.get('/topCitiesByPerHourSales', async function(req, res, next) {
  try {
    res.json(await queries.queryTopCitiesByPerHourSales());
  } catch (err) {
    console.error(`Error while querying `, err.message);
    next(err);
  }
});

module.exports = router;