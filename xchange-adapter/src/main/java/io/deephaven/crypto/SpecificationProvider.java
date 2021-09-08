package io.deephaven.crypto;

import java.util.List;
import java.util.ServiceLoader;

public interface SpecificationProvider {

  static Iterable<SpecificationProvider> load() {
    return ServiceLoader.load(SpecificationProvider.class);
  }

  List<Specification> specifications();
}
