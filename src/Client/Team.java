package Client;

public class Team {

    String name;
    String description;
    private User admin;


    public Team(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "name='" + name + "',desc='" + description + "'";
    }

    public void setAdmin(User newAdmin) {
        this.admin = newAdmin;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
