package com.foodapp.foodapp.delivery;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.security.ContextProvider;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DeliveryOptionService {
    private final DeliveryOptionRepository deliveryOptionRepository;
    private final ContextProvider contextProvider;
    private final CompanyRepository companyRepository;


    public List<DeliveryOptionDto> getAllDeliveryOptionByCompanyId(final Long companyId) {
        contextProvider.validateCompanyAccess(List.of(companyId));
        Sort sort = Sort.by(Sort.Order.asc("distance").nullsLast());
        var deliveryOptions = deliveryOptionRepository.findAllByCompanyId(companyId, sort);
        return DeliveryOptionMapper.toDeliveryOptionDto(deliveryOptions);
    }

    public DeliveryOptionDto saveDeliveryOption(final DeliveryOptionDto deliveryOptionDto, final Long companyId) {
        contextProvider.validateCompanyAccess(List.of(companyId));
        var company = companyRepository.findById(companyId)
                .orElseThrow(() -> new SecurityException("Wrong company Id"));
        deliveryOptionDto.setId(null);
        var deliveryOption =
                deliveryOptionRepository.save(DeliveryOptionMapper.toDeliveryOption(deliveryOptionDto, company));
        return DeliveryOptionMapper.toDeliveryOptionDto(deliveryOption);
    }

    public DeliveryOptionDto modifyDeliveryOption(final DeliveryOptionDto deliveryOptionDto, final Long companyId) {
        contextProvider.validateCompanyAccess(List.of(companyId));
        var company = companyRepository.findById(companyId)
                .orElseThrow(() -> new SecurityException("Wrong company Id"));
        validate(deliveryOptionDto.getId(), company);
        var deliveryOption =
                deliveryOptionRepository.save(DeliveryOptionMapper.toDeliveryOption(deliveryOptionDto, company));
        return DeliveryOptionMapper.toDeliveryOptionDto(deliveryOption);
    }


    public void deleteDeliveryOption(final Long deliveryOptionId, final Long companyId) {
        contextProvider.validateCompanyAccess(List.of(companyId));
        var company = companyRepository.findById(companyId)
                .orElseThrow(() -> new SecurityException("Wrong company Id"));
        validate(deliveryOptionId, company);
        deliveryOptionRepository.deleteById(deliveryOptionId);

    }

    private void validate(final Long deliveryOptionId, final Company company) {
        if (CollectionUtils.isEmpty(company.getDeliveryOptions())) {
            throw new SecurityException("Wrong company id in validation delivery option");
        }
        var deliveryOptionIds = company.getDeliveryOptions().stream()
                .map(DeliveryOption::getId)
                .collect(Collectors.toSet());
        if (!deliveryOptionIds.contains(deliveryOptionId)) {
            throw new SecurityException("Wrong delivery option id in validation");
        }
    }
}
