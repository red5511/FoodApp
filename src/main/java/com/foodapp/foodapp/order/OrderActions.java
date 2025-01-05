package com.foodapp.foodapp.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderActions {
    private boolean showApprove;
    private boolean showReject;
    private boolean showSetDeliveryTime;
    private boolean showPrint;
    private boolean showReadyToPickUp;

    public enum OrderAction {
        APPROVE,
        REJECT,
        SET_DELIVERY_TIME,
        PRINT,
        READY_TO_PICK_UP
    }
}