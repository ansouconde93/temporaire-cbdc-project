package com.template.webserver.model;


import com.template.model.CommercialBank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.corda.core.serialization.CordaSerializable;

@Data
@NoArgsConstructor
@CordaSerializable
public class CommercialBankUpdateModel {
    private CommercialBank commercialBank;
    private String commercialBankAccountId;

    public CommercialBankUpdateModel(CommercialBank commercialBank, String commercialBankAccountId) {
        this.commercialBank = commercialBank;
        this.commercialBankAccountId = commercialBankAccountId;
    }
}
