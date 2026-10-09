package org.example.validation;

import org.example.exception.BusinessException;
import org.example.exception.ErrorCode;
import org.example.model.AbstractBuildingConsumptionInput;
import org.example.model.BuildingConsumptionInput;
import org.example.model.HeatBuildingConsumptionInput;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class EsrConsumptionValidator {

    private static final int QUANTITY_PRECISION = 22;
    private static final int QUANTITY_SCALE = 10;

    public void validate(AbstractBuildingConsumptionInput input) {
        switch (input) {
            case HeatBuildingConsumptionInput heat -> validate(heat);
            case BuildingConsumptionInput building -> validate(building);
            default -> throw new IllegalStateException("Unknown input type: " + input.getClass());
        }
    }

    private void validate(BuildingConsumptionInput input) {
        validateBase(input.getQuantity(), input.getStart(), input.getEnd());
    }

    private void validate(HeatBuildingConsumptionInput input) {
        validateBase(input.getQuantity(), input.getStart(), input.getEnd());
        if (input.getIsWeatherAdjusted() == null) {
            throw new BusinessException(ErrorCode.MISSING_REQUIRED_FIELD);
        }
    }

    private void validateBase(String quantity, LocalDate start, LocalDate end) {
        if (quantity == null || start == null || end == null) {
            throw new BusinessException(ErrorCode.MISSING_REQUIRED_FIELD);
        }
        if (end.isBefore(start)) {
            throw new BusinessException(ErrorCode.END_BEFORE_START);
        }
        validateQuantity(quantity);
    }

    private void validateQuantity(String quantity) {
        BigDecimal value;
        try {
            value = new BigDecimal(quantity.trim());
        } catch (NumberFormatException ex) {
            throw new BusinessException(ErrorCode.INVALID_FIELD_FORMAT);
        }

        int fractionDigits = value.scale();
        int integerDigits = value.precision() - value.scale();
        if (fractionDigits > QUANTITY_SCALE || integerDigits > QUANTITY_PRECISION - QUANTITY_SCALE) {
            throw new BusinessException(ErrorCode.INVALID_FIELD_FORMAT);
        }

        if (value.signum() <= 0) {
            throw new BusinessException(ErrorCode.CONSUMPTION_NOT_POSITIVE);
        }
    }
}
