import chainqt3.blockchain.mavenlibrary.Block;
import chainqt3.blockchain.mavenlibrary.BlockHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Blockchain {
    public static void main(String[] args) {
        List<Block> blockchain = new ArrayList<>();
        final int prefix = 4;

        Block genesisBlock = BlockHelper.createBlock("The is the Genesis Block.", "0", new Date());
        genesisBlock.mineBlock(prefix);
        blockchain.add(genesisBlock);
        Block block1 = BlockHelper.createBlock("The is the First Block.", genesisBlock.getHash(), new Date());
        block1.mineBlock(prefix);
        blockchain.add(block1);

        blockchain.forEach(block -> System.out.println(block.getHash()));

    }
}
