package com.template.webserver.service.interfaces;

import com.template.flows.model.AccountIdAndPassword;
import com.template.flows.model.CommercialBankAccountInfo;
import com.template.model.commercialBank.CommercialBankData;
public interface CommercialBankInterface {

    AccountIdAndPassword create(CommercialBankAccountInfo commercialBankAccountInfo);
    CommercialBankData getCommercialBankById(String commercialBankAccountId);

    AccountIdAndPassword update(CommercialBank commercialBank, String commercialBankAccountId);

    int deactivate(String commercialBankAccountId);

    List<StateAndRef<CommercialBankStoreState>> getAll();

}
