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
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.oliveryasuna.math.algebra.group.concrete.permutation;

import com.oliveryasuna.commons.language.marker.Immutable;
import com.oliveryasuna.math.algebra.CommonAlgebraicStructure;
import com.oliveryasuna.math.algebra.group.FiniteGroup;
import com.oliveryasuna.math.algebra.group.Group;
import com.oliveryasuna.math.algebra.group.operation.GroupOperation;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Stream;

/**
 * Represents the symmetric group S_n.
 * <p>
 * The symmetric group S_n is the group of all permutations of n elements.
 * It has order n! and is one of the most important groups in mathematics.
 * <p>
 * The group operation is composition of permutations.
 *
 * @author Oliver Yasuna
 */
@Immutable
public class SymmetricGroup extends CommonAlgebraicStructure<SymmetricGroup, Permutation, int[]>
    implements Group<SymmetricGroup, Permutation>, FiniteGroup<SymmetricGroup, Permutation> {

  // Constructors
  //--------------------------------------------------

  /**
   * Creates the symmetric group S_n.
   *
   * @param n The size of the permutation domain (must be positive).
   *
   * @throws IllegalArgumentException if n <= 0.
   */
  public SymmetricGroup(final int n) {
    super();

    if (n <= 0) {
      throw new IllegalArgumentException("n must be positive");
    }

    this.n = n;
    this.operation = new CompositionOperation();
  }

  // Fields
  //--------------------------------------------------

  protected final int n;

  protected final GroupOperation<Permutation> operation;

  // Methods
  //--------------------------------------------------

  /**
   * Gets the size of the permutation domain.
   *
   * @return The value of n for S_n.
   */
  public int getN() {
    return n;
  }

  /**
   * Creates a permutation from an array mapping.
   *
   * @param mapping The permutation mapping.
   *
   * @return The permutation element.
   */
  public Permutation fromMapping(final int[] mapping) {
    return getElement(mapping);
  }

  /**
   * Creates a permutation from cycle notation.
   * <p>
   * Example: fromCycles(Arrays.asList(Arrays.asList(0, 2, 1), Arrays.asList(3, 4)))
   * creates the permutation (0 2 1)(3 4).
   *
   * @param cycles The cycles.
   *
   * @return The permutation element.
   */
  public Permutation fromCycles(final List<List<Integer>> cycles) {
    return new Permutation(cycles, n, this);
  }

  /**
   * Creates a transposition (2-cycle) that swaps i and j.
   *
   * @param i The first element.
   * @param j The second element.
   *
   * @return The transposition (i j).
   */
  public Permutation transposition(final int i, final int j) {
    if (i < 0 || i >= n || j < 0 || j >= n) {
      throw new IllegalArgumentException("Indices out of range");
    }

    final int[] mapping = new int[n];
    for (int k = 0; k < n; k++) {
      mapping[k] = k;
    }

    mapping[i] = j;
    mapping[j] = i;

    return new Permutation(mapping, this);
  }

  /**
   * Creates a cycle permutation.
   *
   * @param elements The elements in the cycle (in order).
   *
   * @return The cycle permutation.
   */
  public Permutation cycle(final int... elements) {
    return fromCycles(Collections.singletonList(Arrays.asList(
        Arrays.stream(elements).boxed().toArray(Integer[]::new)
    )));
  }

  // Overrides
  //--------------------------------------------------

  // CommonAlgebraicStructure
  //

  @Override
  public Permutation getElementSafe(final int[] mapping) {
    if (mapping.length != n) {
      throw new IllegalArgumentException("Permutation size must be " + n);
    }
    return new Permutation(mapping, this);
  }

  @Override
  public boolean hasElementSafe(final int[] mapping) {
    if (mapping.length != n) {
      return false;
    }

    final boolean[] seen = new boolean[n];
    for (final int val : mapping) {
      if (val < 0 || val >= n || seen[val]) {
        return false;
      }
      seen[val] = true;
    }

    return true;
  }

  // Group
  //

  @Override
  public GroupOperation<Permutation> operation() {
    return operation;
  }

  // FiniteGroup
  //

  @Override
  public Permutation uniformRandomElement() {
    final int[] mapping = new int[n];
    for (int i = 0; i < n; i++) {
      mapping[i] = i;
    }

    // Fisher-Yates shuffle
    final Random random = new SecureRandom();
    for (int i = n - 1; i > 0; i--) {
      final int j = random.nextInt(i + 1);
      final int temp = mapping[i];
      mapping[i] = mapping[j];
      mapping[j] = temp;
    }

    return new Permutation(mapping, this);
  }

  @Override
  public Stream<Permutation> elements() {
    // Generate all permutations using lexicographic ordering
    return generatePermutations(n).map(mapping -> new Permutation(mapping, this));
  }

  @Override
  public BigInteger elementCount() {
    return factorial(n);
  }

  // Nested
  //--------------------------------------------------

  protected class CompositionOperation implements GroupOperation<Permutation> {

    // Constructors
    //--------------------------------------------------

    protected CompositionOperation() {
      super();

      final int[] identityMapping = new int[n];
      for (int i = 0; i < n; i++) {
        identityMapping[i] = i;
      }
      this.identity = new Permutation(identityMapping, SymmetricGroup.this);
    }

    // Fields
    //--------------------------------------------------

    protected final Permutation identity;

    // Overrides
    //--------------------------------------------------

    // BinaryOperation
    //

    @Override
    public Permutation perform(final Permutation p1, final Permutation p2) {
      // Composition: (p1 ∘ p2)(x) = p1(p2(x))
      final int[] mapping1 = p1.getMapping();
      final int[] mapping2 = p2.getMapping();
      final int[] result = new int[n];

      for (int i = 0; i < n; i++) {
        result[i] = mapping1[mapping2[i]];
      }

      return new Permutation(result, SymmetricGroup.this);
    }

    // Identity
    //

    @Override
    public Permutation identity() {
      return identity;
    }

    // Inverse
    //

    @Override
    public Permutation inverse(final Permutation permutation) {
      final int[] mapping = permutation.getMapping();
      final int[] inverse = new int[n];

      for (int i = 0; i < n; i++) {
        inverse[mapping[i]] = i;
      }

      return new Permutation(inverse, SymmetricGroup.this);
    }

  }

  // Static helper methods
  //--------------------------------------------------

  private static Stream<int[]> generatePermutations(final int n) {
    if (n == 0) {
      return Stream.of(new int[0]);
    }

    if (n == 1) {
      return Stream.of(new int[]{0});
    }

    // Generate permutations iteratively to avoid stack overflow
    return Stream.iterate(
        new PermutationIterator(n),
        PermutationIterator::hasNext,
        iter -> {
          iter.next();
          return iter;
        }
    ).map(PermutationIterator::current);
  }

  private static BigInteger factorial(final int n) {
    BigInteger result = BigInteger.ONE;
    for (int i = 2; i <= n; i++) {
      result = result.multiply(BigInteger.valueOf(i));
    }
    return result;
  }

  // Helper iterator class
  //--------------------------------------------------

  private static class PermutationIterator {
    private final int n;
    private final int[] current;
    private boolean hasNext;

    PermutationIterator(final int n) {
      this.n = n;
      this.current = new int[n];
      for (int i = 0; i < n; i++) {
        current[i] = i;
      }
      this.hasNext = true;
    }

    boolean hasNext() {
      return hasNext;
    }

    int[] current() {
      return Arrays.copyOf(current, n);
    }

    void next() {
      // Generate next permutation in lexicographic order
      int i = n - 2;
      while (i >= 0 && current[i] >= current[i + 1]) {
        i--;
      }

      if (i < 0) {
        hasNext = false;
        return;
      }

      int j = n - 1;
      while (current[j] <= current[i]) {
        j--;
      }

      // Swap
      int temp = current[i];
      current[i] = current[j];
      current[j] = temp;

      // Reverse suffix
      int left = i + 1;
      int right = n - 1;
      while (left < right) {
        temp = current[left];
        current[left] = current[right];
        current[right] = temp;
        left++;
        right--;
      }
    }
  }

}