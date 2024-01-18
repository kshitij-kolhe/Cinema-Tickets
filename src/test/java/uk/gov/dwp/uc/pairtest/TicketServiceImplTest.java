package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.model.AdultTicket;
import uk.gov.dwp.uc.pairtest.model.ChildTicket;
import uk.gov.dwp.uc.pairtest.model.InfantTicket;
import uk.gov.dwp.uc.pairtest.model.TicketRequestDetails;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class TicketServiceImplTest {

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Mock
    private TicketPaymentServiceImpl ticketPaymentService;
    @Mock
    private SeatReservationServiceImpl seatReservationService;
    @Mock
    private TicketPriceFactoryImpl ticketPriceFactory;
    @Mock
    private TicketRequestDetails ticketRequestDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(ticketPriceFactory.createTicketPrice(Type.ADULT)).thenReturn(new AdultTicket());
        when(ticketPriceFactory.createTicketPrice(Type.CHILD)).thenReturn(new ChildTicket());
        when(ticketPriceFactory.createTicketPrice(Type.INFANT)).thenReturn(new InfantTicket());
        doNothing().when(ticketRequestDetails).addTicket(any(Type.class), anyInt());

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 16})
    public void shouldThrowExceptionWhenNoAdultIsPresentForInfants(final int infants) {
        when(ticketRequestDetails.isTicketRequestValid()).thenReturn(false);

        Assertions.assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(12L, new TicketTypeRequest(Type.INFANT, infants)));
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 3, 1, 11})
    public void shouldThrowExceptionWhenAccountIdIsInvalid(final int adults) {
        when(ticketRequestDetails.isTicketRequestValid()).thenReturn(false);

        Assertions.assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(-10L, new TicketTypeRequest(Type.ADULT, adults)));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 19, 1, 10})
    public void shouldThrowExceptionWhenNoAdultIsPresentForChildren(final int children) {
        when(ticketRequestDetails.isTicketRequestValid()).thenReturn(false);

        Assertions.assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(12L, new TicketTypeRequest(Type.CHILD, children)));
    }

    @ParameterizedTest
    @CsvSource({"2, 5", "7, 13", "4, 9"})
    public void shouldThrowExceptionWhenNumberOfInfantsIsMoreThanAdults(final int adults, final int infants) {
        when(ticketRequestDetails.isTicketRequestValid()).thenReturn(false);

        Assertions.assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(12L, new TicketTypeRequest(Type.INFANT, infants),
                new TicketTypeRequest(Type.ADULT, adults)));
    }

    @ParameterizedTest
    @ValueSource(ints = {25, 77, 32})
    public void shouldThrowExceptionWhenTicketsGreaterThan20(final int count) {
        when(ticketRequestDetails.isTicketRequestValid()).thenReturn(false);

        Assertions.assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(12L, new TicketTypeRequest(Type.ADULT, count)));
    }

    @ParameterizedTest
    @CsvSource({"200, 8, 4, 5", "180, 7, 4, 5", "160, 7, 2, 5"})
    public void shouldCallPaymentService(final int expectedAmount, final int adults, final int children, final int infants) {
        when(ticketRequestDetails.isTicketRequestValid()).thenReturn(true);
        when(ticketRequestDetails.getTicketsPrice()).thenReturn(expectedAmount);
        when(ticketRequestDetails.getTotalTickets()).thenReturn(adults + children);

        ticketService.purchaseTickets(12L, new TicketTypeRequest(Type.ADULT, adults),
                new TicketTypeRequest(Type.CHILD, children),
                new TicketTypeRequest(Type.INFANT, infants));

        Mockito.verify(ticketPaymentService).makePayment(12L, expectedAmount);
    }

    @ParameterizedTest
    @CsvSource({"12, 6, 6, 4", "3, 3, 0, 1", "8, 2, 6, 0"})
    public void shouldCallSeatReservationService(final int expectedSeats, final int adults, final int children, final int infants) {
        when(ticketRequestDetails.isTicketRequestValid()).thenReturn(true);
        when(ticketRequestDetails.getTicketsPrice()).thenReturn(adults * 20 + children * 10);
        when(ticketRequestDetails.getTotalTickets()).thenReturn(adults + children);

        ticketService.purchaseTickets(12L, new TicketTypeRequest(Type.ADULT, adults),
                new TicketTypeRequest(Type.CHILD, children),
                new TicketTypeRequest(Type.INFANT, infants));

        Mockito.verify(seatReservationService).reserveSeat(12L, expectedSeats);
    }
}