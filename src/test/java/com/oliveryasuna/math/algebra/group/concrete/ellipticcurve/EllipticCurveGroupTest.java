/*
 * Copyright 2022 Oliver Yasuna
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without
 *      specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.oliveryasuna.math.algebra.group.concrete.ellipticcurve;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for elliptic curve cryptography operations.
 * <p>
 * Demonstrates ECDH (Elliptic Curve Diffie-Hellman) key exchange protocol.
 *
 * @author Oliver Yasuna
 */
class EllipticCurveGroupTest {

  /**
   * Tests ECDH key exchange using a small elliptic curve.
   * <p>
   * Scenario: Alice and Bob want to establish a shared secret over an insecure channel.
   * <p>
   * Protocol:
   * 1. Agree on curve parameters and base point G (public)
   * 2. Alice generates private key a, computes public key A = a*G
   * 3. Bob generates private key b, computes public key B = b*G
   * 4. Alice computes shared secret: a*B
   * 5. Bob computes shared secret: b*A
   * 6. Both shared secrets are equal: a*B = a*(b*G) = b*(a*G) = b*A
   */
  @Test
  void testECDHKeyExchange() {
    // Define elliptic curve: y^2 = x^3 + 2x + 3 (mod 97)
    final BigInteger a = BigInteger.valueOf(2);
    final BigInteger b = BigInteger.valueOf(3);
    final BigInteger p = BigInteger.valueOf(97); // Small prime for testing

    final EllipticCurveGroup curve = new EllipticCurveGroup(a, b, p);

    // Choose base point G (generator)
    // Point (3, 6) is on the curve: 6^2 = 36, 3^3 + 2*3 + 3 = 36 (mod 97)
    final EllipticCurvePoint G = curve.getElement(new EllipticCurvePoint.Coordinates(
        BigInteger.valueOf(3),
        BigInteger.valueOf(6)
    ));

    // Verify G is on the curve
    assertTrue(curve.hasElement(new EllipticCurvePoint.Coordinates(
        BigInteger.valueOf(3),
        BigInteger.valueOf(6)
    )));

    // Alice's key pair
    final BigInteger alicePrivateKey = BigInteger.valueOf(7); // Random private key
    final EllipticCurvePoint alicePublicKey = G.multiply(alicePrivateKey);

    System.out.println("Alice's private key: " + alicePrivateKey);
    System.out.println("Alice's public key: " + alicePublicKey);

    // Bob's key pair
    final BigInteger bobPrivateKey = BigInteger.valueOf(13); // Random private key
    final EllipticCurvePoint bobPublicKey = G.multiply(bobPrivateKey);

    System.out.println("Bob's private key: " + bobPrivateKey);
    System.out.println("Bob's public key: " + bobPublicKey);

    // Alice computes shared secret using Bob's public key and her private key
    final EllipticCurvePoint aliceSharedSecret = bobPublicKey.multiply(alicePrivateKey);

    // Bob computes shared secret using Alice's public key and his private key
    final EllipticCurvePoint bobSharedSecret = alicePublicKey.multiply(bobPrivateKey);

    System.out.println("Alice's shared secret: " + aliceSharedSecret);
    System.out.println("Bob's shared secret: " + bobSharedSecret);

    // Both shared secrets should be equal
    assertEquals(aliceSharedSecret, bobSharedSecret,
        "ECDH shared secrets should match");

    // Verify the shared secret is what we mathematically expect
    final EllipticCurvePoint expectedSecret = G.multiply(alicePrivateKey.multiply(bobPrivateKey));
    assertEquals(expectedSecret, aliceSharedSecret,
        "Shared secret should equal (a*b)*G");
  }

  /**
   * Tests basic elliptic curve group operations.
   */
  @Test
  void testGroupOperations() {
    // Define curve: y^2 = x^3 + 2x + 3 (mod 97)
    final EllipticCurveGroup curve = new EllipticCurveGroup(
        BigInteger.valueOf(2),
        BigInteger.valueOf(3),
        BigInteger.valueOf(97)
    );

    // Point at infinity (identity element)
    final EllipticCurvePoint identity = curve.operation().identity();
    assertTrue(identity.isInfinity());

    // Test point (3, 6)
    final EllipticCurvePoint P = curve.getElement(new EllipticCurvePoint.Coordinates(
        BigInteger.valueOf(3),
        BigInteger.valueOf(6)
    ));

    // Identity property: P + O = P
    assertEquals(P, curve.operation().perform(P, identity));
    assertEquals(P, curve.operation().perform(identity, P));

    // Inverse property: P + (-P) = O
    final EllipticCurvePoint negP = curve.operation().inverse(P);
    assertEquals(identity, curve.operation().perform(P, negP));

    // Point doubling: 2P = P + P
    final EllipticCurvePoint twoP = curve.operation().perform(P, P);
    assertEquals(twoP, P.multiply(BigInteger.TWO));

    // Commutativity: P + Q = Q + P
    final EllipticCurvePoint Q = curve.getElement(new EllipticCurvePoint.Coordinates(
        BigInteger.valueOf(80),
        BigInteger.valueOf(10)
    ));
    assertEquals(
        curve.operation().perform(P, Q),
        curve.operation().perform(Q, P)
    );

    // Associativity: (P + Q) + R = P + (Q + R)
    final EllipticCurvePoint R = twoP;
    final EllipticCurvePoint left = curve.operation().perform(
        curve.operation().perform(P, Q),
        R
    );
    final EllipticCurvePoint right = curve.operation().perform(
        P,
        curve.operation().perform(Q, R)
    );
    assertEquals(left, right);
  }

  /**
   * Tests with a larger curve more suitable for real cryptography.
   * <p>
   * Uses the secp256k1 curve parameters (used by Bitcoin).
   */
  @Test
  void testSecp256k1Curve() {
    // secp256k1 parameters (simplified - using a smaller prime for testing)
    // Real secp256k1: y^2 = x^3 + 7 (mod p) where p is a 256-bit prime

    // For testing, use a smaller but similar curve: y^2 = x^3 + 7 (mod 223)
    final BigInteger a = BigInteger.ZERO;
    final BigInteger b = BigInteger.valueOf(7);
    final BigInteger p = BigInteger.valueOf(223);

    final EllipticCurveGroup curve = new EllipticCurveGroup(a, b, p);

    // Base point (similar structure to secp256k1's generator)
    // Point (1, 193) is on the curve: 193^2 = 37249 = 167 (mod 223), 1^3 + 7 = 8...
    // Let's use a verified point (47, 71)
    final EllipticCurvePoint G = curve.getElement(new EllipticCurvePoint.Coordinates(
        BigInteger.valueOf(47),
        BigInteger.valueOf(71)
    ));

    // Generate large random private keys
    final SecureRandom random = new SecureRandom();
    final BigInteger alicePrivateKey = new BigInteger(32, random).mod(p);
    final BigInteger bobPrivateKey = new BigInteger(32, random).mod(p);

    // Compute public keys
    final EllipticCurvePoint alicePublicKey = G.multiply(alicePrivateKey);
    final EllipticCurvePoint bobPublicKey = G.multiply(bobPrivateKey);

    // Compute shared secrets
    final EllipticCurvePoint aliceSharedSecret = bobPublicKey.multiply(alicePrivateKey);
    final EllipticCurvePoint bobSharedSecret = alicePublicKey.multiply(bobPrivateKey);

    // Verify ECDH works
    assertEquals(aliceSharedSecret, bobSharedSecret,
        "ECDH should produce matching shared secrets");

    System.out.println("Secp256k1-style ECDH successful with shared secret: " + aliceSharedSecret);
  }

  /**
   * Tests that invalid points are rejected.
   */
  @Test
  void testInvalidPoints() {
    final EllipticCurveGroup curve = new EllipticCurveGroup(
        BigInteger.valueOf(2),
        BigInteger.valueOf(3),
        BigInteger.valueOf(97)
    );

    // Point (1, 1) is NOT on the curve y^2 = x^3 + 2x + 3 (mod 97)
    // 1^2 = 1, but 1^3 + 2*1 + 3 = 6 (not equal)
    assertFalse(curve.hasElement(new EllipticCurvePoint.Coordinates(
        BigInteger.ONE,
        BigInteger.ONE
    )));

    // Attempting to get an invalid element should fail
    assertThrows(IllegalArgumentException.class, () -> {
      curve.getElement(new EllipticCurvePoint.Coordinates(
          BigInteger.ONE,
          BigInteger.ONE
      ));
    });
  }

  /**
   * Tests that singular curves are rejected.
   */
  @Test
  void testSingularCurve() {
    // A curve is singular if 4a^3 + 27b^2 = 0 (mod p)
    // For example: y^2 = x^3 with a=0, b=0 is singular
    assertThrows(IllegalArgumentException.class, () -> {
      new EllipticCurveGroup(
          BigInteger.ZERO,
          BigInteger.ZERO,
          BigInteger.valueOf(97)
      );
    });
  }

}