package ch.notezilla.model;

import org.springframework.web.bind.annotation.CrossOrigin;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@CrossOrigin
public class Users {

    private int idUsers;
    private String email;
    private String password;
    private String name;

    public Users() {
    }

    @Id
    public int getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(int idUsers) {
        this.idUsers = idUsers;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
