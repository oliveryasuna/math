/*
 * Copyright 2025 Oliver Yasuna
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
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF SUCH DAMAGE.
 */

package com.oliveryasuna.math.algebra.group.concrete.permutation;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for symmetric groups and permutations.
 * <p>
 * Demonstrates combinatorial structures and group actions on sets.
 *
 * @author Oliver Yasuna
 */
class SymmetricGroupTest {

  /**
   * Tests basic permutation group operations.
   */
  @Test
  void testBasicGroupOperations() {
    final SymmetricGroup S3 = new SymmetricGroup(3);

    // Identity permutation
    final Permutation identity = S3.operation().identity();
    assertTrue(identity.isIdentity());
    assertEquals("()", identity.toString());

    // Transposition (0 1)
    final Permutation swap01 = S3.transposition(0, 1);
    assertArrayEquals(new int[]{1, 0, 2}, swap01.getMapping());
    assertEquals("(0 1)", swap01.toString());

    // Verify transposition is its own inverse
    assertEquals(identity, S3.operation().perform(swap01, swap01));

    // Verify order of transposition is 2
    assertEquals(2, swap01.order());

    // Cycle (0 1 2)
    final Permutation cycle012 = S3.cycle(0, 1, 2);
    assertArrayEquals(new int[]{1, 2, 0}, cycle012.getMapping());
    assertEquals("(0 1 2)", cycle012.toString());

    // Verify order of 3-cycle is 3
    assertEquals(3, cycle012.order());

    // Verify (0 1 2)^3 = identity
    Permutation result = cycle012;
    result = S3.operation().perform(result, cycle012);
    result = S3.operation().perform(result, cycle012);
    assertEquals(identity, result);
  }

  /**
   * Tests composition of permutations.
   */
  @Test
  void testComposition() {
    final SymmetricGroup S4 = new SymmetricGroup(4);

    // (0 1) and (1 2)
    final Permutation p1 = S4.transposition(0, 1); // (0 1)
    final Permutation p2 = S4.transposition(1, 2); // (1 2)

    // (0 1)(1 2) = (0 1 2)
    final Permutation composition = S4.operation().perform(p1, p2);
    assertEquals("(0 1 2)", composition.toString());

    // Verify composition is not commutative
    final Permutation reverseComposition = S4.operation().perform(p2, p1);
    assertNotEquals(composition, reverseComposition);
    assertEquals("(0 2 1)", reverseComposition.toString());
  }

  /**
   * Tests permutation inverses.
   */
  @Test
  void testInverses() {
    final SymmetricGroup S4 = new SymmetricGroup(4);

    final Permutation p = S4.cycle(0, 1, 2, 3); // (0 1 2 3)
    final Permutation pInverse = S4.operation().inverse(p);

    // p * p^-1 = identity
    assertEquals(S4.operation().identity(),
        S4.operation().perform(p, pInverse));

    // p^-1 * p = identity
    assertEquals(S4.operation().identity(),
        S4.operation().perform(pInverse, p));

    // Inverse of (0 1 2 3) is (0 3 2 1)
    assertEquals("(0 3 2 1)", pInverse.toString());
  }

  /**
   * Tests permutation parity (sign).
   */
  @Test
  void testPermutationSign() {
    final SymmetricGroup S4 = new SymmetricGroup(4);

    // Identity is even
    assertEquals(1, S4.operation().identity().sign());

    // Single transposition is odd
    assertEquals(-1, S4.transposition(0, 1).sign());

    // Two transpositions: even
    final Permutation twoSwaps = S4.operation().perform(
        S4.transposition(0, 1),
        S4.transposition(2, 3)
    );
    assertEquals(1, twoSwaps.sign());

    // 3-cycle is even (can be written as 2 transpositions)
    assertEquals(1, S4.cycle(0, 1, 2).sign());

    // 4-cycle is odd (can be written as 3 transpositions)
    assertEquals(-1, S4.cycle(0, 1, 2, 3).sign());
  }

  /**
   * Tests that S_n has order n!
   */
  @Test
  void testGroupOrder() {
    final SymmetricGroup S3 = new SymmetricGroup(3);
    assertEquals(BigInteger.valueOf(6), S3.elementCount()); // 3! = 6

    final SymmetricGroup S4 = new SymmetricGroup(4);
    assertEquals(BigInteger.valueOf(24), S4.elementCount()); // 4! = 24

    final SymmetricGroup S5 = new SymmetricGroup(5);
    assertEquals(BigInteger.valueOf(120), S5.elementCount()); // 5! = 120
  }

  /**
   * Tests generating all elements of a small symmetric group.
   */
  @Test
  void testGenerateAllPermutations() {
    final SymmetricGroup S3 = new SymmetricGroup(3);

    final List<Permutation> allPerms = S3.elements().collect(Collectors.toList());
    assertEquals(6, allPerms.size());

    // Check all permutations are distinct
    assertEquals(6, allPerms.stream().distinct().count());

    // Verify identity is present
    assertTrue(allPerms.stream().anyMatch(Permutation::isIdentity));
  }

  /**
   * Tests cycle decomposition.
   */
  @Test
  void testCycleDecomposition() {
    final SymmetricGroup S6 = new SymmetricGroup(6);

    // Create permutation: (0 2 4)(1 3)
    final Permutation p = S6.fromCycles(Arrays.asList(
        Arrays.asList(0, 2, 4),
        Arrays.asList(1, 3)
    ));

    // Verify the mapping
    assertEquals(2, p.apply(0));
    assertEquals(3, p.apply(1));
    assertEquals(4, p.apply(2));
    assertEquals(1, p.apply(3));
    assertEquals(0, p.apply(4));
    assertEquals(5, p.apply(5)); // Fixed point

    // Order is LCM(3, 2) = 6
    assertEquals(6, p.order());

    System.out.println("Permutation: " + p);
    System.out.println("Order: " + p.order());
    System.out.println("Cycles: " + p.getCycles());
  }

  /**
   * Tests demonstrating Rubik's Cube group structure.
   * <p>
   * A 2x2x2 Rubik's Cube has 8 corner pieces (cubies).
   * Each move is a permutation of these 8 positions.
   * The Rubik's Cube group is a subgroup of S_8.
   * <p>
   * This simplified model only tracks position (not orientation).
   */
  @Test
  void testRubiksCubeGroupStructure() {
    // Model a 2x2x2 Rubik's cube with 8 corner positions
    // Positions numbered 0-7:
    //   Top layer: 0(front-left), 1(front-right), 2(back-right), 3(back-left)
    //   Bottom layer: 4(front-left), 5(front-right), 6(back-right), 7(back-left)

    final SymmetricGroup S8 = new SymmetricGroup(8);

    // Define basic moves as permutations

    // R (Right face clockwise): rotates positions 1,2,5,6
    final Permutation R = S8.fromCycles(List.of(Arrays.asList(1, 5, 6, 2)));

    // U (Up face clockwise): rotates positions 0,1,2,3
    final Permutation U = S8.fromCycles(List.of(Arrays.asList(0, 1, 2, 3)));

    // F (Front face clockwise): rotates positions 0,1,5,4
    final Permutation F = S8.fromCycles(List.of(Arrays.asList(0, 4, 5, 1)));

    System.out.println("Rubik's Cube 2x2x2 Basic Moves:");
    System.out.println("R (Right): " + R);
    System.out.println("U (Up): " + U);
    System.out.println("F (Front): " + F);

    // Test: R has order 4 (R^4 = identity)
    assertEquals(4, R.order());
    Permutation R4 = R;
    for (int i = 0; i < 3; i++) {
      R4 = S8.operation().perform(R4, R);
    }
    assertTrue(R4.isIdentity(), "R^4 should be identity");

    // Test: U has order 4
    assertEquals(4, U.order());

    // Test: F has order 4
    assertEquals(4, F.order());

    // Test a sequence: R U R' U' (where R' = R^-1)
    final Permutation R_inv = S8.operation().inverse(R);
    final Permutation U_inv = S8.operation().inverse(U);

    Permutation sequence = R;
    sequence = S8.operation().perform(sequence, U);
    sequence = S8.operation().perform(sequence, R_inv);
    sequence = S8.operation().perform(sequence, U_inv);

    System.out.println("Sequence R U R' U': " + sequence);
    System.out.println("Order of sequence: " + sequence.order());

    // This sequence should have a specific order
    assertTrue(sequence.order() > 1, "Non-trivial sequence should not be identity");

    // Test: Solving - apply sequence multiple times to return to identity
    Permutation state = S8.operation().identity();
    int moves = 0;
    do {
      state = S8.operation().perform(state, sequence);
      moves++;
    } while (!state.isIdentity() && moves < 100);

    System.out.println("Sequence repeated " + moves + " times returns to solved state");
    assertTrue(state.isIdentity(), "Repeated application should return to identity");
    assertEquals(sequence.order(), moves, "Number of repetitions should equal order");
  }

  /**
   * Tests group action: how permutations act on sets.
   */
  @Test
  void testGroupAction() {
    final SymmetricGroup S5 = new SymmetricGroup(5);

    // Start with an ordered list
    final String[] items = {"A", "B", "C", "D", "E"};

    // Apply permutation (0 2 4)(1 3)
    final Permutation p = S5.fromCycles(Arrays.asList(
        Arrays.asList(0, 2, 4),
        Arrays.asList(1, 3)
    ));

    // Apply the permutation to rearrange items
    final String[] permuted = new String[5];
    for (int i = 0; i < 5; i++) {
      permuted[i] = items[p.apply(i)];
    }

    assertArrayEquals(new String[]{"C", "D", "E", "B", "A"}, permuted);

    System.out.println("Original: " + Arrays.toString(items));
    System.out.println("After applying " + p + ": " + Arrays.toString(permuted));

    // Apply inverse to get back original
    final Permutation p_inv = S5.operation().inverse(p);
    final String[] restored = new String[5];
    for (int i = 0; i < 5; i++) {
      restored[i] = permuted[p_inv.apply(i)];
    }

    assertArrayEquals(items, restored);
  }

  /**
   * Tests conjugacy classes (permutations with same cycle type).
   */
  @Test
  void testCycleTypes() {
    final SymmetricGroup S4 = new SymmetricGroup(4);

    // All 3-cycles have the same cycle structure
    final Permutation cycle1 = S4.cycle(0, 1, 2);
    final Permutation cycle2 = S4.cycle(0, 2, 3);

    // They have the same order
    assertEquals(cycle1.order(), cycle2.order());

    // They are conjugate (there exists g such that g * cycle1 * g^-1 = cycle2)
    // This is a property of permutations with the same cycle type

    System.out.println("Cycle 1: " + cycle1 + ", order: " + cycle1.order());
    System.out.println("Cycle 2: " + cycle2 + ", order: " + cycle2.order());
  }

  /**
   * Tests random permutation generation.
   */
  @Test
  void testRandomPermutations() {
    final SymmetricGroup S10 = new SymmetricGroup(10);

    // Generate 100 random permutations
    for (int i = 0; i < 100; i++) {
      final Permutation random = S10.uniformRandomElement();

      // Verify it's a valid permutation
      assertTrue(S10.hasElement(random.getMapping()));

      // Verify it has an inverse
      final Permutation inverse = S10.operation().inverse(random);
      assertEquals(S10.operation().identity(),
          S10.operation().perform(random, inverse));
    }
  }

}
