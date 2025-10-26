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
import com.oliveryasuna.math.algebra.AbstractAlgebraicElement;
import com.oliveryasuna.math.algebra.group.GroupElement;
import com.oliveryasuna.math.algebra.group.helper.MultiplicativeMagmaElement;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a permutation of n elements.
 * <p>
 * A permutation is a bijective mapping from {0, 1, ..., n-1} to itself.
 * Permutations can be composed (multiplied) and inverted.
 * <p>
 * This implementation uses cycle notation internally and can convert to/from
 * array notation (where mapping[i] represents where element i maps to).
 *
 * @author Oliver Yasuna
 */
@Immutable
public class Permutation extends AbstractAlgebraicElement<Permutation, SymmetricGroup>
    implements GroupElement<Permutation, SymmetricGroup>, MultiplicativeMagmaElement<Permutation, SymmetricGroup> {

  // Constructors
  //--------------------------------------------------

  /**
   * Creates a permutation from an array representation.
   * <p>
   * The array mapping[i] = j means element i maps to element j.
   *
   * @param mapping   The permutation mapping.
   * @param structure The symmetric group.
   *
   * @throws IllegalArgumentException if the mapping is not a valid permutation.
   */
  protected Permutation(final int[] mapping, final SymmetricGroup structure) {
    super(structure);

    if (!isValidPermutation(mapping)) {
      throw new IllegalArgumentException("Invalid permutation: not a bijection");
    }

    this.mapping = Arrays.copyOf(mapping, mapping.length);
    this.cycles = computeCycles(mapping);
  }

  /**
   * Creates a permutation from cycle notation.
   * <p>
   * For example: [[0, 2, 1], [3, 4]] represents (0 2 1)(3 4).
   *
   * @param cycles    The cycles.
   * @param n         The size of the permutation domain.
   * @param structure The symmetric group.
   */
  protected Permutation(final List<List<Integer>> cycles, final int n, final SymmetricGroup structure) {
    super(structure);

    this.cycles = cycles.stream()
        .map(ArrayList::new)
        .collect(Collectors.toList());
    this.mapping = cyclesToMapping(cycles, n);
  }

  // Fields
  //--------------------------------------------------

  protected final int[] mapping;

  protected final List<List<Integer>> cycles;

  // Methods
  //--------------------------------------------------

  /**
   * Gets the size of the permutation domain.
   *
   * @return The size n (permutes {0, 1, ..., n-1}).
   */
  public int getSize() {
    return mapping.length;
  }

  /**
   * Applies this permutation to an element.
   *
   * @param element The element to permute.
   *
   * @return The image of the element under this permutation.
   */
  public int apply(final int element) {
    if (element < 0 || element >= mapping.length) {
      throw new IllegalArgumentException("Element out of range: " + element);
    }
    return mapping[element];
  }

  /**
   * Gets the mapping array (where mapping[i] = j means i maps to j).
   *
   * @return A copy of the mapping array.
   */
  public int[] getMapping() {
    return Arrays.copyOf(mapping, mapping.length);
  }

  /**
   * Gets the cycle decomposition of this permutation.
   *
   * @return The cycles (as a list of lists).
   */
  public List<List<Integer>> getCycles() {
    return cycles.stream()
        .map(ArrayList::new)
        .collect(Collectors.toList());
  }

  /**
   * Gets the order of this permutation (smallest positive k such that p^k = identity).
   *
   * @return The order of the permutation.
   */
  public int order() {
    // The order is the LCM of all cycle lengths
    int lcm = 1;
    for (final List<Integer> cycle : cycles) {
      if (cycle.size() > 1) {
        lcm = lcm(lcm, cycle.size());
      }
    }
    return lcm;
  }

  /**
   * Checks if this is the identity permutation.
   *
   * @return true if this is the identity, false otherwise.
   */
  public boolean isIdentity() {
    for (int i = 0; i < mapping.length; i++) {
      if (mapping[i] != i) {
        return false;
      }
    }
    return true;
  }

  /**
   * Gets the sign (parity) of this permutation.
   * <p>
   * Returns 1 for even permutations, -1 for odd permutations.
   *
   * @return The sign of the permutation.
   */
  public int sign() {
    // Count inversions to determine parity
    int inversions = 0;
    for (int i = 0; i < mapping.length; i++) {
      for (int j = i + 1; j < mapping.length; j++) {
        if (mapping[i] > mapping[j]) {
          inversions++;
        }
      }
    }
    return (inversions % 2 == 0) ? 1 : -1;
  }

  // Object methods
  //--------------------------------------------------

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final Permutation that = (Permutation) o;

    return Arrays.equals(mapping, that.mapping);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(mapping);
  }

  @Override
  public String toString() {
    // Use cycle notation for display
    if (isIdentity()) {
      return "()";
    }

    final StringBuilder sb = new StringBuilder();
    for (final List<Integer> cycle : cycles) {
      if (cycle.size() > 1) {
        sb.append("(");
        for (int i = 0; i < cycle.size(); i++) {
          if (i > 0) sb.append(" ");
          sb.append(cycle.get(i));
        }
        sb.append(")");
      }
    }

    return sb.length() > 0 ? sb.toString() : "()";
  }

  // Helper methods
  //--------------------------------------------------

  private static boolean isValidPermutation(final int[] mapping) {
    final int n = mapping.length;
    final boolean[] seen = new boolean[n];

    for (final int val : mapping) {
      if (val < 0 || val >= n || seen[val]) {
        return false;
      }
      seen[val] = true;
    }

    return true;
  }

  private static List<List<Integer>> computeCycles(final int[] mapping) {
    final int n = mapping.length;
    final boolean[] visited = new boolean[n];
    final List<List<Integer>> cycles = new ArrayList<>();

    for (int i = 0; i < n; i++) {
      if (!visited[i]) {
        final List<Integer> cycle = new ArrayList<>();
        int current = i;

        do {
          visited[current] = true;
          cycle.add(current);
          current = mapping[current];
        } while (current != i);

        cycles.add(cycle);
      }
    }

    return cycles;
  }

  private static int[] cyclesToMapping(final List<List<Integer>> cycles, final int n) {
    final int[] mapping = new int[n];

    // Initialize to identity
    for (int i = 0; i < n; i++) {
      mapping[i] = i;
    }

    // Apply each cycle
    for (final List<Integer> cycle : cycles) {
      for (int i = 0; i < cycle.size(); i++) {
        final int from = cycle.get(i);
        final int to = cycle.get((i + 1) % cycle.size());
        mapping[from] = to;
      }
    }

    return mapping;
  }

  private static int gcd(final int a, final int b) {
    return b == 0 ? a : gcd(b, a % b);
  }

  private static int lcm(final int a, final int b) {
    return (a * b) / gcd(a, b);
  }

}