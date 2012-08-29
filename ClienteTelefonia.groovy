//@Grapes([
//	@Grab(group='log4j', module='log4j', version='1.2.16')
//])
import groovy.util.logging.Log

class Servico {
        def configuracaoService
	String host
	int port
	long intervaloReconexao = 1000
	
	private Socket socket
	private volatile boolean executando = true
	private volatile boolean conectado = false

	Socket getSocket() {
		this.socket
	}

	boolean isExecutando() {
		this.executando
	}

	boolean isConectado() {
		this.conectado
	}

	void iniciar() {
		executando = true
		log.info "Iniciando"
		try {
			conectar()
		} catch(e) {
			log.warning "Erro ao conectar: ${e.message}"
			Thread.startDaemon {
				reconectar()
			}
		}
		log.info "Iniciado"
	}

	void conectar() {
		log.info "Conectando"
		socket = new Socket(configuracaoService.obterUrlTelefonia(), configuracaoService.obterPortaTelefonia())
		log.info "Conectado"
		conectado = true
		tratarDados()
	}

	void fecharSocket() {
		conectado = false
		try {
			if (socket) {
				socket.close()
			}
		} catch(e) {
			log.warning "Erro ao terminar conexão: ${e.message}"
		} finally {
			socket = null
		}		
	}

	void terminar() {
		executando = false
		fecharSocket()
	}

	void tratarPacote(String pacote) {
		log.info "Dados recebidos - ${pacote}"
	}

	void reconectar() {
		fecharSocket()
		while (executando && !conectado) {
			try {
				log.info "Reconectando"
				conectar()
			} catch (e) {
				log.info "Aguardando ${intervaloReconexao} ms para reconectar"
				sleep(intervaloReconexao)
			}
		}
	}

	void tratarDados() {
		Thread.startDaemon {
			try {
				log.info "Recebendo dados"
				socket.withStreams { inputStream, outputStream ->
					inputStream.eachLine { line ->
						tratarPacote(line)
					}
				}			
			} catch (e) {
				log.warning "Erro ao receber dados: ${e.message}"
			} finally  {
				reconectar()
			}
		}
	}

}

new Servico(host: 'localhost', port: 20000).with {
	iniciar()
	System.in.read()
	terminar()
}
