package uk.gov.dwp.uc.pairtest.model;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.TicketPriceFactoryImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;

public class TicketPriceFactoryImplTest {

    @Test
    public void testCreateTicketPrice() {

        final TicketPriceFactoryImpl ticketPriceFactory = new TicketPriceFactoryImpl();

        Assertions.assertEquals(AdultTicket.class, ticketPriceFactory.createTicketPrice(Type.ADULT).getClass(), "should get adult ticket class");

        Assertions.assertEquals(ChildTicket.class, ticketPriceFactory.createTicketPrice(Type.CHILD).getClass(), "should get child ticket class");

        Assertions.assertEquals(InfantTicket.class, ticketPriceFactory.createTicketPrice(Type.INFANT).getClass(), "should get infant ticket class");
    }
}