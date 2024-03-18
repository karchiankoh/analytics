-- INSERT INTO fact_city_amt (city_cd, rptg_amt)
-- SELECT ship_to_city_cd AS city_cd,
-- 	SUM(rptg_amt) AS rptg_amt
-- FROM dim_city_district_map LEFT JOIN fact_orders
-- ON dim_city_district_map.city_district_id = fact_orders.city_district_id
-- GROUP BY 1;

-- Summary stats
DELIMITER $$
CREATE PROCEDURE `sp_stats`()
BEGIN
SELECT ROUND(SUM(rptg_amt)) AS 'Total Sales (RMB)', 
	COUNT(order_id) AS 'Total Orders', 
    SUM(order_qty) AS 'Total Units', 
    ROUND(AVG(rptg_amt)) AS 'Avg Order Sales (RMB)', 
	(SELECT ROUND(AVG(rptg_amt)) AS avg_sales FROM fact_city_amt) AS 'Avg City Sales (RMB)'
FROM fact_orders;
END $$

-- Total sales by city
DELIMITER $$
CREATE PROCEDURE `sp_total_sales_by_city`()
BEGIN
WITH top_cities_by_total_sales AS (
	SELECT city_cd, rptg_amt FROM fact_city_amt ORDER BY rptg_amt DESC LIMIT 10
)
SELECT city_cd AS 'City Cd', ROUND(rptg_amt) AS 'Total Sales' FROM top_cities_by_total_sales
UNION
SELECT 'Others' AS 'City Cd', ROUND(SUM(rptg_amt)) AS 'Total Sales' FROM fact_city_amt WHERE city_cd NOT IN (SELECT city_cd FROM top_cities_by_total_sales);
END $$

-- Total sales by city district
DELIMITER $$
CREATE PROCEDURE `sp_total_sales_by_city_district`()
BEGIN
WITH top_city_districts_by_total_sales AS (
	SELECT city_district_id, SUM(rptg_amt) AS total_sales FROM fact_orders GROUP BY 1 ORDER BY 2 DESC LIMIT 20
)
(SELECT CONCAT(ship_to_city_cd, '  ', ship_to_district_name) AS 'City District', ROUND(SUM(rptg_amt)) AS 'Total Sales'
FROM fact_orders, dim_city_district_map
WHERE fact_orders.city_district_id = dim_city_district_map.city_district_id
	AND fact_orders.city_district_id IN (SELECT city_district_id FROM top_city_districts_by_total_sales)
GROUP BY 1
ORDER BY 2 DESC)
UNION
SELECT 'Others' AS 'City District', ROUND(grand_total.sales - top_city_districts.sales) AS 'Total Sales'
FROM (SELECT SUM(fact_city_amt.rptg_amt) AS sales FROM fact_city_amt) grand_total, 
	(SELECT SUM(top_city_districts_by_total_sales.total_sales) AS sales FROM top_city_districts_by_total_sales) top_city_districts;
END $$

-- Total sales by hour cst
DELIMITER $$
CREATE PROCEDURE `sp_total_sales_by_hour_cst`()
BEGIN
WITH total_sales_by_hour_pst AS (
	SELECT hour, SUM(rptg_amt) AS total_sales
	FROM dim_time, fact_orders
	WHERE dim_time.time_id = fact_orders.order_time_id
		AND hour != 0
	GROUP BY 1
	ORDER BY 1
)
SELECT CONCAT((hour + 15) % 24, ':00') AS 'Hour CST', ROUND(total_sales) AS 'Total Sales' FROM total_sales_by_hour_pst;
END $$

-- Top 10 cities by average order sales
DELIMITER $$
CREATE PROCEDURE `sp_top_cities_by_avg_order_sales`()
BEGIN
SELECT ship_to_city_cd AS 'City Cd', ROUND(SUM(rptg_amt) / count(order_id)) AS 'Avg Order Sales'
FROM dim_city_district_map LEFT JOIN fact_orders
ON dim_city_district_map.city_district_id = fact_orders.city_district_id
GROUP BY 1
ORDER BY 2 DESC
LIMIT 10;
END $$

-- Top 10 cities by average district sales
DELIMITER $$
CREATE PROCEDURE `sp_top_cities_by_avg_district_sales`()
BEGIN
SELECT ship_to_city_cd AS 'City Cd', ROUND(SUM(rptg_amt) / count(distinct fact_orders.city_district_id)) AS 'Avg District Sales'
FROM dim_city_district_map LEFT JOIN fact_orders
ON dim_city_district_map.city_district_id = fact_orders.city_district_id
GROUP BY 1
ORDER BY 2 DESC
LIMIT 10;
END $$

-- Top 10 cities by per hour sales
DELIMITER $$
CREATE PROCEDURE `sp_top_cities_by_per_hour_sales`()
BEGIN
SELECT city_cd AS 'City Cd', ROUND(rptg_amt / num) AS 'Per Hour Sales'
FROM fact_city_amt, (SELECT max(hour) - min(hour) + 1 AS num FROM dim_time WHERE hour != 0) hours
ORDER BY 2 DESC
LIMIT 10;
END $$

-- N tier total sales
DELIMITER $$
CREATE PROCEDURE `sp_n_tier_total_sales`(in num int)
BEGIN
WITH city_amt_tiers AS (
	SELECT city_cd, rptg_amt, NTILE(num) OVER (ORDER BY rptg_amt DESC) AS tier FROM fact_city_amt
)
SELECT CONCAT('Tier', tier) AS 'Tier', ROUND(SUM(rptg_amt)) AS 'Total Sales'
FROM city_amt_tiers
GROUP BY 1;
END $$

-- N tier details
DELIMITER $$
CREATE PROCEDURE `sp_n_tier_details`(in num int)
BEGIN
WITH city_amt_tiers AS (
	SELECT city_cd, rptg_amt, NTILE(num) OVER (ORDER BY rptg_amt DESC) AS tier FROM fact_city_amt
)
SELECT tier AS 'Tier', ROUND(MIN(rptg_amt)) AS 'Min Sales', ROUND(MAX(rptg_amt)) AS 'Max Sales', COUNT(distinct city_cd) AS 'Num Cities', GROUP_CONCAT(city_cd) AS 'Cities'
FROM city_amt_tiers
GROUP BY 1;
END $$