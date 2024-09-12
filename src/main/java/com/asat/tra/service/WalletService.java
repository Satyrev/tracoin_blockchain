package com.asat.tra.service;

import com.asat.tra.blockchain.Wallet;
import jakarta.annotation.PostConstruct;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Base64;

@Service
public class WalletService {

    @PostConstruct
    public void init() {
        Security.addProvider(new BouncyCastleProvider());
    }


    public Wallet createWallet() {
        KeyPair keyPair = generateKeyPair();
        Wallet wallet = new Wallet();
        wallet.setPublicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        wallet.setPrivateKey(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));

        try {
            FileUtil.saveKeys(wallet.getPublicKey(), wallet.getPrivateKey());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wallet;
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
}
