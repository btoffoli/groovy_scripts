@Grapes([
	@Grab(group='com.googlecode.xmemcached', module='xmemcached', version='1.3.7'),
	@Grab(group='org.slf4j', module='slf4j-jdk14', version='1.6.6')
])

import net.rubyeye.xmemcached.*
import net.rubyeye.xmemcached.utils.*

MemcachedClientBuilder builder = new XMemcachedClientBuilder(
                                        AddrUtil.getAddresses("localhost:11211"));
MemcachedClient memcachedClient = builder.build();
def date = new Date()

try {
	String key = 'my.date'
	Date d = memcachedClient.get(key)
	println("Old date: ${d?.format('yyyy-MM-dd HH:mms')}")
    memcachedClient.set(key, 0, date);
    println("New date: ${date.format('yyyy-MM-dd HH:mms')}")
} finally {
	if (memcachedClient) {
		memcachedClient.shutdown();
	}
}