package org.acme.config

import java.security.KeyPairGenerator
import java.util.*

class CertGenerator {
    fun generateKeys() {
        val keyGen = KeyPairGenerator.getInstance("RSA")
        keyGen.initialize(4096)
        val pair = keyGen.generateKeyPair()
        println(Base64.getMimeEncoder().encodeToString(pair.private.encoded))
        println()
        println(Base64.getMimeEncoder().encodeToString(pair.public.encoded))
    }
}