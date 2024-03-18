package dataeng;

import dataeng.dao.*;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class DataProcessor {
    private HanyuPinyinOutputFormat hpOutputFormat = new HanyuPinyinOutputFormat();
    private Map<String, Double> cityAmtMap = new HashMap<>();
    private CityDistrictDao cityDistrictDao = new CityDistrictDao();
    private TimeDao timeDao = new TimeDao();
    private OrderDao orderDao = new OrderDao();
    private CityAmtDao cityAmtDao = new CityAmtDao();

    public DataProcessor() {
        hpOutputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        hpOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        hpOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    public void processCityAmt() {
        cityAmtDao.setAndInsertCityAmtMap(cityAmtMap);
    }

    public void processUnknownCityDistrict() {
        cityDistrictDao.insertCityDistrictMap(-1, "UNKNOWN", "UNKNOWN");
    }
    public void processJsonData(String fileName)  {
        try (Reader dataset2 = new FileReader(fileName)) {
            JSONArray orderArray = (JSONArray) new JSONParser().parse(dataset2);
            for (Object order : orderArray) {
                JSONObject jsonOrder = (JSONObject) order;
                String shipToCityCd = translateIfNeeded((String) jsonOrder.get("SHIP_TO_CITY_CD"));
                String shipToDistrictName = translateIfNeeded((String) jsonOrder.get("SHIP_TO_DISTRICT_NAME"));
                if (!cityDistrictDao.containsCityDistrict(shipToCityCd, shipToDistrictName)) {
                    cityDistrictDao.insertCityDistrictMap(shipToCityCd, shipToDistrictName);
                }
                int cityDistrictId = cityDistrictDao.getCityDistrictId(shipToCityCd, shipToDistrictName);
                String orderId = (String) jsonOrder.get("ORDER_ID");
                int orderTime = Math.toIntExact((Long) jsonOrder.get("ORDER_TIME_PST"));
                String currencyCd = (String) jsonOrder.get("CURRENCY_CD");
                double rptgAmt = ((Number) jsonOrder.get("RPTG_AMT")).doubleValue();
                int orderQty = Math.toIntExact((Long) jsonOrder.get("ORDER_QTY"));
                processOrder(orderId, orderTime, cityDistrictId, currencyCd, rptgAmt, orderQty);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void processCsvData(String fileName) {
        try (Reader data = new FileReader(fileName)) {
            Iterable<CSVRecord> records = CSVFormat.EXCEL
                    .withFirstRecordAsHeader()
                    .parse(data);

            for (CSVRecord record : records) {
                String orderId = record.get("ORDER_ID");
                int orderTime = NumberUtils.toInt(record.get("ORDER_TIME  (PST)"), 0);
                int cityDistrictId = NumberUtils.toInt(record.get("CITY_DISTRICT_ID"), -1);
                String currencyCd = record.get("CURRENCY_CD");
                double rptgAmt = NumberUtils.toDouble(record.get("RPTG_AMT"));
                int orderQty = NumberUtils.toInt(record.get("ORDER_QTY"), 1);
                processOrder(orderId, orderTime, cityDistrictId, currencyCd, rptgAmt, orderQty);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processCityDistrictMap(String fileName) {
        try (Reader cityDistrictReader = new FileReader(fileName)) {
            Iterable<CSVRecord> records = CSVFormat.EXCEL
                    .withFirstRecordAsHeader()
                    .parse(cityDistrictReader);
            for (CSVRecord record : records) {
                int cityDistrictId = Integer.valueOf(record.get("CITY_DISTRICT_ID"));
                String shipToCityCd = translateIfNeeded(record.get("SHIP_TO_CITY_CD"));
                String shipToDistrictName = translateIfNeeded(record.get("SHIP_TO_DISTRICT_NAME"));
                cityDistrictDao.insertCityDistrictMap(cityDistrictId, shipToCityCd, shipToDistrictName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processOrder(String orderId, int orderTime, int cityDistrictId, String currencyCd, double rptgAmt, int orderQty) {
        if (!timeDao.containsOrderTime(orderTime)) {
            timeDao.insertTime(orderTime);
        }
        if (!cityDistrictDao.containsCityDistrictId(cityDistrictId)) {
            cityDistrictId = -1;
        }
        rptgAmt = convertIfNeeded(currencyCd, rptgAmt);
        orderDao.insertFactOrder(orderId, orderTime, cityDistrictId, "RMB", rptgAmt, orderQty);
        String cityCd = cityDistrictDao.getCityCd(cityDistrictId);
        cityAmtMap.put(cityCd, cityAmtMap.getOrDefault(cityCd, 0D) + rptgAmt);
    }

    private double convertIfNeeded(String currencyCd, double amt) {
        return currencyCd.equals("USD")
                ? amt * 7.19
                : amt;
    }
    private String translateIfNeeded(String str) {
        try {
            if (str.matches("^[a-zA-Z][a-zA-Z\\s]+$")) {
                return str;
            } else {
                return PinyinHelper.toHanyuPinyinString(str, hpOutputFormat, "");
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            throw new IllegalStateException("This shouldn't happen");
        }
    }
}
