package com.template.flows.commercialBankFlows;

import co.paralleluniverse.fibers.Suspendable;
import com.template.contracts.CommercialBankContract;
import com.template.flows.model.AccountIdAndPassword;
import com.template.model.CommercialBank;
import com.template.states.CommercialBankStoreState;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

import java.util.Arrays;

@InitiatingFlow
@StartableByRPC
public class CommercialBankUpdaterFlowInitiator extends FlowLogic<AccountIdAndPassword> {
    private CommercialBank commercialBank;
    private String commercialBankAccountId;
    private final ProgressTracker progressTracker = new ProgressTracker();

    public CommercialBankUpdaterFlowInitiator(CommercialBank commercialBank, String commercialBankAccountId) {
        this.commercialBank = commercialBank;
        this.commercialBankAccountId = commercialBankAccountId;
    }

    @Override
    public ProgressTracker getProgressTracker(){
        return progressTracker;
    }


    @Suspendable
    @Override
    public AccountIdAndPassword call() throws FlowException {
        SignedTransaction signedTransaction = getServiceHub().getValidatedTransactions().getTransaction(SecureHash.parse(commercialBankAccountId));
        try {
            CommercialBankStoreState bankStoreState = (CommercialBankStoreState)signedTransaction.toLedgerTransaction(getServiceHub()).getOutput(0);
            if (bankStoreState == null || bankStoreState.getCommercialBank() == null){
                return null;
            }
            if (getOurIdentity().getOwningKey().toString().equals(bankStoreState.getParticipants().get(0).getOwningKey().toString())){

                //Create the transaction components
                CommercialBankStoreState commercialBankStoreState = new CommercialBankStoreState( commercialBank,bankStoreState.getAddedBy(),bankStoreState.getOwner(), new UniqueIdentifier());
                Command<CommercialBankContract.Create> txCommand = new Command<>(
                        new CommercialBankContract.Create(), Arrays.asList(bankStoreState.getAddedBy().getOwningKey(),bankStoreState.getOwner().getOwningKey()));

                //Create a transaction builder and add the components
                final TransactionBuilder builder = new TransactionBuilder(signedTransaction.getNotary());
                builder.addOutputState(commercialBankStoreState);
                builder.addCommand(txCommand);
                //Verify if the transaction is valid
                builder.verify(getServiceHub());

                //Signing the transaction
                SignedTransaction signedTx = getServiceHub().signInitialTransaction(builder);

                //Creating a session with the other party
                FlowSession otherPartySession = initiateFlow(bankStoreState.getOwner());

                // Obtaining the counterparty's signature.
                SignedTransaction fullySignedTx = subFlow(new CollectSignaturesFlow(
                        signedTx, Arrays.asList(otherPartySession), CollectSignaturesFlow.tracker()));

                String compteId = subFlow(new FinalityFlow(fullySignedTx, otherPartySession)).getId().toString();
                String password = commercialBankStoreState.getLinearId().getId().toString();
                return new AccountIdAndPassword(compteId,password);
            }
            return null;
        }catch(Exception exception){
            return null;

        }
    }
}
