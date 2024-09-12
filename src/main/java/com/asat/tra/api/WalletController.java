package com.asat.tra.api;

import com.asat.tra.blockchain.Block;
import com.asat.tra.blockchain.Wallet;
import com.asat.tra.service.BlockChainService;
import com.asat.tra.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class WalletController {

    private final WalletService walletService;

    @Autowired
    private BlockChainService blockChainService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/createWallet")
    public ResponseEntity<Wallet> createWallet() throws IOException {
        Wallet wallet = blockChainService.createWallet();
        return ResponseEntity.ok(wallet);
    }

    @PostMapping("/mine")
    public ResponseEntity<Block> mineBlock(@RequestBody Map<String, String> request) {
        String publicKey = request.get("publicKey");
        Block minedBlock = blockChainService.mineBlock(publicKey);
        return ResponseEntity.ok(minedBlock);
    }

    @PostMapping("/sendTransaction")
    public ResponseEntity<Boolean> sendTransaction(
            @RequestParam String senderPublicKey,
            @RequestParam String recipientPublicKey,
            @RequestParam String senderPrivateKey,
            @RequestParam float amount) {
        boolean result = blockChainService.processTransaction(senderPublicKey, recipientPublicKey, senderPrivateKey, amount);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/getBalance")
    public ResponseEntity<Map<String, Float>> getBalance(@RequestBody Map<String, String> request) {
        String privateKey = request.get("privateKey");
        float balance = blockChainService.getBalance(privateKey);
        return ResponseEntity.ok(Map.of("balance", balance));
    }

}
