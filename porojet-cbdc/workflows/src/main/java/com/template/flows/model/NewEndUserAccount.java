package com.template.flows.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.corda.core.serialization.CordaSerializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@CordaSerializable
public class NewEndUserAccount {
    private String cin;
    private String endUserEmail;
    private String bankIndcation;
    private boolean suspend = false; // suspend = true the account is suspend, else false. false is default value
    private String accountType ="courant"; // accountType = epargne or courant. courant by default
}
