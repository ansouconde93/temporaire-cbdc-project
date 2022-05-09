package com.template.webserver.service.implementations;

import com.template.flows.commercialBankFlows.CommercialBankCreatorFlowInitiator;
import com.template.flows.commercialBankFlows.CommercialBankDeactivateFlowInitiator;
import com.template.flows.commercialBankFlows.CommercialBankReadFlowInitiator;
import com.template.flows.commercialBankFlows.CommercialBankUpdaterFlowInitiator;
import com.template.flows.model.AccountIdAndPassword;
import com.template.flows.model.CommercialBankAccountInfo;
import com.template.webserver.NodeRPCConnection;
import com.template.webserver.emailSender.Email;
import com.template.webserver.emailSender.EmailSender;
import com.template.webserver.service.interfaces.CommercialBankInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class CommercialBankInterfaceImpl implements CommercialBankInterface {

    @Autowired
    private NodeRPCConnection nodeRPCConnection;


    @Override
    public AccountIdAndPassword create(CommercialBankAccountInfo commercialBankAccountInfo) {
        try {
            AccountIdAndPassword compteIdAndPassword =  nodeRPCConnection.proxy.startTrackedFlowDynamic(
                    CommercialBankCreatorFlowInitiator.class, commercialBankAccountInfo).getReturnValue().get();
            if (compteIdAndPassword != null){
                Email email = new Email();
                email.setMailFrom("cbdc.talan@gmail.com");
                email.setMailFromPassword("cbdctalan2022");
                email.setMailTo(commercialBankAccountInfo.getCommercialBankData().getEmail());
                email.setMailSubject("Ajout de vos données dans le système");

                String content = "Bonjour Chèr(e) responsable de la banque "+commercialBankAccountInfo.getCommercialBankData().getName()+"<br>"
                        +"Vous venez d'intégrer le nouveau système monétaire numérique, dont " +
                        "nous vous en remercions.<br> "
                        +"Désormais, vous pouvez accéder à votre wallet numérique en "
                        +"<h3><a href=\"wallet_link_here\" target=\"_self\">cliquant ici</a></h3>"
                        + "Merci d'utiliser ces informations pour vos différentes actions sur la plateforme.<br>"
                        + "Numéro de compte : <b style=\"color:rgb(255,0,0);\"> "+ compteIdAndPassword.getCompteId() +"</b><br>"
                        + "Mot de passe : <b style=\"color:rgb(255,0,0);\">"+ compteIdAndPassword.getPassword() +"</b><br>"
                        + "Nous vous mercions à bien vouloir garder secrète ces informations. En cas de perte, contacter nous en"
                        + "<h4><a href=\"problem_link_here\" target=\"_self\">cliquant ici</a></h4>";
                email.setMailContent(content);

                //Send now email
                new EmailSender(email).sendmail();

            }
            return compteIdAndPassword;
        }catch (Exception exception){
            return null;
        }

    }

    @Override
    public CommercialBank getCommercialBankById(String commercialBankAccountId) {
        try {
            return nodeRPCConnection.proxy.startTrackedFlowDynamic(
                    CommercialBankReadFlowInitiator.class,commercialBankAccountId).getReturnValue().get();

        }catch (Exception exception){
            return null;
        }
    }

    public List<StateAndRef<CommercialBankStoreState>> getAll(){
        return nodeRPCConnection.proxy.vaultQuery(CommercialBankStoreState.class).getStates();
    }

    @Override
    public AccountIdAndPassword update(CommercialBank commercialBank, String commercialBankAccountId) {
        try {
            AccountIdAndPassword compteIdAndPassword = nodeRPCConnection.proxy.startTrackedFlowDynamic(
                    CommercialBankUpdaterFlowInitiator.class, commercialBank, commercialBankAccountId).getReturnValue().get();
            if (compteIdAndPassword != null){
                //send email
                Email email = new Email();
                email.setMailFrom("cbdc.talan@gmail.com");
                email.setMailFromPassword("cbdctalan2022");
                email.setMailTo(commercialBank.getEmail());
                email.setMailSubject("Mise à jour de vos données");

                String content = "Bonjour Chèr(e)"+commercialBank.getName()+"<br>"
                        +"Vos données viennent d'être mises à jour dans le système.<br>"
                        +"Desormais, vous allez utiliser votre wallet numérique avec : <br>"
                        +"Numéro de compte : <b style=\"color:rgb(255,0,0);\"> \"+ compteIdAndPassword.getCompteId() +\"</b><br>"
                        + "Mot de passe : <b style=\"color:rgb(255,0,0);\">\"+ compteIdAndPassword.getPassword() +\"</b><br>"
                        + "Nous vous mercions à bien vouloir garder secrète ces informations. En cas de perte, contacter nous en"
                        + "<h4><a href=\"problem_link_here\" target=\"_self\">cliquant ici</a></h4>" ;
                email.setMailContent(content);

                //Send now email
                new EmailSender(email).sendmail();

            }
            return compteIdAndPassword;


        }catch (Exception exception){
            return null;
        }
    }

    @Override
    public int deactivate(String commercialBankAccountId) {
        try {
            String deactivateCommercialBank = nodeRPCConnection.proxy.startTrackedFlowDynamic(
                    CommercialBankDeactivateFlowInitiator.class, commercialBankAccountId).getReturnValue().get();
            if (deactivateCommercialBank != null) {
                return 1;
            }
            return 0;
        } catch (Exception exception) {
            return 0;
        }
    }



}
