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

import com.oliveryasuna.commons.language.condition.Arguments;

public abstract class CommonAlgebraicStructure<S extends AlgebraicStructure<S, E>, E extends AlgebraicElement<E, S>, V> {

  // Constructors
  //--------------------------------------------------

  protected CommonAlgebraicStructure() {
    super();
  }

  // Methods
  //--------------------------------------------------

  // Elements
  //

  public abstract E getElementSafe(V value);

  /**
   * Gets an element of the structure by value.
   *
   * @param value The value.
   *
   * @return The element of the structure.
   *
   * @implSpec Must call {@link #getElementSafe(Object)}.
   */
  public E getElement(final V value) {
    Arguments.requireTrue(hasElement(value), "The structure does not contain the element: " + value + ".");

    return getElementSafe(value);
  }

  public abstract boolean hasElementSafe(V value);

  /**
   * Gets whether an element belongs to the structure by value.
   *
   * @param value The value.
   *
   * @return {@code true}, if the element belongs to the structure; {@code false}, otherwise.
   *
   * @implSpec Must call {@link #hasElementSafe(Object)}.
   */
  public boolean hasElement(final V value) {
    return (value != null && hasElementSafe(value));
  }

}
