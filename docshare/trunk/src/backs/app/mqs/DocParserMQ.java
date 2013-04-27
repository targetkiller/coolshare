package mqs;

import models.Document;
import play.Play;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import com.sudocn.rabbitmq.MQ;
import com.sudocn.rabbitmq.StringMessage;

public class DocParserMQ {
	
	public static void publishDocument(Document doc) {
		StringMessage msg = initStringMessage();
		msg.setString(new Gson().toJson(doc));
		MQ.publish(msg);
	}

	private static StringMessage initStringMessage() {
		StringMessage msg = new StringMessage();
		Builder bob = new AMQP.BasicProperties.Builder();
		msg.setProps(bob.build());
		msg.setExchange(Play.configuration.getProperty("mq.exchange.docshare"));
		msg.setRoutingKey(Play.configuration.getProperty("mq.queue.docshare.parser"));
		return msg;
	}
}
