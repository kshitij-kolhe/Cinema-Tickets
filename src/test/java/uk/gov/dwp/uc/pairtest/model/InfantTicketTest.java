package uk.gov.dwp.uc.pairtest.model;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InfantTicketTest {

    private InfantTicket infantTicket;

    @BeforeEach
    public void setup() {
        this.infantTicket = new InfantTicket();
    }

    @Test
    public void testGetTicketPrice() {

        Assertions.assertEquals(0, infantTicket.getTicketPrice(), "Infant ticket price should be 0");
    }
}