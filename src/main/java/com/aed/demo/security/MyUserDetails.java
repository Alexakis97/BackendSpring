package com.aed.demo.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.aed.demo.entity.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetails implements UserDetails {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String userName;
    private String password;
    private Integer isActive;

    private List<GrantedAuthority> authorities;

    public MyUserDetails(User user) {

        System.out.println("---Data From DB---");
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println("---Data From DB---");
        System.out.println();

        this.userName = user.getUsername();
        this.password = user.getPassword();
        this.isActive = user.getIsActive();

        this.authorities = Arrays.stream(user.getRoles().split(",")).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // CHECK IF USER IS FIRST TIME LOGGED IN OR NOT
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // CHECK IF USERS SUBSCRIPTION IS ACTIVE OR NOT
    @Override
    public boolean isEnabled() {
        System.out.println("IS ACTIVE = " + isActive);

   
        // 0 = ACOUNT BANNED
        if(isActive==0)
        {
           return false; //MEANS SUBSCRIPTION IS NOT ACTIVE SO REDIRECT TO PLANS
        }
        //1 = SUBCRIPTION ACTIVE | 2 = FIRST LOGIN | 3 = SUBSCRIPTION INACTIVE
        else if (isActive==1 || isActive==2 || isActive==3 ) 
        {
            return true; //MEANS SUBSCRIPTION IS ACTIVE REDIRECT TO MAIN APP
        }
        return false;   
    }

  




}