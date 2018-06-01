package com.xianjinxia.conf;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Configuration
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "ext", ignoreInvalidFields = false)
public class ExtProperties {

	private final Swagger				swagger					= new Swagger();
	private final FaceProperties face=new FaceProperties();

	private final RiskAddress riskAddress = new RiskAddress();

	private final Authorization authorization = new Authorization();

	private final OldCashmanServerAddressConfig oldCashmanServerAddressConfig = new OldCashmanServerAddressConfig();
	
	private final SouKaConfig souKaConfig=new SouKaConfig();
	private final Soouu soouu = new Soouu();

	public Soouu getSoouu() {
		return soouu;
	}

	public RiskAddress getRiskAddress(){
		return riskAddress;
	}

	public Authorization getAuthorization(){
		return authorization;
	}

	public OldCashmanServerAddressConfig getOldCashmanServerAddressConfig(){
		return oldCashmanServerAddressConfig;
	}


	public Swagger getSwagger() {
		return swagger;
	}
	public FaceProperties getFace() {return face;}
	public static class Swagger {

		private String	title		= "uninoty API";

		private String	description	= "uninoty API documentation";

		private String	version		= "0.0.1";

		private String	termsOfServiceUrl;

		private String	contactName;

		private String	contactUrl;

		private String	contactEmail;

		private String	license;

		private String	licenseUrl;

		private Boolean	enabled;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getTermsOfServiceUrl() {
			return termsOfServiceUrl;
		}

		public void setTermsOfServiceUrl(String termsOfServiceUrl) {
			this.termsOfServiceUrl = termsOfServiceUrl;
		}

		public String getContactName() {
			return contactName;
		}

		public void setContactName(String contactName) {
			this.contactName = contactName;
		}

		public String getContactUrl() {
			return contactUrl;
		}

		public void setContactUrl(String contactUrl) {
			this.contactUrl = contactUrl;
		}

		public String getContactEmail() {
			return contactEmail;
		}

		public void setContactEmail(String contactEmail) {
			this.contactEmail = contactEmail;
		}

		public String getLicense() {
			return license;
		}

		public void setLicense(String license) {
			this.license = license;
		}

		public String getLicenseUrl() {
			return licenseUrl;
		}

		public void setLicenseUrl(String licenseUrl) {
			this.licenseUrl = licenseUrl;
		}

		public Boolean getEnabled() {
			return enabled;
		}

		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}

	}
	public static class FaceProperties{
		private String apiKey;
		private String apiSecret;

		public String getApiKey() {
			return apiKey;
		}

		public void setApiKey(String apiKey) {
			this.apiKey = apiKey;
		}

		public String getApiSecret() {
			return apiSecret;
		}

		public void setApiSecret(String apiSecret) {
			this.apiSecret = apiSecret;
		}
	}

	public static class RiskAddress{
		private String pushUrl;

		private String userName;

		private String password;

		public String getPushUrl() {
			return pushUrl;
		}

		public void setPushUrl(String pushUrl) {
			this.pushUrl = pushUrl;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}


	public static class Authorization{

		private String userName;

		private String password;

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}


	}

	public static class OldCashmanServerAddressConfig{

		private String serverAddress;

		public String getServerAddress() {
			return serverAddress;
		}

		public void setServerAddress(String serverAddress) {
			this.serverAddress = serverAddress;
		}
	}

	public static class Soouu{
		private String url;

		private String customerId;

		private String sign;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getCustomerId() {
			return customerId;
		}

		public void setCustomerId(String customerId) {
			this.customerId = customerId;
		}

		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}
	}
	public static class SouKaConfig{
		private String secretKey;//商户密钥
		public String getSecretKey() {
			return secretKey;
		}
		public void setSecretKey(String secretKey) {
			this.secretKey = secretKey;
		}
	}
	public SouKaConfig getSouKaConfig() {
		return souKaConfig;
	}



}
