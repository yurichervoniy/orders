package kz.alfabank.alfaordersbpm.domain.models.order;

import java.util.*;

public enum OrderState {
    CREATED,
    RUNNING,
    CANCELED,
    REFUSED,
    COMPLETED,
    FAULTED
    ;

    private static final Set<OrderState> TERMINATED_STATES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(CANCELED, REFUSED, COMPLETED)));

    public static Set<OrderState> getUnmodifiableTerminatedStates(){
        return TERMINATED_STATES;
    }
}

