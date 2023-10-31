package com.week12.ecommerce.security;

import com.week12.ecommerce.model.Users;
import com.week12.ecommerce.repository.UsersRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UsersRepository usersRepository;

    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.print("username="+username);
        Users users = usersRepository.findByEmail(username);
        System.out.println(users);
        if(users != null){
            if(users.isBlocked()){
                throw new UsernameNotFoundException("temporarily blocked this user....");
            }else{
                List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
                grantedAuthorityList.add(new SimpleGrantedAuthority(users.getRole()));
                return new CustomUser(users.getEmail(),users.getPassword(),grantedAuthorityList,users.getId(),
                        users.getFirstName(),users.getLastName(),users.getPhoneNumber(),
                        users.isDelete(),users.isBlocked(),users.isActive(),
                        users.getCreatedAt(),users.getUpdateOn());
            }

        }else{
             throw new UsernameNotFoundException("Invalid Username or password");
        }
    }
}
