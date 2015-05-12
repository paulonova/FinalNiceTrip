package se.finalwork.nicetrip.domain;

public class Trip {

    public static final String TABLE = "trip";
    public static final String _ID = "_id";
    public static final String DESTINY = "destiny";
    public static final String ARRIVAL_DATE = "arrival_date";
    public static final String EXIT_DATE = "exit_date";
    public static final String BUDGET = "budget";
    public static final String NUMBER_PEOPLES = "number_peoples";
    public static final String TYPE_TRIP = "type_trip";
    public static final String[] COLUMNS = new String[]{
                                _ID, DESTINY, ARRIVAL_DATE, EXIT_DATE, TYPE_TRIP, BUDGET, NUMBER_PEOPLES};


    private Integer id;
    private String destiny;
    private String typeTrip;
    private String arrivalDate;
    private String exitDate;
    private Double budget;
    private Integer numberPeoples;
    private Integer actualTrip;

    // Constructor
    public Trip() {
    }

    // Constructor
    public Trip(Integer id, String destiny, String typeTrip, String arrivalDate, String exitDate, Double budget, Integer numberPeoples) {
        this.id = id;
        this.destiny = destiny;
        this.typeTrip = typeTrip;
        this.arrivalDate = arrivalDate;
        this.exitDate = exitDate;
        this.budget = budget;
        this.numberPeoples = numberPeoples;
    }


    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDestiny() {
        return destiny;
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
    }

    public String getTypeTrip() {
        return typeTrip;
    }

    public void setTypeTrip(String typeTrip) {
        this.typeTrip = typeTrip;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getExitDate() {
        return exitDate;
    }

    public void setExitDate(String exitDate) {
        this.exitDate = exitDate;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Integer getNumberPeoples() {
        return numberPeoples;
    }

    public void setNumberPeoples(Integer numberPeoples) {
        this.numberPeoples = numberPeoples;
    }

    public Integer getActualTrip() {
        return actualTrip;
    }

    public void setActualTrip(Integer actualTrip) {
        this.actualTrip = actualTrip;
    }
}
