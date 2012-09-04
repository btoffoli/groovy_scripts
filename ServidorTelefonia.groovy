def server = new ServerSocket(10001)
def outputs = [:]

class Faker {
    public static final Random generator = new Random();

    static String numerify(String val) {
        def ret = []
        val.each { letter ->
            ret << letter.replace('#', generator.nextInt(10).toString())
        }
        return ret.join('')
    }

    static String letterify(String val) {
        def ret = []
        val.each { letter ->
            ret << letter.replace('?', ('a'..'z')[generator.nextInt(26)])
        }
        return ret.join('')
    }

    static String bothify(String val) {
        return letterify(numerify(val))
    }

    protected static Object[] shuffle(Object[] a, int cnt = 5) {
        def ret = []
        for (int i=0; i<cnt; i++) {
            ret << a[generator.nextInt(a.length)]
        }
        return ret
    }
}

def log = { data ->
	println "[${new Date().format('yyyy-MM-dd HH:mm:ss')}] ${data}"
}

def obterCauseErro = { Throwable e ->
	while (e.cause) {
		e = e.cause
	}
	return e
}

def handleConnection = { Socket socket ->
	def hc = socket.hashCode()
	log "[${hc}] Conexao iniciada: ${socket}"
	try {
		socket.withStreams { inputStream, outputStream ->
			outputs[hc] = socket.outputStream
			StringBuilder buffer = new StringBuilder()
			inputStream.eachByte { b ->
				char c = b as char
				buffer << c
				if (c == '\n') {
					log "[${hc}] ${buffer}"
					buffer.setLength(0)
				}
			}
		}
	} catch (e) {
		def t = obterCauseErro(e)
		log "[${hc}] Erro na conexao: ${e.message}"
	} finally {
		log "[${hc}] Conexao terminada: ${socket}"
		outputs.remove(hc)
		try {
			socket.close()
		} catch (e) {

		}
	}
}

def enviarPacote = { String telefone = Faker.numerify('##########'), String ramal = 5047/*Faker.numerify('###')*/ ->
	String pacote = "ani=${telefone}|dnis=${ramal}|userinfo=|ucid=00001060051346348404\n" as String
	log "Enviando pacote ${pacote}"
	outputs.each { hc, os ->
		os << "${pacote}"
	}
}

def handleCommandLine = { String line ->
	if (!line) {
		return null
	}
	String [] params = line.split()
	String command = params[0]
	if (command == 'exit') {
		throw new RuntimeException('Exit recebido')
	} else if (command == 'send') {
		String telefone = params.length > 1 ? params[1] : Faker.numerify('##########')
		String ramal = params.length > 2 ? params[2] : Faker.numerify('###')
		enviarPacote(telefone, ramal)
	} else if (command == 'fake') {
		enviarPacote()
	}
}

Thread.startDaemon {
	while (Thread.currentThread().alive && !Thread.currentThread().interrupted) {
		enviarPacote()
		long interval = (0 + Faker.generator.nextInt(2)) * 1000l
		log "Aguardando ${interval} milisegundos"
		Thread.sleep(interval) {
			log 'Interrompeu'
		}
	}
}

Thread.startDaemon {
	while (!server.closed) {
		log "Aguardando Conexao"
		try {
			server.accept(handleConnection)
		} catch (Throwable e) {
			def t = obterCauseErro(e)
			log "Servidor terminado: ${t.message}"
		}
	}
}

System.in.withReader { reader ->
	String line = null
	String data = null
	while ((line = reader.readLine()) != null) {
		try {
			handleCommandLine(line)
		} catch (e) {
			def t = obterCauseErro(e)
			log "Terminando aplicação: ${t.message}"
			break
		}
	}
}
