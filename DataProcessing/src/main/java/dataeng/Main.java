package dataeng;

public class Main {
    public static void main(String[] args) {
        DataProcessor dataProcessor = new DataProcessor();
        dataProcessor.processUnknownCityDistrict();
        dataProcessor.processCityDistrictMap("src/main/resources/dataset1/CITY_DISTRICT_MAP.csv");
        dataProcessor.processCsvData("src/main/resources/dataset1/DATA.csv");
        dataProcessor.processJsonData("src/main/resources/dataset2.json");
        dataProcessor.processCityAmt();
    }
}