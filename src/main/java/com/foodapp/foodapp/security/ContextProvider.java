package com.foodapp.foodapp.security;

import com.foodapp.foodapp.company.Company;
import com.foodapp.foodapp.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
public class ContextProvider {
    public Optional<Long> getUserId() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return Optional.ofNullable(user.getId());
        }
        return Optional.empty();
    }

    public Set<Company> getCompanyList() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return user.getCompanies();
        }
        return Set.of();
    }

    public void validateCompanyAccess(final Long companyId) {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            var companyIds = user.getCompanies().stream()
                    .map(Company::getId)
                    .toList();
            if (!companyIds.contains(companyId)) {
                throw new SecurityException("No access to provided company");
            }
        }
    }

}
