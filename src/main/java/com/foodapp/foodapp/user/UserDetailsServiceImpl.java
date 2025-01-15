package com.foodapp.foodapp.user;

import com.foodapp.foodapp.administration.userAdministration.UsersSearchParams;
import com.foodapp.foodapp.advice.BusinessException;
import com.foodapp.foodapp.auth.activationToken.ActivationTokenConfirmationService;
import com.foodapp.foodapp.common.SearchParams;
import com.foodapp.foodapp.common.UsersPagedResult;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final ActivationTokenConfirmationService tokenConfirmationService;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nieprawidłowe dane logowania"));
    }

    @SneakyThrows
    public User loadUserByUsername2(final String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new BusinessException("Nieprawidłowe dane logowania"));
    }

    @SneakyThrows
    public String registerUser(final User user) {
        boolean userExists = userRepository.findByEmail(user.getUsername()).isPresent();
        if (userExists) {
            throw new BusinessException("Podany email jest juz zajęty");
        }
        userRepository.save(user);
        return tokenConfirmationService.initTokenConfirmation(user);
    }

    public void updatePassword(final User user, final String newPassword) {
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<UserDto> getDtoUsers() {
        return UserMapper.toUsersDto(getUsers());
    }

    public List<UserDto> getDtoUsers(final Long companyId) {
        return UserMapper.toUsersDto(userRepository.findByCompanyId(companyId));
    }

    public UsersPagedResult getDtoUsersBySearchParams(final UsersSearchParams searchParams) {
        return userRepository.searchOrders(searchParams);
    }

    public List<UserDto> getDtoUsersNotBelongToCompany(final Long companyId) {
        return UserMapper.toUsersDto(userRepository.findUsersNotBelongingToCompany(companyId));
    }
}
