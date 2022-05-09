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

package com.oliveryasuna.math.algebra.group.concrete.integers;

import com.oliveryasuna.commons.language.condition.Arguments;
import com.oliveryasuna.commons.language.marker.Immutable;
import com.oliveryasuna.math.algebra.group.CommutativeGroup;
import com.oliveryasuna.math.algebra.group.operation.CommutativeGroupOperation;

import java.math.BigInteger;

/**
 * Abstract representation of the group of integers under addition.
 * <p>
 * Mathematically defined as \(\left(\mathbb{Z},+\right)\).
 *
 * @author Oliver Yasuna
 */
@Immutable
public abstract class IntegersAdditiveGroupBase<S extends IntegersAdditiveGroupBase<S, E>, E extends IntegersAdditiveGroupElementBase<E, S>> implements CommutativeGroup<S, E> {

  // Constructors
  //--------------------------------------------------

  public IntegersAdditiveGroupBase() {
    super();

    this.operation = new AdditionOperation();
  }

  // Fields
  //--------------------------------------------------

  protected final CommutativeGroupOperation<E> operation;

  // Methods
  //--------------------------------------------------

  // Elements
  //

  public abstract E getElementSafe(final BigInteger value);

  public E getElement(final BigInteger value) {
    Arguments.requireTrue(hasElement(value), "The group does not contain the element: " + value + ".");

    return getElementSafe(value);
  }

  public boolean hasElement(final BigInteger value) {
    return (value != null);
  }

  // Overrides
  //--------------------------------------------------

  // CommutativeGroup
  //

  @Override
  public CommutativeGroupOperation<E> operation() {
    return operation;
  }

  // Nested
  //--------------------------------------------------

  protected class AdditionOperation implements CommutativeGroupOperation<E> {

    // Constructors
    //--------------------------------------------------

    protected AdditionOperation() {
      super();

      this.identity = IntegersAdditiveGroupBase.this.getElement(BigInteger.ZERO);
    }

    // Fields
    //--------------------------------------------------

    protected final E identity;

    // Overrides
    //--------------------------------------------------

    // BinaryOperation
    //

    @Override
    public E perform(final E augend, final E addend) {
      return IntegersAdditiveGroupBase.this.getElement(augend.getValue().add(addend.getValue()));
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
      return IntegersAdditiveGroupBase.this.getElement(element.getValue().negate());
    }

  }

}
