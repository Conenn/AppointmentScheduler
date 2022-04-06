package Model;

public class Country {

    private int country_id;
    private String name;

    public Country(int country_id, String name) {
        this.country_id = country_id;
        this.name = name;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return (name);
    }
}
