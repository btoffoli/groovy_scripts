// Groovy Code here
​import​ br.com.geocontrol.despacho.modelo.ativo.*
import br.com.geocontrol.despacho.modelo.atendimento.*

lista = ['54-3723', '54-3741', '54-3735', '54-3713', '54-3746', '54-3733', '54-3721', '52-1061']

atvs = ​Ativo.withCriteria{  
  'in' 'nome', lista
}​*.id

data = new java.sql.Timestamp(System.currentTimeMillis())
Assuncao.withTransaction{
Assuncao.withCriteria{
  'in' 'ativo.id', atvs
  isNull 'fim'
}.each{
   it.fim = data
   it.save()
}
}
​
