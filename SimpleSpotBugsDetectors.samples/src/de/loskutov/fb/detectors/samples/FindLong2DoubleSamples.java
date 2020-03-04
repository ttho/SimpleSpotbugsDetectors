package de.loskutov.fb.detectors.samples;

import edu.umd.cs.findbugs.annotations.Confidence;
import edu.umd.cs.findbugs.annotations.ExpectWarning;
import edu.umd.cs.findbugs.annotations.NoWarning;

@SuppressWarnings("deprecation")
public class FindLong2DoubleSamples {

    @ExpectWarning(value = "INT2FLOAT", rank = 2, confidence = Confidence.MEDIUM, num = 3)
    void testl2f() {
        long long1 = Long.MAX_VALUE - 1;
        long long2 = Long.MAX_VALUE - 2;
        float float3 = long1; // implicit conversion to float value
        float float4 = long2; // implicit conversion to float value
        System.out.println(float3 == float4); // true
        System.out.println((long)float3 == long1); // false
        toFloat(long1);
    }

    @ExpectWarning(value = "LONG2DOUBLE", rank = 2, confidence = Confidence.MEDIUM, num = 3)
    void testl2d() {
        long long1 = Long.MAX_VALUE - 1;
        long long2 = Long.MAX_VALUE - 2;
        double double1 = long1; // implicit conversion to double value
        double double2 = long2; // implicit conversion to double value
        System.out.println(double1 == double2); // true
        System.out.println((long)double1 == long1); // false
        toDouble(long1);
    }

    @ExpectWarning(value = "LONG2FLOAT", rank = 2, confidence = Confidence.MEDIUM, num = 3)
    void testi2f() {
        int int1 = Integer.MAX_VALUE - 1;
        int int2 = Integer.MAX_VALUE - 2;
        float float1 = int1; // implicit conversion to float value
        float float2 = int2; // implicit conversion to float value
        System.out.println(float1 == float2); // true
        System.out.println((int)float1 == int1); // false
        toFloat(int1);
    }

    void i2d() {
        int int1 = Integer.MAX_VALUE - 1;
        toDouble(int1);
    }

    static void toFloat(float f) {
    }

    static void toDouble(double d) {
    }
}
