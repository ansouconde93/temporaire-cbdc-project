package com.template.webserver.controllers;

import com.template.flows.model.AccountIdAndPassword;
import com.template.flows.model.CentralBankAccountInfo;
import com.template.flows.model.NewCentralBankAccount;
import com.template.model.centralBank.CentralBankData;
import com.template.webserver.model.CentralBankUpdateModel;
import com.template.webserver.model.SuspendOrActiveOrSwithAccountTypeModel;
import com.template.webserver.service.interfaces.CentralBankInterface;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *  Central bank API endpoints.
 */

@Api(description="Central bank API endpoints")
@RestController
public class CentralBankController {
    //private final CordaRPCOps proxy;
    private final static Logger logger = LoggerFactory.getLogger(CentralBankController.class);

    @Autowired
    private CentralBankInterface centralBankInterface;

    @ApiOperation(value = "Endpoint: /centralbank/save créer le compte de la banque centrale dans le réseau. Il retourne: un objet {AccountId, password } si ajout reussit sinon null.")
    @PostMapping("/centralbank/save")
    public AccountIdAndPassword save(@RequestBody CentralBankAccountInfo centralBankAccountInfo) {
      return centralBankInterface.save(centralBankAccountInfo);
    }

    @ApiOperation(value = "Endpoint: /centralbank/saveotheraccount créer un autre compte de la banque centrale dans le réseau. Il retourne: un objet {AccountId, password } si ajout reussit sinon null.")
    @PostMapping("/centralbank/saveotheraccount")
    public AccountIdAndPassword createOtherBankCount(@RequestBody NewCentralBankAccount newCentralBankAccount){
        return centralBankInterface.createOtherBankCount(newCentralBankAccount);
    }

    @ApiOperation(value = "Endpoint: /centralbank/update met à jour le compte de la banque centrale dans le réseau. Il retourne : un objet {AccountId, password } et null sinon.")
    @PostMapping("/centralbank/update")
    public AccountIdAndPassword update(@RequestBody CentralBankUpdateModel centralBankUpdateModel) {
        return centralBankInterface.update(
                centralBankUpdateModel.getBanqueCentrale(),centralBankUpdateModel.getCentralBankAccountId());
    }

    @ApiOperation(value = "Endpoint: /centralbank/deleteOrActiveOrSwithAccountType desactif ou active ou change le type de compte le compte de la banque centrale dans le réseau. Il retourne un int: 1 desactivation reussie et 0 sinon . En cas desactivation reussie,un email est envoyer au responsable de la banque centrale.")
    @PostMapping("/centralbank/deleteOrActiveOrSwithAccountType")
    public int suspendOrActiveOrSwithAccountType(@RequestBody SuspendOrActiveOrSwithAccountTypeModel supendOrUpdateAccount) {
        return centralBankInterface.suspendOrActiveOrSwithAccountType(supendOrUpdateAccount);//boolean suspendFlag, String newAccountType
    }

    @ApiOperation(value = "Endpoint: /centralbank/read lue le compte de la banque centrale du réseau. Il retourne null en cas de lecture non reussie")
    @PostMapping("/centralbank/read")
    public CentralBankData getCentralBank(@RequestBody String centralBankAccountId) {
        return centralBankInterface.read(centralBankAccountId);
    }
}