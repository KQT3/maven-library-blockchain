import chainqt3.blockchain.mavenlibrary.Block;
import chainqt3.blockchain.mavenlibrary.BlockHelper;

public class Blockchain {
    public static void main(String[] args) {
        Block block1 = BlockHelper.createBlock(0, new String[]{"BTC: 1", "ETH: 1"});
        Block block2 = BlockHelper.createBlock(block1.getBlockHash(), new String[]{"BTC: 10", "ETH: 10"});
        Block block3 = BlockHelper.createBlock(block2.getBlockHash(), new String[]{"BTC: 100", "ETH: 100"});
        Block block4 = BlockHelper.createBlock(block3.getBlockHash(), new String[]{"BTC: 1000", "ETH: 1000"});

        System.out.println(block1.getBlockHash());
        System.out.println(block2.getBlockHash());
        System.out.println(block3.getBlockHash());
        System.out.println(block4.getBlockHash());
    }
}
