package Client;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;

public class User {

    private String username;
    private String name;
    private String lastName;
    private String email;
    private LocalDate birth;
    private String password;
    public LinkedList<Team> myTeams = new LinkedList<>();


    public String getPassword() {
        return password;
    }

    private LinkedList<Task> tasks = new LinkedList<>();

    public User(String username) {
        this.username = username;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * @param username Username
     * @param name Name
     * @param lastName LastName
     * @param email Email
     * @param date Age
     */
    public User(String username, String name, String lastName, String email, LocalDate date, String password) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.birth = date;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    // registerUser:email='email',username='username',password='password',name='name',lastname='lastname',birth='birth'
    @Override
    public String toString() {
        return "email='" + getEmail() + "',username='" + getUsername() + "',password='" + password + "',name='" + getName() + "',lastname='" + getLastName() + "',birth='" + getBirth() + "'";
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }
}
