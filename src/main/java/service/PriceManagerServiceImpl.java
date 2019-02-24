package service;

import model.Price;

import java.util.*;
import java.util.stream.Collectors;

public class PriceManagerServiceImpl implements PriceManagerService {


    @Override
    public Collection<Price> join(Collection<Price> currentPrices, Collection<Price> incomingPrices) throws Exception {

        if (isNullOrEmpty(currentPrices) && isNullOrEmpty(incomingPrices)) {
            throw new Exception("Отсутствуют цены для объединения");
        }

        if (isNullOrEmpty(currentPrices)) {
            return incomingPrices;
        }

        if (isNullOrEmpty(incomingPrices)) {
            return currentPrices;
        }

        List<Price> result = new ArrayList<>(currentPrices);

        for (Price incoming : incomingPrices) {

            List<Price> prices = result.stream().filter(price -> isContain(price, incoming)).collect(Collectors.toList());

            result.removeAll(prices);

            joinPrices(result, prices, incoming);
        }

        return new HashSet<>(result);
    }

    private void joinPrices(List<Price> result, List<Price> prices, Price incoming) {

        result.add(incoming);

        prices.stream().filter(price -> incoming.getInterval().overlapCenterOf(price.getInterval()))
                .findFirst()
                .ifPresent(price -> {
                    result.add(new Price(price, price.getBegin(), incoming.getBegin()));
                    result.add(new Price(price, incoming.getEnd(), price.getEnd()));
                });

        prices.stream().filter(price -> incoming.getInterval().overlapLeftSideOf(price.getInterval()))
                .findFirst()
                .ifPresent(price -> {

                    if (price.getValue() != incoming.getValue()) {
                        result.add(new Price(price, incoming.getEnd(), price.getEnd()));
                    } else {
                        incoming.setEnd(price.getEnd());
                    }
                });

        prices.stream().filter(price -> incoming.getInterval().overlapRightSideOf(price.getInterval()))
                .findFirst()
                .ifPresent(price -> {

                    if (price.getValue() != incoming.getValue()) {
                        result.add(new Price(price, price.getBegin(), incoming.getBegin()));
                    } else {
                        incoming.setBegin(price.getBegin());
                    }
                });

        prices.stream().filter(price -> incoming.getInterval().outOf(price.getInterval())).forEachOrdered(result::add);
    }

    private boolean isContain(Price correct, Price incoming) {
        return incoming.getCode().equals(correct.getCode()) && incoming.getNumber() == correct.getNumber() && incoming.getDepart() == correct.getDepart();
    }

    private boolean isNullOrEmpty(Collection<Price> prices) {
        return prices == null || prices.isEmpty();
    }
}
