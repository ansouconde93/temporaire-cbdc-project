package com.template.webserver.service.interfaces;

import com.template.flows.model.AccountIdAndPassword;
import com.template.flows.model.EndUserAccountInfo;
import com.template.flows.model.NewEndUserAccount;
import com.template.flows.model.SuspendOrActiveOrSwithAccountType;
import com.template.model.endUser.EndUserData;

public interface EndUserInterface {
    AccountIdAndPassword save(EndUserAccountInfo endUserAccountInfo);
    AccountIdAndPassword createOtherEndUserCount(NewEndUserAccount newEndUserAccount);
    AccountIdAndPassword update(EndUserData endUser, String endUserAccountId);
    int suspendOrActiveOrSwithAccountType(SuspendOrActiveOrSwithAccountType suspendOrActiveOrSwithAccountType);
    EndUserData read(String endUserAccountId);

}
