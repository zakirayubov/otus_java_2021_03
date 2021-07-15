package ru.otus;

import java.util.*;
import java.util.stream.Collectors;

public class AtmImpl implements Atm{
    private final Storage storage;

    public AtmImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void put(Map<Banknote, Integer> banknotes) {
        storage.getContainer().forEach((banknote, quantity) ->  storage.getContainer().compute(banknote, (b, n) -> n == null ? quantity : n + quantity));
    }

    @Override
    public Map<Banknote, Integer> get(int amount) {
        Map<Banknote, Integer> result = new TreeMap<>();

        if (calculateSum(storage.getContainer()) < amount) {
            throw new AtmWithdrawException("Insufficient funds");
        }

        int sumLeft = amount;
        List<Banknote> banknotes = Arrays.stream(Banknote.values()).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        for (Banknote banknote : banknotes) {
            int containerNumber =  storage.getContainer().getOrDefault(banknote, 0);
            int requiredNumber = sumLeft / banknote.getValue();

            int takenNumber = Math.min(containerNumber, requiredNumber);
            int withdrawAmount = takenNumber * banknote.getValue();

            sumLeft -= withdrawAmount;

            if (takenNumber > 0) {
                storage.getContainer().put(banknote, containerNumber - takenNumber);
                result.put(banknote, takenNumber);
            }
        }

        if (sumLeft != 0) {
            if (result.size() != 0) {
                put(result);
            }
            throw new AtmWithdrawException("No suitable banknotes available");
        }

        return result;
    }

    @Override
    public int sum() {
        return calculateSum(storage.getContainer());
    }

    public int calculateSum(Map<Banknote, Integer> banknotes) {
        return banknotes.entrySet().stream().mapToInt(entry -> entry.getKey().getValue() * entry.getValue()).sum();
    }
}
