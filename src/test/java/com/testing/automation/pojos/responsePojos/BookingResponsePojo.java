package com.testing.automation.pojos.responsePojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.testing.automation.pojos.requestPojos.BookingRequestPojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponsePojo {

    @JsonProperty("bookingid")
    private int bookingId;

    private BookingRequestPojo booking;
}
