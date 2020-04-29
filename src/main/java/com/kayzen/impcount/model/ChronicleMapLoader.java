package com.kayzen.impcount.model;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.Callable;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

/**
 * Created by raghuteja on 16/01/19.
 */
public class ChronicleMapLoader<K, V> implements Callable<ChronicleMap<K, V>> {

  private File chronicleMapFile;
  private String identifier;
  private int keySize;
  private int entriesSize;
  private Class<K> kClass;
  private Class<V> vClass;

  public ChronicleMapLoader(File file, String identifier, int keySize, int entriesSize) {
    this.chronicleMapFile = file;
    this.identifier = identifier;
    this.keySize = keySize;
    this.entriesSize = entriesSize;
    this.kClass = (Class<K>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    this.vClass = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
  }

  @Override
  public ChronicleMap<K, V> call() throws Exception {
    ChronicleMap<K, V> chronicleMap = ChronicleMapBuilder
        .of(kClass, vClass)
        .name(identifier)
        .averageKeySize(keySize)
        .entries(entriesSize)
        .createOrRecoverPersistedTo(chronicleMapFile);
    return chronicleMap;
  }
}
