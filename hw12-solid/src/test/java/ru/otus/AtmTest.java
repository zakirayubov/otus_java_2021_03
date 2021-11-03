package ru.otus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.Atm;
import ru.otus.AtmImpl;
import ru.otus.AtmWithdrawException;
import ru.otus.Banknote;

import java.util.Map;
import java.util.TreeMap;

@DisplayName("банкомат")
class AtmTest {

    private Atm atm;
    private Storage storage;

    @BeforeEach
    private void setUp() {
        Map<Banknote, Integer> container = new TreeMap<>();
        container.put(Banknote.HUNDRED, 5);
        container.put(Banknote.FIVE_HUNDRED, 5);
        container.put(Banknote.THOUSAND, 5);
        container.put(Banknote.TWO_THOUSAND, 5);
        container.put(Banknote.FIVE_THOUSAND, 5);

        storage = new Storage(container);
        atm = new AtmImpl(storage);
    }

    @DisplayName("должен принимать банкноты разных номиналов")
    @Test
    void shouldAcceptDifferentBanknotes() {
        Map<Banknote, Integer> container = new TreeMap<>();
        container.put(Banknote.HUNDRED, 5);
        container.put(Banknote.FIVE_HUNDRED, 5);
        container.put(Banknote.THOUSAND, 5);
        container.put(Banknote.TWO_THOUSAND, 5);
        container.put(Banknote.FIVE_THOUSAND, 5);

        atm.put(container);

        assertThat(atm.sum()).isEqualTo(86000);
    }

    @DisplayName("должен выдавать запрошенную сумму минимальным количеством банкнот")
    @Test
    void shouldGiveRequestedAmountByMinimalNumberOfBanknotes() {
        Map<Banknote, Integer> expected = Map.of(
                Banknote.FIVE_HUNDRED, 1,
                Banknote.TWO_THOUSAND, 2,
                Banknote.FIVE_THOUSAND, 1
        );

        Map<Banknote, Integer> banknotes = atm.get(9500);

        assertThat(banknotes).isNotEmpty().isEqualTo(expected);
    }

    @DisplayName("должен выдавать ошибку если сумму нельзя выдать")
    @Test
    void shouldThrowExceptionIfAmountCannotBeGiven() {
       assertThatThrownBy(() -> atm.get(1)).isInstanceOf(AtmWithdrawException.class);
        assertThatThrownBy(() -> atm.get(50000)).isInstanceOf(AtmWithdrawException.class);
    }

    @DisplayName("должен выдавать сумму остатка денежных средств")
    @Test
    void shouldGiveSumOfAtmLeftAmount() {
        int sum = atm.sum();

        assertThat(sum).isEqualTo(43000);
    }
}
