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
import com.oliveryasuna.math.algebra.group.CyclicGroup;
import com.oliveryasuna.math.algebra.group.FiniteGroup;
import com.oliveryasuna.math.algebra.group.helper.MagmaCommon;
import com.oliveryasuna.math.algebra.group.operation.CommutativeGroupOperation;
import com.oliveryasuna.math.util.BigIntegerUtils;

import java.math.BigInteger;
import java.util.stream.Stream;

// TODO: Javadoc.
@Immutable
public abstract class IntegersModuloNAdditiveGroupBase
    <S extends IntegersModuloNAdditiveGroupBase<S, E>, E extends IntegersModuloNAdditiveGroupElementBase<E, S>>
    extends MagmaCommon<S, E, BigInteger> implements CommutativeGroup<S, E>, FiniteGroup<S, E>, CyclicGroup<S, E> {

  // Constructors
  //--------------------------------------------------

  protected IntegersModuloNAdditiveGroupBase(final BigInteger n) {
    super();

    Arguments.requireNotNull(n, "The group requires a modulo. Thus, it cannot be null.");
    Arguments.requireGreater(n.compareTo(BigInteger.ZERO), "The group requires a positive modulo.");

    this.operation = new AdditionOperation();

    this.n = n;

    this.generator = getElement(this.n); // TODO: Is this right?
  }

  // Fields
  //--------------------------------------------------

  protected final CommutativeGroupOperation<E> operation;

  protected final BigInteger n;

  protected final E generator;

  // Overrides
  //--------------------------------------------------

  // MagmaCommon
  //

  @Override
  public boolean hasElementSafe(final BigInteger value) {
    return BigIntegerUtils.isInRange(value, BigInteger.ZERO, getN());
  }

  // CommutativeGroup
  //

  @Override
  public CommutativeGroupOperation<E> operation() {
    return operation;
  }

  // FiniteGroup
  //

  @Override
  public Stream<E> elements() {
    return Stream.iterate(BigInteger.ZERO, i -> i.compareTo(getN()) < 0, BigIntegerUtils::increment) // TODO: Is this right?
        .map(this::getElementSafe);
  }

  @Override
  public BigInteger elementCount() {
    return n;
  }

  // CyclicGroup
  //

  @Override
  public E generator() {
    return generator;
  }

  // Getters
  //--------------------------------------------------

  public BigInteger getN() {
    return n;
  }

  // TODO: Object methods
  //--------------------------------------------------

  // Nested
  //--------------------------------------------------

  protected class AdditionOperation implements CommutativeGroupOperation<E> {

    // Constructors
    //--------------------------------------------------

    protected AdditionOperation() {
      super();

      this.identity = IntegersModuloNAdditiveGroupBase.this.getElement(BigInteger.ZERO);
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
      return IntegersModuloNAdditiveGroupBase.this.getElement(augend.getValue().add(addend.getValue()).mod(IntegersModuloNAdditiveGroupBase.this.getN()));
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
      return IntegersModuloNAdditiveGroupBase.this.getElementSafe(element.getValue().modInverse(IntegersModuloNAdditiveGroupBase.this.getN()));
    }

    // TODO: Object methods
    //--------------------------------------------------

  }

}
