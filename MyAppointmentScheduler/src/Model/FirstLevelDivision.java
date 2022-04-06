package Model;

public class FirstLevelDivision {

    private int division_id;
    private String division;
    private int country_id_FK;

    public FirstLevelDivision(int division_id, String division, int country_id_FK) {
        this.division_id = division_id;
        this.division = division;
        this.country_id_FK = country_id_FK;
    }

    public int getDivision_id() {
        return division_id;
    }

    public void setDivision_id(int division_id) {
        this.division_id = division_id;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getCountry_id_FK() {
        return country_id_FK;
    }

    public void setCountry_id_FK(int country_id_FK) {
        this.country_id_FK = country_id_FK;
    }

    @Override
    public String toString()
    {
        return (division);
    }
}
