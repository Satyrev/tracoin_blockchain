package com.asat.tra.service;

import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    public static void saveKeys(String publicKey, String privateKey) throws IOException {
        try (FileWriter writer = new FileWriter("keys.txt", true)) {
            writer.write("Public Key: " + publicKey + "\n");
            writer.write("Private Key: " + privateKey + "\n");
            writer.write("--------------------\n");
        }
    }

    public static void saveKeys(String publicKey, String privateKey, float balance) throws IOException {
        try (FileWriter writer = new FileWriter("keys.txt", true)) {
            writer.write("Public Key: " + publicKey + "\n");
            writer.write("Private Key: " + privateKey + "\n");
            writer.write("Balance: " + balance + "\n");
            writer.write("--------------------\n");
        }
    }
}