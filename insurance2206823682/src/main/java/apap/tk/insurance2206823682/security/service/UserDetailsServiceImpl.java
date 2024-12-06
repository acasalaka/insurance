package apap.tk.insurance2206823682.security.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import apap.tk.insurance2206823682.restdto.response.BaseResponseDTO;
import apap.tk.insurance2206823682.restdto.response.UserResponseDTO;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final WebClient webClient;

    public UserDetailsServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8084/api")
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<?> userOptional = Optional.empty();
        String role = null;
        String password = "";

        var endUserData = webClient.get()
                .uri(String.format("/user/detail/" + email))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<UserResponseDTO>>() {
                })
                .block();

        UserResponseDTO endUserResponseDTO = endUserData.getData();
        System.out.println(endUserResponseDTO);

        if (endUserResponseDTO != null) {
            password = endUserResponseDTO.getPassword();
            System.out.println(endUserResponseDTO.getPassword());
            System.out.println(endUserResponseDTO.getUsername());
            role = endUserResponseDTO.getRole().toUpperCase();

            return createUserDetails(email, password, role);
        }
        throw new UsernameNotFoundException("User not found with email: " + email);
    }

    private UserDetails createUserDetails(String username, String password, String role) {
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        grantedAuthoritySet.add(new SimpleGrantedAuthority(role));

        return new User(username, password, grantedAuthoritySet);

    }

}
