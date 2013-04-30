
/**
 *
 * @autor igor
 * Date: 9/20/11
 * Time: 4:25 PM
 * To change this template use File | Settings | File Templates.
 * @version 1.0
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


/**
* @param nome descrição para a descrição de argumentos.
* @return descrição para a descrição do valor de retorno.
* @exception nomeClasseExceção descrição para a descrição de exceções lançadas pelo método.
*/
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

//String strConexao = 'failover:(tcp://localhost:61616)?initialReconnectDelay=100&maxReconnectDelay=2000'
//String strConexao = 'failover:(tcp://189.115.198.124:21016)?initialReconnectDelay=100&maxReconnectDelay=2000'
String strConexao = 'failover:(tcp://localhost:61616)?initialReconnectDelay=100&maxReconnectDelay=2000'

//String nomeTopico = 'GPRS.MESSAGE_RECEIVED'
String nomeTopico = 'API_TIDE'

new ActiveMQConnectionFactory( strConexao ).with {
    createConnection().with {
        createSession(false, Session.AUTO_ACKNOWLEDGE).with {
            createConsumer(createTopic(nomeTopico)).with {
                messageListener = { Message message ->
                    println """################################################################
                        msg => ${message}
                        text => ${message.text}
                    ################################################################"""
                } as MessageListener
            }
        }
        start()
        System.in.read()
        close()
    }
}
