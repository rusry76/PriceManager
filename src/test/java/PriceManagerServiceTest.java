import model.Price;
import org.junit.Before;
import org.junit.Test;
import service.PriceManagerServiceImpl;
import service.PriceManagerService;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

public class PriceManagerServiceTest {

    private PriceManagerService managerService;

    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    private Collection<Price> currentPrices;
    private Collection<Price> incomingPrices;
    private List<Price> result;


    @Before
    public void setUp() throws Exception {
        managerService = new PriceManagerServiceImpl();

        currentPrices = new ArrayList<>();
        incomingPrices = new ArrayList<>();
        result = new ArrayList<>();
    }

    @Test(expected = Exception.class)
    public void joinWithNullPricesTest() throws Exception {
        currentPrices = null;
        incomingPrices = null;

        managerService.join(currentPrices, incomingPrices);
    }

    @Test(expected = Exception.class)
    public void joinWithEmptyPricesTest() throws Exception {
        currentPrices = new ArrayList<>();
        incomingPrices = new ArrayList<>();

        managerService.join(currentPrices, incomingPrices);
    }

    @Test
    public void joinWithNullOrEmptyCurrentPricesTest() throws Exception {
        incomingPrices.add(new Price("PC", 1, 1, dateFormat.parse("01.01.2019 00:00:00"), dateFormat.parse("31.01.2019 23:59:59"), 10000));

        currentPrices = null;
        assertEquals(incomingPrices, managerService.join(currentPrices, incomingPrices));

        currentPrices = new ArrayList<>();
        assertEquals(incomingPrices, managerService.join(currentPrices, incomingPrices));
    }

    @Test
    public void joinWithNullOrEmptyIncomingPricesTest() throws Exception {
        currentPrices.add(new Price("PC", 1, 1, dateFormat.parse("10.01.2019 00:00:00"), dateFormat.parse("20.01.2019 23:59:59"), 5000));

        incomingPrices = null;
        assertEquals(currentPrices, managerService.join(currentPrices, incomingPrices));

        incomingPrices = new ArrayList<>();
        assertEquals(currentPrices, managerService.join(currentPrices, incomingPrices));
    }

    // Тест для проверки задания с таблицей
    @Test
    public void joinPricesExampleWithTable() throws Exception {
        currentPrices.add(new Price("122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("31.01.2013 23:59:59"), 11000));
        currentPrices.add(new Price("122856", 2, 1, dateFormat.parse("10.01.2013 00:00:00"), dateFormat.parse("20.01.2013 23:59:59"), 99000));
        currentPrices.add(new Price("6654", 1, 2, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("31.01.2013 00:00:00"), 5000));

        incomingPrices.add(new Price("122856", 1, 1, dateFormat.parse("20.01.2013 00:00:00"), dateFormat.parse("20.02.2013 23:59:59"), 11000));
        incomingPrices.add(new Price("122856", 2, 1, dateFormat.parse("15.01.2013 00:00:00"), dateFormat.parse("25.01.2013 23:59:59"), 92000));
        incomingPrices.add(new Price("6654", 1, 2, dateFormat.parse("12.01.2013 00:00:00"), dateFormat.parse("13.01.2013 00:00:00"), 4000));

        result.add(new Price("122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("20.02.2013 23:59:59"), 11000));
        result.add(new Price("122856", 2, 1, dateFormat.parse("10.01.2013 00:00:00"), dateFormat.parse("15.01.2013 00:00:00"), 99000));
        result.add(new Price("122856", 2, 1, dateFormat.parse("15.01.2013 00:00:00"), dateFormat.parse("25.01.2013 23:59:59"), 92000));
        result.add(new Price("6654", 1, 2, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("12.01.2013 00:00:00"), 5000));
        result.add(new Price("6654", 1, 2, dateFormat.parse("12.01.2013 00:00:00"), dateFormat.parse("13.01.2013 00:00:00"), 4000));
        result.add(new Price("6654", 1, 2, dateFormat.parse("13.01.2013 00:00:00"), dateFormat.parse("31.01.2013 00:00:00"), 5000));

        List<Price> actual= new ArrayList<>(managerService.join(currentPrices, incomingPrices));

        assertTrue(result.containsAll(actual) && result.size() == actual.size());
    }

    //Тест для проверки ТЗ Пример 1
    //              |--------------------50---------------------|
    //                        |----------60---------|
    // Результат:   |----50---|----------60---------|----50-----|
    @Test
    public void joinPricesFirstExample() throws Exception {
        currentPrices.add(new Price("PC", 1, 1, dateFormat.parse("01.01.2019 00:00:00"), dateFormat.parse("31.01.2019 23:59:59"), 10000));
        incomingPrices.add(new Price("PC", 1, 1, dateFormat.parse("10.01.2019 00:00:00"), dateFormat.parse("20.01.2019 23:59:59"), 5000));

        result.add(new Price("PC", 1, 1, dateFormat.parse("01.01.2019 00:00:00"), dateFormat.parse("10.01.2019 00:00:00"), 10000));
        result.add(new Price("PC", 1, 1, dateFormat.parse("10.01.2019 00:00:00"), dateFormat.parse("20.01.2019 23:59:59"), 5000));
        result.add(new Price("PC", 1, 1, dateFormat.parse("20.01.2019 23:59:59"), dateFormat.parse("31.01.2019 23:59:59"), 10000));

        List<Price> actual= new ArrayList<>(managerService.join(currentPrices, incomingPrices));

        assertTrue(result.containsAll(actual) && result.size() == actual.size());
    }

    //Тест для проверки ТЗ Пример 2
    //              |--------100---------|---------120----------|
    //                         |--------110--------|
    // Результат:   |---100----|--------110--------|-----120----|
    @Test
    public void joinPricesSecondExample() throws Exception {
        currentPrices.add(new Price("PC", 1, 1, dateFormat.parse("01.01.2019 00:00:00"), dateFormat.parse("31.01.2019 23:59:59"), 10000));
        currentPrices.add(new Price("PC", 1, 1, dateFormat.parse("01.02.2019 00:00:00"), dateFormat.parse("28.02.2019 23:59:59"), 20000));
        incomingPrices.add(new Price("PC", 1, 1, dateFormat.parse("15.01.2019 00:00:00"), dateFormat.parse("15.02.2019 23:59:59"), 15000));

        result.add(new Price("PC", 1, 1, dateFormat.parse("01.01.2019 00:00:00"), dateFormat.parse("15.01.2019 00:00:00"), 10000));
        result.add(new Price("PC", 1, 1, dateFormat.parse("15.01.2019 00:00:00"), dateFormat.parse("15.02.2019 23:59:59"), 15000));
        result.add(new Price("PC", 1, 1, dateFormat.parse("15.02.2019 23:59:59"), dateFormat.parse("28.02.2019 23:59:59"), 20000));

        List<Price> actual= new ArrayList<>(managerService.join(currentPrices, incomingPrices));

        assertTrue(result.containsAll(actual) && result.size() == actual.size());
    }

    //Тест для проверки ТЗ Пример 3
    //              |-----80-----|-----87-----|-------90--------|
    //                     |----80-----|------85------|
    // Результат:   |--------80--------|------85------|----90---|
    @Test
    public void joinPricesThirdExample() throws Exception {
        currentPrices.add(new Price("PC", 1, 1, dateFormat.parse("01.01.2019 00:00:00"), dateFormat.parse("31.01.2019 23:59:59"), 80000));
        currentPrices.add(new Price("PC", 1, 1, dateFormat.parse("01.02.2019 00:00:00"), dateFormat.parse("28.02.2019 23:59:59"), 87000));
        currentPrices.add(new Price("PC", 1, 1, dateFormat.parse("01.03.2019 00:00:00"), dateFormat.parse("31.03.2019 23:59:59"), 90000));

        incomingPrices.add(new Price("PC", 1, 1, dateFormat.parse("15.01.2019 00:00:00"), dateFormat.parse("15.02.2019 23:59:59"), 80000));
        incomingPrices.add(new Price("PC", 1, 1, dateFormat.parse("15.02.2019 23:59:59"), dateFormat.parse("15.03.2019 23:59:59"), 85000));

        result.add(new Price("PC", 1, 1, dateFormat.parse("01.01.2019 00:00:00"), dateFormat.parse("15.02.2019 23:59:59"), 80000));
        result.add(new Price("PC", 1, 1, dateFormat.parse("15.02.2019 23:59:59"), dateFormat.parse("15.03.2019 23:59:59"), 85000));
        result.add(new Price("PC", 1, 1, dateFormat.parse("15.03.2019 23:59:59"), dateFormat.parse("31.03.2019 23:59:59"), 90000));

        List<Price> actual= new ArrayList<>(managerService.join(currentPrices, incomingPrices));

        assertTrue(result.containsAll(actual) && result.size() == actual.size());
    }

    @Test
    public void overlapLeftSideWithEqualValues() throws Exception {
        currentPrices.add(new Price("PC", 1, 1, dateFormat.parse("01.01.2019 00:00:00"), dateFormat.parse("31.01.2019 23:59:59"), 80000));
        currentPrices.add(new Price("PC", 1, 1, dateFormat.parse("01.02.2019 00:00:00"), dateFormat.parse("28.02.2019 23:59:59"), 87000));

        incomingPrices.add(new Price("PC", 1, 1, dateFormat.parse("01.12.2018 00:00:00"), dateFormat.parse("15.01.2019 23:59:59"), 80000));

        result.add(new Price("PC", 1, 1, dateFormat.parse("01.12.2018 00:00:00"), dateFormat.parse("31.01.2019 23:59:59"), 80000));
        result.add(new Price("PC", 1, 1, dateFormat.parse("01.02.2019 00:00:00"), dateFormat.parse("28.02.2019 23:59:59"), 87000));

        List<Price> actual= new ArrayList<>(managerService.join(currentPrices, incomingPrices));

        assertTrue(result.containsAll(actual) && result.size() == actual.size());
    }

    @Test
    public void overlapRightWithEqualValues() throws Exception {
        currentPrices.add(new Price("PC", 1, 1, dateFormat.parse("01.01.2019 00:00:00"), dateFormat.parse("31.01.2019 23:59:59"), 80000));
        currentPrices.add(new Price("PC", 1, 1, dateFormat.parse("01.02.2019 00:00:00"), dateFormat.parse("28.02.2019 23:59:59"), 87000));

        incomingPrices.add(new Price("PC", 1, 1, dateFormat.parse("15.02.2019 00:00:00"), dateFormat.parse("15.03.2019 23:59:59"), 87000));

        result.add(new Price("PC", 1, 1, dateFormat.parse("01.01.2019 00:00:00"), dateFormat.parse("31.01.2019 23:59:59"), 80000));
        result.add(new Price("PC", 1, 1, dateFormat.parse("01.02.2019 00:00:00"), dateFormat.parse("15.03.2019 23:59:59"), 87000));

        List<Price> actual= new ArrayList<>(managerService.join(currentPrices, incomingPrices));

        assertTrue(result.containsAll(actual) && result.size() == actual.size());
    }
}
