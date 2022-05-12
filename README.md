# math

Growing mathematics structures and components library for Java.

I've been teaching myself abstract algebra, and it fascinates me!
Right now, I am working on group- and ring-like structures.
I plan on adding more later (thinking logic operations).
If the library grows large enough, I will probably split it into multiple modules.

The original goal was to provide abstractions for my [crypto](https://github.com/oliveryasuna/math) library, but I imagine I will expand upon this library after
working on groups and rings.

This is **not** an alternative to or replacement for [commons-math](https://commons.apache.org/proper/commons-math/).

## Usage

Right now, the focus is on group- and ring-like structures.

Here is a minimalistic **naive** implementation of the multiplicative group of integers modulo n, <img src="https://latex.codecogs.com/svg.latex?\left(\mathbb{Z}/n\mathbb{Z}\right)^\times"/>.

```java
public class Zn
    extends CommonAlgebraicStructure<Zn, Zn.ZnElement, BigInteger>
    implements CommutativeGroup<Zn, Zn.ZnElement>, FiniteGroup<Zn, Zn.ZnElement> {

  public Zn(final BigInteger n) {
    super();

    this.n = n;
  }

  protected final BigInteger n;

  @Override
  public ZnElement uniformRandomElement() throws UnsupportedOperationException {
    return getElement(BigIntegerUtils.random(getN(), new SecureRandom()));
  }

  @Override
  public ZnElement getElementSafe(final BigInteger value) {
    return new ZnElement(value);
  }

  @Override
  public boolean hasElementSafe(final BigInteger value) {
    return BigIntegerUtils.isInRange(value, BigInteger.ZERO, getN()); // [0,n)
  }

  @Override
  public CommutativeGroupOperation<ZnElement> operation() {
    return new MultiplicationOperation();
  }

  @Override
  public Stream<ZnElement> elements() {
    return Stream.iterate(BigInteger.ZERO, i -> i.compareTo(getN()) < 0, BigIntegerUtils::increment)
        .map(this::getElement);
  }

  @Override
  public BigInteger elementCount() {
    return getN();
  }

  public final BigInteger getN() {
    return n;
  }

  public class ZnElement
      extends AbstractAlgebraicElement<ZnElement, Zn>
      implements CommutativeGroupElement<ZnElement, Zn>, MultiplicativeMagmaElement<ZnElement, Zn> {

    protected ZnElement(final BigInteger value) {
      super(Zn.this);

      this.value = value;
    }

    protected final BigInteger value;

    public BigInteger getValue() {
      return value;
    }

  }

  public class MultiplicationOperation implements CommutativeGroupOperation<ZnElement> {

    @Override
    public ZnElement perform(final ZnElement multiplier, final ZnElement multiplicand) {
      return getElement(multiplier.getValue().multiply(multiplicand.getValue()).mod(getN()));
    }

    @Override
    public ZnElement identity() {
      return getElement(BigInteger.ONE);
    }

    @Override
    public ZnElement inverse(final ZnElement element) {
      return getElement(element.getValue().modInverse(getN()));
    }

  }

}
```
