package com.template.flows.commercialBankFlows;

import co.paralleluniverse.fibers.Suspendable;
import com.template.contracts.CommercialBankContract;
import com.template.model.CommercialBank;
import com.template.states.CommercialBankStoreState;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

import java.security.SignatureException;
import java.util.Arrays;

public class CommercialBankDeactivateFlowInitiator extends FlowLogic<String> {
    private String commercialBankAccountId;
    private Party addedBy;

    private final ProgressTracker progressTracker = new ProgressTracker();

    public CommercialBankDeactivateFlowInitiator(String commercialBankAccountId) {
        this.commercialBankAccountId = commercialBankAccountId;
    }
    @Override
    public ProgressTracker getProgressTracker(){
        return progressTracker;
    }


    @Override
    @Suspendable
    public String call() throws FlowException {

        SignedTransaction signedTransaction = getServiceHub().getValidatedTransactions().getTransaction(SecureHash.parse(commercialBankAccountId));

        try {
            //Set the party who will add the commercial bank account
            CordaX500Name name = new CordaX500Name("PartyA", "London", "GB");

            addedBy = getServiceHub().getNetworkMapCache().getNodeByLegalName(name).identityFromX500Name(name);
            //Retrieve the notary identity from the network map
            Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

            CommercialBankStoreState commercialBankStoreState = (CommercialBankStoreState) signedTransaction.toLedgerTransaction(getServiceHub()).getOutput(0);
            if (commercialBankStoreState == null || commercialBankStoreState.getCommercialBank() == null) {
                return null;
            }
            if (getOurIdentity().getOwningKey().toString().equals(commercialBankStoreState.getParticipants().get(0).getOwningKey().toString())) {
                commercialBankStoreState.getCommercialBank().setDeactivate(true);


                //Create the transaction components
                CommercialBankStoreState commercialBankState = new CommercialBankStoreState(commercialBankStoreState.getCommercialBank(), getOurIdentity(), addedBy, new UniqueIdentifier());

                Command command = new Command<>(new CommercialBankContract.Create(), Arrays.asList(getOurIdentity().getOwningKey(), addedBy.getOwningKey()));

                //Create a transaction builder and add the components
                TransactionBuilder builder = new TransactionBuilder(notary);
                builder.addOutputState(commercialBankState, CommercialBankContract.ID);
                builder.addCommand(command);
                //Verify if the transaction is valid
                builder.verify(getServiceHub());
                //Signing the transaction
                SignedTransaction signedTx = getServiceHub().signInitialTransaction(builder);

                //Creating a session with the other party
                FlowSession otherPartySession = initiateFlow(addedBy);
                // Obtaining the counterparty's signature.
                SignedTransaction fullySignedTx = subFlow(new CollectSignaturesFlow(
                        signedTx, Arrays.asList(otherPartySession), CollectSignaturesFlow.tracker()));
                //Finalise the transaction
                subFlow(new FinalityFlow(fullySignedTx, otherPartySession));
                return commercialBankStoreState.getCommercialBank().toString();
            }
            return null;
        }catch (SignatureException e){
            return null;
        }

    }
}
