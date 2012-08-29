
/**
 *
 * @autor igor
 * Date: 9/20/11
 * Time: 4:25 PM
 * To change this template use File | Settings | File Templates.
 */

@Grapes(
    @Grab(group = 'org.apache.activemq', module = 'activemq-all', version = '5.3.2')
)
import org.apache.activemq.ActiveMQConnectionFactory
import javax.jms.Session
import javax.jms.Topic
import javax.jms.MessageConsumer
import javax.jms.MessageListener
import javax.jms.Message

def body(message) {
	message.mapNames.inject([:]) { map, name ->
		map[name] = message.getObject(name)
		return map
	}
}

def header(message) {
	message.propertyNames.inject([:]) { map, name ->
		map[name] = message.getObjectProperty(name)
		return map
	}
}

new ActiveMQConnectionFactory('failover:(tcp://localhost:51515)?initialReconnectDelay=100&maxReconnectDelay=2000').with {
    createConnection().with {
        createSession(false, Session.AUTO_ACKNOWLEDGE).with {
            createConsumer(createTopic('GPRS.MESSAGE_RECEIVED')).with {
                messageListener = { Message message ->
                    println """################################################################
HEADER:
\t${header(message).entrySet().join('\n\t')}
BODY:
\t${body(message).entrySet().join('\n\t')}
################################################################"""
                } as MessageListener
            }
        }
        start()
        System.in.read()
        close()
    }
}
