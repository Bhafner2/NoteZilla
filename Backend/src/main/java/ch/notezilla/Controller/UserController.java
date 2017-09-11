package ch.notezilla.Controller;

import ch.notezilla.model.Users;
import ch.notezilla.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping(path = "/Users")
public class UserController {

    @Autowired
    UserRepository userRepo;

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Users> getAllUsers() {
        return userRepo.findAll();

    }

    @GetMapping(value = "/all/{id}")
    public @ResponseBody
    Users getUserById(@PathVariable("id") int id) {
        return userRepo.findOne(id);
    }

    @DeleteMapping(value = "/delete/{id}")
    public @ResponseBody
    Users deleteUser(@PathVariable("id") int id) {
        userRepo.delete(id);
        return null;
    }

    @PatchMapping(value = "/edit/{id}")
    public @ResponseBody
    Users updateUser(@PathVariable("id") int id, String email, String password, String name) {
        Users user = userRepo.findOne(id);
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        userRepo.save(user);
        return userRepo.findOne(id);
    }

    @PutMapping(value = "/new/")
    public @ResponseBody
    Users addNewUser(String email, String password, String name) {
        Users user = new Users();
        user.setPassword(password);
        user.setEmail(email);
        user.setName(name);
        return userRepo.save(user);
    }

    @PutMapping(value = "/valid/")
    public @ResponseBody
    Users validUser(String email, String password) {
    for(Users user : userRepo.findAll()){
        if(user.getPassword() == password){
            return user;
        }
    }
    return null;
    }
}
