package rnd.statemachine.order;

import rnd.statemachine.ProcessState;

/**  
 * DEFAULT        -  SUBMIT -> orderProcessor()   -> ORDERCREATED   -> PAYMENTPENDING
 * PAYMENTPENDING -  PAY    -> paymentProcessor() -> PAYMENTERROR   -> PMTERREMAILSENT
 * PMTERREMAILSENT -  PAY    -> paymentProcessor() -> PAYMENTSUCCESS -> PAYMENTSUCCESS
 */
public enum OrderState implements ProcessState {
    DEFAULT,
    PAYMENTPENDING,
    PAYINPROGRESS,
    PAYMENTERROREMAILSENT,
    RETRYPAYINPROGRESS,
    PAYMENTSUCCESSEMAILSENT
}
