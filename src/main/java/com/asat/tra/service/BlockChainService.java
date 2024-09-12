package com.asat.tra.service;

import org.springframework.stereotype.Service;
import com.asat.tra.blockchain.*;

import javax.annotation.PostConstruct;
import java.io.*;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.asat.tra.blockchain.trachain.addBlock;

@Service
public class BlockChainService {

    private Map<String, Wallet> wallets = new HashMap<>();

    public Wallet createWallet() throws IOException {
        Wallet wallet = new Wallet();
        String publicKeyString = StringUtil.getStringFromKey(wallet.publicKey);
        String privateKeyString = StringUtil.getStringFromKey(wallet.privateKey);

        wallets.put(publicKeyString, wallet);
        FileUtil.saveKeys(publicKeyString, privateKeyString, 0.0f);
        return wallet;
    }

    public Block mineBlock(String publicKeyString) {
        Wallet wallet = wallets.get(publicKeyString);
        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found for the given public key");
        }

        Block newBlock = new Block(trachain.getLatestBlockHash());
        Transaction transaction = wallet.sendFunds(wallet.publicKey, 1.0f);
        if (transaction != null) {
            newBlock.addTransaction(transaction);
        }

        trachain.addBlock(newBlock);
        wallet.addBlock(newBlock);

        float newBalance = wallet.getBalance() + 1.0f;
        wallet.updateBalance(newBalance);

        try {
            FileUtil.saveKeys(wallet.getPublicKey(), wallet.getPrivateKey(), wallet.getBalance());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newBlock;
    }


    public boolean processTransaction(String senderPublicKeyString, String recipientPublicKeyString, String senderPrivateKeyString, float amount) {
        Wallet senderWallet = wallets.get(senderPublicKeyString);
        if (senderWallet == null) {
            throw new IllegalArgumentException("Sender wallet not found for the given public key");
        }

        PublicKey recipientPublicKey = StringUtil.getPublicKeyFromString(recipientPublicKeyString);
        PrivateKey senderPrivateKey = StringUtil.getPrivateKeyFromString(senderPrivateKeyString);

        if (!senderPrivateKey.equals(senderWallet.privateKey)) {
            throw new IllegalArgumentException("Private key does not match the sender's wallet");
        }

        Transaction newTransaction = senderWallet.sendFunds(recipientPublicKey, amount);
        if (newTransaction == null) {
            throw new IllegalStateException("Transaction creation failed.");
        }

        newTransaction.generateSignature(senderPrivateKey);
        Block latestBlock = trachain.blockchain.isEmpty() ? null : trachain.blockchain.get(trachain.blockchain.size() - 1);
        if (latestBlock != null) {
            latestBlock.addTransaction(newTransaction);
        } else {
            throw new IllegalStateException("No blocks in the blockchain");
        }

        senderWallet.updateBalance(senderWallet.getBalance() - amount);

        Wallet recipientWallet = wallets.get(recipientPublicKeyString);
        if (recipientWallet != null) {
            recipientWallet.updateBalance(recipientWallet.getBalance() + amount);
        }

        try {
            FileUtil.saveKeys(senderWallet.getPublicKey(), senderWallet.getPrivateKey(), senderWallet.getBalance());
            if (recipientWallet != null) {
                FileUtil.saveKeys(recipientWallet.getPublicKey(), recipientWallet.getPrivateKey(), recipientWallet.getBalance());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }


    @PostConstruct
    public void init() {
        if (trachain.blockchain.isEmpty()) {
            Block genesisBlock = new Block("0");
            trachain.blockchain.add(genesisBlock);
        }
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
            keyGen.initialize(256); // Key size in bits
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating key pair", e);
        }
    }

    public float getBalance(String privateKeyString) {
        for (Wallet wallet : wallets.values()) {
            if (wallet.getPrivateKey().equals(privateKeyString)) {
                return wallet.getBalance();
            }
        }
        throw new IllegalArgumentException("Wallet not found for the given private key");
    }

}
