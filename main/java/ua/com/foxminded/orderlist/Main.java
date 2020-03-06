package ua.com.foxminded.orderlist;

public class Main {
    public static void main(String[] args) {
        DriversSorter driversSorter = new DriversSorter();
        System.out.println(driversSorter.createSortedReport(Constants.START_LOG, Constants.END_LOG,
                Constants.ABBREVIATIONS));
    }
}
