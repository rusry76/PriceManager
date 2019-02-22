package service;

import model.Price;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

        List<Price> result = new ArrayList<>();

        for (Price incoming : incomingPrices) {
            List<Price> prices = currentPrices.stream().filter(price -> isContain(price, incoming)).collect(Collectors.toList());

            join(result, prices, incoming);
        }

        return result;
    }

    private void joinPricesWithEqualValues(List<Price> result, List<Price> prices, Price incoming) {

        prices.stream().filter(price -> incoming.getInterval().overlapLeftSideOf(price.getInterval()) && price.getValue() == incoming.getValue())
                .findFirst()
                .ifPresent(price -> result.add(new Price(price, incoming.getBegin(), price.getEnd())));


        prices.stream().filter(price -> incoming.getInterval().overlapRightSideOf(price.getInterval()) && price.getValue() == incoming.getValue())
                .findFirst()
                .ifPresent(price -> result.add(new Price(price, price.getBegin(), incoming.getEnd())));
    }

    private void join(List<Price> result, List<Price> prices, Price incoming) {

        joinPricesWithEqualValues(result, prices, incoming);

        joinPricesWithDifferentValues(result, prices, incoming);
    }

    private void joinPricesWithDifferentValues(List<Price> result, List<Price> prices, Price incoming) {

        prices.stream().filter(price -> incoming.getInterval().overlapCenterOf(price.getInterval()))
                .findFirst()
                .ifPresent(price -> {
                    result.add(new Price(price, price.getBegin(), incoming.getBegin()));
                    result.add(incoming);
                    result.add(new Price(price, incoming.getEnd(), price.getEnd()));
                });

        prices.stream().filter(price -> incoming.getInterval().overlapLeftSideOf(price.getInterval()) && price.getValue() != incoming.getValue())
                .findFirst()
                .ifPresent(price -> {
                    result.add(incoming);
                    result.add(new Price(price, incoming.getEnd(), price.getEnd()));
                });

        prices.stream().filter(price -> incoming.getInterval().overlapRightSideOf(price.getInterval()) && price.getValue() != incoming.getValue())
                .findFirst()
                .ifPresent(price -> {
                    result.add(new Price(price, price.getBegin(), incoming.getBegin()));
                    result.add(incoming);
                });

    }

    private boolean isContain(Price correct, Price incoming) {
        return incoming.getCode().equals(correct.getCode()) && incoming.getNumber() == correct.getNumber() && incoming.getDepart() == correct.getDepart();
    }

    private boolean isNullOrEmpty(Collection<Price> prices) {
        return prices == null || prices.isEmpty();
    }
}
