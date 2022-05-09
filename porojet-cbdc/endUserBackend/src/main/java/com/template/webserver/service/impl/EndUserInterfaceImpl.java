package com.template.webserver.service.impl;

import com.template.flows.endUserFlows.*;
import com.template.flows.model.AccountIdAndPassword;
import com.template.flows.model.EndUserAccountInfo;
import com.template.flows.model.NewEndUserAccount;
import com.template.flows.model.SuspendOrActiveOrSwithAccountType;
import com.template.model.endUser.EndUserData;
import com.template.webserver.NodeRPCConnection;
import com.template.webserver.emailSender.EmailFromTo;
import com.template.webserver.emailSender.EmailSender;
import com.template.webserver.service.interfaces.EndUserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class EndUserInterfaceImpl implements EndUserInterface {

    @Autowired
    private NodeRPCConnection nodeRPCConnection;

    @Override
    public AccountIdAndPassword save(EndUserAccountInfo endUserAccountInfo) {
        try {
            AccountIdAndPassword compteIdAndPassword  = nodeRPCConnection.proxy.startTrackedFlowDynamic(
                    EndUserCreatorFlowInitiator.class,endUserAccountInfo).getReturnValue().get();
            if (compteIdAndPassword != null){
                //envoyer le mail de creation
                EmailFromTo emailFromTo = new EmailFromTo();
                emailFromTo.setEmailFrom("cbdc.talan@gmail.com");
                emailFromTo.setEmailFromPassWord("cbdctalan2022");
                emailFromTo.setEmailReceiver(endUserAccountInfo.getEndUserData().getEmail());
                emailFromTo.setEmailSubject("Ajout de vos données et création du compte dans le système");

                String content = "Bonjour Chèr(e) "+endUserAccountInfo.getEndUserData().getNom()+". <br> "
                        + "Vous venez d'intégrer le nouveau système monétaire numérique, dont nous vous en remercions.<br>"
                        + "Desormais, vous pouvez acceder à votre wallet numérique en "
                        + "<h3><a href=\"wallet_link_here\" target=\"_self\">cliquant ici</a></h3>"
                        + "Merci d'utiliser ces informations pour vos différentes actions sur la plateforme.<br>"
                        + "Numéro de compte : <b style=\"color:rgb(255,0,0);\"> "+ compteIdAndPassword.getCompteId() +"</b><br>"
                        + "Mot de passe : <b style=\"color:rgb(255,0,0);\">"+ compteIdAndPassword.getPassword() +"</b><br>"
                        + "Nous vous mercions à bien vouloir garder secrète ces informations. En cas de perte, contacter nous en"
                        + "<h4><a href=\"problem_link_here\" target=\"_self\">cliquant ici</a></h4>";
                emailFromTo.setEmailContent(content);

                //send email now
                new EmailSender(emailFromTo).sendmail();
            }
            return compteIdAndPassword;
        }catch (Exception exception){
            return null;
        }
    }

    @Override
    public AccountIdAndPassword createOtherEndUserCount(NewEndUserAccount newEndUserAccount) {
        try {
            AccountIdAndPassword compteIdAndPassword  = nodeRPCConnection.proxy.startTrackedFlowDynamic(
                    EndUserOtherAccountCreatorFlowInitiator.class,newEndUserAccount).getReturnValue().get();

            if (compteIdAndPassword != null){
                //envoyer le mail de creation
                EmailFromTo emailFromTo = new EmailFromTo();
                emailFromTo.setEmailFrom("cbdc.talan@gmail.com");
                emailFromTo.setEmailFromPassWord("cbdctalan2022");
                emailFromTo.setEmailReceiver(newEndUserAccount.getEndUserEmail());
                emailFromTo.setEmailSubject("Création d'un autre compte dans le système");

                String content = "Bonjour chèr(e) client(e) "+newEndUserAccount.getCin()+". <br>"
                        + "Vous venez de créer un autre compte le nouveau système monétaire numérique, dont nous vous en remercions.<br>"
                        + "Desormais, vous pouvez acceder à votre wallet numérique en "
                        + "<h3><a href=\"wallet_link_here\" target=\"_self\">cliquant ici</a></h3>"
                        + "Merci d'utiliser ces informations pour vos différentes actions sur la plateforme.<br>"
                        + "Numéro de compte : <b style=\"color:rgb(255,0,0);\"> "+ compteIdAndPassword.getCompteId() +"</b><br>"
                        + "Mot de passe : <b style=\"color:rgb(255,0,0);\">"+ compteIdAndPassword.getPassword() +"</b><br>"
                        + "Nous vous mercions à bien vouloir garder secrète ces informations. En cas de perte, contacter nous en"
                        + "<h4><a href=\"problem_link_here\" target=\"_self\">cliquant ici</a></h4>";
                emailFromTo.setEmailContent(content);

                //send email now
                new EmailSender(emailFromTo).sendmail();

                return compteIdAndPassword;
            }
            return null;
        }catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public AccountIdAndPassword update(EndUserData endUser, String endUserAccountId) {
        try {
            AccountIdAndPassword compteIdAndPassword = nodeRPCConnection.proxy.startTrackedFlowDynamic(
                    EndUserUpdaterFlowInitiator.class,endUser,endUserAccountId).getReturnValue().get();
            if (compteIdAndPassword != null){
                //envoyer le mail de creation
                EmailFromTo emailFromTo = new EmailFromTo();
                emailFromTo.setEmailFrom("cbdc.talan@gmail.com");
                emailFromTo.setEmailFromPassWord("cbdctalan2022");
                emailFromTo.setEmailReceiver(endUser.getEmail());
                emailFromTo.setEmailSubject("Mise à jour de vos données");

                String content = "Bonjour Chèr(e) "+endUser.getNom()+"<br>"
                        + "Vos données viennent d'être mises à jour dans le système . <br>"
                        + "Desormais, vous allez utiliser votre wallet numérique avec : <br>"
                        + "Numéro de compte : <b style=\"color:rgb(255,0,0);\"> "+ compteIdAndPassword.getCompteId() +"</b><br>"
                        + "Mot de passe : <b style=\"color:rgb(255,0,0);\">"+ compteIdAndPassword.getPassword() +"</b><br>"
                        + "Nous vous mercions à bien vouloir garder secrète ces informations. En cas de perte, contacter nous en"
                        + "<h4><a href=\"problem_link_here\" target=\"_self\">cliquant ici</a></h4>";
                emailFromTo.setEmailContent(content);

                //send email now
                new EmailSender(emailFromTo).sendmail();
            }
            return compteIdAndPassword;

        }catch (Exception exception){
            return null;
        }
    }


    @Override
    public int suspendOrActiveOrSwithAccountType(SuspendOrActiveOrSwithAccountType suspendOrActiveOrSwithAccountType) {

        try {
            String suspendEndUserEmail = nodeRPCConnection.proxy.startTrackedFlowDynamic(
                    EndUserSuspendOrActiveOrSwithAccountTypeFlowInitiator.class,suspendOrActiveOrSwithAccountType).getReturnValue().get();
            if (suspendEndUserEmail != null){
                //envoyer le mail de creation
                EmailFromTo emailFromTo = new EmailFromTo();
                emailFromTo.setEmailFrom("cbdc.talan@gmail.com");
                emailFromTo.setEmailFromPassWord("cbdctalan2022");
                emailFromTo.setEmailReceiver(suspendEndUserEmail);
                String activeOrDesactiveOrSwithAccountType;
                if (suspendOrActiveOrSwithAccountType.getNewAccountType()!=null){
                    emailFromTo.setEmailSubject("Changement de type de votre compte");
                    activeOrDesactiveOrSwithAccountType = "changé de type";
                }
                else if (suspendOrActiveOrSwithAccountType.isSuspendFlag() ) {
                    emailFromTo.setEmailSubject("Suppression de votre compte");
                    activeOrDesactiveOrSwithAccountType = "supprimé";
                }else{
                    emailFromTo.setEmailSubject("Activation de votre compte");
                    activeOrDesactiveOrSwithAccountType = "Activé";
                }

                String content = "Bonjour chèr(e) client(e) . <br>"
                        + "Votre compte dans le système monétaire numérique est "+activeOrDesactiveOrSwithAccountType+"<br>"
                        + "En cas de changement d'avis, contacter nous en "
                        + "<h4><a href=\"problem_link_here\" target=\"_self\">cliquant ici</a></h4>";
                emailFromTo.setEmailContent(content);

                //send email now
                new EmailSender(emailFromTo).sendmail();

                return 1;
            }
            return 0;

        }catch (Exception exception){
            return -1;
        }
    }


    @Override
    public EndUserData read(String endUserAccountId) {
        try {
            return  nodeRPCConnection.proxy.startTrackedFlowDynamic(
                    EndUserReaderFlowInitiator.class,endUserAccountId).getReturnValue().get();
        }catch (Exception exception){
            return null;
        }
    }
}
