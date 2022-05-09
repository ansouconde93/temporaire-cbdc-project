package com.template.webserver.service.interfaces;

import com.template.flows.model.*;
import com.template.model.merchant.MerchantData;

import javax.mail.MessagingException;
import java.io.IOException;

public interface MerchantInterface {
    AccountIdAndPassword create(MerchantAccountInfo merchantAccountInfo);

    MerchantData getMerchantById(String merchantAccountId);

    AccountIdAndPassword update(MerchantData merchantData, String merchantAccountId);

    int sendEmailVerification(String email) throws MessagingException, IOException;

    int suspendOrActiveOrSwithAccountType(SuspendOrActiveOrSwithAccountType suspendOrActiveOrSwithAccountType);
    AccountIdAndPassword createOtherMerchantAccount(NewMerchantAccount newMerchantAccount);

}
