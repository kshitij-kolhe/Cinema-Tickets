package uk.gov.dwp.uc.pairtest.service;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.model.TicketPrice;

public interface TicketPriceFactory {
    TicketPrice createTicketPrice(final Type type);
}
