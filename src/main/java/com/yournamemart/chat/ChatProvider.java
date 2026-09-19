package com.yournamemart.chat;

/**
 * Abstraction for chatbot backends.
 * Mock implementation first; real LLM can be plugged in later via config.
 */
public interface ChatProvider {
    String reply(String userMessage);
}
