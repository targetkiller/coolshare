package mqs;

import com.sudocn.rabbitmq.MQ;

import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class InitBoostrap extends Job {

	@Override
	public void doJob() {
		MQ.listen(Play.configuration.getProperty("mq.queue.docshare.parser"), new DocParserListener());
	}
}
