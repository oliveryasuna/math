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

import java.math.BigInteger;
import java.util.stream.Stream;

/**
 * Represents a finite group.
 *
 * @param <S> The type of subclass.
 * @param <E> The type of elements.
 *
 * @author Oliver Yasuna
 */
public interface FiniteGroup<S extends FiniteGroup<S, E>, E extends FiniteGroupElement<E, S>> extends Group<S, E> {

  /**
   * Gets the order of the group.
   *
   * @return The order of the group.
   *
   * @implSpec Must return the same as {@link #elementCount()}.
   * @implNote By default, returns the result of {@link #elementCount()}.
   */
  default BigInteger order() {
    return elementCount();
  }

  /**
   * @implSpec Since a finite group has a finite set of elements, this method must be implemented and must not throw a
   *     {@link UnsupportedOperationException}.
   */
  @Override
  Stream<E> elements();

  /**
   * @implSpec Since a finite group has a finite set of elements, this method must be implemented and must not throw a
   *     {@link UnsupportedOperationException}.
   */
  @Override
  BigInteger elementCount();

}
