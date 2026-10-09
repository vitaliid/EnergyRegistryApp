package org.example.shared.comparator;

import org.example.model.User;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public final class UserComparator {

    private UserComparator() {
    }

    public static Comparator<User> byLastNameThenFirstName() {
        Collator collator = Collator.getInstance(Locale.GERMAN);
        Comparator<String> order = Comparator.nullsLast(collator::compare);

        return Comparator
                .comparing(User::getLastName, order)
                .thenComparing(User::getFirstName, order)
                .thenComparing(User::getId);
    }
}
