@Grab(group='postgresql', module='postgresql', version='9.1-901.jdbc4')
@GrabConfig(systemClassLoader = true)

import groovy.sql.Sql

def sql = Sql.newInstance('jdbc:postgresql://localhost/tide', 'geocontrol', 'geo007', 'org.postgresql.Driver')
def users = ['ATENDENTE1', 'DESPACHO1'].collect{"'${it}'"}.toString().replace('[', '(').replace(']', ')')
String query = "select * from usuario where identificacao in $users"
println users

try {
	sql.rows(query).each { row ->
		println "ident. = ${row.identificacao}"
	}
} finally {
	sql.close()
}
