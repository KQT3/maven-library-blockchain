import com.bloxbean.cardano.client.account.Account;
import com.bloxbean.cardano.client.api.exception.ApiException;
import com.bloxbean.cardano.client.api.helper.FeeCalculationService;
import com.bloxbean.cardano.client.api.helper.TransactionHelperService;
import com.bloxbean.cardano.client.api.helper.model.TransactionResult;
import com.bloxbean.cardano.client.api.model.Result;
import com.bloxbean.cardano.client.backend.api.BackendService;
import com.bloxbean.cardano.client.backend.api.BlockService;
import com.bloxbean.cardano.client.backend.blockfrost.common.Constants;
import com.bloxbean.cardano.client.backend.blockfrost.service.BFBackendService;
import com.bloxbean.cardano.client.common.CardanoConstants;
import com.bloxbean.cardano.client.common.model.Networks;
import com.bloxbean.cardano.client.exception.AddressExcepion;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.metadata.cbor.CBORMetadata;
import com.bloxbean.cardano.client.metadata.cbor.CBORMetadataList;
import com.bloxbean.cardano.client.metadata.cbor.CBORMetadataMap;
import com.bloxbean.cardano.client.transaction.model.PaymentTransaction;
import com.bloxbean.cardano.client.transaction.model.TransactionDetailsParams;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.util.Properties;

public class Metadata {
    public static void main(String[] args) throws ApiException, AddressExcepion, CborSerializationException {
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
        FeeCalculationService feeCalculationService = backendService.getFeeCalculationService();
        TransactionHelperService transactionHelperService = backendService.getTransactionHelperService();

        CBORMetadataMap productDetailsMap = new CBORMetadataMap()
                .put("code", "PROD-800")
                .put("slno", "SL20000039484");


        CBORMetadataList tagList = new CBORMetadataList()
                .add("laptopfe")
                .add("computer");

        CBORMetadata metadata = new CBORMetadata()
                .put(new BigInteger("670001"), productDetailsMap)
                .put(new BigInteger("670002"), tagList);

        PaymentTransaction paymentTransaction = PaymentTransaction.builder()
                .sender(senderAccount)
                .receiver(receiverAccount.baseAddress())
                .amount(BigInteger.valueOf(2000000))
                .unit(CardanoConstants.LOVELACE)
                .build();

        long ttl = blockService.getLatestBlock().getValue().getSlot() + 1000;
        TransactionDetailsParams detailsParams = TransactionDetailsParams.builder()
                .ttl(ttl)
                .build();
        BigInteger fee = feeCalculationService.calculateFee(paymentTransaction, detailsParams, metadata);
        paymentTransaction.setFee(fee);

        Result<TransactionResult> transfer = transactionHelperService.transfer(paymentTransaction, detailsParams, metadata);

        if (transfer.isSuccessful())
            System.out.println("Transaction Id: " + transfer.getValue());
        else
            System.out.println("Transaction failed: " + transfer);

    }
}
