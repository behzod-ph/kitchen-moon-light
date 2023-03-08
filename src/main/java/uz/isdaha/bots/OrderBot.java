package uz.isdaha.bots;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.isdaha.entity.Order;

@Component
public class OrderBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(OrderBot.class);

    @Value("${bot.order.token}")
    public String token;

    @Value("${bot.order.username}")
    public String username;

    @Value("${bot.order.groupId}")
    public String groupId;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);

    }

    public void sendNotification(Order order, Boolean change) {
        StringBuilder s = new StringBuilder();
        order.getProducts().forEach(e -> {
            s.append(e.getProduct().getProductName()).append(" - ").append(e.getCount()).append(" ta \n\n");
        });

        StringBuilder rs = new StringBuilder();
        if (change) {
            rs.append("Change status\n\n");
        } else {
            rs.append("New order\n\n");
        }
        rs.append("📎Order - ").append(order.getId()).append("\n\n👤 User phone - ").append(order.getUser().getPhoneNumber()).append("\n\n✅ Status  - ").append(order.getStatus().name()).append("\n\n📝MEALS : \n\n").append(s).append("💰 Price - ").append(order.getTotalPrice()).append("\n\n💳 Pay - ").append(order.getPaymentMethod().name()).append("\n\n⏰Delivery time  - ").append(order.getDeliveryTime()).append("\n\n📍Address - ").append(order.getAddress().getDistrict());

        try {
            execute(new SendMessage(groupId, rs.toString()));
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }

    }

}
