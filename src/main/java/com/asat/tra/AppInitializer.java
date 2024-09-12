package com.asat.tra;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class AppInitializer {
    public static void initialize() {
        Security.addProvider(new BouncyCastleProvider());
    }
}