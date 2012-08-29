@Grapes(
@Grab(group='com.googlecode', module='google-api-translate-java', version='0.92')
)
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

Translate.setHttpReferrer("www.google.com.br");
class Translator {
Language from
String text

def methodMissing(String name, params) {
if (name.startsWith('from')) {
from = Language."${name[4..-1].toUpperCase()}"
text = params[0]
return this
} else if (name.startsWith('to')) {
Language to = Language."${name[2..-1].toUpperCase()}"
return Translate.execute(text, from, to);
} else {
throw new MissingMethodException(name, delegate, args)
}
}

}

def translator = new Translator()
println translator.fromEnglish("I don't want to touch you, oh baby").toPortuguese()
println translator.fromEnglish("I don't want to touch you, oh baby").toFrench()
println translator.fromEnglish("I don't want to touch you, oh baby").toItalian()
println translator.fromEnglish("I don't want to touch you, oh baby").toJapanese()
println translator.fromEnglish("I don't want to touch you, oh baby").toSpanish()


String.metaClass.methodMissing = { String name, params ->
return new Translator()."$name"(delegate)
}

println "I don't want to touch you, oh baby".fromEnglish().toPortuguese()
