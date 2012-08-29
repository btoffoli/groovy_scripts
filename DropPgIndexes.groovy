@Grab(group='postgresql', module='postgresql', version='9.1-901.jdbc4')
@GrabConfig(systemClassLoader = true)

import groovy.sql.Sql

String query = "select relname from pg_class where relkind='i' and not (relname like 'pg_%' or relname like 'spati%' or relname like 'geom%' )"
def sql = Sql.newInstance('jdbc:postgresql://localhost/tide', 'geocontrol', 'geo007', 'org.postgresql.Driver')

try {
	println 'begin;'
	sql.eachRow(query) { row ->
//		println row.relname
		println "drop index '${row.relname}' cascade;"
	}
	println 'commit;'
} finally {
	sql.close()
}
