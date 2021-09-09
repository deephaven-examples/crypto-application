package io.deephaven.crypto;

import java.math.BigDecimal;

class DeephavenHelper {

  public static double adapt(BigDecimal decimal) {
    return decimal == null ? -Double.MAX_VALUE : decimal.doubleValue();
  }
}
