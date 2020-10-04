/*
Freeware License, some rights reserved

Copyright (c) 2020 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy 
of this software and associated documentation files (the "Software"), 
to work with the Software within the limits of freeware distribution and fair use. 
This includes the rights to use, copy, and modify the Software for personal use. 
Users are also allowed and encouraged to submit corrections and modifications 
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for 
commercial use in any way, or for a user's educational materials such as books 
or blog articles without prior permission from the copyright holder. 

The above copyright notice and this permission notice need to be included 
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.apress.prospringmvc.bookstore;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * @author Iuliana Cosmina
 * @date 11/08/2020
 */
public class ChatHandler extends TextWebSocketHandler {

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws IOException {
		if(textMessage.getPayload().toLowerCase().contains("hello")||textMessage.getPayload().toLowerCase().contains("hi")) {
			session.sendMessage(new TextMessage(BOT_ANSWERS.get(0)));
			session.sendMessage(new TextMessage(BOT_ANSWERS.get(1)));
		} else {
			session.sendMessage(new TextMessage(randomMessages()));
		}
	}

	private static final Random RANDOM = new Random(System.currentTimeMillis());

	private static final List<String> BOT_ANSWERS = List.of(
			"Hello!",
			"How can I help?",
			"Can you please tell me more about your problem?",
			"Have you tried turning it off and on again?",
			"Give me a moment to verify your order was sent.",
			"This never happened before.",
			"I will investigate and come back to you.",
			"Can you send me your order number,please?",
			"Please bear with me while I search for your order.",
			"So Long, and Thanks for All the Fish!",
			"The answer is: 42."
	);

	private static String randomMessages() {
		return BOT_ANSWERS.get(RANDOM.nextInt(BOT_ANSWERS.size() - 2) + 2);
	}
}
