package rnd.statemachine.order;

/**
 * DEFAULT               - CREATE   -> orderProcessor()   -> ORDERCREATED   -> PAYMENTPENDING
 * PAYMENTPENDING        - PAY      -> paymentProcessor() -> PAYMENTERROR   -> PAYMENTERROREMAILSENT
 * PAYMENTERROREMAILSENT - RETRYPAY -> paymentProcessor() -> PAYMENTSUCCESS -> PAYMENTSUCCESS
 * PAYMENTPENDING        - PAY      -> paymentProcessor() -> PAYMENTSUCCESS -> PAYMENTSUCCESS
 */
public enum OrderState {
    DEFAULT,
    PAYMENTPENDING,
    PAYMENTERROR,
    PAYMENTSUCCESS
}
