package com.asat.tra.api;

import com.asat.tra.blockchain.Block;
import com.asat.tra.blockchain.Wallet;
import com.asat.tra.service.BlockChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/blocks")
public class BlockController {

    @Autowired
    private BlockChainService blockChainService;

    @PostMapping("/createWallet")
    public ResponseEntity<Wallet> createWallet() throws IOException {
        Wallet wallet = blockChainService.createWallet();
        return ResponseEntity.ok(wallet);
    }

    @PostMapping("/mine")
    public ResponseEntity<Block> mineForUser(@RequestBody Map<String, String> requestBody) {
        String publicKey = requestBody.get("publicKey");
        Block block = blockChainService.mineBlock(publicKey);
        return ResponseEntity.ok(block);
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