package uk.gov.dwp.uc.pairtest.model;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AdultTicketTest {

    private AdultTicket adultTicket;

    @BeforeEach
    public void setup() {
        adultTicket = new AdultTicket();
    }

    @Test
    public void testGetTicketPrice() {

        Assertions.assertEquals(20, adultTicket.getTicketPrice(), "Adult ticket price should be 20");
    }
}