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

package com.oliveryasuna.math.util;

import com.oliveryasuna.commons.language.condition.Arguments;
import com.oliveryasuna.commons.language.exception.UnsupportedInstantiationException;
import com.oliveryasuna.commons.language.marker.Utility;

import java.math.BigInteger;
import java.util.Random;

/**
 * Various utilities relating to {@link BigInteger}.
 *
 * @author Oliver Yasuna
 */
@Utility
public final class BigIntegerUtils {

  // Static utility methods
  //--------------------------------------------------

  public static BigInteger random(final BigInteger boundExclusive, final Random random) {
    Arguments.requireNotNull(boundExclusive);
    Arguments.requireNotNull(random);

    BigInteger result;

    do {
      result = new BigInteger(boundExclusive.bitLength(), random);
    } while(result.compareTo(boundExclusive) >= 0);

    return result;
  }

  public static boolean isInRange(final BigInteger value, final BigInteger lowerInclusive, final BigInteger upperExclusive) {
    Arguments.requireNotNull(value);
    Arguments.requireNotNull(lowerInclusive);
    Arguments.requireNotNull(upperExclusive);

    return (value.compareTo(lowerInclusive) >= 0 && value.compareTo(upperExclusive) < 0);
  }

  public static boolean isInRangeInclusive(final BigInteger value, final BigInteger lowerInclusive, final BigInteger upperInclusive) {
    Arguments.requireNotNull(value);
    Arguments.requireNotNull(lowerInclusive);
    Arguments.requireNotNull(upperInclusive);

    return (value.compareTo(lowerInclusive) >= 0 && value.compareTo(upperInclusive) <= 0);
  }

  public static BigInteger increment(final BigInteger value) {
    Arguments.requireNotNull(value);

    return value.add(BigInteger.ONE);
  }

  public static BigInteger decrement(final BigInteger value) {
    Arguments.requireNotNull(value);

    return value.subtract(BigInteger.ONE);
  }

  // Constructors
  //--------------------------------------------------

  private BigIntegerUtils() {
    super();

    throw new UnsupportedInstantiationException();
  }

}
