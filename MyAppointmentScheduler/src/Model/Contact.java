package Model;

import DAO.ModelImpl;

import java.sql.SQLException;

public class Contact {
    private int contact_id;
    private String name;
    private String email;

    public Contact(int contact_id, String name, String email) {
        this.contact_id = contact_id;
        this.name = name;
        this.email = email;
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static Contact getContactById(int id) throws SQLException {
        for (Contact c : ModelImpl.getAllContacts())
        {
            if (id == c.getContact_id()){
                return c;
            }
        }
        return null;
    }

    @Override
    public String toString()
    {
        return (name);
    }
}
