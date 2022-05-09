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

package com.oliveryasuna.math.algebra.group.concrete.realnumbers;

import com.oliveryasuna.commons.language.condition.Arguments;
import com.oliveryasuna.commons.language.marker.Immutable;
import com.oliveryasuna.math.algebra.CommonAlgebraicStructure;
import com.oliveryasuna.math.algebra.group.CommutativeGroup;
import com.oliveryasuna.math.algebra.group.operation.CommutativeGroupOperation;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Abstract representation of the group of real numbers without \(0\) under multiplication.
 * <p>
 * Mathematically defined as \(\left(\mathbb{R}\setminus\left{0\right},\times\right)\).
 *
 * @author Oliver Yasuna
 */
@Immutable
public abstract class RealNumbersMultiplicativeGroupBase
    <S extends RealNumbersMultiplicativeGroupBase<S, E>, E extends RealNumbersMultiplicativeGroupElementBase<E, S>> extends CommonAlgebraicStructure<S, E, BigDecimal>
    implements CommutativeGroup<S, E> {

  // Constructors
  //--------------------------------------------------

  public RealNumbersMultiplicativeGroupBase(final RoundingMode roundingMode) {
    super();

    Arguments.requireNotNull(roundingMode);

    this.operation = new MultiplicationOperation();

    this.roundingMode = roundingMode;
  }

  // Fields
  //--------------------------------------------------

  protected final CommutativeGroupOperation<E> operation;

  protected final RoundingMode roundingMode;

  // Overrides
  //--------------------------------------------------

  // CommonAlgebraicStructure
  //

  @Override
  public boolean hasElementSafe(final BigDecimal value) {
    return !value.equals(BigDecimal.ZERO);
  }


  // CommutativeGroup
  //

  @Override
  public CommutativeGroupOperation<E> operation() {
    return operation;
  }

  // Getters
  //--------------------------------------------------

  public RoundingMode getRoundingMode() {
    return roundingMode;
  }

  // TODO: Object methods
  //--------------------------------------------------

  // Nested
  //--------------------------------------------------

  protected class MultiplicationOperation implements CommutativeGroupOperation<E> {

    // Constructors
    //--------------------------------------------------

    protected MultiplicationOperation() {
      super();

      this.identity = RealNumbersMultiplicativeGroupBase.this.getElement(BigDecimal.ONE);
    }

    // Fields
    //--------------------------------------------------

    protected final E identity;

    // Overrides
    //--------------------------------------------------

    // BinaryOperation
    //

    @Override
    public E perform(final E multiplier, final E multiplicand) {
      return RealNumbersMultiplicativeGroupBase.this.getElement(multiplier.getValue().multiply(multiplicand.getValue()));
    }

    // Identity
    //

    @Override
    public E identity() {
      return identity;
    }

    // Inverse
    //

    @Override
    public E inverse(final E element) {
      return RealNumbersMultiplicativeGroupBase.this.getElement(BigDecimal.ONE.divide(element.getValue(), RealNumbersMultiplicativeGroupBase.this.getRoundingMode()));
    }

    // TODO: Object methods
    //--------------------------------------------------

  }

}
