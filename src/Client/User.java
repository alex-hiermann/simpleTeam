package Client;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Class for the User
 * <p>
 * Created and modified by Burger Maximilian and Hiermann Alexander.
 * Please consider correct usage of the LICENSE.
 */
public class User {

    /**
     * The Users username
     */
    private String username = "";

    /**
     * The Users name
     */
    private String name = "";

    /**
     * The Users last name
     */
    private String lastName = "";

    /**
     * The Users email
     */
    private String email;

    /**
     * The Users birth
     */
    private LocalDate birth;

    /**
     * The Users password (encrypted)
     */
    private String password = "";

    /**
     * The Users teams
     */
    public LinkedList<Team> myTeams = new LinkedList<>();

    /**
     * The Users unique id
     */
    private int id = 0;

    /**
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * The Users tasks
     */
    private LinkedList<Task> tasks = new LinkedList<>();

    /**
     * constructor to create a user with its email
     *
     * @param email email of the user
     */
    public User(String email) {
        this.email = email;
    }

    /**
     * constructor to create a user with its email and password
     *
     * @param email    email of the user
     * @param password password of the user
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * @return unique user id
     */
    public int getId() {
        return id;
    }

    /**
     * sets unique user id
     *
     * @param id unique user id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * constructor to create a "normally" used user with all of possible inputs but without the id
     *
     * @param username Username
     * @param name     Name
     * @param lastName LastName
     * @param email    Email
     * @param date     Age
     */
    public User(String username, String name, String lastName, String email, LocalDate date, String password) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.birth = date;
        this.password = password;
    }

    /**
     * @param o Other
     * @return Whether or not the email equals o.email
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    // registerUser:email='email',username='username',password='password',name='name',lastname='lastname',birth='birth'

    /**
     * Returns a string conform for constructing a STRequest
     *
     * @return STRequest conform string
     */
    @Override
    public String toString() {
        return "email=ꠦ" + getEmail() + "ꠦ,username=ꠦ" + getUsername() + "ꠦ,password=ꠦ" + password + "ꠦ,name=ꠦ" + getName() + "ꠦ,lastname=ꠦ" + getLastName() + "ꠦ,birth=ꠦ" + getBirth() + "ꠦ,userId=ꠦ" + getId() + "ꠦ";
    }


    /**
     * constructor to create a "normally" used user with all of possible inputs but without the id
     *
     * @param username Username
     * @param name     Name
     * @param lastName Lastname
     * @param email    Email
     * @param birth    Birth
     * @param password Password
     * @param id       Id
     */
    public User(String username, String name, String lastName, String email, LocalDate birth, String password, int id) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.birth = birth;
        this.password = password;
        this.id = id;
    }

    /**
     * adds a task to the users task list
     *
     * @param task new task to be assigned
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * @return username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * sets the username
     *
     * @param username username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name
     *
     * @param name name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return lastname of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * sets the lastname
     *
     * @param lastName lastname of the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * sets the email
     *
     * @param email name of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return birth date of the user
     */
    public LocalDate getBirth() {
        return birth;
    }

    /**
     * sets the birth
     *
     * @param birth birth date of the user
     */
    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }
}
