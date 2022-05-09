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

package com.oliveryasuna.math.algebra.group;

import com.oliveryasuna.math.algebra.AlgebraicStructure;
import com.oliveryasuna.math.algebra.group.operation.MagmaOperation;

/**
 * Represents a magma.
 * <p>
 * A magma is an algebraic structure containing a set \(S\) closed under a single binary operation \(\cdot\).
 * In mathematical notation: \(a,b\in S\implies a\cdot b\in S\).
 * <p>
 * This representation defines a magma as the pair \(\left(S,\cdot\right)\).
 *
 * @param <S> The type of subclass.
 * @param <E> The type of elements.
 *
 * @author Oliver Yasuna
 */
public interface Magma<S extends Magma<S, E>, E extends MagmaElement<E, S>> extends AlgebraicStructure<S, E> {

  /**
   * The binary operation.
   *
   * @return The binary operation.
   */
  MagmaOperation<E> operation();

  /**
   * @return {@code 1}.
   */
  @Override
  default int operationCount() {
    return 1;
  }

}
