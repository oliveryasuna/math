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
import com.oliveryasuna.math.algebra.CommonAlgebraicStructure;
import com.oliveryasuna.math.algebra.group.CommutativeGroup;
import com.oliveryasuna.math.algebra.group.operation.CommutativeGroupOperation;

import java.math.BigInteger;

/**
 * Represents an elliptic curve group over a prime field.
 * <p>
 * Defines the elliptic curve \(y^2 = x^3 + ax + b \pmod{p}\) where \(p\) is prime.
 * <p>
 * The group operation is point addition, with the point at infinity as the identity element.
 *
 * @author Oliver Yasuna
 */
@Immutable
public class EllipticCurveGroup
    extends CommonAlgebraicStructure<EllipticCurveGroup, EllipticCurvePoint, EllipticCurvePoint.Coordinates>
    implements CommutativeGroup<EllipticCurveGroup, EllipticCurvePoint> {

  // Constructors
  //--------------------------------------------------

  /**
   * Creates an elliptic curve group.
   *
   * @param a The coefficient a in y^2 = x^3 + ax + b.
   * @param b The coefficient b in y^2 = x^3 + ax + b.
   * @param p The prime modulus.
   *
   * @throws IllegalArgumentException if the curve is singular (4a^3 + 27b^2 ≡ 0 mod p).
   */
  public EllipticCurveGroup(final BigInteger a, final BigInteger b, final BigInteger p) {
    super();

    this.a = a.mod(p);
    this.b = b.mod(p);
    this.p = p;

    // Check curve is non-singular: 4a^3 + 27b^2 != 0 (mod p)
    final BigInteger discriminant = BigInteger.valueOf(4)
        .multiply(a.modPow(BigInteger.valueOf(3), p))
        .add(BigInteger.valueOf(27).multiply(b.modPow(BigInteger.TWO, p)))
        .mod(p);

    if (discriminant.equals(BigInteger.ZERO)) {
      throw new IllegalArgumentException("Curve is singular (discriminant is zero)");
    }

    this.operation = new PointAdditionOperation();
  }

  // Fields
  //--------------------------------------------------

  protected final BigInteger a;

  protected final BigInteger b;

  protected final BigInteger p;

  protected final CommutativeGroupOperation<EllipticCurvePoint> operation;

  // Overrides
  //--------------------------------------------------

  // CommonAlgebraicStructure
  //

  @Override
  public EllipticCurvePoint getElementSafe(final EllipticCurvePoint.Coordinates coords) {
    if (coords.isInfinity()) {
      return new EllipticCurvePoint(this);
    }
    return new EllipticCurvePoint(coords.getX(), coords.getY(), this);
  }

  @Override
  public boolean hasElementSafe(final EllipticCurvePoint.Coordinates coords) {
    if (coords.isInfinity()) {
      return true;
    }

    final BigInteger x = coords.getX();
    final BigInteger y = coords.getY();

    // Check: y^2 = x^3 + ax + b (mod p)
    final BigInteger lhs = y.modPow(BigInteger.TWO, p);
    final BigInteger rhs = x.modPow(BigInteger.valueOf(3), p)
        .add(a.multiply(x))
        .add(b)
        .mod(p);

    return lhs.equals(rhs);
  }

  // CommutativeGroup
  //

  @Override
  public CommutativeGroupOperation<EllipticCurvePoint> operation() {
    return operation;
  }

  // Getters
  //--------------------------------------------------

  public BigInteger getA() {
    return a;
  }

  public BigInteger getB() {
    return b;
  }

  public BigInteger getP() {
    return p;
  }

  // Nested
  //--------------------------------------------------

  protected class PointAdditionOperation implements CommutativeGroupOperation<EllipticCurvePoint> {

    // Constructors
    //--------------------------------------------------

    protected PointAdditionOperation() {
      super();

      this.identity = EllipticCurveGroup.this.getElement(EllipticCurvePoint.Coordinates.infinity());
    }

    // Fields
    //--------------------------------------------------

    protected final EllipticCurvePoint identity;

    // Overrides
    //--------------------------------------------------

    // BinaryOperation
    //

    @Override
    public EllipticCurvePoint perform(final EllipticCurvePoint p1, final EllipticCurvePoint p2) {
      // Identity cases
      if (p1.isInfinity()) return p2;
      if (p2.isInfinity()) return p1;

      final BigInteger x1 = p1.getX();
      final BigInteger y1 = p1.getY();
      final BigInteger x2 = p2.getX();
      final BigInteger y2 = p2.getY();

      // Check if p2 is the inverse of p1
      if (x1.equals(x2) && y1.add(y2).mod(p).equals(BigInteger.ZERO)) {
        return identity;
      }

      // Calculate slope
      final BigInteger slope;
      if (x1.equals(x2) && y1.equals(y2)) {
        // Point doubling: slope = (3x1^2 + a) / (2y1)
        final BigInteger numerator = BigInteger.valueOf(3)
            .multiply(x1.modPow(BigInteger.TWO, p))
            .add(a)
            .mod(p);
        final BigInteger denominator = BigInteger.TWO.multiply(y1).mod(p);
        slope = numerator.multiply(denominator.modInverse(p)).mod(p);
      } else {
        // Point addition: slope = (y2 - y1) / (x2 - x1)
        final BigInteger numerator = y2.subtract(y1).mod(p);
        final BigInteger denominator = x2.subtract(x1).mod(p);
        slope = numerator.multiply(denominator.modInverse(p)).mod(p);
      }

      // Calculate result point
      final BigInteger x3 = slope.modPow(BigInteger.TWO, p).subtract(x1).subtract(x2).mod(p);
      final BigInteger y3 = slope.multiply(x1.subtract(x3)).subtract(y1).mod(p);

      return EllipticCurveGroup.this.getElementSafe(new EllipticCurvePoint.Coordinates(x3, y3));
    }

    // Identity
    //

    @Override
    public EllipticCurvePoint identity() {
      return identity;
    }

    // Inverse
    //

    @Override
    public EllipticCurvePoint inverse(final EllipticCurvePoint point) {
      if (point.isInfinity()) {
        return point;
      }

      // The inverse of (x, y) is (x, -y mod p)
      final BigInteger x = point.getX();
      final BigInteger y = point.getY().negate().mod(p);

      return EllipticCurveGroup.this.getElementSafe(new EllipticCurvePoint.Coordinates(x, y));
    }

  }

}
