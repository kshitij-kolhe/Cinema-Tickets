package uk.gov.dwp.uc.pairtest.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

public class TicketRequestDetailTest {

    private TicketRequestDetails ticketRequestDetails;

    @BeforeEach
    public void setUp() {
        this.ticketRequestDetails = new TicketRequestDetails();
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 5, 7, 2})
    public void shouldIncrementTicketsWhenAdultOrChildTicketIsAdded(final int count) {
        ticketRequestDetails.addTicket(TicketTypeRequest.Type.ADULT, count);
        ticketRequestDetails.addTicket(TicketTypeRequest.Type.CHILD, count);
        ticketRequestDetails.addTicket(TicketTypeRequest.Type.INFANT, count);

        Assertions.assertEquals(2 * count, ticketRequestDetails.getTotalTickets());
    }


    @ParameterizedTest
    @ValueSource(ints = {3, 5, 7, 2})
    public void shouldNotIncrementTicketsWhenInfantTicketIsAdded(final int count) {
        ticketRequestDetails.addTicket(TicketTypeRequest.Type.INFANT, count);
        ticketRequestDetails.addTicket(TicketTypeRequest.Type.ADULT, count);

        Assertions.assertEquals(count, ticketRequestDetails.getTotalTickets());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 16})
    public void returnFalseWhenNoAdultIsPresentForInfants(final int infants) {

        ticketRequestDetails.addTicket(TicketTypeRequest.Type.INFANT, infants);
        Assertions.assertFalse(ticketRequestDetails.isTicketRequestValid());
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 19, 1, 10})
    public void returnFalseWhenNoAdultIsPresentForChildren(final int children) {

        ticketRequestDetails.addTicket(TicketTypeRequest.Type.CHILD, children);
        Assertions.assertFalse(ticketRequestDetails.isTicketRequestValid());
    }

    @ParameterizedTest
    @CsvSource({"2, 5", "7, 13", "4, 9"})
    public void returnFalseWhenNumberOfInfantsIsMoreThanAdults(final int adults, final int infants) {

        ticketRequestDetails.addTicket(TicketTypeRequest.Type.INFANT, infants);
        ticketRequestDetails.addTicket(TicketTypeRequest.Type.ADULT, adults);
        Assertions.assertFalse(ticketRequestDetails.isTicketRequestValid());
    }

    @ParameterizedTest
    @ValueSource(ints = {25, 77, 32})
    public void returnFalseWhenTicketsGreaterThan20(final int count) {

        ticketRequestDetails.addTicket(TicketTypeRequest.Type.ADULT, count);
        Assertions.assertFalse(ticketRequestDetails.isTicketRequestValid());
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 2})
    public void returnTrueWhenTicketRequestIsValid(final int count) {

        ticketRequestDetails.addTicket(TicketTypeRequest.Type.ADULT, count);
        ticketRequestDetails.addTicket(TicketTypeRequest.Type.CHILD, count);
        ticketRequestDetails.addTicket(TicketTypeRequest.Type.INFANT, count);
        Assertions.assertTrue(ticketRequestDetails.isTicketRequestValid());
    }

}
