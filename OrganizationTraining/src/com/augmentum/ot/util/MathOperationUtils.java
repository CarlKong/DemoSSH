package com.augmentum.ot.util;

import java.math.BigDecimal;
import java.math.BigInteger;


/** 
* @ClassName: MathOperationUtils 
* @date 2012-8-7 
* @version V1.0 
*/
public abstract class MathOperationUtils {

     /** 
     * @Description: convert 2^n to BigInteger
     * @param radix
     * @param index
     * @return BigInteger
     */ 
    public static BigInteger binaryToBigInteger(int radix, Integer index) {
        Double doubleNumber = Math.pow(radix, index);
        BigDecimal bd = new BigDecimal(doubleNumber);
        return bd.toBigInteger();
    }
}
