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
    private boolean showSetExecutionTime;
    private boolean showPrint;
    private boolean showReadyToPickUp;
    private boolean showCancel;
    private boolean showToTheCashier;
    private boolean showModify;

    public enum OrderAction {
        APPROVE,
        REJECT,
        SET_DELIVERY_TIME,
        PRINT,
        READY_TO_PICK_UP
    }
}