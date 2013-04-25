/*
 *  Copyright (c) 2012-2013 Malhar, Inc.
 *  All Rights Reserved.
 */
package com.malhartech.lib.chart;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author David Yan <davidyan@malhar-inc.com>
 */
public abstract class EnumChartOperator<K, X> extends XYChartOperator<K, X, Number>
{
  protected Map<K, Map<X, Number>> dataMap = new TreeMap<K, Map<X, Number>>();

  @Override
  public Type getChartType()
  {
    return Type.ENUM;
  }

  @Override
  public Map<X, Number> getPoints(K key)
  {
    return dataMap.get(key);
  }

  @Override
  public Collection<K> getKeys()
  {
    return dataMap.keySet();
  }

  public abstract X convertTupleToX(Object tuple);

  @Override
  public void processTuple(Object tuple)
  {
    K key = convertTupleToKey(tuple);
    X x = convertTupleToX(tuple);
    Number number = convertTupleToY(tuple);
    if (number != null) {
      Map<X, Number> map = dataMap.get(key);
      if (map == null) {
        map = new TreeMap<X, Number>();
        dataMap.put(key, map);
      }
      Number oldValue = map.get(x);
      if (yNumberType == NumberType.DOUBLE) {
        double value = number.doubleValue();
        if (oldValue == null) {
          map.put(x, value);
        }
        else {
          map.put(x, oldValue.doubleValue() + value);
        }
      }
      else {
        double value = number.longValue();
        if (oldValue == null) {
          map.put(x, value);
        }
        else {
          map.put(x, oldValue.longValue() + value);
        }
      }
    }
  }

}