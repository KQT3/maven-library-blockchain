import com.bloxbean.cardano.client.account.Account;
import com.bloxbean.cardano.client.api.exception.ApiException;
import com.bloxbean.cardano.client.api.helper.FeeCalculationService;
import com.bloxbean.cardano.client.api.helper.TransactionHelperService;
import com.bloxbean.cardano.client.api.helper.model.TransactionResult;
import com.bloxbean.cardano.client.api.model.Result;
import com.bloxbean.cardano.client.backend.api.AssetService;
import com.bloxbean.cardano.client.backend.api.BackendService;
import com.bloxbean.cardano.client.backend.api.BlockService;
import com.bloxbean.cardano.client.backend.blockfrost.common.Constants;
import com.bloxbean.cardano.client.backend.blockfrost.service.BFBackendService;
import com.bloxbean.cardano.client.common.model.Networks;
import com.bloxbean.cardano.client.crypto.KeyGenUtil;
import com.bloxbean.cardano.client.crypto.Keys;
import com.bloxbean.cardano.client.crypto.SecretKey;
import com.bloxbean.cardano.client.crypto.VerificationKey;
import com.bloxbean.cardano.client.exception.AddressExcepion;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.metadata.cbor.CBORMetadata;
import com.bloxbean.cardano.client.metadata.cbor.CBORMetadataList;
import com.bloxbean.cardano.client.metadata.cbor.CBORMetadataMap;
import com.bloxbean.cardano.client.transaction.model.MintTransaction;
import com.bloxbean.cardano.client.transaction.model.TransactionDetailsParams;
import com.bloxbean.cardano.client.transaction.spec.Asset;
import com.bloxbean.cardano.client.transaction.spec.MultiAsset;
import com.bloxbean.cardano.client.transaction.spec.script.ScriptPubkey;
import com.bloxbean.cardano.client.util.HexUtil;
import io.github.cdimascio.dotenv.Dotenv;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MintToken {
    public static void main(String[] args) throws CborSerializationException, ApiException, AddressExcepion {
        final String blockfrost_api_key_secret = Dotenv.load().get("BLOCKFROST_API_KEY_SECRET");
        final String mnemonic1 = Dotenv.load().get("MNEMONIC1");
        final String mnemonic2 = Dotenv.load().get("MNEMONIC2");

        Account senderAccount = new Account(Networks.testnet(), mnemonic1);
        Account receiverAccount = new Account(Networks.testnet(), mnemonic2);

        System.out.println("senderAccount");
        System.out.println(senderAccount);
        System.out.println("receiverAccount");
        System.out.println(receiverAccount);

        Keys keys = KeyGenUtil.generateKey();
        VerificationKey vkey = keys.getVkey();
        SecretKey skey = keys.getSkey();
        ScriptPubkey scriptPubkey = ScriptPubkey.create(vkey);

        String policyId = scriptPubkey.getPolicyId();
        MultiAsset multiAsset = new MultiAsset();
        multiAsset.setPolicyId(policyId);
        Asset testCoin = new Asset("TestCoin", BigInteger.valueOf(250000));
        multiAsset.getAssets().add(testCoin);

        CBORMetadataMap tokenInfoMap = new CBORMetadataMap()
                .put("token", "Test Token")
                .put("Symbol", "TTOK");

        CBORMetadataList tagList = new CBORMetadataList()
                .add("tag1")
                .add("tag2");

        CBORMetadata metadata = new CBORMetadata()
                .put(new BigInteger("770001"), tokenInfoMap)
                .put(new BigInteger("770002"), tagList);

        MintTransaction mintTransaction = MintTransaction.builder()
                .sender(senderAccount)
                .mintAssets(Arrays.asList(multiAsset))
                .policyScript(scriptPubkey)
                .policyKeys(Arrays.asList(skey))
                .build();

        BFBackendService backendService = new BFBackendService(Constants.BLOCKFROST_TESTNET_URL, blockfrost_api_key_secret);
        BlockService blockService = backendService.getBlockService();
        long ttl = blockService.getLatestBlock().getValue().getSlot() + 1000;
        TransactionDetailsParams detailsParams = TransactionDetailsParams.builder()
                .ttl(ttl)
                .build();

        FeeCalculationService feeCalculationService = backendService.getFeeCalculationService();
        BigInteger fee = feeCalculationService.calculateFee(mintTransaction, detailsParams, metadata);
        mintTransaction.setFee(fee);


        /*TransactionHelperService transactionHelperService = backendService.getTransactionHelperService();
        Result<TransactionResult> transactionResultResult =
                transactionHelperService.mintToken(mintTransaction, detailsParams, metadata);

        if (transactionResultResult.isSuccessful())
            System.out.println("Transaction Id: " + transactionResultResult.getValue());
        else
            System.out.println("Transaction failed: " + transactionResultResult);*/


    }


}




