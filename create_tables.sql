USE GBI;

DROP TABLE IF EXISTS fact_city_amt, fact_orders, dim_time, dim_city_district_map;

CREATE TABLE dim_city_district_map (
	city_district_id		INT NOT NULL AUTO_INCREMENT,
    ship_to_city_cd			VARCHAR(150) NOT NULL,
    ship_to_district_name	VARCHAR(150) NOT NULL,
    PRIMARY KEY				(city_district_id)
);

CREATE TABLE dim_time (
	time_id		INT NOT NULL,
    time_pst	TIME NOT NULL,
    hour		INT NOT NULL,
    minute		INT NOT NULL,
    second		INT NOT NULL,
    PRIMARY KEY (time_id)
);

CREATE TABLE fact_orders (
	order_id			CHAR(11) NOT NULL,
    order_time_id		INT NOT NULL,
    city_district_id	INT NOT NULL,
    currency_cd			CHAR(3) NOT NULL,
    rptg_amt			DECIMAL(13, 2) NOT NULL,
    order_qty			INT,
    PRIMARY KEY 		(order_id),
    FOREIGN KEY			(order_time_id) REFERENCES dim_time(time_id),
    FOREIGN KEY			(city_district_id) REFERENCES dim_city_district_map(city_district_id)
);

CREATE TABLE fact_city_amt (
	city_cd		VARCHAR(150) NOT NULL,
    rptg_amt	DECIMAL(13, 2) NOT NULL
);