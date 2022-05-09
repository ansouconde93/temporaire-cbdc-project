package com.template.webserver.controllers;

import com.template.flows.model.AccountIdAndPassword;
import com.template.flows.model.EndUserAccountInfo;
import com.template.flows.model.NewEndUserAccount;
import com.template.flows.model.SuspendOrActiveOrSwithAccountType;
import com.template.model.endUser.EndUserData;
import com.template.webserver.model.EndUserUpdateModel;
import com.template.webserver.service.interfaces.EndUserInterface;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *  End user API endpoints.
 */

@Api(description="End User API endpoints")
@RestController
public class EndUserController {
    //private final CordaRPCOps proxy;
    private final static Logger logger = LoggerFactory.getLogger(EndUserController.class);

    @Autowired
    private EndUserInterface endUserInterface;

    @ApiOperation(value = "Endpoint: /enduser/save ajoute un utilisateur final dans le réseau. Il retourne: un objet {AccountId, password } si ajout reussit et sinon null.")
    @PostMapping("/enduser/save")
    public AccountIdAndPassword save(@RequestBody EndUserAccountInfo endUserAccountInfo) {
      return endUserInterface.save(endUserAccountInfo);
    }

    @ApiOperation(value = "Endpoint: /enduser/saveotheraccount crée un autre compte d'un end user dans le réseau. Il retourne: un objet {AccountId, password } si ajout reussit et sinon null.")
    @PostMapping("/enduser/saveotheraccount")
    public AccountIdAndPassword createOtherEndUserCount(@RequestBody NewEndUserAccount newEndUserAccount){
        return endUserInterface.createOtherEndUserCount(newEndUserAccount);
    }

    @ApiOperation(value = "Endpoint: /enduser/update met à jour un utilisateur final dans le réseau. Il retourne : un objet {AccountId, password } et null sinon.")
    @PostMapping("/enduser/update")
    public AccountIdAndPassword update(@RequestBody EndUserUpdateModel endUserUpdateModel) {
        return endUserInterface.update(
                endUserUpdateModel.getEndUser(),endUserUpdateModel.getEndUserAccountId());
    }

    @ApiOperation(value = "Endpoint: /enduser/deleteoractiveorswithacountytype desactif le compte du end user dans le réseau. Il retourne un int: 1 desactivation reussie et 0 sinon . En cas desactivation reussie,un email est envoyer au responsable de la banque centrale.")
    @PostMapping("/enduser/deleteoractiveorswithacountytype")
    public int suspendOrActiveOrSwithAccountType(SuspendOrActiveOrSwithAccountType suspendOrActiveOrSwithAccountType) {
         return endUserInterface.suspendOrActiveOrSwithAccountType(suspendOrActiveOrSwithAccountType);
    }

    @ApiOperation(value = "Endpoint: /enduser/read lue un utilisateur final du réseau. Il retourne null en cas de lecture non reussie")
    @PostMapping("/enduser/read")
    public EndUserData getEndUser(@RequestBody String endUserAccountId) {
        return endUserInterface.read(endUserAccountId);
    }
}