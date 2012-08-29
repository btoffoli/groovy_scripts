import java.util.concurrent.atomic.AtomicInteger

def counter = new AtomicInteger()

final Object lock = new Object()
long totalTime = 0

def inc = { val ->
	synchronized(lock) {
		totalTime += val
	}
}

def req = {
	int id = counter.incrementAndGet()
	long c = System.currentTimeMillis()
	def t = new URL('http://localhost:8181/maxtrack/pacote/obterPacotes?chave=a69acf32-e44e-4452-86ad-09bf2d745af8').text
	c = System.currentTimeMillis() - c
	inc(c)
	println "[${id}] ${c} ms"
}

def numReqs = args[0].toInteger()
def numThreads = args[1].toInteger()
def threads = []
numThreads.times {
	threads << Thread.start {
		numReqs.times {
			req()
		}
	}
}
threads*.join()

println "Tempo medio: ${totalTime / counter.get()} ms / req"
