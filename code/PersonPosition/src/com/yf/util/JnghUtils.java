package com.yf.util;

import java.math.BigDecimal;

public final class JnghUtils {
    public static double round(double value, int scale, int roundingMode)
    {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }
    public static final int Server = 1;
    public static final int Router = 2;
    public static final int Switch = 3;
    
  


}
