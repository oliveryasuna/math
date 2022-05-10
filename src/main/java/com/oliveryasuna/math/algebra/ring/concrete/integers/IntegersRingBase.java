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

package com.oliveryasuna.math.algebra.ring.concrete.integers;

import com.oliveryasuna.commons.language.condition.Arguments;
import com.oliveryasuna.commons.language.marker.Immutable;
import com.oliveryasuna.math.algebra.CommonAlgebraicStructure;
import com.oliveryasuna.math.algebra.property.Distributivity;
import com.oliveryasuna.math.algebra.ring.CyclicRing;
import com.oliveryasuna.math.algebra.ring.operation.CommutativeRingOperation2;
import com.oliveryasuna.math.algebra.ring.operation.RingOperation1;

import java.math.BigInteger;

@Immutable
public abstract class IntegersRingBase<S extends IntegersRingBase<S, E>, E extends IntegersRingElementBase<E, S>>
    extends CommonAlgebraicStructure<S, E, BigInteger> implements CyclicRing<S, E> {

  // Constructors
  //--------------------------------------------------

  public IntegersRingBase(final BigInteger generator) {
    super();

    Arguments.requireNotNull(generator, "This structure is a cyclic ring. It requires a non-null generator.");

    this.operation1 = new AdditionOperation();
    this.operation2 = new MultiplicationOperation();

    this.generator = getElement(generator);
  }

  // Fields
  //--------------------------------------------------

  protected final RingOperation1<E> operation1;

  protected final CommutativeRingOperation2<E> operation2;

  protected final E generator;

  // Overrides
  //--------------------------------------------------

  // CommonAlgebraicStructure
  //

  @Override
  public boolean hasElementSafe(final BigInteger value) {
    return true;
  }

  // Semiring
  //

  @Override
  public RingOperation1<E> operation1() {
    return operation1;
  }

  @Override
  public CommutativeRingOperation2<E> operation2() {
    return operation2;
  }

  // CyclicRing
  //

  @Override
  public E generator() {
    return generator;
  }

  // TODO: Object methods
  //--------------------------------------------------

  // Nested
  //--------------------------------------------------

  protected class AdditionOperation implements RingOperation1<E> {

    // Constructors
    //--------------------------------------------------

    protected AdditionOperation() {
      super();

      this.identity = IntegersRingBase.this.getElement(BigInteger.ZERO);
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
      return IntegersRingBase.this.getElementSafe(augend.getValue().add(addend.getValue()));
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
      return IntegersRingBase.this.getElementSafe(element.getValue().negate());
    }

    // TODO: Object methods
    //--------------------------------------------------

  }

  protected class MultiplicationOperation implements CommutativeRingOperation2<E> {

    // Constructors
    //--------------------------------------------------

    protected MultiplicationOperation() {
      super();

      this.distributivity = Distributivity.over(IntegersRingBase.this.operation1());

      this.identity = IntegersRingBase.this.getElement(BigInteger.ZERO);
    }

    // Fields
    //--------------------------------------------------

    protected final Distributivity distributivity;

    protected final E identity;

    // Overrides
    //--------------------------------------------------

    // BinaryOperation
    //

    @Override
    public E perform(final E augend, final E addend) {
      return IntegersRingBase.this.getElementSafe(augend.getValue().add(addend.getValue()));
    }

    // Distributive
    //

    @Override
    public Distributivity distributivity() {
      return distributivity;
    }

    // Identity
    //

    @Override
    public E identity() {
      return identity;
    }

    // TODO: Object methods
    //--------------------------------------------------

  }

}
