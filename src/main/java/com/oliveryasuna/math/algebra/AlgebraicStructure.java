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

package com.oliveryasuna.math.algebra;

import java.math.BigInteger;
import java.util.stream.Stream;

/**
 * Represents an algebraic structure.
 *
 * @param <S> The type of subclass.
 * @param <E> The type of elements.
 *
 * @author Oliver Yasuna
 */
public interface AlgebraicStructure<S extends AlgebraicStructure<S, E>, E extends AlgebraicElement<E, S>> {

  /**
   * Gets an element of this structure drawn uniformly at random.
   *
   * @return An element of this structure drawn uniformly at random.
   *
   * @throws UnsupportedOperationException If this structure does not support this operation.
   *                                       For example, the structure is boundless on either side.
   * @implSpec If implemented, must not throw {@link UnsupportedOperationException}.
   * @implNote By default, throws {@link UnsupportedOperationException}.
   */
  default E uniformRandomElement() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets a stream of all elements belonging to this structure.
   *
   * @return A stream of elements belonging to this structure.
   *
   * @throws UnsupportedOperationException If this structure does not support this operation.
   *                                       For example, the structure is boundless on the lower side.
   * @implSpec If implemented, must not throw {@link UnsupportedOperationException}.
   * @implSpec If {@link #elementCount()} is implemented, this method must also be.
   * @implNote By default, throws {@link UnsupportedOperationException}.
   */
  default Stream<E> elements() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the number of elements belonging to this structure.
   *
   * @return The number of elements belonging to this structure.
   *
   * @throws UnsupportedOperationException If this structure does not support this operation.
   *                                       For example, the structure is boundless on either side.
   * @implSpec If implemented, must not throw {@link UnsupportedOperationException}.
   * @implSpec If implemented, must also implement {@link #elements()}.
   * @implNote By default, throws {@link UnsupportedOperationException}.
   */
  default BigInteger elementCount() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the number of operations of this structure.
   *
   * @return The number of operations of this structure.
   */
  int operationCount();

}
