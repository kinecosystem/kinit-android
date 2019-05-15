package org.kinecosystem.kinit;

import android.support.annotation.NonNull;

import org.kinecosystem.appsdiscovery.sender.service.SendKinServiceBase;

import java.math.BigDecimal;


public class SendKinService extends SendKinServiceBase {

    @Override
    public @NonNull
    KinTransferComplete transferKin(@NonNull String toAddress, int amount, @NonNull String memo) throws KinTransferException {
//        SampleWallet sampleWallet = ((SenderApplication) getApplicationContext()).getSampleWallet();
//        String sourceAddress = "None";
//
//        if (!sampleWallet.hasActiveAccount()) {
//
//            throw new KinTransferException(sourceAddress, "Cannot transfer Kin. Account not initialized");
//        }
//
       try {
//            sourceAddress = sampleWallet.getAccount().getPublicAddress();
//
//            int fee = 100; // no whitelisting for sample app, so using a fee
//            Transaction transaction = sampleWallet.getAccount().buildTransactionSync(toAddress,
//                    new BigDecimal(amount), fee, memo);
//            TransactionId transactionId = sampleWallet.getAccount().sendTransactionSync(transaction);
//
//            // here you may add some code to add the transaction details to
//            // transaction history metadata

            return new KinTransferComplete("my address", "transid");

        } catch (Exception e) {

            e.printStackTrace();

            throw new KinTransferException("my addresse2",
                    "Cannot transfer Kin. Exception " + e + ", with message " + e.getMessage());

        }
    }

    @Override
    public BigDecimal getCurrentBalance() throws BalanceException {

        try {
            return new BigDecimal(555);

        } catch (Exception e) {
            throw new BalanceException("Unable to retrieve Kin balance. Exception " + e + ", with message " + e.getMessage());
        }

    }
}