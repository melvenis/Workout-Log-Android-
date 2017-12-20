package leggettm.workoutlog;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
class Year {
    private List<String> jan;
    private List<String> feb;
    private List<String> mar;
    private List<String> apr;
    private List<String> may;
    private List<String> jun;
    private List<String> jul;
    private List<String> aug;
    private List<String> sep;
    private List<String> oct;
    private List<String> nov;
    private List<String> dec;
    private int name;
    //fill info (zero) field of arrays
    private static final String FIRST_ROW = "Choose New Day to Add";

    public Year() {}
    
    public Year(int year) {
        name = year;
        buildMonths();
    }

    public void buildMonths() {
        jan = new ArrayList<>();
        jan.add(FIRST_ROW);
        fillMonths(jan,31);

        feb = new ArrayList<>();
        feb.add(FIRST_ROW);
        fillMonths(feb,29);

        mar = new ArrayList<>();
        mar.add(FIRST_ROW);
        fillMonths(mar,31);

        apr = new ArrayList<>();
        apr.add(FIRST_ROW);
        fillMonths(apr,30);

        may = new ArrayList<>();
        may.add(FIRST_ROW);
        fillMonths(may,31);

        jun = new ArrayList<>();
        jun.add(FIRST_ROW);
        fillMonths(jun,30);

        jul = new ArrayList<>();
        jul.add(FIRST_ROW);
        fillMonths(jul,31);

        aug = new ArrayList<>();
        aug.add(FIRST_ROW);
        fillMonths(aug,31);

        sep = new ArrayList<>();
        sep.add(FIRST_ROW);
        fillMonths(sep,30);

        oct = new ArrayList<>();
        oct.add(FIRST_ROW);
        fillMonths(oct,31);

        nov = new ArrayList<>();
        nov.add(FIRST_ROW);
        fillMonths(nov,30);

        dec = new ArrayList<>();
        dec.add(FIRST_ROW);
        fillMonths(dec,31);
    }
    
    private void fillMonths(List<String> ourMonth, int days) {
        for (int i = 1; i <= days; i++) {
            if (i < 10)
                ourMonth.add("0" + i);
            else
                ourMonth.add(String.valueOf(i));
        }
    }

//    public void setMonth(String code, String[] month) {
//        int key = Integer.valueOf(monthToDigit(code));
//        switch (key) {
//            case 1:
//                jan = month;
//                break;
//            case 2:
//                feb = month;
//                break;
//            case 3:
//                mar = month;
//                break;
//            case 4:
//                apr = month;
//                break;
//            case 5:
//                may = month;
//                break;
//            case 6:
//                jun = month;
//                break;
//            case 7:
//                jul = month;
//                break;
//            case 8:
//                aug = month;
//                break;
//            case 9:
//                sep = month;
//                break;
//            case 10:
//                oct = month;
//                break;
//            case 11:
//                nov = month;
//                break;
//            default:
//                dec = month;
//                break;
//        }
//    }

    public List<String> getMonth(String key) {
        if(key.equalsIgnoreCase("01"))
            return jan;
        if(key.equalsIgnoreCase("02"))
            return feb;
        if(key.equalsIgnoreCase("03"))
            return mar;
        if(key.equalsIgnoreCase("04"))
            return apr;
        if(key.equalsIgnoreCase("05"))
            return may;
        if(key.equalsIgnoreCase("06"))
            return jun;
        if(key.equalsIgnoreCase("07"))
            return jul;
        if(key.equalsIgnoreCase("08"))
            return aug;
        if(key.equalsIgnoreCase("09"))
            return sep;
        if(key.equalsIgnoreCase("10"))
            return oct;
        if(key.equalsIgnoreCase("11"))
            return nov;
        return dec;
    }

    static String monthToDigit(String month) {
        if (month.equalsIgnoreCase("jan"))
            return "01";
        if (month.equalsIgnoreCase("feb"))
            return "02";
        if (month.equalsIgnoreCase("mar"))
            return "03";
        if (month.equalsIgnoreCase("apr"))
            return "04";
        if (month.equalsIgnoreCase("may"))
            return "05";
        if (month.equalsIgnoreCase("jun"))
            return "06";
        if (month.equalsIgnoreCase("jul"))
            return "07";
        if (month.equalsIgnoreCase("aug"))
            return "08";
        if (month.equalsIgnoreCase("sep"))
            return "09";
        if (month.equalsIgnoreCase("oct"))
            return "10";
        if (month.equalsIgnoreCase("nov"))
            return "11";
        return "12";
    }

    static String digitToMonth(String key) {
        if(key.equalsIgnoreCase("01"))
            return "January";
        if(key.equalsIgnoreCase("02"))
            return "February";
        if(key.equalsIgnoreCase("03"))
            return "March";
        if(key.equalsIgnoreCase("04"))
            return "April";
        if(key.equalsIgnoreCase("05"))
            return "May";
        if(key.equalsIgnoreCase("06"))
            return "June";
        if(key.equalsIgnoreCase("07"))
            return "July";
        if(key.equalsIgnoreCase("08"))
            return "August";
        if(key.equalsIgnoreCase("09"))
            return "September";
        if(key.equalsIgnoreCase("10"))
            return "October";
        if(key.equalsIgnoreCase("11"))
            return "November";
        return "December";
    }

    static String digitToAbbMonth(String key) {
        if (key.equalsIgnoreCase("01"))
            return "Jan";
        if (key.equalsIgnoreCase("02"))
            return "Feb";
        if (key.equalsIgnoreCase("03"))
            return "Mar";
        if (key.equalsIgnoreCase("04"))
            return "Apr";
        if (key.equalsIgnoreCase("05"))
            return "May";
        if (key.equalsIgnoreCase("06"))
            return "Jun";
        if (key.equalsIgnoreCase("07"))
            return "Jul";
        if (key.equalsIgnoreCase("08"))
            return "Aug";
        if (key.equalsIgnoreCase("09"))
            return "Sep";
        if (key.equalsIgnoreCase("10"))
            return "Oct";
        if (key.equalsIgnoreCase("11"))
            return "Nov";
        return "Dec";
    }

    static String borderMonths(String month) {
        if (month.equalsIgnoreCase("jan"))
            return "Dec Feb";
        if (month.equalsIgnoreCase("feb"))
            return "Jan Mar";
        if (month.equalsIgnoreCase("mar"))
            return "Feb Apr";
        if (month.equalsIgnoreCase("apr"))
            return "Mar May";
        if (month.equalsIgnoreCase("may"))
            return "Apr Jun";
        if (month.equalsIgnoreCase("jun"))
            return "May Jul";
        if (month.equalsIgnoreCase("jul"))
            return "Jun Aug";
        if (month.equalsIgnoreCase("aug"))
            return "Jul Sep";
        if (month.equalsIgnoreCase("sep"))
            return "Aug Oct";
        if (month.equalsIgnoreCase("oct"))
            return "Sep Nov";
        if (month.equalsIgnoreCase("nov"))
            return "Oct Dec";
        return "Nov Jan";
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public List<String> getJan() {
        return jan;
    }

    public void setJan(List<String> jan) {
        this.jan = jan;
    }

    public List<String> getFeb() {
        return feb;
    }

    public void setFeb(List<String> feb) {
        this.feb = feb;
    }

    public List<String> getMar() {
        return mar;
    }

    public void setMar(List<String> mar) {
        this.mar = mar;
    }

    public List<String> getApr() {
        return apr;
    }

    public void setApr(List<String> apr) {
        this.apr = apr;
    }

    public List<String> getMay() {
        return may;
    }

    public void setMay(List<String> may) {
        this.may = may;
    }

    public List<String> getJun() {
        return jun;
    }

    public void setJun(List<String> jun) {
        this.jun = jun;
    }

    public List<String> getJul() {
        return jul;
    }

    public void setJul(List<String> jul) {
        this.jul = jul;
    }

    public List<String> getAug() {
        return aug;
    }

    public void setAug(List<String> aug) {
        this.aug = aug;
    }

    public List<String> getSep() {
        return sep;
    }

    public void setSep(List<String> sep) {
        this.sep = sep;
    }

    public List<String> getOct() {
        return oct;
    }

    public void setOct(List<String> oct) {
        this.oct = oct;
    }

    public List<String> getNov() {
        return nov;
    }

    public void setNov(List<String> nov) {
        this.nov = nov;
    }

    public List<String> getDec() {
        return dec;
    }

    public void setDec(List<String> dec) {
        this.dec = dec;
    }
}