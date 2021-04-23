package Client;

import java.util.LinkedList;
import java.util.Objects;

public class User {

    private String username;
    private String name;
    private String lastName;
    private String email;
    private String age;
    private String password;

    private LinkedList<Task> tasks = new LinkedList<>();

    public User(String username) {
        this.username = username;
    }

    /**
     * @param username Username
     * @param name Name
     * @param lastName LastName
     * @param email Email
     * @param age Age
     */
    public User(String username, String name, String lastName, String email, String age, String password) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.password = password;
    }

    // registerUser:email='email',username='username',password='password',name='name',lastname='lastname',age='age'


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }

    @Override
    public String toString() {
        return "email='" + getEmail() + "',username='" + getUsername() + "',password='" + password + "',name='" + getName() + "',lastname='" + getLastName() + "',age='" + getAge() + "'";
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
