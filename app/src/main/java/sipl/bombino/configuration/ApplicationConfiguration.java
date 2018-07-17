package sipl.bombino.configuration;

public class ApplicationConfiguration {
	//private String URL = "http://demo.sagarinfotech.com/JsonWebService/SIPL.asmx";
	private static String URL = "http://uat58.sagarinfotech.com/bombinoexpress/BombinoExpressWebService/sipl.asmx";
	//private  static  String URL = "http://192.168.1.99/LocalAsmxServices/NECCWebService/NECCWebService.asmx";
	private static String NAMESPACE = "http://sagarinfotech.com/";
	private static String CCode = "ConnectionStringName";
	private static String Token = "9990SIPL261963";

	public ApplicationConfiguration() {

	}

	public static String GetNameSpace() {
		return NAMESPACE;
	}

	public static String GetURL() {
		return URL;
	}

	public static String GetCCode() {
		return CCode;
	}

	public static String GetToken() {
		return Token;
	}

}
