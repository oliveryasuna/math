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

package com.oliveryasuna.math.algebra.group.concrete.ellipticcurve;

import com.oliveryasuna.commons.language.marker.Immutable;
import com.oliveryasuna.math.algebra.AbstractAlgebraicElement;
import com.oliveryasuna.math.algebra.group.CommutativeGroupElement;
import com.oliveryasuna.math.algebra.group.helper.AdditiveMagmaElement;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Represents a point on an elliptic curve.
 *
 * @author Oliver Yasuna
 */
@Immutable
public class EllipticCurvePoint extends AbstractAlgebraicElement<EllipticCurvePoint, EllipticCurveGroup>
    implements CommutativeGroupElement<EllipticCurvePoint, EllipticCurveGroup>, AdditiveMagmaElement<EllipticCurvePoint, EllipticCurveGroup> {

  // Constructors
  //--------------------------------------------------

  /**
   * Creates the point at infinity (identity element).
   *
   * @param structure The elliptic curve group.
   */
  protected EllipticCurvePoint(final EllipticCurveGroup structure) {
    super(structure);

    this.x = null;
    this.y = null;
    this.infinity = true;
  }

  /**
   * Creates a finite point on the elliptic curve.
   *
   * @param x         The x-coordinate.
   * @param y         The y-coordinate.
   * @param structure The elliptic curve group.
   */
  protected EllipticCurvePoint(final BigInteger x, final BigInteger y, final EllipticCurveGroup structure) {
    super(structure);

    this.x = x;
    this.y = y;
    this.infinity = false;
  }

  // Fields
  //--------------------------------------------------

  protected final BigInteger x;

  protected final BigInteger y;

  protected final boolean infinity;

  // Methods
  //--------------------------------------------------

  public boolean isInfinity() {
    return infinity;
  }

  public BigInteger getX() {
    if (infinity) {
      throw new IllegalStateException("Point at infinity has no x-coordinate");
    }
    return x;
  }

  public BigInteger getY() {
    if (infinity) {
      throw new IllegalStateException("Point at infinity has no y-coordinate");
    }
    return y;
  }

  /**
   * Returns the negation (inverse) of this point.
   *
   * @return The negation of this point.
   */
  public EllipticCurvePoint negate() {
    return getStructure().operation().inverse(this);
  }

  /**
   * Scalar multiplication: computes k * P using double-and-add algorithm.
   *
   * @param k The scalar multiplier.
   *
   * @return The result of k * P.
   */
  public EllipticCurvePoint multiply(final BigInteger k) {
    if (k.signum() < 0) {
      return multiply(k.negate()).negate();
    }

    if (k.equals(BigInteger.ZERO)) {
      return getStructure().operation().identity();
    }

    if (k.equals(BigInteger.ONE)) {
      return this;
    }

    // Double-and-add algorithm
    EllipticCurvePoint result = getStructure().operation().identity();
    EllipticCurvePoint addend = this;

    BigInteger scalar = k;
    while (scalar.signum() > 0) {
      if (scalar.testBit(0)) {
        result = getStructure().operation().perform(result, addend);
      }
      addend = getStructure().operation().perform(addend, addend);
      scalar = scalar.shiftRight(1);
    }

    return result;
  }

  // Object methods
  //--------------------------------------------------

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final EllipticCurvePoint that = (EllipticCurvePoint) o;

    if (infinity != that.infinity) return false;
    if (infinity) return true; // Both at infinity

    return Objects.equals(x, that.x) && Objects.equals(y, that.y);
  }

  @Override
  public int hashCode() {
    if (infinity) return 0;
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    if (infinity) {
      return "O"; // Point at infinity
    }
    return "(" + x + ", " + y + ")";
  }

  // Nested
  //--------------------------------------------------

  /**
   * Represents coordinates of a point (or infinity).
   */
  public static class Coordinates {

    // Static
    //--------------------------------------------------

    public static Coordinates infinity() {
      return INFINITY;
    }

    private static final Coordinates INFINITY = new Coordinates(null, null);

    // Constructors
    //--------------------------------------------------

    public Coordinates(final BigInteger x, final BigInteger y) {
      this.x = x;
      this.y = y;
    }

    // Fields
    //--------------------------------------------------

    protected final BigInteger x;

    protected final BigInteger y;

    // Methods
    //--------------------------------------------------

    public boolean isInfinity() {
      return x == null && y == null;
    }

    public BigInteger getX() {
      if (isInfinity()) {
        throw new IllegalStateException("Point at infinity has no x-coordinate");
      }
      return x;
    }

    public BigInteger getY() {
      if (isInfinity()) {
        throw new IllegalStateException("Point at infinity has no y-coordinate");
      }
      return y;
    }

  }

}
