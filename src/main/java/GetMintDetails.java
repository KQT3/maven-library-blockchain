import com.bloxbean.cardano.client.api.exception.ApiException;
import com.bloxbean.cardano.client.api.model.Result;
import com.bloxbean.cardano.client.backend.api.AssetService;
import com.bloxbean.cardano.client.backend.api.BackendService;
import com.bloxbean.cardano.client.backend.api.BlockService;
import com.bloxbean.cardano.client.backend.blockfrost.common.Constants;
import com.bloxbean.cardano.client.backend.blockfrost.service.BFBackendService;
import com.bloxbean.cardano.client.backend.model.Asset;
import com.bloxbean.cardano.client.backend.model.Block;
import com.bloxbean.cardano.client.crypto.Keys;
import com.bloxbean.cardano.client.crypto.SecretKey;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.transaction.spec.script.*;
import com.bloxbean.cardano.client.util.HexUtil;
import com.bloxbean.cardano.client.util.JsonUtil;
import com.bloxbean.cardano.client.util.Tuple;
import io.github.cdimascio.dotenv.Dotenv;

import java.nio.charset.StandardCharsets;

public class GetMintDetails extends BaseTest {

    public static void main(String[] args) throws ApiException, CborSerializationException {
        final String blockfrost_api_key_secret = Dotenv.load().get("BLOCKFROST_API_KEY_SECRET");
        String policyId1 = "18016a05b7c01b95e2f7a937ade5d5f06f547fa17156925d9be94d97";
        String assetName = HexUtil.encodeHexString("TestCoin".getBytes(StandardCharsets.UTF_8));
        System.out.println(assetName);
        System.out.println(policyId1);
        System.out.println("TestCoin".getBytes(StandardCharsets.UTF_8));
        String assetId = policyId1 + assetName;

        BackendService backendService = new BFBackendService(Constants.BLOCKFROST_TESTNET_URL, blockfrost_api_key_secret);
        System.out.println(assetId);
        AssetService assetService = backendService.getAssetService();
        Result<Asset> asset = assetService.getAsset(assetId);
        System.out.println(JsonUtil.getPrettyJson(asset.getValue()));

        //Key 1 - Single Key Policy
        Tuple<ScriptPubkey, Keys> tuple1 = ScriptPubkey.createWithNewKey();
        ScriptPubkey scriptPubkey1 = tuple1._1;
        SecretKey sk1 = tuple1._2.getSkey();

        //Key 2 - Single Key Policy
        Tuple<ScriptPubkey, Keys> tuple2 = ScriptPubkey.createWithNewKey();
        ScriptPubkey scriptPubkey2 = tuple2._1;
        SecretKey sk2 = tuple2._2.getSkey();

        //Key 3 - Single Key Policy
        Tuple<ScriptPubkey, Keys> tuple3 = ScriptPubkey.createWithNewKey();
        ScriptPubkey scriptPubkey3 = tuple3._1;
        SecretKey sk3 = tuple3._2.getSkey();

        ScriptAtLeast scriptAtLeast = new ScriptAtLeast(2)
                .addScript(scriptPubkey1)
                .addScript(scriptPubkey2)
                .addScript(scriptPubkey3);

        //getTtl
        BlockService blockService = backendService.getBlockService();
        Block block = blockService.getLatestBlock().getValue();
        long slot = block.getSlot() + 2000;

        RequireTimeBefore requireTimeBefore = new RequireTimeBefore(slot);

        ScriptAll scriptAll = new ScriptAll()
                .addScript(requireTimeBefore)
                .addScript(scriptAtLeast);

        System.out.println(scriptAll.getPolicyId());

    }

}
