package com.foodapp.foodapp.common;

import com.foodapp.foodapp.order.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public interface SearchParamsInterface {
    default public List<OrderStatus> getOrderStatuses() {
        return null;
    }

    default public String getName() {
        return null;
    }

    default public BigDecimal getPrice() {
        return null;
    }

    default public String getDescription() {
        return null;
    }

    default public List<Sort> getSorts() {
        return null;
    }

    default public int getPage() {
        return 0;
    }

    default public int getSize() {
        return 0;
    }

    default public Long getCompanyId() {
        return null;
    }
}
