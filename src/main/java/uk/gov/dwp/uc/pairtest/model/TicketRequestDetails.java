package uk.gov.dwp.uc.pairtest.model;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;

import java.util.HashMap;
import java.util.Map;

public class TicketRequestDetails {

    final private Map<TicketTypeRequest.Type, Integer> tickets = new HashMap<>();
    private int ticketsPrice = 0;
    private int totalTickets = 0;

    public TicketRequestDetails() {
    }

    public void addTicket(Type type, int noOfTickets) {
        tickets.put(type, tickets.getOrDefault(type, 0) + noOfTickets);

        if (type != TicketTypeRequest.Type.INFANT)
            totalTickets += noOfTickets;
    }

    public int getTicketsPrice() {
        return ticketsPrice;
    }

    public void setTicketsPrice(final int ticketsPrice) {
        this.ticketsPrice = ticketsPrice;
    }

    public boolean isTicketRequestValid() {
        return getTotalTickets() > 0 && getTotalTickets() <= 20
                && tickets.getOrDefault(TicketTypeRequest.Type.ADULT, 0) > 0
                && tickets.getOrDefault(TicketTypeRequest.Type.INFANT, 0) <= tickets.getOrDefault(TicketTypeRequest.Type.ADULT, 0);
    }

    public int getTotalTickets() {
        return totalTickets;
    }
}
