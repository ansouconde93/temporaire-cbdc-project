package com.template.webserver.controller;

import com.template.flows.model.AccountIdAndPassword;
import com.template.flows.model.CommercialBankAccountInfo;
import com.template.model.commercialBank.CommercialBankData;
import com.template.webserver.model.CommercialBankUpdateModel;
import com.template.webserver.service.interfaces.CommercialBankInterface;
import io.swagger.annotations.Api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:4200")
@Api(description ="Commercial bank API endpoints")
@RestController
public class CommercialBankController {

    private static final Logger logger = LoggerFactory.getLogger(CommercialBankController.class);


    @Autowired
    private CommercialBankInterface commercialBankInterface;

    @PostMapping("/commercialbank/create")
    public AccountIdAndPassword create(@RequestBody CommercialBankAccountInfo commercialBankAccountInfo){
        return commercialBankInterface.create(commercialBankAccountInfo);
    }

    @GetMapping("/commercialbank/{commercialBankAccountId}")
    public CommercialBankData getCommercialBank(@PathVariable(value = "commercialBankAccountId") String commercialBankAccountId){
        return commercialBankInterface.getCommercialBankById(commercialBankAccountId);
    }
    @GetMapping("/commercialbank/all")
    public List<StateAndRef<CommercialBankStoreState>> getAll(){
        return commercialBankInterface.getAll();
    }

    @PostMapping("/commercialbank/update")
    public AccountIdAndPassword update(@RequestBody CommercialBankUpdateModel commercialBankUpdateModel){
        return commercialBankInterface.update(
                commercialBankUpdateModel.getCommercialBank(),commercialBankUpdateModel.getCommercialBankAccountId());
    }

    @PostMapping("/commercialbank/deactivate")
    public int deactivate(@RequestBody String commercialBankAccountId){
        return commercialBankInterface.deactivate(commercialBankAccountId);
    }




}
