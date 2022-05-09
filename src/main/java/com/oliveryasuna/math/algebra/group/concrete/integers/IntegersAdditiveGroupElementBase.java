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

import com.oliveryasuna.commons.language.marker.Immutable;
import com.oliveryasuna.math.algebra.AbstractAlgebraicElement;
import com.oliveryasuna.math.algebra.group.CommutativeGroupElement;
import com.oliveryasuna.math.algebra.group.helper.AdditiveMagmaElement;

import java.math.BigInteger;

/**
 * Abstract representation of an element of the group of integers under addition.
 *
 * @author Oliver Yasuna
 */
@Immutable
public class IntegersAdditiveGroupElementBase<E extends IntegersAdditiveGroupElementBase<E, S>, S extends IntegersAdditiveGroupBase<S, E>> extends AbstractAlgebraicElement<E, S>
    implements CommutativeGroupElement<E, S>, AdditiveMagmaElement<E, S> {

  // Constructors
  //--------------------------------------------------

  protected IntegersAdditiveGroupElementBase(final BigInteger value, final S structure) {
    super(structure);

    this.value = value;
  }

  // Fields
  //--------------------------------------------------

  protected final BigInteger value;

  // Getters
  //--------------------------------------------------s

  public BigInteger getValue() {
    return value;
  }

  // TODO: Object methods
  //--------------------------------------------------

}
