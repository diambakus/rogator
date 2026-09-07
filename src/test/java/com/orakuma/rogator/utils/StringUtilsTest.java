package com.orakuma.rogator.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringUtilsTest {

    @Test
    public void givenATrackingCodeLengthShouldMatchExpectation() {
        String trackingCode6 = TrackingCodeUtils.generate(6);
        Assertions.assertEquals(6, trackingCode6.length());

        String trackingCode3 = TrackingCodeUtils.generate(3);
        Assertions.assertEquals(3, trackingCode3.length());
    }
}
