package com.template.flows.commercialBankFlows;

import co.paralleluniverse.fibers.Suspendable;
import com.template.model.CommercialBank;
import com.template.states.CommercialBankStoreState;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.utilities.ProgressTracker;

import java.security.SignatureException;

@InitiatingFlow
@StartableByRPC
public class CommercialBankReadFlowInitiator extends FlowLogic<CommercialBank> {
    private String commercialBankAccountId;
    private final ProgressTracker progressTracker = new ProgressTracker();

    public CommercialBankReadFlowInitiator(String commercialBankAccountId) {
        this.commercialBankAccountId = commercialBankAccountId;
    }
    @Override
    public ProgressTracker getProgressTracker(){
        return progressTracker;
    }

    @Override
    @Suspendable
    public CommercialBank call() throws FlowException {
        SignedTransaction signedTransaction = getServiceHub().getValidatedTransactions().getTransaction(SecureHash.parse(commercialBankAccountId));
        try {
            CommercialBankStoreState commercialBankStoreState = (CommercialBankStoreState) signedTransaction.toLedgerTransaction(getServiceHub()).getOutput(0);
            if(commercialBankStoreState == null || commercialBankStoreState.getCommercialBank() ==null ){
                return null;
            }
            if (getOurIdentity().getOwningKey().toString().equals(commercialBankStoreState.getParticipants().get(0).getOwningKey().toString())){
                return commercialBankStoreState.getCommercialBank();
            }

        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
