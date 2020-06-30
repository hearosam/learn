package com.sam.delayorderforjava.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {

    private String orderNo;
    private String orderNote;
    private String status;

    @Override
    public String toString() {
        return "OrderEntity{" +
                "orderNo='" + orderNo + '\'' +
                ", orderNote='" + orderNote + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
