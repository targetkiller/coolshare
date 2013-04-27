package mqs;

import org.scauhci.play.rabbitmq.DBMQListener;

import com.rabbitmq.client.QueueingConsumer.Delivery;

public class DocParserListener extends DBMQListener {

	@Override
	protected void onDeliveryWithDB(Delivery deli) {
		String msg = new String(deli.getBody());
		System.out.println(msg);
	}

}
