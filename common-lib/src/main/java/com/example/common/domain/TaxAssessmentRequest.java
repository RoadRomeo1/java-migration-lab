package com.example.common.domain;

import com.example.common.enums.TaxRegime;

public record TaxAssessmentRequest(
        Person person,
        TaxRegime regime) {
}
