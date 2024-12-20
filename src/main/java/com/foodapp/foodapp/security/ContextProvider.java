package com.foodapp.foodapp.security;

import com.foodapp.foodapp.administration.company.Company;
import com.foodapp.foodapp.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ContextProvider {
    public static final Long HOLDING_ID = -888L;

    public Optional<User> getUser() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

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

    public List<Long> getCompanyIdList() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return user.getCompanies().stream()
                    .map(Company::getId)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    public boolean isHolding(final List<Long> companyIds) {
        return companyIds.size() == 1 && HOLDING_ID.equals(companyIds.get(0));
    }

    public void validateCompanyAccess(final List<Long> companyIdsToCheck) {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            var companyIds = getCompanyIdList();
            if (!companyIds.containsAll(companyIdsToCheck)) {
                throw new SecurityException("No access to provided company");
            }
        }
    }

    public void validateCompanyAccessWithHolding(final List<Long> companyIds) {
        if (!isHolding(companyIds)){
            validateCompanyAccess(companyIds);
        }
    }

    public List<Long> getCompanyIdsWithHolding(final List<Long> companyIds) {
        if (isHolding(companyIds)) {
            return getCompanyIdList();
        }
        validateCompanyAccess(companyIds);
        return companyIds;
    }
}
