package com.testing.converter;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ConverterLogic {

    @RequestMapping(value = "/unitConverterWeight", method = RequestMethod.GET)
    public ResponseEntity<?> unitConverterWeight(@RequestBody Map<String, Object> payload) {
        Set<String> setWeight = new HashSet<String>(Arrays.asList("milli", "centi", "gram", "kilo", "metrictonnes", "pounds", "ounces", ""));
        String unit1 = (String) payload.get("unit1");
        String unit2 = (String) payload.get("unit2");
        double res = 0;
        double input1 = Double.parseDouble((String) payload.get("input1"));
        if (setWeight.contains(unit1) && setWeight.contains(unit2)) {
            double kilo = 0.0;

            switch (unit1) {
                case "milli":
                    kilo = input1 / 1000000;
                    break;

                case "centi":
                    kilo = input1 / 100000;
                    break;

                case "gram":
                    kilo = input1 / 1000;
                    break;

                case "kilo":
                    kilo = input1;
                    break;

                case "matrictonnes":
                    kilo = input1 * 1000;
                    break;

                case "pounds":
                    kilo = input1 / 2.20462;
                    break;

                case "ounces":
                    kilo = input1 / 35.274;
                    break;

                default:
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }

            switch (unit2) {
                case "milli":
                    res = kilo * 1000000;
                    break;

                case "centi":
                    res = kilo * 100000;
                    break;

                case "gram":
                    res = kilo * 1000;
                    break;

                case "kilo":
                    res = kilo;
                    break;

                case "matrictonnes":
                    res = kilo / 1000;
                    break;

                case "pounds":
                    res = kilo * 2.20462;
                    break;

                case "ounces":
                    res = kilo / 35.274;
                    break;

                default:
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
        else
        {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(res);
    }

    @PostMapping("/convertTime")
    public ResponseEntity<?> convertTime(@RequestBody TimeZoneRequest timeZoneRequest) {
        try {
            // Parse the source time string to Date object
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneRequest.getSourceTimeZone()));
            Date sourceTime = dateFormat.parse(timeZoneRequest.getSourceTime());

            // Set the target time zone
            dateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneRequest.getTargetTimeZone()));

            // Format the converted time to a string
            String convertedTime = dateFormat.format(sourceTime);

            return ResponseEntity.ok(convertedTime);
        } catch (ParseException e) {
            // Handle invalid date format
            return new ResponseEntity<>("Invalid date format", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/convertNumber")
    public ResponseEntity<?> convertNumber(@RequestBody ConversionRequest request) {
        try {
            String input = request.getInput();
            String output;

            switch (request.getConversionType()) {
                case "decimalToBinary":
                    int decimalValue = Integer.parseInt(input);
                    output = Integer.toBinaryString(decimalValue);
                    break;

                case "binaryToDecimal":
                    int binaryValue = Integer.parseInt(input, 2);
                    output = String.valueOf(binaryValue);
                    break;

                case "decimalToHexadecimal":
                    int decimalValueForHex = Integer.parseInt(input);
                    output = Integer.toHexString(decimalValueForHex).toUpperCase();
                    break;

                case "hexadecimalToDecimal":
                    int decimalValueForHex1 = Integer.parseInt(input, 16);
                    output = String.valueOf(decimalValueForHex1);
                    break;

                default:
                    return new ResponseEntity<>("Invalid conversion type", HttpStatus.BAD_REQUEST);
            }

            return ResponseEntity.ok(output);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid input or conversion type", HttpStatus.BAD_REQUEST);
        }
    }

    public static class ConversionRequest {
        private String input;
        private String conversionType;

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public String getConversionType() {
            return conversionType;
        }

        public void setConversionType(String conversionType) {
            this.conversionType = conversionType;
        }
    }
}

