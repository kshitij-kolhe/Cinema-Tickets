package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.exception.InvalidTicketTypeException;
import uk.gov.dwp.uc.pairtest.model.TicketPrice;
import uk.gov.dwp.uc.pairtest.model.TicketRequestDetails;
import uk.gov.dwp.uc.pairtest.service.TicketService;


public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */
    private final TicketPriceFactoryImpl ticketPriceFactory;
    private final TicketPaymentServiceImpl ticketPaymentService;
    private final SeatReservationServiceImpl seatReservationService;

    public TicketServiceImpl() {
        ticketPriceFactory = new TicketPriceFactoryImpl();
        ticketPaymentService = new TicketPaymentServiceImpl();
        seatReservationService = new SeatReservationServiceImpl();
    }


    public TicketServiceImpl(final TicketPriceFactoryImpl ticketPriceFactory, final TicketPaymentServiceImpl ticketPaymentService, final SeatReservationServiceImpl seatReservationService) {
        this.ticketPriceFactory = ticketPriceFactory;
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        int totalAmountToPay = 0;
        int totalNumberOfSeats = 0;

        final TicketRequestDetails ticketRequestDetails = generateTicketRequestDetails(ticketTypeRequests);

        if ( accountId <= 0 || !ticketRequestDetails.isTicketRequestValid()) {
            throw new InvalidPurchaseException("Invalid Purchase Request: ticket is not valid");
        }

        totalAmountToPay = ticketRequestDetails.getTicketsPrice();
        totalNumberOfSeats = ticketRequestDetails.getTotalTickets();

        ticketPaymentService.makePayment(accountId, totalAmountToPay);
        seatReservationService.reserveSeat(accountId, totalNumberOfSeats);

    }


    private TicketRequestDetails generateTicketRequestDetails(final TicketTypeRequest[] ticketTypeRequests) throws InvalidPurchaseException {
        final TicketRequestDetails ticketRequestDetails = new TicketRequestDetails();
        int totalTicketsPrice = 0;

        try {
            for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {

                totalTicketsPrice += getTicketsPrice(ticketTypeRequest);
                ticketRequestDetails.addTicket(ticketTypeRequest.getTicketType(), ticketTypeRequest.getNoOfTickets());
            }
        } catch (InvalidTicketTypeException invalidTicketTypeException) {
            throw new InvalidPurchaseException("Invalid Purchase Request: invalid ticket type received");
        }

        ticketRequestDetails.setTicketsPrice(totalTicketsPrice);

        return ticketRequestDetails;
    }

    private int getTicketsPrice(final TicketTypeRequest ticketTypeRequest) throws InvalidTicketTypeException{
        final TicketPrice ticketPrice = ticketPriceFactory.createTicketPrice(ticketTypeRequest.getTicketType());

        return ticketPrice.getTicketPrice() * ticketTypeRequest.getNoOfTickets();
    }

}