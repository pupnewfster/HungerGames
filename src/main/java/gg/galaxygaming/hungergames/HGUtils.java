package gg.galaxygaming.hungergames;

public class HGUtils {
    public static String translate(String message) {
        return message;
		/*String lang = locale();
		if(lang.equalsIgnoreCase("en"))
			return message;
		Locale loc = new Locale(lang);
		AsynchronousPrefetchThread.setEnabled(false);
	    ResourceBundle res = ResourceBundle.getBundle("com.javanetworkframework.rb.webtranslator.WebTranslator", loc);
	    return res.getString(message);*/
    }

    /*private String locale() {
        YamlConfiguration config = HungerGames.getInstance().getConfig();
        String lang = config.getString("language");
        if (lang == null)
            return "en";
        if (lang.equalsIgnoreCase("DE") || lang.equalsIgnoreCase("GERMAN"))
            return "de";
        else if (lang.equalsIgnoreCase("EL") || lang.equalsIgnoreCase("GREEK"))
            return "el";
        else if (lang.equalsIgnoreCase("ES") || lang.equalsIgnoreCase("SPANISH"))
            return "es";
        else if (lang.equalsIgnoreCase("FR") || lang.equalsIgnoreCase("FRENCH"))
            return "fr";
        else if (lang.equalsIgnoreCase("IT") || lang.equalsIgnoreCase("ITALIAN"))
            return "it";
        else if (lang.equalsIgnoreCase("JA") || lang.equalsIgnoreCase("JAPANESE"))
            return "ja";
        else if (lang.equalsIgnoreCase("KO") || lang.equalsIgnoreCase("KOREAN"))
            return "ko";
        else if (lang.equalsIgnoreCase("NL") || lang.equalsIgnoreCase("DUTCH"))
            return "nl";
        else if (lang.equalsIgnoreCase("NO") || lang.equalsIgnoreCase("NORWEGIAN"))
            return "no";
        else if (lang.equalsIgnoreCase("PT") || lang.equalsIgnoreCase("PORTUGUESE"))
            return "pt";
        else if (lang.equalsIgnoreCase("RU") || lang.equalsIgnoreCase("RUSSIAN"))
            return "ru";
        else if (lang.equalsIgnoreCase("ZH") || lang.equalsIgnoreCase("CHINESE (SIMPLIFIED)"))
            return "zh";
        else if (lang.equalsIgnoreCase("ZT") || lang.equalsIgnoreCase("CHINESE (TRADITIONAL)"))
            return "zt";
        else
            return "en";
    }*/
}