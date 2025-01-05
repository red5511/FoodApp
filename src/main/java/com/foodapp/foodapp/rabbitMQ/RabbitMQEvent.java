package com.foodapp.foodapp.rabbitMQ;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RabbitMQEvent implements Serializable {
    private Long orderId;
    private String topicToSend;
}
