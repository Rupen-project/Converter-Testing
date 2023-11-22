package com.testing.converter;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.MathContext;
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
    public ResponseEntity<?> convertNumber(@RequestBody Number_ConversionRequest request) {
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

    public static class Number_ConversionRequest {
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

    static final Map<String, BigDecimal> exchangeRates = new HashMap<>();

    static {
        // Example exchange rates (for illustration purposes, not actual rates)
        exchangeRates.put("USD", BigDecimal.ONE);
        exchangeRates.put("EUR", new BigDecimal("0.85"));
        exchangeRates.put("GBP", new BigDecimal("0.73"));
        exchangeRates.put("JPY", new BigDecimal("111.31"));
        exchangeRates.put("AUD", new BigDecimal("1.36"));
        exchangeRates.put("CAD", new BigDecimal("1.26"));
        exchangeRates.put("INR", new BigDecimal("73.68"));
        exchangeRates.put("CNY", new BigDecimal("6.46"));
        exchangeRates.put("BRL", new BigDecimal("5.40"));
        exchangeRates.put("ZAR", new BigDecimal("16.79"));
        // Add more currencies and their respective rates as needed
    }

    @PostMapping("/convertCurrency")
    public ResponseEntity<?> convertCurrency(@RequestBody CurrencyConversionRequest request) {
        try {
            String sourceCurrency = request.getSourceCurrency();
            String targetCurrency = request.getTargetCurrency();
            BigDecimal amount = request.getAmount();

            if (!exchangeRates.containsKey(sourceCurrency) || !exchangeRates.containsKey(targetCurrency)) {
                return new ResponseEntity<>("Invalid source or target currency", HttpStatus.BAD_REQUEST);
            }

            BigDecimal sourceToUSD = amount.divide(exchangeRates.get(sourceCurrency), 4, BigDecimal.ROUND_HALF_UP);
            BigDecimal targetAmount = sourceToUSD.multiply(exchangeRates.get(targetCurrency));

            return ResponseEntity.ok(targetAmount);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid input or conversion", HttpStatus.BAD_REQUEST);
        }
    }

    public static class CurrencyConversionRequest {
        private String sourceCurrency;
        private String targetCurrency;
        private BigDecimal amount;

        public String getSourceCurrency() {
            return sourceCurrency;
        }

        public void setSourceCurrency(String sourceCurrency) {
            this.sourceCurrency = sourceCurrency;
        }

        public String getTargetCurrency() {
            return targetCurrency;
        }

        public void setTargetCurrency(String targetCurrency) {
            this.targetCurrency = targetCurrency;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }

    @PostMapping("/convertTemprature")
    public ResponseEntity<?> convertTemperature(@RequestBody TemperatureConversionRequest request) {
        try {
            String sourceUnit = request.getSourceUnit().toLowerCase();
            String targetUnit = request.getTargetUnit().toLowerCase();
            BigDecimal temperature = request.getTemperature();

            if (!isValidUnit(sourceUnit) || !isValidUnit(targetUnit)) {
                return new ResponseEntity<>("Invalid temperature unit", HttpStatus.BAD_REQUEST);
            }

            BigDecimal convertedTemperature;
            switch (sourceUnit) {
                case "celsius":
                    convertedTemperature = convertFromCelsius(temperature, targetUnit);
                    break;
                case "fahrenheit":
                    convertedTemperature = convertFromFahrenheit(temperature, targetUnit);
                    break;
                case "kelvin":
                    convertedTemperature = convertFromKelvin(temperature, targetUnit);
                    break;
                default:
                    return new ResponseEntity<>("Invalid source temperature unit", HttpStatus.BAD_REQUEST);
            }

            return ResponseEntity.ok(convertedTemperature);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid input or conversion", HttpStatus.BAD_REQUEST);
        }
    }

    BigDecimal convertFromCelsius(BigDecimal temperature, String targetUnit) {
        switch (targetUnit) {
            case "fahrenheit":
                return temperature.multiply(BigDecimal.valueOf(9)).divide(BigDecimal.valueOf(5), MathContext.DECIMAL128).add(BigDecimal.valueOf(32));
            case "kelvin":
                return temperature.add(BigDecimal.valueOf(273.15));
            default:
                throw new IllegalArgumentException("Invalid target temperature unit");
        }
    }

    BigDecimal convertFromFahrenheit(BigDecimal temperature, String targetUnit) {
        switch (targetUnit) {
            case "celsius":
                return temperature.subtract(BigDecimal.valueOf(32)).multiply(BigDecimal.valueOf(5)).divide(BigDecimal.valueOf(9), MathContext.DECIMAL128);
            case "kelvin":
                return temperature.subtract(BigDecimal.valueOf(32)).multiply(BigDecimal.valueOf(5)).divide(BigDecimal.valueOf(9), MathContext.DECIMAL128).add(BigDecimal.valueOf(273.15));
            default:
                throw new IllegalArgumentException("Invalid target temperature unit");
        }
    }

    BigDecimal convertFromKelvin(BigDecimal temperature, String targetUnit) {
        switch (targetUnit) {
            case "celsius":
                return temperature.subtract(BigDecimal.valueOf(273.15));
            case "fahrenheit":
                return temperature.subtract(BigDecimal.valueOf(273.15)).multiply(BigDecimal.valueOf(9)).divide(BigDecimal.valueOf(5), MathContext.DECIMAL128).add(BigDecimal.valueOf(32));
            default:
                throw new IllegalArgumentException("Invalid target temperature unit");
        }
    }


    private boolean isValidUnit(String unit) {
        return unit.equals("celsius") || unit.equals("fahrenheit") || unit.equals("kelvin");
    }

    public static class TemperatureConversionRequest {
        private BigDecimal temperature;
        private String sourceUnit;
        private String targetUnit;

        public BigDecimal getTemperature() {
            return temperature;
        }

        public void setTemperature(BigDecimal temperature) {
            this.temperature = temperature;
        }

        public String getSourceUnit() {
            return sourceUnit;
        }

        public void setSourceUnit(String sourceUnit) {
            this.sourceUnit = sourceUnit;
        }

        public String getTargetUnit() {
            return targetUnit;
        }

        public void setTargetUnit(String targetUnit) {
            this.targetUnit = targetUnit;
        }
    }



}

