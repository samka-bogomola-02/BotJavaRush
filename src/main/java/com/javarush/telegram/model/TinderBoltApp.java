package com.javarush.telegram.model;

import com.javarush.telegram.service.ChatGPTService;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;

public class TinderBoltApp extends MultiSessionTelegramBot {
    public static final String TELEGRAM_BOT_NAME = "chat_gpt_and_connection_aibot";
    public static final String TELEGRAM_BOT_TOKEN = "7698332175:AAE5aOoCuAoDYjKDOBTGalOorQ2VZRKfdVM";
    public static final String OPEN_AI_TOKEN = "gpt:4dws6NYyD0BDK2ufp71ZJFkblB3TCC3tppbmX6OYmhSFydbM";
    private ChatGPTService chatGpt = new ChatGPTService(OPEN_AI_TOKEN);
    private DialogMode mode = null;
    private ArrayList<String> list = new ArrayList<>();

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

            Message msg = sendTextMessage("печатает...");

            String answer = chatGpt.sendMessage(prompt, message);
            updateTextMessage(msg, answer);
            return;
        }
        if (message.equals("/date")){
            mode = DialogMode.DATE;
            String text = loadMessage("date");
            sendPhotoMessage("date");
            sendTextButtonsMessage(text,
                    "Гранде", "date_grande", "Робби", "date_robbie",
                    "Гослинг", "date_gosling", "Харди", "date_hardy", "Зендея", "date_zendaya");
            return;
        }
        if (mode == DialogMode.DATE) {
            String query = getCallbackQueryButtonKey();
            if (query.startsWith("date_")){
                sendPhotoMessage(query);
                sendTextMessage("Отличный выбор");
                String prompt = loadPrompt(query);
                chatGpt.setPrompt(prompt);
                return;
            }
            Message msg = sendTextMessage("печатает...");

            String answer = chatGpt.addMessage(message);
            updateTextMessage(msg ,answer);
            return;
        }

        if (message.equals("/message")) {
            mode = DialogMode.MESSAGE;
            sendPhotoMessage("message");
            sendTextButtonsMessage("Пришлите в чат вашу переписку",
                    "Следующее сообщение", "message_next", "Пригласить на свидание", "message_date");
            return;

        }

        if (mode == DialogMode.MESSAGE) {
            String query = getCallbackQueryButtonKey();
            if (query.startsWith("message_")) {
                String prompt = loadPrompt(query);
                String userChatHistory = String.join("\n\n", list);

                Message msg = sendTextMessage("печатает...");

                String answer = chatGpt.sendMessage(prompt, userChatHistory);
                updateTextMessage(msg, answer);
                return;
            }
            list.add(message);
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
