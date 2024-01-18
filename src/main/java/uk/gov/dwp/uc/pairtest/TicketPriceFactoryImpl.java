package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidTicketTypeException;
import uk.gov.dwp.uc.pairtest.model.AdultTicket;
import uk.gov.dwp.uc.pairtest.model.ChildTicket;
import uk.gov.dwp.uc.pairtest.model.InfantTicket;
import uk.gov.dwp.uc.pairtest.model.TicketPrice;
import uk.gov.dwp.uc.pairtest.service.TicketPriceFactory;

public class TicketPriceFactoryImpl implements TicketPriceFactory {

    @Override
    public TicketPrice createTicketPrice(Type type) throws InvalidTicketTypeException {
        switch (type) {
            case ADULT:
                return new AdultTicket();

            case CHILD:
                return new ChildTicket();

            case INFANT:
                return new InfantTicket();

            default:
                throw new InvalidTicketTypeException("Ticket type not found");
        }
    }

}
