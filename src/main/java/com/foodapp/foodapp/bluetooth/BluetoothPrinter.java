package com.foodapp.foodapp.bluetooth;

import com.foodapp.foodapp.common.PolishCharConverter;
import com.foodapp.foodapp.order.dto.OrderDto;
import com.foodapp.foodapp.orderProduct.OrderProductDto;
import com.foodapp.foodapp.productProperties.ProductPropertiesDto;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BluetoothPrinter {
    public static final byte YHDAA_MAX_ROW_NUMBER_OF_CHARS_FOR_SMALL_TEXT = 31;
    private static final String CHINESE = "GBK";
    private static final String DELIVERY_NAME = "Dostawa: ";

    public static List<String> encodeTextForBluetooth(final OrderDto order, final Long displayableOrderId) throws UnsupportedEncodingException {
        var bigCenteredText1 = Command.ESC_Align.clone();
        bigCenteredText1[2] = 1; // to centruje text
        var bigCenteredText2 = Command.GS_ExclamationMark.clone();
        bigCenteredText2[2] = 17;//to powieksza text

        var header = "Zamowienie #" + displayableOrderId + "\n";
        var headerBytes = header.getBytes(CHINESE);
        var header2 = order.getExecutionTime().toString().replace('T', ' ');
        var headerBytes2 = header2.getBytes(CHINESE);

        var smallLeftText1 = Command.ESC_Align.clone();
        smallLeftText1[2] = 1; //troche dziwne bo znowu musze centrowac (moze wielkosc czcionki cos resetuje)
        var smallLeftTex2 = Command.GS_ExclamationMark.clone();
        smallLeftTex2[2] = 0; //pomniejszam text
        var smallLeftText3 = Command.ESC_Align.clone();
        smallLeftText3[2] = 0;// aligne do lewej? cos nie dokonca to idzie tak jak mysle ze zmniejszaniem

        var byteListToEncode = new ArrayList<byte[]>();
        byteListToEncode.add(bigCenteredText1);
        byteListToEncode.add(bigCenteredText2);
        byteListToEncode.add(headerBytes);
        byteListToEncode.add(smallLeftText1);
        byteListToEncode.add(smallLeftTex2);
        byteListToEncode.add(headerBytes2);
        byteListToEncode.add(smallLeftText3);

        StringBuilder stringToPrint = new StringBuilder("\n\n");
        if (order.getOrderProducts() != null) {
            order.getOrderProducts().forEach(orderProduct -> {
                for (int i = 0; i < orderProduct.getQuantity(); i++) {
                    stringToPrint.append(getOrderProductText(orderProduct));
                }
            });
            stringToPrint.append('\n');

        }
        if (StringUtils.isNotEmpty(order.getDescription())) {
            stringToPrint.append("Opis: ").append(PolishCharConverter.removePolishDiacritics(order.getDescription())).append("\n");
        }
        if (order.getDeliveryPrice() != null) {
            stringToPrint.append("\n");
            stringToPrint.append("\n");
            stringToPrint.append(DELIVERY_NAME);
            String deliveryPrice = order.getDeliveryPrice().setScale(2).toString();
            stringToPrint.append(" ".repeat(Math.max(0, YHDAA_MAX_ROW_NUMBER_OF_CHARS_FOR_SMALL_TEXT - (deliveryPrice.length() + DELIVERY_NAME.length()))));
            stringToPrint.append(deliveryPrice);
            stringToPrint.append("\n");
        }

        System.out.println(stringToPrint);

        var byteResult = PrinterCommand.POS_Print_Text(stringToPrint.toString(), CHINESE, 0, 0, 0, 0);
        byteListToEncode.add(byteResult);

        String totalPrice = "Suma: " + order.getTotalPrice().setScale(2, RoundingMode.HALF_UP).toString();
        byteListToEncode.add(bigCenteredText1);
        byteListToEncode.add(bigCenteredText2);
        byteListToEncode.add(totalPrice.getBytes(CHINESE));
        byteListToEncode.add(smallLeftTex2);
        byteListToEncode.add("\n\n\n\n\n".getBytes(CHINESE));
        byteListToEncode.add(smallLeftText3);

        return byteListToEncode.stream()
                .map(el -> Base64.getEncoder().encodeToString(el))
                .collect(Collectors.toList());
    }

    private static String getOrderProductText(final OrderProductDto orderProduct) {
        StringBuilder stringToPrint =
                new StringBuilder(PolishCharConverter.removePolishDiacritics(orderProduct.getProduct().getName()) + "\n");
        stringToPrint.append(getProductPropertyText(orderProduct));

        if (StringUtils.isNotEmpty(orderProduct.getNote())) {
            stringToPrint.append("\n");
            stringToPrint.append("Notatka: ").append(PolishCharConverter.removePolishDiacritics(orderProduct.getNote()));
        }
        stringToPrint.append("\n");
        String price =
                orderProduct.getPrice().divide(BigDecimal.valueOf(orderProduct.getQuantity()))
                        .setScale(2)
                        .toString();
        stringToPrint.append(" ".repeat(Math.max(0, YHDAA_MAX_ROW_NUMBER_OF_CHARS_FOR_SMALL_TEXT - price.length())));
        stringToPrint.append(price);
        stringToPrint.append("\n");
        return stringToPrint.toString();
    }

    private static String getProductPropertyText(final OrderProductDto orderProduct) {
        StringBuilder tempTextToPrint = new StringBuilder();

        String propertyText = orderProduct.getProductPropertiesList().stream()
                .sorted(Comparator.comparing(ProductPropertiesDto::isRequired).reversed())
                .map(ProductPropertiesDto::getPropertyList)
                .filter(properties -> properties != null && !properties.isEmpty())
                .map(properties -> properties.stream()
                        .map(el -> PolishCharConverter.removePolishDiacritics(el.getName()))
                        .collect(Collectors.joining(", ")))
                .collect(Collectors.joining(", "));

        tempTextToPrint.append(propertyText);
        return tempTextToPrint.toString();
    }
}
