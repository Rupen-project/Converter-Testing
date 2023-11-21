package com.testing.converter;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UnitConverterTesting {

    ConverterLogic converterLogic = new ConverterLogic();
    @Test
    public void testDUPath1() {
        // DU Path 1

        double input1 = 100.0;
        String unit1 = "gram";
        String unit2 = "kilo";

        // Calculate expected result manually
        double expectedResult = input1 / 1000;

        // Mock payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("input1", String.valueOf(input1));
        payload.put("unit1", unit1);
        payload.put("unit2", unit2);

        // Call the controller method
        ResponseEntity<?> response = converterLogic.unitConverterWeight(payload);

        // Verify the result
        assertEquals(ResponseEntity.ok(expectedResult), response);
    }

    @Test
    public void testDUPath2() {
        // DU Path 2

        double input1 = 500.0;
        String unit1 = "centi";
        String unit2 = "milli";

        // Calculate expected result manually
        double expectedResult = input1 * 10;

        // Mock payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("input1", String.valueOf(input1));
        payload.put("unit1", unit1);
        payload.put("unit2", unit2);

        // Call the controller method
        ResponseEntity<?> response = converterLogic.unitConverterWeight(payload);

        // Verify the result
        assertEquals(ResponseEntity.ok(expectedResult), response);
    }

    @Test
    public void testDUPath3() {
        // DU Path 3

        double input1 = 200.0;
        String unit1 = "invalidUnit";
        String unit2 = "kilo";

        // Mock payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("input1", String.valueOf(input1));
        payload.put("unit1", unit1);
        payload.put("unit2", unit2);

        // Call the controller method
        ResponseEntity<?> response = converterLogic.unitConverterWeight(payload);

        // Verify the result
        assertEquals(new ResponseEntity(HttpStatus.BAD_REQUEST), response);
    }


}
