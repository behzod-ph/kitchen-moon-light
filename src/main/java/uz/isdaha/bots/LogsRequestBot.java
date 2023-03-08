package uz.isdaha.bots;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.isdaha.util.Utils;

import java.io.*;
import java.nio.file.Path;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class LogsRequestBot extends TelegramLongPollingBot {


    @Value("${bot.log.token}")
    public String token;

    @Value("${bot.log.username}")
    public String username;

    @Value("${bot.log.groupId}")
    public String groupId;

    @Value("${bot.log.admin}")
    public String adminId;

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
        if (update.hasMessage()) {
            if (update.getMessage().getText().equals("/log")) {
                SendDocument document = new SendDocument();
                document.setDocument(new InputFile(new File("./logs/moti.log")));
                document.setCaption(Utils.dateFormat());
                document.setChatId(adminId);
                try {
                    execute(document);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendMessage(String data) {
        try {
            execute(new SendMessage(groupId, data));
        } catch (TelegramApiException e) {
            System.out.println("error telegram");
        }
    }

    public void sendLog(Throwable exception) {

        try {
            String log = log(exception);
            FileOutputStream fileOutputStream = new FileOutputStream(new File("./error.txt"));
            fileOutputStream.write(log.getBytes(), 0, log.length());
            fileOutputStream.close();
            SendDocument document = new SendDocument();
            File file = new File("./error.txt");
            document.setDocument(new InputFile(file));
            document.setCaption(Utils.dateFormat());
            document.setChatId(adminId);
            execute(document);
            document.setChatId(groupId);
            execute(document);

        } catch (Exception e) {
            exception.printStackTrace();
            e.printStackTrace();
        }
    }

    public String log(Throwable exception) throws IOException {
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        stringWriter.close();
        return stringWriter.toString();
    }
}
