package com.example.projectserver.uniserver.controllers;

import com.example.projectserver.uniserver.User.User;
import com.example.projectserver.uniserver.User.UserDAO;
import com.example.projectserver.uniserver.exception.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserDAO userDAO;
    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return (List<User>) userDAO.findAll();
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUsersById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        User user = userDAO
                        .findById(userId)
                        .orElseThrow(() -> {
                            return new ResourceNotFoundException("User not found on :: " + userId);
                        });
        return ResponseEntity.ok().body(user);
    }


    @PostMapping(value = "/signUp")
    public String newUser(@RequestBody String attributes, HttpServletResponse response){
            JSONObject jso = new JSONObject(attributes);
            User user = userDAO.findByUsername(jso.getString("username"));
            if(user!=null)  return "usernameexists";
            user = userDAO.findByEmail(jso.getString("email"));
            if (user!=null) return "emailexists";
            else {
                User finaluser = new User (jso.getString("username"), jso.getString("email"), jso.getString("password"));
                userDAO.save(finaluser);
                String generatedJWT = JWTBuilder(finaluser.getId(), finaluser.getUsername(), finaluser.getEmail(), finaluser.getPassword());
                response.addHeader("userid", finaluser.getId().toString());
                return generatedJWT;
            }

    }

    @PostMapping(value = "/login")
    public String login(@RequestBody String attributes, HttpServletResponse response){
        boolean emailExists = false;
        JSONObject jso = new JSONObject(attributes);
        User user = userDAO.findByUsernameOrEmail(jso.getString("email"), jso.getString("email"));
        if(user!=null){
            emailExists=true;
        }
        if(emailExists) {
            if(jso.get("password").equals(user.getPassword())) {
                String jws = JWTBuilder(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
                response.addHeader("userid", user.getId().toString());
                return jws;
            }
            else return "wrongpassword";
        }
        else return "noAccount";
    }
    private String JWTBuilder(Long id, String username, String email, String password) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");
        String jws = Jwts.builder()
                .signWith(secretKey)
                .setIssuer("Jay")
                .setSubject("Userdata")
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .claim("id", id)
                .claim("username", username)
                .claim("email", email)
                .claim("password", password)
                .setIssuedAt(Calendar.getInstance().getTime())
                .compact();
        return jws;
    }
    public static SecretKey secretKey = Keys.hmacShaKeyFor("lksadjsknvcsklavnwiroufhuipwuwhifuhkadkljnvrtshsgffgnlkjxzncvzxncvxzcnaldkfnaialknv".getBytes());
}
