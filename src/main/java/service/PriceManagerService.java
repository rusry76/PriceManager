package service;

import model.Price;

import java.util.Collection;

public interface PriceManagerService {

    Collection<Price> join(Collection<Price> currentPrices, Collection<Price> incomingPrices) throws Exception;

}
