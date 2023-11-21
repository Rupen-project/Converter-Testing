package com.testing.converter;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UnitConverterTesting {

    ConverterLogic converterLogic = new ConverterLogic();
    @Test
    public void unit_testDUPath1() {
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
    public void unit_testDUPath2() {
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
    public void unit_testDUPath3() {
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


    @Test
    public void time_testDUPath1() throws ParseException {
        // DU Path 1
        String sourceTime = "2023-01-01 12:00:00";
        String sourceTimeZone = "UTC";
        String targetTimeZone = "America/New_York";

        ResponseEntity<?> response = convertTime(sourceTime, sourceTimeZone, targetTimeZone);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Compare the converted time with the expected time
        String expectedTime = "2023-01-01 07:00:00";
        assertEquals(expectedTime, response.getBody());
    }

    @Test
    public void time_testDUPath2() throws ParseException {
        // DU Path 2
        String sourceTime = "2023-01-01 12:00:00";
        String sourceTimeZone = "UTC";
        String targetTimeZone = "America/Los_Angeles";

        ResponseEntity<?> response = convertTime(sourceTime, sourceTimeZone, targetTimeZone);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Compare the converted time with the expected time
        String expectedTime = "2023-01-01 04:00:00";
        assertEquals(expectedTime, response.getBody());
    }

    @Test
    public void time_testDUPath3() throws ParseException {
        // DU Path 3
        String sourceTime = "2023-01-01 12:00:00";
        String sourceTimeZone = "UTC";
        String targetTimeZone = "Europe/London";

        ResponseEntity<?> response = convertTime(sourceTime, sourceTimeZone, targetTimeZone);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Compare the converted time with the expected time
        String expectedTime = "2023-01-01 12:00:00";
        assertEquals(expectedTime, response.getBody());
    }

    @Test
    public void time_testDUPath4() throws ParseException {
        // DU Path 4
        String sourceTime = "2023-01-01 12:00:00";
        String sourceTimeZone = "UTC";
        String targetTimeZone = "Asia/Tokyo";

        ResponseEntity<?> response = convertTime(sourceTime, sourceTimeZone, targetTimeZone);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Compare the converted time with the expected time
        String expectedTime = "2023-01-01 21:00:00";
        assertEquals(expectedTime, response.getBody());
    }

    @Test
    public void time_testDUPath5() {
        // DU Path 5
        String sourceTime = "invalidFormat";
        String sourceTimeZone = "UTC";
        String targetTimeZone = "America/New_York";

        ResponseEntity<?> response = convertTime(sourceTime, sourceTimeZone, targetTimeZone);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private ResponseEntity<?> convertTime(String sourceTime, String sourceTimeZone, String targetTimeZone) {
        TimeZoneRequest timeZoneRequest =
                new TimeZoneRequest();
        timeZoneRequest.setSourceTime(sourceTime);
        timeZoneRequest.setSourceTimeZone(sourceTimeZone);
        timeZoneRequest.setTargetTimeZone(targetTimeZone);

        return new ConverterLogic().convertTime(timeZoneRequest);
    }

    @Test
    public void number_testDUPath1() {
        // DU Path 1: "decimalToBinary" case
        ConverterLogic.ConversionRequest request = new ConverterLogic.ConversionRequest();
        request.setInput("10");
        request.setConversionType("decimalToBinary");

        ResponseEntity<?> response = new ConverterLogic().convertNumber(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("1010", response.getBody());
    }

    @Test
    public void number_testDUPath2() {
        // DU Path 2: "binaryToDecimal" case
        ConverterLogic.ConversionRequest request = new ConverterLogic.ConversionRequest();
        request.setInput("1010");
        request.setConversionType("binaryToDecimal");

        ResponseEntity<?> response = new ConverterLogic().convertNumber(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("10", response.getBody());
    }

    @Test
    public void number_testDUPath3() {
        // DU Path 3: "decimalToHexadecimal" case
        ConverterLogic.ConversionRequest request = new ConverterLogic.ConversionRequest();
        request.setInput("255");
        request.setConversionType("decimalToHexadecimal");

        ResponseEntity<?> response = new ConverterLogic().convertNumber(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("FF", response.getBody());
    }

    @Test
    public void number_testDUPath4() {
        // DU Path 4: "hexadecimalToDecimal" case
        ConverterLogic.ConversionRequest request = new ConverterLogic.ConversionRequest();
        request.setInput("FF");
        request.setConversionType("hexadecimalToDecimal");

        ResponseEntity<?> response = new ConverterLogic().convertNumber(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("255", response.getBody());
    }

    @Test
    public void number_testDUPath5() {
        // DU Path 5: Exception block for NumberFormatException
        ConverterLogic.ConversionRequest request = new ConverterLogic.ConversionRequest();
        request.setInput("invalidInput");
        request.setConversionType("decimalToBinary");

        ResponseEntity<?> response = new ConverterLogic().convertNumber(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void number_testDUPath6() {
        // DU Path 6: Default case (invalid conversionType)
        ConverterLogic.ConversionRequest request = new ConverterLogic.ConversionRequest();
        request.setInput("10");
        request.setConversionType("invalidConversionType");

        ResponseEntity<?> response = new ConverterLogic().convertNumber(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


}
