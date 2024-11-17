package com.javarush.telegram.model;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TinderBoltApp extends MultiSessionTelegramBot {
    public static final String TELEGRAM_BOT_NAME = "chat_gpt_and_connection_aibot";
    public static final String TELEGRAM_BOT_TOKEN = "7698332175:AAE5aOoCuAoDYjKDOBTGalOorQ2VZRKfdVM";
    public static final String OPEN_AI_TOKEN = "chat-gpt-token"; //TODO: добавь токен ChatGPT в кавычках

    public TinderBoltApp() {
        super(TELEGRAM_BOT_NAME, TELEGRAM_BOT_TOKEN);
    }

    @Override
    public void onUpdateEventReceived(Update update) {
        String message = getMessageText();
        if (message.equals("/start")) {
            sendPhotoMessage("main");
            String messageText = loadMessage("main");
            sendTextMessage(messageText);
            return;
        }

        sendTextMessage("Здравствуйте, я *ChatBot | AI | GPT*");
        sendTextButtonsMessage("\n_Рассказатть вам что я умею?_",
                "Да", "Yes", "Нет", "No");
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());
    }
}
