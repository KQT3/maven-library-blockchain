import com.bloxbean.cardano.client.account.Account;
import com.bloxbean.cardano.client.api.exception.ApiException;
import com.bloxbean.cardano.client.api.helper.FeeCalculationService;
import com.bloxbean.cardano.client.api.helper.TransactionHelperService;
import com.bloxbean.cardano.client.api.helper.model.TransactionResult;
import com.bloxbean.cardano.client.api.model.Result;
import com.bloxbean.cardano.client.backend.api.*;
import com.bloxbean.cardano.client.backend.blockfrost.common.Constants;
import com.bloxbean.cardano.client.backend.blockfrost.service.BFBackendService;
import com.bloxbean.cardano.client.common.CardanoConstants;
import com.bloxbean.cardano.client.common.model.Networks;
import com.bloxbean.cardano.client.exception.AddressExcepion;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.transaction.model.PaymentTransaction;
import com.bloxbean.cardano.client.transaction.model.TransactionDetailsParams;
import io.github.cdimascio.dotenv.Dotenv;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class BlockFrost {
    public static void main(String[] args) throws NoSuchAlgorithmException, ApiException, AddressExcepion, CborSerializationException {
        final String blockfrost_api_key_secret = Dotenv.load().get("BLOCKFROST_API_KEY_SECRET");
        final String mnemonic1 = Dotenv.load().get("MNEMONIC1");
        final String mnemonic2 = Dotenv.load().get("MNEMONIC2");

        Account senderAccount = new Account(Networks.testnet(), mnemonic1);
        Account receiverAccount = new Account(Networks.testnet(), mnemonic2);

        System.out.println("senderAccount");
        System.out.println(senderAccount);
        System.out.println("receiverAccount");
        System.out.println(receiverAccount);

        BackendService backendService = new BFBackendService(Constants.BLOCKFROST_TESTNET_URL, blockfrost_api_key_secret);
        BlockService blockService = backendService.getBlockService();
        FeeCalculationService feeCalcService = backendService.getFeeCalculationService();
        TransactionHelperService transactionHelperService = backendService.getTransactionHelperService();

      /*  PaymentTransaction paymentTransaction = PaymentTransaction.builder()
                .sender(senderAccount)
                .receiver(receiverAccount.baseAddress())
                .amount(BigInteger.valueOf(1500000))
                .unit(CardanoConstants.LOVELACE)
                .build();

        long ttl = blockService.getLatestBlock().getValue().getSlot() + 1000;

        TransactionDetailsParams detailsParams = TransactionDetailsParams.builder()
                        .ttl(ttl)
                        .build();

        BigInteger fee = feeCalcService.calculateFee(paymentTransaction, detailsParams, null);
        paymentTransaction.setFee(fee);
        Result<TransactionResult> transfer = transactionHelperService.transfer(paymentTransaction, detailsParams);

        if(transfer.isSuccessful()){
            System.out.println("Transaction ID: " + transfer.getValue());
        }
        else System.out.println("Transaction failed: " + transfer);*/

      /*  PaymentTransaction paymentTransaction =
                PaymentTransaction.builder()
                        .sender(senderAccount)
                        .receiver(receiverAccount.baseAddress())
                        .amount(BigInteger.valueOf(3000))
                        .unit("d11b0562dcac7042636c9dbb44897b38.......")
                        .build();//Calculate Ttl = Latest slot + 1000
        long ttl = blockService.getLatestBlock().getValue().getSlot() + 1000;
        TransactionDetailsParams detailsParams =
                TransactionDetailsParams.builder()
                        .ttl(ttl)
                        .build();//Calculate fee
        BigInteger fee = feeCalcService.calculateFee(paymentTransaction, detailsParams, null);//Set fee
        paymentTransaction.setFee(fee);

        Result<TransactionResult> result = transactionHelperService.transfer(paymentTransaction, detailsParams);
        System.out.println(result);*/
    }


}
