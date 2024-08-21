package com.foodapp.foodapp.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Content implements Serializable {
    @JdbcTypeCode(SqlTypes.JSON)
    private OpenHours openHours;
}
