package com.javarush.telegram.model;

import com.javarush.telegram.service.ChatGPTService;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TinderBoltApp extends MultiSessionTelegramBot {
    public static final String TELEGRAM_BOT_NAME = "chat_gpt_and_connection_aibot";
    public static final String TELEGRAM_BOT_TOKEN = "7698332175:AAE5aOoCuAoDYjKDOBTGalOorQ2VZRKfdVM";
    public static final String OPEN_AI_TOKEN = "gpt:4dws6NYyD0BDK2ufp71ZJFkblB3TCC3tppbmX6OYmhSFydbM";
    private ChatGPTService chatGpt = new ChatGPTService(OPEN_AI_TOKEN);
    private DialogMode mode = null;

    public TinderBoltApp() {
        super(TELEGRAM_BOT_NAME, TELEGRAM_BOT_TOKEN);
    }

    @Override
    public void onUpdateEventReceived(Update update) {
        String message = getMessageText();
        if (message.equals("/start")) {
            mode = DialogMode.MAIN;
            sendPhotoMessage("main");
            String messageText = loadMessage("main");
            sendTextMessage(messageText);

            showMainMenu("Главное меню бота", "/start",
                    "Генерация Tinder-профля \uD83D\uDE0E", "/profile",
                    "Сообщение для знакомства \uD83E\uDD70", "/opener ",
                    "Переписка от вашего имени \uD83D\uDE08", "/message",
                    "Переписка со звездами \uD83D\uDD25", "/date",
                    "Задать вопрос чату GPT \uD83E\uDDE0", "/gpt");
            return;
        }

        if (message.equals("/gpt")) {
            mode = DialogMode.GPT;
            sendPhotoMessage("gpt");
            String text = loadMessage("gpt");
            sendTextMessage(text);
            return;
        }
        if (mode == DialogMode.GPT) {
            String prompt = loadPrompt("gpt");
            String answer = chatGpt.sendMessage(prompt, message);
            sendTextMessage(answer);
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
