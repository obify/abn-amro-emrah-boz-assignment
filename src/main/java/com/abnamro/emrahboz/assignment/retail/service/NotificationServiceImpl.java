package com.abnamro.emrahboz.assignment.retail.service;

import com.abnamro.emrahboz.assignment.retail.data.model.UserEntity;
import com.abnamro.emrahboz.assignment.retail.service.model.OrderDto;
import com.abnamro.emrahboz.assignment.retail.service.model.OrderProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;


@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Value("#{'${retail.service.notificationChannels}'.split(',')}")
    private List<String> notificationChannels;
    @Value("${spring.mail.host}")
    private String mailHost;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean smtpAuth;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean startTls;
    @Value("${spring.mail.port}")
    private String port;
    @Value("${spring.mail.properties.mail.smtp.ssl.trust}")
    private String sslTrust;

    @Value("${spring.mail.username}")
    private String userName;

    @Value("${spring.mail.password}")
    private String password;


    @Override
    @Async
    public boolean sendNotification(OrderDto orderDto, UserEntity userEntity) {


        for (String channel : notificationChannels) {

            switch (channel) {
                case "email":
                    sendEmailNotification(orderDto, userEntity);
                    break;
                case "sms":
                    sendSmsNotification(orderDto, userEntity);
                    break;
            }
        }
        return true;
    }

    private void sendEmailNotification(OrderDto orderDto, UserEntity userEntity) {
        log.info("Mail send start");

        try {

            Properties prop = new Properties();
            prop.put("mail.smtp.auth", smtpAuth);
            prop.put("mail.smtp.starttls.enable", startTls);
            prop.put("mail.smtp.host", mailHost);
            prop.put("mail.smtp.port", port);
            prop.put("mail.smtp.ssl.trust", sslTrust);

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userName));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(userEntity.getEmail()));
            message.setSubject((String.format("Dear : %s Your order received",
                    userEntity.getName())));


            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(createEmailBody(orderDto, userEntity), "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);
            Transport.send(message);


        } catch (Exception ex) {
            log.error("Mail send end with error {}, {}", ex.getMessage(), ex);
            return;
        }

        log.info("Mail sent successfully");
        return;
    }

    private void sendSmsNotification(OrderDto orderDto, UserEntity userEntity) {
        //TODO Improvement point
    }


    private String createEmailBody(OrderDto orderDto, UserEntity userEntity) {

        String tableFormat = "<table>%s</table>";
        String keyValueFormat = "<tr><td>%s</td><td>%s</td></tr>";
        String valueFormat = "<tr><td colspan=\"2\">%s</td></tr>";
        String slicer = "<tr><td colspan=\"2\">-------------------------------------------</td></tr>";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        StringBuilder builder = new StringBuilder();

        builder.append(String.format(valueFormat, "Dear " + userEntity.getName()));
        builder.append(String.format(valueFormat, "Your order received successfully"));
        builder.append(String.format(keyValueFormat, "Order Id", orderDto.getId()));
        builder.append(String.format(keyValueFormat, "Order Total Amount", orderDto.getOrderTotalAmount()));
        builder.append(String.format(keyValueFormat, "Order Total Product Count", orderDto.getTotalItemCount()));
        builder.append(String.format(keyValueFormat, "Order Date", orderDto.getLastUpdate().format(formatter)));
        builder.append(String.format(valueFormat, "Below, you can find product(s) details"));

        builder.append(slicer);

        for (OrderProductDto productDto : orderDto.getProducts()) {

            builder.append(String.format(keyValueFormat, "Product Name", productDto.getProductName()));
            builder.append(String.format(keyValueFormat, "Product Amount", productDto.getProductAmount()));
            builder.append(String.format(keyValueFormat, "Product Quantity", productDto.getProductQuantity()));
            builder.append(slicer);
        }
        return String.format(tableFormat, builder.toString());

    }
}
