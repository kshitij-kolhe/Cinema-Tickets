package uk.gov.dwp.uc.pairtest.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChildTicketTest {

    private ChildTicket childTicket;

    @BeforeEach
    public void setup() {
        childTicket = new ChildTicket();
    }

    @Test
    public void testGetTicketPrice() {

        Assertions.assertEquals(10, childTicket.getTicketPrice(), "Child ticket price should be 10");
    }
}